package com.learning.demomode.search.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.learning.common.base.BaseFragment
import com.learning.common.bean.ArticleBean
import com.learning.common.ui.*
import com.learning.common.weight.MToast
import com.learning.demomode.R
import com.learning.demomode.REQUEST_SEARCH
import com.learning.common.bean.SEARCH_KEYWORD
import com.learning.demomode.databinding.FragmentSearchDetailBinding
import kotlinx.android.synthetic.main.fragment_search_detail.*

class SearchDetailFragment : BaseFragment<SearchDetailViewModel, FragmentSearchDetailBinding>() {
    private var keyword: String? = null
    private var requestState: Int = LOADING_OTHERS
    private val searchAdapter by lazy { SearchDetailAdapter() }

    override fun layoutId(): Int = R.layout.fragment_search_detail

    override fun initWithCreate() {
        arguments?.apply {
            keyword = this.getString(SEARCH_KEYWORD)
        }
        setTitle = safeContext.getString(R.string.search_detail)
        onUpdateToolbar()

        baseViewModel.getSearchResult().observe(this, Observer {
            search_detailProgress.visibility = View.GONE
            when (requestState) {
                LOADING_BOTTOM -> {
                    val tempData = ArrayList<ArticleBean>()
                    tempData.addAll( searchAdapter.getData())
                    tempData.addAll(it)
                    baseViewModel.calculateDiff(ArrayList(searchAdapter.getData()), tempData)
                    searchAdapter.addMoreData(it)
                }
                LOADING_TOP, LOADING_OTHERS -> {
                    baseViewModel.calculateDiff(ArrayList(searchAdapter.getData()), ArrayList(it))
                    searchAdapter.updateData(it)
                }
            }
        })

        baseViewModel.getDiffResult().observe(this, Observer {
            it.dispatchUpdatesTo(search_detailList.rAdapter as RecyclerView.Adapter<*>)
            search_swipeView.setLoadingState(false)
        })

        baseViewModel.getRequestError().observe(this, Observer {
            for (key in it.keys) {
                Log.e(
                    TAG,
                    "Network Error [type: $key] -> code : ${it[key]?.statusCode} / msg : ${it[key]?.errorMsg}"
                )
                when (key) {
                    REQUEST_SEARCH -> {
                        search_detailProgress.visibility = View.GONE
                    }
                }
            }

            MToast.makeText(
                safeContext,
                safeContext.getString(R.string.network_error),
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    override fun initWithViewCreate(savedInstanceState: Bundle?) {
        with(search_swipeView) {
            setColorSchemeResources(R.color.blue_2, R.color.blue_3, R.color.blue_4)
            setDistanceToTriggerSync(300)
            setSize(SwipeRefreshLayout.DEFAULT)

            addOnLoadMoreListener {
                when (it) {
                    LOADING_BOTTOM -> {
                        requestState = LOADING_BOTTOM
                        keyword?.apply {
                            baseViewModel.onRequestSearch(keyword = this)
                        }
                    }

                    LOADING_TOP -> {
                        requestState = LOADING_TOP
                        keyword?.apply {
                            baseViewModel.onRequestSearch(0, this)
                        }
                    }
                }
            }
        }

        with(search_detailList) {
            adapter = searchAdapter
            addItemDecoration(MyDistanceDividerDecoration(DIRECTION_BOTTOM, DISTANCE))
        }
    }

    override fun initWithResume() {
        keyword?.apply {
            search_detailProgress.visibility = View.VISIBLE
            baseViewModel.onRequestSearch(0, this)
        }
    }
}
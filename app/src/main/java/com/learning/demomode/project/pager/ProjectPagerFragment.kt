package com.learning.demomode.project.pager

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.learning.common.base.BaseFragment
import com.learning.common.ui.*
import com.learning.common.weight.MToast
import com.learning.demomode.*
import com.learning.common.bean.ARTICLE_ID
import com.learning.common.bean.ArticleListBean
import com.learning.demomode.databinding.FragmentProjectPagerBinding
import kotlinx.android.synthetic.main.fragment_project_pager.*
import kotlinx.android.synthetic.main.fragment_project_pager.pager_list

class ProjectPagerFragment : BaseFragment<ProjectPagerViewModel, FragmentProjectPagerBinding>() {

    private val projectPagerAdapter by lazy { ProjectListAdapter() }
    private var requestState = LOADING_OTHERS
    private var cid = 0

    companion object {
        fun getInstance(cid: Int) = ProjectPagerFragment().apply {
            arguments = Bundle().apply {
                putInt(ARTICLE_ID, cid)
            }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_project_pager

    override fun initWithCreate() {
        baseViewModel.getProjectDetailData().observe(this, Observer {
            when (requestState) {
                LOADING_BOTTOM -> {
                    val tempList = ArrayList(projectPagerAdapter.getData())
                    tempList.addAll(it.datas)
                    baseViewModel.calculateDiff(projectPagerAdapter.getData(), tempList)
                    projectPagerAdapter.addMoreData(it.datas)
                }

                LOADING_TOP, LOADING_OTHERS -> {
                    baseViewModel.calculateDiff(projectPagerAdapter.getData(), it.datas)
                    projectPagerAdapter.updateData(it.datas)
                }
            }

            pager_progress.visibility = View.GONE
        })

        baseViewModel.getDiffResult().observe(this, Observer {
            it.dispatchUpdatesTo(pager_list.rAdapter as RecyclerView.Adapter<*>)
            pager_swipeView.setLoadingState(false)
        })

        baseViewModel.getRequestError().observe(this, Observer {
            for (key in it.keys) {
                Log.e(
                    TAG,
                    "Network Error [type: $key] -> code : ${it[key]?.statusCode} / msg : ${it[key]?.errorMsg}"
                )
                when (key) {
                    REQUEST_PROJECT_PAGER -> {
                        pager_progress.visibility = View.GONE
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
        arguments?.apply {
            cid = this.getInt(ARTICLE_ID)
        }

        with(pager_swipeView) {
            setColorSchemeResources(R.color.blue_2, R.color.blue_3, R.color.blue_4)
            setDistanceToTriggerSync(300)
            setSize(SwipeRefreshLayout.DEFAULT)
            addOnLoadMoreListener {
                when (it) {
                    LOADING_TOP -> {
                        requestState = LOADING_TOP
                        baseViewModel.requestProjectArticle(cid, 1)
                    }

                    LOADING_BOTTOM -> {
                        requestState = LOADING_BOTTOM
                        baseViewModel.requestProjectArticle(cid)
                    }
                }
            }
        }

        with(pager_list) {
            adapter = projectPagerAdapter
            layoutManager = LinearLayoutManager(context)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            addItemDecoration(
                MyDistanceDividerDecoration(
                    DIRECTION_BOTTOM,
                    DISTANCE
                )
            )
        }
    }

    override fun initWithResume() {
        if (baseViewModel.getProjectDetailData().value == null) {
            projectPagerAdapter.clearProjectList()
            baseViewModel.requestProjectArticle(cid, 1)
            pager_progress.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        Glide.get(safeContext).clearMemory()
        super.onDestroyView()
    }
}
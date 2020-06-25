package com.learning.demomode.officail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.learning.common.base.BaseFragment
import com.learning.common.ui.*
import com.learning.common.weight.MToast
import com.learning.demomode.*
import com.learning.common.bean.ArticleBean
import com.learning.common.bean.OfficialAuthorBean
import com.learning.demomode.databinding.FragmentOfficailBinding
import kotlinx.android.synthetic.main.fragment_officail.*
import com.learning.common.database.DemoDataBase
import com.learning.common.weight.Utility
import com.learning.demomode.R

class OfficialAccountFragment : BaseFragment<OfficialViewModel, FragmentOfficailBinding>() {
    private val requestData by lazy { ArrayList<ArticleBean>() }
    private val officialAdapter by lazy { OfficialListAdapter() }
    private val officialDB by lazy {
        DemoDataBase.getDataBase(safeContext)
    }

    private var requestState: Int = LOADING_OTHERS

    override fun layoutId(): Int = R.layout.fragment_officail

    override fun initWithCreate() {
        baseViewModel.setCategoryDao(officialDB)

        baseViewModel.getOfficialAuthorList().observe(this, Observer {
            if (it.isNotEmpty()) {
                baseViewModel.setSelectAccountId(it[0].id)
                official_progress.visibility = View.GONE
                setOfficialAuthor(ArrayList(it))
            }
        })

        baseViewModel.getOfficialArticleList().observe(this, Observer {
            official_progress.visibility = View.GONE

            when (requestState) {
                /**
                 *  上拉加载
                 *  先将原数据与新数据合并,通过DiffUtil.calculateDiff()计算新旧数据需要的最小更新量
                 *  因为计算操作属于耗时操作，故通过viewModel开启IO协程进行
                 *  */
                LOADING_BOTTOM -> {
                    requestData.clear()
                    requestData.addAll(officialAdapter.getData())
                    requestData.addAll(it)
                    baseViewModel.onCalculateDiff(officialAdapter.getData(), requestData)
                    officialAdapter.addMoreData(ArrayList(it))
                }
                /**
                 *  下拉刷新 、 切换作者
                 *  通过DiffUtil.calculateDiff()计算新旧数据需要的最小更新量
                 *  */
                LOADING_OTHERS, LOADING_TOP -> {
                    baseViewModel.onCalculateDiff(officialAdapter.getData(), ArrayList(it))
                    officialAdapter.updateData(ArrayList(it))
                }
            }
        })

        baseViewModel.getDiffResult().observe(this, Observer {
            it.dispatchUpdatesTo(official_list.rAdapter as RecyclerView.Adapter<*>)
            official_swipeView.setLoadingState(false)
        })

        baseViewModel.getRequestError().observe(this, Observer {
            for (key in it.keys) {
                Log.e(
                        TAG,
                        "Network Error [type: $key] -> code : ${it[key]?.statusCode} / msg : ${it[key]?.errorMsg}"
                )

                when (key) {
                    REQUEST_OFFICIAL_ALL -> {
                        official_progress.visibility = View.GONE
                    }
                    REQUEST_OFFICIAL_AUTHOR -> {
                        official_progress.visibility = View.GONE
                    }
                    REQUEST_OFFICIAL_ARTICLE -> {
                        official_progress.visibility = View.GONE
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
        with(official_list) {
            adapter = officialAdapter
            addItemDecoration(MyDistanceDividerDecoration(DIRECTION_BOTTOM, DISTANCE))
        }

        with(official_swipeView) {
            setColorSchemeResources(R.color.blue_2, R.color.blue_3, R.color.blue_4)
            setSize(SwipeRefreshLayout.DEFAULT)
            setDistanceToTriggerSync(300)

            addOnLoadMoreListener {
                when (it) {
                    LOADING_TOP -> {
                        requestState = LOADING_TOP
                        baseViewModel.onRequestArticle(baseViewModel.getSelectAccountId(), 1)
                    }

                    LOADING_BOTTOM -> {
                        requestState = LOADING_BOTTOM
                        baseViewModel.onRequestArticle(baseViewModel.getSelectAccountId())
                    }
                }
            }
        }
    }

    override fun initWithResume() {
        setOfficialAuthor(ArrayList(officialDB.officialAuthorDao().getCategoryAuthors()))

        if (Utility.isNetworkAvailable()) {
            baseViewModel.onRequestFirstPage()
        }
    }

    private fun setOfficialAuthor(list: ArrayList<OfficialAuthorBean>) {
        val nameList = ArrayList<String>()
        for (category in list) {
            if (category.id == baseViewModel.getSelectAccountId()) {
                official_author.text = category.name
            }
            nameList.add(category.name)
        }

        with(official_img) {
            setOnClickListener {
                val dialog = BottomSheetDialog(safeContext)
                val view =
                        LayoutInflater.from(safeContext).inflate(R.layout.item_sheet_dialog, null)
                val rv = view.findViewById<RecyclerView>(R.id.official_authorList)

                //设置Dialog弹出的最高高度
                val layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )

                layoutParams.height = (safeContext.resources.displayMetrics.heightPixels * 0.5).toInt()
                view.layoutParams = layoutParams

                val sheetAdapter = OfficialSheetAdapter(list).apply {
                    updateSelectId(baseViewModel.getSelectAccountId())
                    setExecuteClick {
                        if (list[it].id != baseViewModel.getSelectAccountId()) {
                            official_author.text = nameList[it]
                            official_progress.visibility = View.VISIBLE

                            requestState = LOADING_OTHERS
                            baseViewModel.setSelectAccountId(list[it].id)
                            baseViewModel.onRequestArticle(list[it].id, 1)
                        }
                        dialog.dismiss()
                    }
                }

                rv.apply {
                    adapter = sheetAdapter
                    setItemViewCacheSize(20)
                    addItemDecoration(MyDistanceDividerDecoration(DIRECTION_BOTTOM, DISTANCE / 2))
                }

                dialog.setContentView(view)
                //由于它上面蒙了一层布局 design_bottom_sheet是系统的布局，设置为全透明就好了
                dialog.delegate.findViewById<FrameLayout>(R.id.design_bottom_sheet)
                        ?.setBackgroundColor(safeContext.resources.getColor(R.color.transparent, null))

                dialog.show()
            }
        }
    }
}
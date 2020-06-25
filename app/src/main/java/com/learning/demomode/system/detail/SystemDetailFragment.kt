package com.learning.demomode.system.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.learning.common.base.BaseFragment
import com.learning.common.ui.*
import com.learning.common.bean.ARTICLE_ID
import com.learning.common.bean.ARTICLE_TITLE
import com.learning.common.bean.ArticleBean
import com.learning.demomode.R
import com.learning.demomode.databinding.FragmentSysDetailBinding
import kotlinx.android.synthetic.main.fragment_sys_detail.*

class SystemDetailFragment : BaseFragment<SystemDetailViewModel, FragmentSysDetailBinding>() {
    private val systemDetailAdapter by lazy { SystemDetailListAdapter() }

    private var cid: Int = 0
    private var requestState: Int = LOADING_OTHERS

    override fun layoutId(): Int = R.layout.fragment_sys_detail

    override fun initWithCreate() {
        baseViewModel.getSystemArticle().observe(this, Observer {
            system_detailProgress.visibility = View.GONE

            when (requestState) {
                /**
                 *  上拉加载
                 *  先将原数据与新数据合并,通过DiffUtil.calculateDiff()计算新旧数据需要的最小更新量
                 *  因为计算操作属于耗时操作，故通过viewModel开启IO协程进行
                 *  */
                LOADING_BOTTOM -> {
                    val requestData = ArrayList<ArticleBean>()
                    requestData.addAll(systemDetailAdapter.getSysDetailData())
                    requestData.addAll(it.datas)
                    baseViewModel.onCalculateDiff(
                        ArrayList(systemDetailAdapter.getSysDetailData()),
                        requestData
                    )
                    systemDetailAdapter.addMoreArticle(it.datas)
                }
                /**
                 *  下拉刷新 、 切换作者
                 *  通过DiffUtil.calculateDiff()计算新旧数据需要的最小更新量
                 *  */
                LOADING_OTHERS, LOADING_TOP -> {
                    baseViewModel.onCalculateDiff(ArrayList(systemDetailAdapter.getSysDetailData()), ArrayList(it.datas))
                    systemDetailAdapter.updateArticle(it.datas)
                }
            }
        })

        baseViewModel.getDiffResult().observe(this, Observer {
            it.dispatchUpdatesTo(system_detailList.rAdapter as RecyclerView.Adapter<*>)
            system_swipeView.setLoadingState(false)
        })
    }

    override fun initWithViewCreate(savedInstanceState: Bundle?) {
        arguments?.apply {
            cid = this.getInt(ARTICLE_ID)
            setTitle = this.getString(ARTICLE_TITLE)
            onUpdateToolbar()
        }

        with(system_swipeView) {
            setColorSchemeResources(R.color.blue_2, R.color.blue_3, R.color.blue_4)
            setSize(SwipeRefreshLayout.DEFAULT)
            setDistanceToTriggerSync(300)

            addOnLoadMoreListener {
                when (it) {
                    LOADING_TOP -> {
                        requestState = LOADING_TOP
                        baseViewModel.onRequestArticle(cid, 0)
                    }

                    LOADING_BOTTOM -> {
                        requestState = LOADING_BOTTOM
                        baseViewModel.onRequestArticle(cid)
                    }
                }
            }
        }

        with(system_detailList) {
            adapter = systemDetailAdapter

            addItemDecoration(
                MyDistanceDividerDecoration(
                    DIRECTION_BOTTOM,
                    DISTANCE
                )
            )
        }
    }

    override fun initWithResume() {
        system_detailProgress.visibility = View.VISIBLE
        baseViewModel.onRequestArticle(cid, 0)
    }
}
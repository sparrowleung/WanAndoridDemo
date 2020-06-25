package com.learning.demomode.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.bingoogolapple.bgabanner.BGABanner
import com.learning.common.weight.MToast
import com.learning.common.base.BaseFragment
import com.learning.common.ui.*
import com.learning.common.webview.CommonWebParams
import com.learning.common.weight.GlideClient
import com.learning.common.weight.PerformLinkCenter
import com.learning.demomode.*
import com.learning.common.bean.ArticleBean
import com.learning.demomode.databinding.FragmentHomeBinding
import com.learning.common.bean.BannerBean
import com.learning.common.database.DemoDataBase
import com.learning.common.weight.Utility
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    private lateinit var bannerLayout: View

    private var requestState = LOADING_OTHERS
    private var headerBanner: BGABanner? = null
    private val homeListAdapter by lazy { HomeListAdapter() }
    private val homeDB by lazy { DemoDataBase.getDataBase(safeContext) }
    /**
     *   因为banner作为headerView添加到RecyclerView上，使用DiffUtil更新时，需要添加一个伪数据在list头部
     *   避免数据错乱
     */
    private val tempBean by lazy {
        ArticleBean(
            1, 2, false, 3, 4, "", "", "", "", "", "",
            5, "", "", "", "", 6, false, false, "", "",
            "", "", 0, 7, ""
        )
    }

    override fun layoutId(): Int = R.layout.fragment_home

    override fun initWithCreate() {
        baseViewModel.initHomeDao(homeDB)

        baseViewModel.getTopAndFirstPageData().observe(this, Observer {
            val oldData = ArrayList<ArticleBean>()
            oldData.add(tempBean)
            oldData.addAll(homeListAdapter.getData())

            val newData = ArrayList<ArticleBean>()
            newData.add(tempBean)
            newData.addAll(it)

            baseViewModel.calculateDiff(oldData, newData)
            homeListAdapter.updateData(it)
    })

        baseViewModel.getNormalData().observe(this, Observer {
            if (requestState == LOADING_BOTTOM) {
                val oldData = ArrayList<ArticleBean>()
                oldData.add(tempBean)
                oldData.addAll(homeListAdapter.getData())

                val newData = ArrayList<ArticleBean>()
                newData.add(tempBean)
                newData.addAll(homeDB.homeArticleTopDao().getTopArticle())
                newData.addAll(it)

                baseViewModel.calculateDiff(oldData, newData)
                val finalData = ArrayList(newData)
                finalData.removeAt(0)
                homeListAdapter.updateData(finalData)
            }
        })

        baseViewModel.getBannerData().observe(this, Observer {
            setBannerView(ArrayList(it))
            bannerLayout.findViewById<ProgressBar>(R.id.home_bannerProgress).visibility =
                View.GONE
        })

        baseViewModel.getDiffResult().observe(this, Observer {
            it.dispatchUpdatesTo(home_list.rAdapter as RecyclerView.Adapter<*>)
            home_swipeView.setLoadingState(false)
        })

        baseViewModel.getRequestError().observe(this, Observer {
            for (key in it.keys) {
                Log.e(
                    TAG,
                    "Network Error [type: $key] -> code : ${it[key]?.statusCode} / msg : ${it[key]?.errorMsg}"
                )
                when (key) {
                    REQUEST_ARTICLE_TOP -> {
                        bannerLayout.findViewById<ProgressBar>(R.id.home_bannerProgress)
                            .visibility = View.GONE
                        home_progress.visibility = View.GONE
                        home_swipeView.setLoadingState(false)
                    }

                    REQUEST_ARTICLE ->{
                        home_swipeView.setLoadingState(false)
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
        bannerLayout = LayoutInflater.from(safeContext).inflate(R.layout.item_banner, null)
        headerBanner = bannerLayout.findViewById(R.id.home_banner)

        with(home_swipeView) {
            setDistanceToTriggerSync(300)
            setColorSchemeResources(R.color.blue_2, R.color.blue_3, R.color.blue_4)
            setSize(SwipeRefreshLayout.DEFAULT)
            addOnLoadMoreListener {
                when (it) {
                    LOADING_BOTTOM -> {
                        requestState = LOADING_BOTTOM
                        baseViewModel.onRequestNormalArticle()
                    }

                    LOADING_TOP -> {
                        requestState = LOADING_TOP
                        baseViewModel.onRequestTopArticle()
                    }
                }
            }
        }
    }

    override fun initWithResume() {
        setBannerView(ArrayList(homeDB.homeBannerDao().getBanners()))

        val firstPageList = ArrayList<ArticleBean>()
        firstPageList.addAll(homeDB.homeArticleTopDao().getTopArticle())
        firstPageList.addAll(homeDB.homeArticleDao().getArticlesForAdv(19))
        homeListAdapter.updateData(firstPageList)

        with(home_list) {
            setHeaderView(bannerLayout)
            adapter = homeListAdapter

            addItemDecoration(
                MyDistanceDividerDecoration(
                    DIRECTION_BOTTOM,
                    DISTANCE
                )
            )
        }

        if (Utility.isNetworkAvailable()) {
            baseViewModel.onRequestTopArticle()
        }
    }

    private fun setBannerView(list: ArrayList<BannerBean>) {
        headerBanner?.apply {
            if (list.size <= 1) this.setAutoPlayAble(false)

            setDelegate { _, _, model, _ ->
                val bundle = Bundle()
                bundle.putString(CommonWebParams.TITLE.name, (model as BannerBean).title)
                PerformLinkCenter.instance.performLink(activity, model.url, bundle)
            }

            setAdapter { _, itemView, model, _ ->
                GlideClient.create(
                    safeContext,
                    (model as BannerBean).imagePath,
                    itemView as ImageView
                )
            }

            setData(list, null)
        }
    }
}
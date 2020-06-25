package com.learning.demomode.home

import androidx.recyclerview.widget.DiffUtil
import com.learning.common.retrofit.RetrofitClient
import com.learning.demomode.*
import com.learning.common.base.BaseResult
import com.learning.common.bean.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class HomeModel(private val viewModel: HomeViewModel) {

    suspend fun requestArticle(page: Int) {
        val result = RetrofitClient.create(IAppRequestService::class.java)
            .getHomeArticle(page)
        onResponseSuccess(result, REQUEST_ARTICLE)
        viewModel.setArticlePageNum(page + 1)
    }

    suspend fun requestBanner(): BaseResult<List<BannerBean>> =
        RetrofitClient.create(IAppRequestService::class.java).getHomeBanner()

    suspend fun requestTop(): BaseResult<ArrayList<HomeTopArticleBean>> =
        RetrofitClient.create(IAppRequestService::class.java).getHomeArticleTop()

    suspend fun requestFirstArticle(): BaseResult<HomeArticleListBean> =
        RetrofitClient.create(IAppRequestService::class.java)
            .getHomeArticle(0)

    fun calculateListDiff(
        oldData: ArrayList<ArticleBean>,
        newData: ArrayList<ArticleBean>
    ) {
        val result = DiffUtil.calculateDiff(MyDiffUtilCallBack(oldData, newData), true)
        viewModel.setDiffResult(result)
    }

    suspend fun handleTopResponse(
        top: ArrayList<HomeTopArticleBean>,
        article: ArrayList<HomeArticleBean>,
        banner: List<BannerBean>
    ) = withContext(Dispatchers.Main) {
        viewModel.setBannerData(banner)
        delay(500)
        viewModel.setTopData(top)
        viewModel.setNormalData(article)

        val topAndFirstList = ArrayList<ArticleBean>()
        topAndFirstList.addAll(top)
        topAndFirstList.addAll(article)
        viewModel.setTopAndFirstPageData(topAndFirstList)
    }

    private suspend fun <T : Any> onResponseSuccess(responseBody: T, responseId: Int) =
        withContext(Dispatchers.Main) {
            val result = responseBody as BaseResult<T>
            when (responseId) {
                REQUEST_ARTICLE -> {
                    val dataList = result.data as HomeArticleListBean
                    viewModel.setNormalData(dataList.datas)
                }
            }
        }
}
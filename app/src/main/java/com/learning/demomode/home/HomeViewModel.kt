package com.learning.demomode.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.learning.common.base.BaseViewModel
import com.learning.common.bean.*
import com.learning.common.retrofit.VocError
import com.learning.demomode.*
import com.learning.common.database.DemoDataBase
import kotlinx.coroutines.async

class HomeViewModel : BaseViewModel() {
    private var articlePageNum = 1
    private var homeModel = HomeModel(this)

    private var diffResult: MutableLiveData<DiffUtil.DiffResult> = MutableLiveData()
    private var requestError: MutableLiveData<Map<Int, VocError>> = MutableLiveData()
    private var topArticleData: MutableLiveData<ArrayList<ArticleBean>> = MutableLiveData()

    private lateinit var homeArticleDao: DemoDataBase

    fun initHomeDao(dao: DemoDataBase) {
        homeArticleDao = dao
    }

    fun onRequestNormalArticle(page: Int = articlePageNum) {
        vmScopeMainWithException(
            { homeModel.requestArticle(page) },
            { requestError.value = mapOf(REQUEST_ARTICLE to it) }
        )
    }

    fun onRequestTopArticle() {
        vmScopeMainWithException(
            {
                val banner = async { homeModel.requestBanner() }
                val top = async { homeModel.requestTop() }
                val normal = async { homeModel.requestFirstArticle() }

                articlePageNum = 1

                homeModel.handleTopResponse(
                    top.await().data,
                    normal.await().data.datas,
                    banner.await().data
                )
            },
            { requestError.value = mapOf(REQUEST_ARTICLE_TOP to it) }
        )
    }

    fun calculateDiff(oldData: ArrayList<ArticleBean>, newData: ArrayList<ArticleBean>) {
        vmScopeIO { homeModel.calculateListDiff(oldData, newData) }
    }

    fun getNormalData(): LiveData<List<HomeArticleBean>> =
        homeArticleDao.homeArticleDao().getArticleList()

    fun getTopData(): LiveData<List<HomeTopArticleBean>> =
        homeArticleDao.homeArticleTopDao().getTopArticleList()

    fun getTopAndFirstPageData(): LiveData<ArrayList<ArticleBean>> = topArticleData

    fun getBannerData(): LiveData<List<BannerBean>> = homeArticleDao.homeBannerDao().getBannerList()

    fun getRequestError(): LiveData<Map<Int, VocError>> = requestError

    fun getDiffResult(): LiveData<DiffUtil.DiffResult> = diffResult

    fun setNormalData(articleBean: ArrayList<HomeArticleBean>) {
        homeArticleDao.homeArticleDao().insertList(articleBean)
    }

    fun setTopData(topBean: ArrayList<HomeTopArticleBean>) {
        homeArticleDao.homeArticleTopDao().insertList(topBean)
    }

    fun setTopAndFirstPageData(topFirstBean: ArrayList<ArticleBean>) {
        topArticleData.value = topFirstBean
    }

    fun setBannerData(bannerBean: List<BannerBean>) {
        homeArticleDao.homeBannerDao().insertList(bannerBean)
    }

    fun updateBannerData(bannerBean: List<BannerBean>) {
        homeArticleDao.homeBannerDao().updateList(bannerBean)
    }

    fun setArticlePageNum(num: Int) {
        articlePageNum = num
    }

    fun setDiffResult(result: DiffUtil.DiffResult) {
        diffResult.postValue(result)
    }
}
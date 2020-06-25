package com.learning.demomode.system.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.learning.common.base.BaseViewModel
import com.learning.common.retrofit.VocError
import com.learning.common.bean.ArticleBean
import com.learning.common.bean.HomeArticleListBean
import com.learning.demomode.MyDiffUtilCallBack
import com.learning.demomode.REQUEST_SYSTEM_ARTICLE

class SystemDetailViewModel : BaseViewModel() {
    private var articlePageNum: Int = 0
    private val model = SystemDetailModel(this)

    private val systemArticleList: MutableLiveData<HomeArticleListBean> = MutableLiveData()
    private val requestError: MutableLiveData<Map<Int, VocError>> = MutableLiveData()
    private val diffResult: MutableLiveData<DiffUtil.DiffResult> = MutableLiveData()

    fun onRequestArticle(cid: Int, page: Int = articlePageNum) {
        vmScopeMainWithException(
            { model.onRequestArticle(cid, page) },
            { requestError.value = mapOf(REQUEST_SYSTEM_ARTICLE to it) }
        )
    }

    fun onCalculateDiff(
        oldData: ArrayList<ArticleBean> = ArrayList(),
        newData: ArrayList<ArticleBean> = ArrayList()
    ) {
        vmScopeIO {
            diffResult.postValue(DiffUtil.calculateDiff(MyDiffUtilCallBack(oldData, newData)))
        }
    }

    fun getSystemArticle(): LiveData<HomeArticleListBean> = systemArticleList

    fun getDiffResult(): LiveData<DiffUtil.DiffResult> = diffResult

    fun setSystemArticle(list: HomeArticleListBean) {
        systemArticleList.value = list
    }

    fun setArticlePageNum(page: Int) {
        articlePageNum = page
    }
}
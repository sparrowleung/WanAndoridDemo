package com.learning.demomode.search.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.learning.common.base.BaseViewModel
import com.learning.common.retrofit.VocError
import com.learning.demomode.MyDiffUtilCallBack
import com.learning.demomode.REQUEST_SEARCH
import com.learning.common.bean.ArticleBean
import com.learning.common.bean.HomeArticleBean

class SearchDetailViewModel : BaseViewModel() {

    private val searchModel = SearchDetailModel(this)
    private var pageNumber: Int = 0
    private val diffResult: MutableLiveData<DiffUtil.DiffResult> = MutableLiveData()
    private val requestError: MutableLiveData<Map<Int, VocError>> = MutableLiveData()
    private val searchResult: MutableLiveData<ArrayList<HomeArticleBean>> = MutableLiveData()

    fun onRequestSearch(page: Int = pageNumber, keyword: String) {
        vmScopeMainWithException(
            { searchModel.requestSearch(page, keyword) },
            { requestError.value = mapOf(REQUEST_SEARCH to it) }
        )
    }

    fun calculateDiff(oldData: ArrayList<ArticleBean>, newData: ArrayList<ArticleBean>) {
        vmScopeIO {
            diffResult.postValue(DiffUtil.calculateDiff(MyDiffUtilCallBack(oldData, newData), true))
        }
    }

    fun getSearchResult(): LiveData<ArrayList<HomeArticleBean>> = searchResult

    fun getDiffResult(): LiveData<DiffUtil.DiffResult> = diffResult

    fun getRequestError(): LiveData<Map<Int, VocError>> = requestError

    fun setSearchResult(list: ArrayList<HomeArticleBean>) {
        searchResult.value = list
    }

    fun setPageNumbers(num: Int) {
        pageNumber = num
    }
}
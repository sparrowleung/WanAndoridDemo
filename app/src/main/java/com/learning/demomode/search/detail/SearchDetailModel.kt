package com.learning.demomode.search.detail

import com.learning.common.base.BaseResult
import com.learning.common.retrofit.RetrofitClient
import com.learning.demomode.IAppRequestService
import com.learning.common.bean.HomeArticleListBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchDetailModel(private val viewModel: SearchDetailViewModel) {

    suspend fun requestSearch(page: Int, keyword: String) {
        val result =
            RetrofitClient.create(IAppRequestService::class.java).getSearchData(page, keyword)
        onResponseSuccess(result)
        viewModel.setPageNumbers(page + 1)
    }

    private suspend fun <T : Any> onResponseSuccess(responseBody: T, statusCode: Int = 0) =
        withContext(Dispatchers.Main) {
            val result = responseBody as BaseResult<T>
            when (result.data) {
                is HomeArticleListBean -> {
                    val dataList = result.data as HomeArticleListBean
                    viewModel.setSearchResult(dataList.datas)
                }
            }
        }
}
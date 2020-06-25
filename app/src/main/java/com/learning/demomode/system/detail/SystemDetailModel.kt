package com.learning.demomode.system.detail

import com.learning.common.retrofit.RetrofitClient
import com.learning.common.base.BaseResult
import com.learning.common.bean.HomeArticleListBean
import com.learning.demomode.IAppRequestService

class SystemDetailModel(private val viewModel: SystemDetailViewModel) {
    suspend fun onRequestArticle(cid: Int, page: Int) {
        val result =
            RetrofitClient.create(IAppRequestService::class.java).getSystemArticle(page, cid)
        handleResponse(result)
        viewModel.setArticlePageNum(page + 1)
    }

    private fun <T : Any> handleResponse(result: T, statusCod: Int = 0) {
        val response = result as BaseResult<T>
        when (response.data) {
            is HomeArticleListBean -> {
                val data = response.data as HomeArticleListBean
                viewModel.setSystemArticle(data)
            }
        }
    }
}
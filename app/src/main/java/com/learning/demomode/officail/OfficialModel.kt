package com.learning.demomode.officail

import com.learning.common.retrofit.RetrofitClient
import com.learning.common.base.BaseResult
import com.learning.demomode.IAppRequestService
import com.learning.common.bean.OfficialArticleListBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfficialModel(private val viewModel: OfficialViewModel) {


    suspend fun onRequestArticle(id: Int, page: Int) {
        val result = RetrofitClient.create(IAppRequestService::class.java)
            .getOfficialArticle(id, page)
        handleResponseData(result)
        viewModel.setArticlePageNum(page + 1)
    }

    private suspend fun <T> handleResponseData(result: T, statusCode: Int = 0) =
        withContext(Dispatchers.Main) {
            val response = result as BaseResult<T>
            when (response.data) {
                is OfficialArticleListBean -> {
                    val data = response.data as OfficialArticleListBean
                    viewModel.setOfficialArticleList(data.datas)
                }
            }
        }
}
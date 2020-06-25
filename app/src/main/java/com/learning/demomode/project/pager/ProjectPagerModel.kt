package com.learning.demomode.project.pager

import com.learning.common.retrofit.RetrofitClient
import com.learning.common.bean.ArticleListBean
import com.learning.common.base.BaseResult
import com.learning.demomode.IAppRequestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectPagerModel(private val viewModel: ProjectPagerViewModel) {
    suspend fun onRequestProjectDetail(cid: Int, page: Int) {
        val result = RetrofitClient.create(IAppRequestService::class.java)
            .getProjectPagerData(page, cid)
        handleProjectDetailData(result)
        viewModel.setProjectPageNum(page + 1)
    }

    private suspend fun <T : Any> handleProjectDetailData(
        result: BaseResult<T>,
        statusCode: Int = 0
    ) =
        withContext(Dispatchers.Main) {
            when (result.data) {
                is ArticleListBean -> {
                    viewModel.setProjectDetailData(result.data as ArticleListBean)
                }
            }
        }
}
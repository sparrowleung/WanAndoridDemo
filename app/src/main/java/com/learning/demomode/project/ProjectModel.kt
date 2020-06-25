package com.learning.demomode.project

import com.learning.common.retrofit.RetrofitClient
import com.learning.common.base.BaseResult
import com.learning.demomode.IAppRequestService
import com.learning.common.bean.ProjectCategoryBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectModel(private val viewModel: ProjectViewModel) {
    suspend fun onRequestProject() {
        val result = RetrofitClient.create(IAppRequestService::class.java)
            .getProjectData()
        handleProjectResponse(result)
    }

    private suspend fun <T : Any> handleProjectResponse(
        result: BaseResult<T>,
        statusCode: Int = 0
    ) =
        withContext(Dispatchers.Main) {
            when (result.data) {
                is ArrayList<*> -> {
                    viewModel.setProjectCategory(result.data as ArrayList<ProjectCategoryBean>)
                }
            }
        }

}
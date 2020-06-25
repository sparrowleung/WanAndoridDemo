package com.learning.demomode.system

import com.learning.common.retrofit.RetrofitClient
import com.learning.common.base.BaseResult
import com.learning.demomode.IAppRequestService
import com.learning.common.bean.SystemCategoryBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SystemModel(private val viewModel: SystemViewModel) {
    suspend fun onRequestSystemData() {
        val result = RetrofitClient.create(IAppRequestService::class.java)
            .getSystemData()
        handleSystemResponse(result)
    }

    private suspend fun <T : Any> handleSystemResponse(result: BaseResult<T>, statusCode: Int = 0) =
        withContext(Dispatchers.Main) {
            when (result.data) {
                is ArrayList<*> -> {
                    val response = result.data as ArrayList<SystemCategoryBean>
                    viewModel.setSystemData(response)
                }
            }
        }
}
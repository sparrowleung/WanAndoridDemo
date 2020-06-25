package com.learning.common.base

import androidx.lifecycle.*
import com.learning.common.retrofit.ExceptionHandle
import com.learning.common.retrofit.VocError
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class BaseViewModel : AndroidViewModel(BaseApplication.instance), LifecycleObserver {

    /**
     *   ViewModelScope可以管理协程的生命周期
     *   当ViewModel被Destroy时，会直接clear()函数遍历取消viewModelScope的协程
     *   viewModelScope默认使用Dispatchers.Main
     * */
    fun vmScopeIO(io: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(Dispatchers.IO) { io() }

    fun vmScopeMain(main: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { main() }
    }

    /**
    *  流的模式进行网络请求
    * */
    fun <T : Any> vmScopeWithFlow(scope: suspend () -> T): Flow<T> {
        return flow { emit(scope()) }
    }

    /**
     *  对返回结果不过滤
     * @param block 请求体
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     * */
    fun vmScopeIOWithException(
        scope: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(VocError) -> Unit
    ) {
        vmScopeIO {
            handleWithException(
                scope,
                { error(it) }
            )
        }
    }

    fun vmScopeMainWithException(
        scope: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(VocError) -> Unit
    ) {
        vmScopeMain {
            handleWithException(
                withContext(Dispatchers.IO) { scope },
                { error(it) }
            )
        }
    }

    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun <T> vmScopeWithExcute(
        scope: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: (T) -> Unit,
        error: (VocError) -> Unit
    ) {
        vmScopeMain {
            handleWithException(
                { withContext(Dispatchers.IO) { scope() } },
                { res ->
                    executeResponse(res) { success(it) }
                },
                { error(it) }
            )
        }
    }

    private suspend fun <T> executeResponse(
        response: IBaseResponse<T>,
        success: suspend CoroutineScope.(T) -> Unit
    ) {
        coroutineScope {
            if (response.isSuccess()) success(response.handleResponse())
            else throw Throwable(response.handleErrorMsg())
        }
    }

    private suspend fun <T> handleWithException(
        scope: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: suspend CoroutineScope.(IBaseResponse<T>) -> Unit,
        error: suspend CoroutineScope.(VocError) -> Unit
    ) {
        coroutineScope {
            try {
                success(scope())
            } catch (e: Throwable) {
                error(ExceptionHandle.handle(e))
            }
        }
    }

    private suspend fun handleWithException(
        scope: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(VocError) -> Unit
    ) {
        coroutineScope {
            try {
                scope()
            } catch (e: Throwable) {
                error(ExceptionHandle.handle(e))
            }
        }
    }
}
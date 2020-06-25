package com.learning.common.exception

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class HandleCoroutineException : CoroutineExceptionHandler {
    companion object {
        fun getInstance(): CoroutineExceptionHandler =
            HandleCoroutineException()
    }

    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Log.e("HandleCoroutineException", "Coroutine exception: ${exception.message}")
    }
}
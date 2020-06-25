package com.learning.common.retrofit

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import retrofit2.Response
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.getOrNull
import java.io.IOException
import java.lang.NullPointerException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val ERROR_SOCKET_EXCEPTION = -101
const val ERROR_CONNECT = -102
const val ERROR_SSL_HANDSHAKE = -103
const val ERROR_HTTP = -104
const val ERROR_UNKNOWN_HOST = -105
const val ERROR_EMPTY_BODY = -106
const val ERROR_SERVER_IO = -107
const val ERROR_TIMEOUT = -108
const val ERROR_UNEXPECTED = -176
const val ERROR_UNKNOWN = -178

const val ERROR_NETWORK = 504
const val ERROR_NOT_FOUND = 404

data class VocError(val statusCode: Int, val errorMsg: String?)

suspend fun <T : Any> ILifecycleCall<T>.await() : Result<T>{
    return suspendCoroutine {
        onEnqueue(object : ILifecycleCallback<T> {
            override fun onSuccess(statusCode: Int, result: Response<T>) {
                it.resumeWith(runCatching {
                    if (result.isSuccessful) {
                        val body = result.body()
                        if (body == null) {
                            Result.Exception(NullPointerException("Response body is null"))
                        } else {
                            Result.Ok(body, result.raw())
                        }
                    } else {
                        Result.Error(HttpException(result), result.raw())
                    }
                })
            }

            override fun onError(statusCode: Int, throwable: Throwable) {
              it.resume(Result.Exception(throwable))
            }

            override fun onComplete() {}
        })
    }
}

fun <T : Any> Result<T>.handleResult(
    success: (statusCode: Int, response: T) -> Unit,
    fail: (statusCode: Int, error: VocError) -> Unit
) {
    val body = getOrNull()
    if (body == null) {
        when (this) {
            is Result.Error -> {
                fail(exception.code(), VocError(exception.code(), exception.message))
            }

            is Result.Exception -> {
                when (val result = exception) {
                    is IOException -> {
                        fail(ERROR_SERVER_IO, VocError(ERROR_SERVER_IO, result.message))
                    }
                    is SocketException -> {
                        fail(
                            ERROR_SOCKET_EXCEPTION,
                            VocError(ERROR_SOCKET_EXCEPTION, result.message)
                        )
                    }
                    is ConnectException -> {
                        fail(ERROR_CONNECT, VocError(ERROR_CONNECT, result.message))
                    }
                    is SSLHandshakeException -> {
                        fail(ERROR_SSL_HANDSHAKE, VocError(ERROR_SSL_HANDSHAKE, result.message))
                    }
                    is HttpException -> {
                        fail(result.code(), VocError(result.code(), result.message))
                    }
                    is UnknownHostException -> {
                        fail(ERROR_UNKNOWN_HOST, VocError(ERROR_UNKNOWN_HOST, result.message))
                    }
                    is NullPointerException -> {
                        fail(ERROR_EMPTY_BODY, VocError(ERROR_EMPTY_BODY, result.message))
                    }
                    else -> {
                        fail(ERROR_UNEXPECTED, VocError(ERROR_UNEXPECTED, result.message))
                    }
                }
            }
        }
    } else {
        if (this is Result.Ok) {
            when (val resultCode = response.code) {
                in 200..399 -> {
                    try {
                        success(resultCode, body)
                    } catch (e: IOException) {
                        Log.e("HS", e.message, e)
                        fail(
                            ERROR_SERVER_IO,
                            VocError(ERROR_SERVER_IO, "server response IOException")
                        )
                    }
                }
                401 -> {
                    fail(resultCode, VocError(resultCode, "Unauthenticated error"))
                }
                in 400..499 -> {
                    fail(resultCode, VocError(resultCode, "Client error"))
                }
                in 500..599 -> {
                    fail(resultCode, VocError(resultCode, "Server error"))
                }
                else -> {
                    fail(resultCode, VocError(resultCode, "Unexpected error"))
                }
            }
        }
    }
}
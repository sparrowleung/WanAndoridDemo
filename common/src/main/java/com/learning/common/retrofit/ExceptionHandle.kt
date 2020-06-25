package com.learning.common.retrofit

import retrofit2.HttpException
import java.io.IOException
import java.lang.NullPointerException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

object ExceptionHandle {

    fun handle(e: Throwable): VocError {
        return when (e) {
            is IOException -> {
                VocError(ERROR_SERVER_IO, e.message)
            }
            is SocketException -> {
                VocError(ERROR_SOCKET_EXCEPTION, e.message)
            }
            is ConnectException -> {
                VocError(ERROR_CONNECT, e.message)
            }
            is SSLHandshakeException -> {
                VocError(ERROR_SSL_HANDSHAKE, e.message)
            }
            is HttpException -> {
                VocError(ERROR_HTTP, e.message)
            }
            is UnknownHostException -> {
                VocError(ERROR_UNKNOWN_HOST, e.message)
            }
            is NullPointerException -> {
                VocError(ERROR_EMPTY_BODY, e.message)
            }
            else -> {
                VocError(ERROR_UNEXPECTED, e.message)
            }
        }
    }
}
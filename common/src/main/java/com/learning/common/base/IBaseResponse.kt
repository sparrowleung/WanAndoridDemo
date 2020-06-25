package com.learning.common.base

interface IBaseResponse<T> {
    fun handleErrorCode(): Int
    fun handleErrorMsg(): String
    fun handleResponse(): T
    fun isSuccess(): Boolean
}
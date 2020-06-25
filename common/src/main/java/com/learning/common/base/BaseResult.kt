package com.learning.common.base

data class BaseResult<T>(
    var errorMsg: String,
    var errorCode: Int,
    var data: T
) : IBaseResponse<T> {
    override fun handleErrorCode(): Int = errorCode

    override fun handleErrorMsg(): String = errorMsg

    override fun handleResponse(): T = data

    override fun isSuccess(): Boolean = errorCode == 0
}
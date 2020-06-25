package com.learning.common.retrofit

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LifecycleCallAdapterFactory(
    private val enableCancel: Boolean = true
) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        check(returnType is ParameterizedType) { "Call must be have generic type" }

        val responseType = getParameterUpperBound(0, returnType)

        return LifecycleCallAdapter<Any>(responseType, enableCancel = enableCancel)
    }
}
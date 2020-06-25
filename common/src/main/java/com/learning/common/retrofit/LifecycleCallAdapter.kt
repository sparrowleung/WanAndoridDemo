package com.learning.common.retrofit

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class LifecycleCallAdapter<R>(
    private val responseType: Type,
    private val enableCancel: Boolean
) : CallAdapter<R, ILifecycleCall<R>> {

    override fun adapt(call: Call<R>): ILifecycleCall<R> {
        return LifecycleCallImp(call, enableCancel)
    }

    override fun responseType(): Type {
        return responseType
    }
}
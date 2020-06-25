package com.learning.common.retrofit

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LifecycleCallImp<T>(
    private val callImp: Call<T>,
    private val enableCancel: Boolean
) : ILifecycleCall<T> {
    override fun clone(): ILifecycleCall<T> {
        return LifecycleCallImp(callImp.clone(), enableCancel)
    }

    override fun cancel() {
        Log.e("LifecycleCallImp", "cancel")
        callImp.cancel()
    }

    override fun isCanceled(): Boolean {
        return callImp.isCanceled
    }

    override fun enableCancel(): Boolean {
        return enableCancel
    }

    override fun onEnqueue(callback: ILifecycleCallback<T>) {
        callImp.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onError(0, t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onSuccess(response.code(), response)
            }
        })
    }
}
package com.learning.common.retrofit

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import retrofit2.Response

interface ILifecycleCallback<T> {
    fun onSuccess(statusCode: Int, result: Response<T>)
    fun onError(statusCode: Int, throwable: Throwable)
    fun onComplete()
}

interface ILifecycleStateObserver : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.e("ILifecycleStateObserver", "onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.e("ILifecycleStateObserver", "onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.e("ILifecycleStateObserver", "onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.e("ILifecycleStateObserver", "onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.e("ILifecycleStateObserver", "onDestroy")
    }
}

interface ILifecycleCall<T> : ILifecycleStateObserver {
    fun cancel()
    fun isCanceled(): Boolean
    fun enableCancel(): Boolean
    fun clone(): ILifecycleCall<T>
    fun onEnqueue(callback: ILifecycleCallback<T>)



}
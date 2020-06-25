package com.learning.common.exception

import android.util.Log
import java.lang.Thread.UncaughtExceptionHandler

//For threads.
class GlobalThreadUncaughtExceptionHandler : UncaughtExceptionHandler {

    companion object {
        fun setUp() {
            Thread.setDefaultUncaughtExceptionHandler(GlobalThreadUncaughtExceptionHandler())
        }
    }

    //Don't use lazy here.
    private val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.e("ThreadUncaughtException", " thread: ${t.name} - error ->${e.message}")
//        defaultUncaughtExceptionHandler?.uncaughtException(t, e)
    }
}
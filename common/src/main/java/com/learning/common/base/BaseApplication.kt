package com.learning.common.base

import android.app.Application
import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.learning.common.exception.GlobalThreadUncaughtExceptionHandler
import kotlin.properties.Delegates

class BaseApplication : Application() {

    companion object {
        var instance: Application by Delegates.notNull()
            private set
        var appContext: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = this.baseContext

        initARouter()
    }

    private fun initARouter() {

        ARouter.openLog()
        ARouter.openDebug()
        GlobalThreadUncaughtExceptionHandler.setUp()
        ARouter.init(this)
    }


}
package com.learning.common.provider

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.learning.common.router.Router

class AppService {
    companion object {
        fun getInstance() = AppService()
    }

    private val appModuleService: IAppModuleService by lazy {
        ARouter.getInstance().build(Router.PROVIDER_APP_ROUTER).navigation() as IAppModuleService
    }

    fun routerToApp(
        context: Context?,
        actionLink: String?,
        host: String?,
        path: String?,
        bundle: Bundle?
    ) {
        appModuleService.router(context, actionLink, host, path, bundle)
    }
}
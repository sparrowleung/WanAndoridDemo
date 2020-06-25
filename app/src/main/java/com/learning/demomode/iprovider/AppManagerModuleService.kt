package com.learning.demomode.iprovider

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.learning.common.provider.IAppModuleService
import com.learning.common.router.Router

@Route(path = Router.PROVIDER_APP_ROUTER)
class AppManagerModuleService : IAppModuleService {
    override fun init(context: Context?) {

    }

    override fun initSelfService() {

    }

    override fun router(
        context: Context?,
        actionLink: String?,
        host: String?,
        path: String?,
        bundle: Bundle?
    ) {
        context ?: return
        host ?: return
        path ?: return

        when (host) {
            "view" -> {
                parseView(context, actionLink, path, bundle)
            }

            "activity" -> {
                parseActivity(context, actionLink, path, bundle)
            }
        }
    }

    override fun parseView(context: Context?, actionLink: String?, path: String?, bundle: Bundle?) {
        when (path) {
            "main" -> {
                val targetBundle = bundle ?: Bundle()

                ARouter.getInstance()
                    .build(Router.APP_MAIN_ACTIVITY)
                    .with(targetBundle)
                    .navigation(context)
            }

            "system/detail" -> {
                val targetBundle = bundle ?: Bundle()

                ARouter.getInstance()
                    .build(Router.APP_SYSTEM_DETAIL_ACTIVITY)
                    .with(targetBundle)
                    .navigation(context)
            }

            "search" -> {
                val targetBundle = bundle ?: Bundle()

                ARouter.getInstance()
                    .build(Router.APP_SEARCH_ACTIVITY)
                    .with(targetBundle)
                    .navigation(context)
            }

            "search/detail" -> {
                val targetBundle = bundle ?: Bundle()

                ARouter.getInstance()
                    .build(Router.APP_SEARCH_DETAIL_ACTIVITY)
                    .with(targetBundle)
                    .navigation(context)
            }
        }
    }

    override fun parseActivity(
        context: Context?,
        actionLink: String?,
        path: String?,
        bundle: Bundle?
    ) {

    }
}
package com.learning.common.weight

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.learning.common.provider.AppService
import com.learning.common.router.Router
import com.learning.common.webview.CommonWebParams

class PerformLinkCenter {

    companion object {
        const val SCHEME_HTTP = "http"
        const val SCHEME_HTTPS = "https"
        const val SCHEME_DEMO = "demo"
        var instance = PerformLinkCenter()
    }

    fun performLink(activity: Activity?, url: String?) {
        activity?.apply {
            if (!this.isFinishing && !this.isDestroyed) {
                url?.apply {
                    performLinkWithContext(activity, this, null)
                }
            }
        }
    }

    fun performLink(context: Context?, url: String?) {
        context?.apply {
            url?.apply {
                performLinkWithContext(context, this, null)
            }
        }
    }

    fun performLink(activity: Activity?, url: String?, bundle: Bundle?) {
        activity?.apply {
            if (!this.isFinishing && !this.isDestroyed) {
                url?.apply {
                    performLinkWithContext(activity, this, bundle)
                }
            }
        }
    }

    fun performLinkWithContext(context: Context?, url: String?, bundle: Bundle?) {
        context ?: return
        url ?: return

        val uri = Uri.parse(url)
        val scheme = uri.scheme ?: ""
        val host = uri.host ?: ""
        var path = uri.path ?: ""

        if (path.length > 1) {
            path = path.substring(1)
        }

        Log.d("LinkCenter", "url -> $url")
        when (scheme) {
            SCHEME_HTTP, SCHEME_HTTPS -> {
                handleHttp(context, url, host, path, bundle)
            }

            SCHEME_DEMO -> {
                handleDemo(context, url, host, path, bundle)
            }
        }
    }

    private fun handleHttp(
        context: Context?,
        url: String?,
        host: String?,
        path: String?,
        bundle: Bundle?
    ) {
        context ?: return
        url ?: return

        val targetBundle = (bundle ?: Bundle()).apply {
            putString(CommonWebParams.URL.name, url)
        }

        ARouter.getInstance()
            .build(Router.COMMON_WEB_VIEW)
            .with(targetBundle)
            .navigation(context)
    }

    private fun handleDemo(
        context: Context?,
        url: String?,
        host: String?,
        path: String?,
        bundle: Bundle?
    ) {
        context ?: return
        url ?: return

        when (host) {
            "view" -> {
                handleView(context, url, host, path, bundle)
            }
        }

    }

    private fun handleView(
        context: Context?,
        url: String?,
        host: String?,
        path: String?,
        bundle: Bundle?
    ) {
        context ?: return
        url ?: return

        when (path) {
            "main", "system/detail","search","search/detail" -> {
                AppService.getInstance().routerToApp(context, url, host, path, bundle)
            }
        }
    }
}
package com.learning.common.webview

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Message
import android.util.AttributeSet
import android.webkit.*

class CommonWebUtility : WebView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    interface IHandleFunction {
        fun onCreateView(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean

        fun onCloseView(window: WebView?)
        fun onProcessing(status: Boolean)
        fun onProgressChange(value: Int)
        fun onShouldOverrideLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean
    }

    fun initSetting() {
        with(settings) {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            textZoom = 100
            allowFileAccess = true
            domStorageEnabled = true
            defaultTextEncodingName = "UTF-8"
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                builtInZoomControls = true
            }

            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            cacheMode = WebSettings.LOAD_NO_CACHE
            databaseEnabled = true
            setAppCacheEnabled(true)
            setSupportMultipleWindows(true)
            setRenderPriority(WebSettings.RenderPriority.HIGH)
        }

        webViewClient = viewClient
        webChromeClient = webViewChromeClient
    }

    var iHandleFunction: IHandleFunction? = null

    fun setHandleFunction(function: IHandleFunction?) {
        iHandleFunction = function
    }

    private val viewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            iHandleFunction?.onProcessing(true)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            iHandleFunction?.onProcessing(false)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return iHandleFunction?.onShouldOverrideLoading(view, request)
                ?: super.shouldOverrideUrlLoading(view, request)
        }
    }

    private val webViewChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            iHandleFunction?.onProgressChange(newProgress)
        }

        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            return iHandleFunction?.onCreateView(view, isDialog, isUserGesture, resultMsg)
                ?: super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
        }

        override fun onCloseWindow(window: WebView?) {
            super.onCloseWindow(window)
            iHandleFunction?.onCloseView(window)
        }
    }

}
package com.learning.common.webview

import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.TextView
import com.learning.common.R
import com.learning.common.base.BaseFragment
import com.learning.common.base.BaseViewModel
import com.learning.common.databinding.FragmentCommonWebBinding
import com.learning.common.weight.SettingType
import com.learning.common.weight.Utility
import kotlinx.android.synthetic.main.fragment_common_web.*

const val WEBVIEW_COUNT_MAX = 10

class CommonWebFragment : BaseFragment<BaseViewModel, FragmentCommonWebBinding>(),
    IBackPressFunction {
    private var currentWebView: CommonWebUtility? = null
    private var commonWebActivity: CommonWebActivity? = null
    private var webViewList: ArrayList<CommonWebUtility> = ArrayList()

    private var mIWebFunction: CommonWebUtility.IHandleFunction =
        object : CommonWebUtility.IHandleFunction {
            override fun onCreateView(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                resultMsg?.apply {
                    if (obj is WebView.WebViewTransport) {
                        onGeneralWebView(null)
                        val transport = this.obj as WebView.WebViewTransport
                        transport.webView = currentWebView
                        sendToTarget()
                        return true
                    }
                }
                return false
            }

            override fun onCloseView(window: WebView?) {
                window?.apply {
                    onCloseWebView()
                }
            }

            override fun onProcessing(status: Boolean) {
                common_web_progress.visibility = if (status) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            override fun onProgressChange(value: Int) {
                common_web_progress.progress = value
            }

            override fun onShouldOverrideLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (request?.url.toString().contains("taobao")) {
                    return true
                }

                return false
            }
        }

    override fun layoutId(): Int = R.layout.fragment_common_web

    override fun initWithCreate() {
        activity?.apply {
            if (!this.isFinishing && this is CommonWebActivity) {
                commonWebActivity = activity as CommonWebActivity
            }
        }
    }

    override fun initWithViewCreate(savedInstanceState: Bundle?) {
        arguments?.apply {
            setTitle = this.getString(CommonWebParams.TITLE.name)
            onUpdateToolbar()

            val url = this.getString(CommonWebParams.URL.name)
            url?.apply {
                if (Utility.isNetworkAvailable()) {
                    onGeneralWebView(this)
                } else {
                    showEmptyView()
                }
            }
        }
    }

    override fun initWithResume() {}

    private fun onGeneralWebView(url: String?) {
        currentWebView = CommonWebUtility(safeContext)

        if (webViewList.size == WEBVIEW_COUNT_MAX) {
            //当加入新webview时webviewList已经满载，将后面5个webview移动并覆盖前5个
            for (i in 0 until WEBVIEW_COUNT_MAX / 2) {
                webViewList[i] = webViewList[WEBVIEW_COUNT_MAX - i - 1]
                webViewList.removeAt(WEBVIEW_COUNT_MAX - i - 1)
            }
        }

        currentWebView?.apply {
            setHandleFunction(mIWebFunction)
            this.initSetting()
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.layoutParams = layoutParams

            webViewList.add(this)
            common_web_container.addView(this)

            url?.apply {
                loadUrl(this)
            }
        }
    }

    private fun onCloseWebView() {
        currentWebView = if (webViewList.size <= 1) {
            webViewList[0]
        } else {
            onDestroyWebView(webViewList.removeAt(webViewList.size - 1))
            webViewList[webViewList.size - 1]
        }
    }

    private fun onDestroyWebView(webView: WebView?) {
        webView?.apply {
            (parent as ViewGroup).removeView(this)
            removeAllViews()
            destroy()
        }
    }

    override fun onBackPress() {
        if (currentWebView?.canGoBack() == true) {
            currentWebView?.goBack()
        } else {
            if (webViewList.size <= 1) {
                commonWebActivity?.onPopOutFragment()
                return
            }
            onCloseWebView()
        }
    }

    private fun showEmptyView() {
        common_error_empty.visibility = View.VISIBLE
        common_web_progress.visibility = View.GONE
        common_web_container.visibility = View.GONE

        val settingTxt: TextView =
            common_error_empty.findViewById(R.id.empty_setting)
        settingTxt.setOnClickListener {
            Utility.openSetting(activity, SettingType.WIFI)
        }
    }

    private fun onClearWebView() {
        if (webViewList.size > 0) {
            for (i in 0 until webViewList.size) {
                currentWebView = webViewList[i]
                onDestroyWebView(currentWebView)
            }
        }
        webViewList.clear()
    }

    override fun onDestroy() {
        onClearWebView()
        super.onDestroy()
    }
}
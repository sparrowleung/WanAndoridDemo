package com.learning.common.weight

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.TypedValue
import androidx.annotation.NonNull
import androidx.core.text.HtmlCompat
import com.learning.common.base.BaseApplication


object Utility {
    fun isNetworkAvailable(): Boolean {
        val systemService =
            BaseApplication.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = systemService.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }

    fun openSetting(activity: Activity?, type: SettingType) {
        activity?.apply {
            var intent = Intent()
            var targetSet: String = ""

            when (type) {
                SettingType.WIFI -> {
                    targetSet = "WifiSettingsActivity"
                }
            }

            intent.component = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings$$targetSet"
            )

            startActivity(intent)
        } ?: return
    }

    fun compatHtmlToStr(@NonNull str: String): String =
        HtmlCompat.fromHtml(str, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    fun setPxToDp(int: Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        int.toFloat(),
        BaseApplication.appContext.resources.displayMetrics
    ).toInt()
}
package com.learning.common.provider

import android.content.Context
import android.os.Bundle

/**
 * Author: yisong.liao
 * Time: 10:11 10:11
 * Desc: com.samsung.android.voc.common.cross.provider.IRouter
 */
interface IRouter {
    fun router(context: Context?, actionLink: String?, host: String?, path: String?, bundle: Bundle?)
    fun parseView(context: Context?, actionLink: String?, path: String?, bundle: Bundle?) {}
    fun parseActivity(context: Context?, actionLink: String?, path: String?, bundle: Bundle?) {}
}
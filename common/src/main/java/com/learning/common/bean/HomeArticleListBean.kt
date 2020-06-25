package com.learning.common.bean

data class HomeArticleListBean(
    var curPage: Int,
    var pageCount: Int,
    var size: Int,
    var total: Int,
    var datas: ArrayList<HomeArticleBean>
)

data class OfficialArticleListBean(
    var curPage: Int,
    var pageCount: Int,
    var size: Int,
    var total: Int,
    var datas: ArrayList<OfficialArticleBean>
)
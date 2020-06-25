package com.learning.common.bean

data class ArticleListBean(
    var curPage: Int,
    var pageCount: Int,
    var size: Int,
    var total: Int,
    var datas: ArrayList<ArticleBean>
)
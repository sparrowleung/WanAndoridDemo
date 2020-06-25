package com.learning.common.bean

import androidx.room.PrimaryKey

const val ARTICLE_ID = "id"
const val ARTICLE_TYPE = "type"
const val ARTICLE_LINK = "link"
const val ARTICLE_DATE = "date"
const val ARTICLE_TITLE = "title"
const val ARTICLE_AUTHOR = "author"
const val ARTICLE_COLLECT = "collect"
const val ARTICLE_CHAPTER = "chapter"
const val ARTICLE_SHAREUSER = "shareUser"
const val ARTICLE_SUPERCHAPTER = "superChapter"

const val SEARCH_KEYWORD = "keyword"

open class ArticleBean(
    var id: Int,
    var type: Int,
    var fresh: Boolean,
    var chapterId: Int,
    var courseId: Int,
    var chapterName: String,
    var desc: String,
    var envelopePic: String,
    var link: String,
    var niceDate: String,
    var projectLink: String,
    var superChapterId: Int,
    var superChapterName: String,
    var title: String,
    var author: String,
    var apkLink: String,
    var audit: Int,
    var canEdit: Boolean,
    var collect: Boolean,
    var descMd: String,
    var niceShareDate: String,
    var origin: String,
    var prefix: String,
    @PrimaryKey(autoGenerate = true) var publishTime: Long,
    var selfVisible: Int,
    var shareUser: String
)
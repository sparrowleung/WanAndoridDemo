package com.learning.common.bean

import androidx.room.Entity

/**
*  将文章按照发布时间顺序（从远到近）排列
*  因为文章发布时间有可能会重复，所以添加id作为复合主键
* */
@Entity(primaryKeys = ["publishTime", "id"])
class HomeArticleBean(
    id: Int,
    type: Int,
    fresh: Boolean,
    chapterId: Int,
    courseId: Int,
    chapterName: String,
    desc: String,
    envelopePic: String,
    link: String,
    niceDate: String,
    projectLink: String,
    superChapterId: Int,
    superChapterName: String,
    title: String,
    author: String,
    apkLink: String,
    audit: Int,
    canEdit: Boolean,
    collect: Boolean,
    descMd: String,
    niceShareDate: String,
    origin: String,
    prefix: String,
    publishTime: Long,
    selfVisible: Int,
    shareUser: String
) : ArticleBean(
    id,
    type,
    fresh,
    chapterId,
    courseId,
    chapterName,
    desc,
    envelopePic,
    link,
    niceDate,
    projectLink,
    superChapterId,
    superChapterName,
    title,
    author,
    apkLink,
    audit,
    canEdit,
    collect,
    descMd,
    niceShareDate,
    origin,
    prefix,
    publishTime,
    selfVisible,
    shareUser
)


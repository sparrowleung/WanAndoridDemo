package com.learning.demomode

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.learning.common.bean.*

class MyDiffUtilCallBack(
    private var oldData: ArrayList<ArticleBean> = ArrayList(),
    private var newData: ArrayList<ArticleBean> = ArrayList()
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].id == newData[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldData[oldItemPosition].link == newData[newItemPosition].link &&
                oldData[oldItemPosition].title == newData[newItemPosition].title &&
                oldData[oldItemPosition].author == newData[newItemPosition].author &&
                oldData[oldItemPosition].niceDate == newData[newItemPosition].niceDate)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldd = oldData[oldItemPosition]
        val neww = newData[newItemPosition]

        val bundle = Bundle()
        if (oldd.type != neww.type) bundle.putInt(ARTICLE_TYPE, neww.type)
        if (oldd.link != neww.link) bundle.putString(ARTICLE_LINK, neww.link)
        if (oldd.title != neww.title) bundle.putString(ARTICLE_TITLE, neww.title)
        if (oldd.author != neww.author) bundle.putString(ARTICLE_AUTHOR, neww.author)
        if (oldd.niceDate != neww.niceDate) bundle.putString(ARTICLE_DATE, neww.niceDate)
        if (oldd.collect != neww.collect) bundle.putBoolean(ARTICLE_COLLECT, neww.collect)
        if (oldd.shareUser != neww.shareUser) bundle.putString(ARTICLE_SHAREUSER, neww.shareUser)
        if (oldd.chapterName != neww.chapterName) bundle.putString(ARTICLE_CHAPTER, neww.chapterName)
        if (oldd.superChapterName != neww.superChapterName) bundle.putString(ARTICLE_SUPERCHAPTER, neww.superChapterName)


        return bundle
    }
}
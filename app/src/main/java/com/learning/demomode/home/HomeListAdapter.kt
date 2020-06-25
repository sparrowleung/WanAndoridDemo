package com.learning.demomode.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.learning.common.webview.CommonWebParams
import com.learning.common.weight.PerformLinkCenter
import com.learning.common.weight.Utility
import com.learning.demomode.R
import com.learning.common.bean.*
import com.learning.demomode.databinding.ItemHomeArticleBinding

class HomeListAdapter : RecyclerView.Adapter<HomeListAdapter.HomeViewHolder>() {
    private var dataList: ArrayList<ArticleBean> = ArrayList()

    class HomeViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemHomeArticleBinding>(view)

        fun bind(bean: ArticleBean) {
            binding?.articleBean = bean
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_article, parent, false)

        return HomeViewHolder(view)
    }

    fun addMoreData(list: ArrayList<ArticleBean>) {
        dataList.addAll(list)
    }

    fun updateData(list: ArrayList<ArticleBean>) {
        dataList = list
    }

    fun getData(): ArrayList<ArticleBean> = ArrayList(dataList)

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(dataList[position])

        // itemView == Holder(view)中的view
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString(
                    CommonWebParams.TITLE.name,
                    Utility.compatHtmlToStr(dataList[position].title)
                )
            }

            PerformLinkCenter.instance.performLinkWithContext(
                it.context,
                dataList[position].link,
                bundle
            )
        }
    }

    override fun onBindViewHolder(
        holder: HomeViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if ((payloads as Bundle).isEmpty) {
            onBindViewHolder(holder, position)
            return
        }

        val bundle = payloads[0] as Bundle
        val article = dataList[position]
        for (key in bundle.keySet()) {
            when (key) {
                ARTICLE_TITLE -> {
                    holder.binding?.dataTitle?.text = article.title
                }
                ARTICLE_DATE -> {
                    holder.binding?.dataInfoDate?.text = article.niceDate
                }
                ARTICLE_AUTHOR -> {
                    holder.binding?.dataInfoAuthor?.text = article.author
                }
                ARTICLE_SHAREUSER -> {
                    holder.binding?.dataInfoAuthor?.text = article.shareUser
                }
                ARTICLE_CHAPTER -> {
                    holder.binding?.dataInfoCategory1?.text = article.chapterName
                }
                ARTICLE_SUPERCHAPTER -> {
                    holder.binding?.dataInfoCategory2?.text = article.superChapterName
                }
            }
        }
    }
}
package com.learning.demomode.search.detail

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

class SearchDetailAdapter(
    private var dataList: ArrayList<HomeArticleBean> = ArrayList()
) : RecyclerView.Adapter<SearchDetailAdapter.SearchViewHolder>() {
    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemHomeArticleBinding>(view)
        fun bind(bean: ArticleBean) {
            binding?.articleBean = bean
        }
    }

    fun addMoreData(list: ArrayList<HomeArticleBean>) {
        dataList.addAll(list)
    }

    fun updateData(list: ArrayList<HomeArticleBean>) {
        dataList = ArrayList(list)
    }

    fun getData(): ArrayList<HomeArticleBean> = ArrayList(dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_article, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(dataList[position])
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                this.putString(
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
        holder: SearchViewHolder,
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
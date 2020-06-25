package com.learning.demomode.system.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.learning.common.webview.CommonWebParams
import com.learning.common.weight.PerformLinkCenter
import com.learning.demomode.*
import com.learning.common.bean.*
import com.learning.demomode.databinding.ItemHomeArticleBinding

class SystemDetailListAdapter(
    private var dataList: ArrayList<HomeArticleBean> = ArrayList()
) : RecyclerView.Adapter<SystemDetailListAdapter.SystemDetailViewHolder>() {
    inner class SystemDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemHomeArticleBinding>(view)
        fun bind(bean: ArticleBean) {
            binding?.articleBean = bean
        }
    }

    fun addMoreArticle(list: ArrayList<HomeArticleBean>) {
        dataList.addAll(list)
    }

    fun updateArticle(list: ArrayList<HomeArticleBean>) {
        dataList = list
    }

    fun getSysDetailData(): ArrayList<HomeArticleBean> = ArrayList(dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemDetailViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_article, parent, false)
        return SystemDetailViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: SystemDetailViewHolder, position: Int) {
        holder.bind(dataList[position])
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                this.putString(CommonWebParams.TITLE.name, dataList[position].title)
            }
            PerformLinkCenter.instance.performLinkWithContext(
                it.context,
                dataList[position].link,
                bundle
            )
        }
    }

    override fun onBindViewHolder(
        holder: SystemDetailViewHolder,
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
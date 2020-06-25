package com.learning.demomode.officail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.learning.common.webview.CommonWebParams
import com.learning.common.weight.PerformLinkCenter
import com.learning.common.weight.Utility
import com.learning.common.bean.ARTICLE_DATE
import com.learning.common.bean.ARTICLE_TITLE
import com.learning.demomode.R
import com.learning.demomode.databinding.ItemOfficialArticleBinding
import com.learning.common.bean.ArticleBean

class OfficialListAdapter : RecyclerView.Adapter<OfficialListAdapter.LeftViewHolder>() {
    private var officialData: ArrayList<ArticleBean> = ArrayList()

    inner class LeftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemOfficialArticleBinding>(view)
        fun bind(bean: ArticleBean) {
            binding?.articleBean = bean
        }
    }

    fun addMoreData(list: ArrayList<ArticleBean>) {
        officialData.addAll(list)
    }

    fun updateData(list: ArrayList<ArticleBean>) {
        officialData = list
    }

    fun getData(): ArrayList<ArticleBean> = ArrayList(officialData)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeftViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_official_article, parent, false)
        return LeftViewHolder(view)
    }

    override fun getItemCount(): Int {
        return officialData.size
    }

    override fun onBindViewHolder(
        holder: LeftViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if ((payloads as Bundle).isEmpty) {
            onBindViewHolder(holder, position)
            return
        }

        val bundle = payloads[0] as Bundle
        val article = officialData[position]
        for (key in bundle.keySet()) {
            when (key) {
                ARTICLE_TITLE -> {
                    holder.binding?.officialArticleTitle?.text = article.title
                }

                ARTICLE_DATE -> {
                    holder.binding?.officialArticleDate?.text = article.niceDate
                }
            }
        }
    }

    override fun onBindViewHolder(holder: LeftViewHolder, position: Int) {
        holder.bind(officialData[position])
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString(
                    CommonWebParams.TITLE.name,
                    Utility.compatHtmlToStr(officialData[position].title)
                )
            }

            PerformLinkCenter.instance.performLinkWithContext(
                it.context,
                officialData[position].link,
                bundle
            )
        }
    }
}
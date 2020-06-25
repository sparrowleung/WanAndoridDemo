package com.learning.demomode.project.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.learning.common.webview.CommonWebParams
import com.learning.common.weight.PerformLinkCenter
import com.learning.common.weight.Utility
import com.learning.common.bean.ArticleBean
import com.learning.demomode.R
import com.learning.common.bean.ARTICLE_DATE
import com.learning.common.bean.ARTICLE_TITLE
import com.learning.demomode.databinding.ItemProjectDataBinding


class ProjectListAdapter(
    private var dataList: ArrayList<ArticleBean> = ArrayList()
) : RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemProjectDataBinding>(view)

        fun bind(bean: ArticleBean) {
            binding?.projectBean = bean
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        // 若需要item的宽度填充屏幕，则调用inflate时，需要调用inflate(int, viewGroup, boolean)方法
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_project_data, parent, false)
        return ProjectViewHolder(view)
    }

    fun addMoreData(list: ArrayList<ArticleBean>) {
        dataList.addAll(list)
    }

    fun updateData(list: ArrayList<ArticleBean>) {
        dataList = list
    }

    fun clearProjectList() {
        dataList.clear()
    }

    fun getData(): ArrayList<ArticleBean> = ArrayList(dataList)

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(dataList[position])
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
        holder: ProjectViewHolder,
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
                    holder.binding?.projectTitle?.text = article.title
                }

                ARTICLE_DATE -> {
                    holder.binding?.projectDate?.text = article.niceDate
                }
            }
        }
    }
}
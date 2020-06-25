package com.learning.common.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

const val NORMAL_VIEW_FLAG = 0
const val HEADER_VIEW_FLAG = 1
const val FOOTER_VIEW_FLAG = 2

class MyRecyclerAdapter(
    private val myAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var headerCount = 0
    private var footerCount = 0
    private var headerList: ArrayList<View> = ArrayList()
    private var footerList: ArrayList<View> = ArrayList()

    class VH(view: View) : RecyclerView.ViewHolder(view)

    fun setFooterView(list: ArrayList<View>) {
        footerCount = 0
        footerList = list
    }

    fun setHeaderView(list: ArrayList<View>) {
        headerCount = 0
        headerList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            NORMAL_VIEW_FLAG -> {
                return myAdapter.onCreateViewHolder(parent, 0)
            }

            HEADER_VIEW_FLAG -> {
                return VH(headerList[headerCount++])
            }

            FOOTER_VIEW_FLAG -> {
                return VH(footerList[footerCount++])
            }
        }

        return super.createViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return (headerList.size + footerList.size + myAdapter.itemCount)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is VH) {
            myAdapter.onBindViewHolder(holder, position - headerList.size)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < headerList.size) return HEADER_VIEW_FLAG

        if (position < headerList.size + myAdapter.itemCount) return NORMAL_VIEW_FLAG

        return FOOTER_VIEW_FLAG
    }

}
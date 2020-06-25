package com.learning.demomode.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learning.common.bean.SearchHistoryBean
import com.learning.demomode.R

class SearchHistoryAdapter : RecyclerView.Adapter<SearchHistoryAdapter.HistoryViewHolder>() {
    private var dataList: ArrayList<SearchHistoryBean> = ArrayList()
    private lateinit var executeDelete: (String, Long) -> Unit
    private lateinit var executeSearch: (String) -> Unit

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val historyTxt: TextView = view.findViewById(R.id.search_text)
        val historyDelete: ImageView = view.findViewById(R.id.search_delete)
    }

    fun addMoreData(data: ArrayList<SearchHistoryBean>) {
        dataList.addAll(data)
    }

    fun updateData(data: ArrayList<SearchHistoryBean>) {
        dataList = data
    }

    fun setDelete(function: (String, Long) -> Unit) {
        executeDelete = function
    }

    fun setSearch(search: (String) -> Unit) {
        executeSearch = search
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.historyTxt.text = dataList[position].historyStr
        holder.historyTxt.setOnClickListener {
            executeSearch(dataList[position].historyStr)
        }

        holder.historyDelete.setOnClickListener {
            executeDelete(dataList[position].historyStr, dataList[position].dateTime)
        }
    }
}
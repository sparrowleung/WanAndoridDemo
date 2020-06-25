package com.learning.demomode.officail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learning.common.bean.OfficialAuthorBean
import com.learning.demomode.R

/**
 * Created by yuyang.liang on 2020/6/25.
 */

class OfficialSheetAdapter(
        private var dataList: ArrayList<OfficialAuthorBean> = ArrayList()
) : RecyclerView.Adapter<OfficialSheetAdapter.SheetViewHolder>() {

    private lateinit var executeClick: (Int) -> Unit
    private var selectId: Int = 0
    fun setExecuteClick(click: (Int) -> Unit) {
        executeClick = click
    }

    fun updateSelectId(index: Int) {
        selectId = index
    }

    inner class SheetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt = view.findViewById<TextView>(R.id.official_sheet_text)
        val img = view.findViewById<ImageView>(R.id.official_sheet_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sheet_rv_content, parent, false)
        return SheetViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: SheetViewHolder, position: Int) {
        holder.txt.text = dataList[position].name
        if (dataList[position].id == selectId) {
            holder.img.setImageResource(R.drawable.benefits)
            holder.itemView.setBackgroundResource(R.drawable.flexbox_text_background)
        }
        holder.itemView.setOnClickListener {
            executeClick(position)
        }
    }
}
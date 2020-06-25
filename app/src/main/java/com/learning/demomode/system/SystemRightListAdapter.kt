package com.learning.demomode.system

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.learning.common.base.BaseApplication
import com.learning.common.bean.*
import com.learning.common.weight.MToast
import com.learning.common.weight.PerformLinkCenter
import com.learning.common.weight.Utility
import com.learning.demomode.R

class SystemRightListAdapter(
    private val context: Context
) : RecyclerView.Adapter<SystemRightListAdapter.RightViewHolder>() {
    private var dataList: ArrayList<SystemCategoryBean> = ArrayList()

    inner class RightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardTitle: TextView = view.findViewById(R.id.system_right_title)
        val flexBox: FlexboxLayout = view.findViewById(R.id.system_right_flex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RightViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_system_right_data, parent, false)
        return RightViewHolder(view)
    }

    fun updateData(data: ArrayList<SystemCategoryBean>) {
        dataList = data
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RightViewHolder, position: Int) {
        holder.cardTitle.text = dataList[position].name
        /**
         *  子布局使用recyclerView设置flexBoxLayoutManager后，父recyclerView通过scrollToPosition时
         *  不能滚动到对应position的item，判断估计是因为recyclerView计算item的高度时出错
         *  故使用flexBoxLayout，并动态添加textView
         */
        holder.flexBox.removeAllViews()
        for (childBean in dataList[position].children) {
            val view = TextView(context)
            view.background = context.getDrawable(R.drawable.flexbox_text_background)
            val layoutParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParam.setMargins(
                Utility.setPxToDp(6),
                Utility.setPxToDp(10),
                Utility.setPxToDp(6),
                0
            )
            view.text = childBean.name
            view.layoutParams = layoutParam
            view.setOnClickListener {
                if (Utility.isNetworkAvailable()) {
                    val bundle = Bundle()
                    bundle.putInt(ARTICLE_ID, childBean.id)
                    bundle.putString(ARTICLE_TITLE, childBean.name)
                    PerformLinkCenter.instance.performLinkWithContext(
                        context,
                        "demo://view/system/detail",
                        bundle
                    )
                } else {
                    MToast.makeText(
                        context,
                        context.getString(R.string.network_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            holder.flexBox.addView(view)
        }
    }
}
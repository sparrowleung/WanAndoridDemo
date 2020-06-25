package com.learning.demomode.system

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.learning.demomode.R
import com.learning.common.bean.SystemCategoryBean
import com.learning.demomode.databinding.ItemSystemLfetDataBinding

class SystemLeftListAdapter(
    private val systemViewModel: SystemViewModel
) : RecyclerView.Adapter<SystemLeftListAdapter.LeftViewHolder>() {
    private var systemLeftData: ArrayList<SystemCategoryBean> = ArrayList()

    private var clickPos: Int = 0
    inner class LeftViewHolder(view: View, private val viewModel: SystemViewModel) :
        RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemSystemLfetDataBinding>(view)

        fun bind(@NonNull bean: SystemCategoryBean) {
            binding?.systemLeftBean = bean
            binding?.viewModel = viewModel
            //去除点击item会闪烁白屏问题
            binding?.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeftViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_system_lfet_data, parent, false)
        return LeftViewHolder(view, systemViewModel)
    }

    fun setOnClickPos(pos:Int) {
        clickPos = pos
    }

    override fun getItemCount(): Int {
        return systemLeftData.size
    }

    fun updateData(list: ArrayList<SystemCategoryBean>) {
        systemLeftData = list
    }

    override fun onBindViewHolder(holder: LeftViewHolder, position: Int) {
        holder.bind(systemLeftData[position])
        holder.binding?.pos = position
        holder.binding?.systemFirstTxt?.isSelected = clickPos == position
    }
}
package com.learning.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerView : RecyclerView {
    var rAdapter: MyRecyclerAdapter? = null

    private val headerViewList by lazy { ArrayList<View>() }
    private val footerViewList by lazy { ArrayList<View>() }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, inStyleAttr: Int) : super(
        context,
        attributeSet,
        inStyleAttr
    )

    fun setHeaderView(view: View) {
        headerViewList.add(view)
        rAdapter?.setHeaderView(headerViewList)
    }

    fun removHeaderView(view: View) {
        headerViewList.remove(view)
        rAdapter?.setFooterView(headerViewList)
    }

    fun setFooterView(view: View) {
        footerViewList.add(view)
        rAdapter?.setFooterView(footerViewList)
    }

    fun removeFooterView(view: View) {
        footerViewList.remove(view)
        rAdapter?.setFooterView(footerViewList)
    }

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        adapter?.apply {
            rAdapter = MyRecyclerAdapter(adapter)
            if (headerViewList.size > 0) rAdapter?.setHeaderView(headerViewList)
            if (footerViewList.size > 0) rAdapter?.setFooterView(footerViewList)
        }

        super.setAdapter(rAdapter)
    }
}
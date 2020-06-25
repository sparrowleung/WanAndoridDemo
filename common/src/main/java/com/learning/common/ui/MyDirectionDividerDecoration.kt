package com.learning.common.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.getDrawableOrThrow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val HORIZONTAL = LinearLayoutManager.HORIZONTAL
const val VERTICAL = LinearLayoutManager.VERTICAL
const val OFFSET: Int = 20

class MyDirectionDividerDecoration(context: Context, orientation: Int, private val mOffset: Int) :
    RecyclerView.ItemDecoration() {

    private val mContext: Context = context
    private val mOrientation: Int = orientation
    private val mDivider: Drawable
    private val mAttribute = intArrayOf(android.R.attr.listDivider)

    init {
        val typeArray = mContext.obtainStyledAttributes(mAttribute)
        mDivider = typeArray.getDrawable(0)!!
        typeArray.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        when (mOrientation) {
            HORIZONTAL -> {
                drawHorizontalView(c, parent)
            }

            VERTICAL -> {
                drawVerticalView(c, parent)
            }
        }
    }

    private fun drawVerticalView(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft + mOffset
        val right = parent.width - parent.paddingRight - mOffset

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams
            val top = view.bottom + layoutParams.bottomMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
    }

    private fun drawHorizontalView(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop + mOffset
        val bottom = parent.height - parent.paddingBottom - mOffset

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams
            val left = view.right + layoutParams.rightMargin
            val right = left + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, mDivider.intrinsicHeight)
        } else {
            outRect.set(0, 0, mDivider.intrinsicWidth, 0)
        }
    }
}
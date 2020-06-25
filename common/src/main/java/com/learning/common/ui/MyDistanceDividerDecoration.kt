package com.learning.common.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

const val DIRECTION_LEFT: Int = 0
const val DIRECTION_RIGHT: Int = 1
const val DIRECTION_TOP: Int = 2
const val DIRECTION_BOTTOM: Int = 3
const val DISTANCE: Int = 40

class MyDistanceDividerDecoration(
    private val mDirection: Int,
    private val mDistance: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        when (mDirection) {
            DIRECTION_LEFT -> {
                outRect.set(mDistance, 0, 0, 0)
            }

            DIRECTION_RIGHT -> {
                outRect.set(0, 0, mDistance, 0)
            }

            DIRECTION_BOTTOM -> {
                outRect.set(0, 0, 0, mDistance)
            }

            DIRECTION_TOP -> {
                outRect.set(0, mDistance, 0, 0)
            }
        }

    }

}
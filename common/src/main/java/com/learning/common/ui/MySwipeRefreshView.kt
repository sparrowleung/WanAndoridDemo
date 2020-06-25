package com.learning.common.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.learning.common.R
import kotlinx.coroutines.*
import kotlin.math.abs

const val LOADING_TOP = 0
const val LOADING_BOTTOM = 1
const val LOADING_OTHERS = 2

class MySwipeRefreshView : SwipeRefreshLayout {
    private var recyclerView: MyRecyclerView? = null

    private var view: View? = null
    private lateinit var loadMoreData: (Int) -> Unit

    private var rollToTop: Boolean = false
    private var loadingState: Boolean = false
    private var rollToBottom: Boolean = false
    private var downStep: Float = 0.toFloat()
    private var distance: Int = 0

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    private fun initView() {
        require(this.childCount <= 1) { "Only Can Add One View In This Layout" }
    }

    /**
     *   判断recyclerView是否滑动到底端
     *   当 recyclerView滑动过的距离offset + 当前页面显示的recyclerView范围extent >= recyclerView纵向总距离时，判断为已滑动到底端
     * */
    private fun isScrollToBottom(): Boolean {
        recyclerView?.apply {
            if (this.computeVerticalScrollOffset() + this.computeVerticalScrollExtent() >= this.computeVerticalScrollRange()) return true
        }
        return false
    }

    /**
     *   判断recyclerView是否滑动到顶端
     *   当 recyclerView滑动过的距离offset == 0，判断为已滑动到顶端
     * */
    private fun isScrollToTop(): Boolean {
        recyclerView?.apply {
            if (this.computeVerticalScrollOffset() == 0) return true
        }
        return false
    }

    override fun setDistanceToTriggerSync(distance: Int) {
        super.setDistanceToTriggerSync(distance)
        this.distance = distance + 30
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                downStep = ev.y
            }
            /**
             *   在用户放手时才执行上拉/下拉的操作，是考虑到用户在拉动的过程中，突然不想更新数据时
             *   可以通过<回滑>的操作，取消更新操作
             * */
            MotionEvent.ACTION_UP -> {
                /**
                 *   当手势向下滑动 && 滑动距离超过设置的距离阈值 && 当前recyclerView已经滑到在顶端 && 加载状态为false的情况下
                 *   执行<下拉刷新>操作 : 设置refreshing = true -> 设置加载状态为true -> 执行刷新操作
                 * */
                if ((ev.y - downStep) > 0 && abs(ev.y - downStep) >= distance && rollToTop && !loadingState) {
                    isRefreshing = true
                    setLoadingState(true)
                    loadMoreData(LOADING_TOP)
                }
                /**
                 *   当手势向上滑动 && 滑动距离超过200px && 当前recyclerView已经滑到在底端 && 加载状态为false的情况下
                 *   执行<上拉加载>操作 : 设置加载状态为true -> 向recyclerView添加footerView -> delay 1s -> 执行刷新操作
                 * */
                else if ((ev.y - downStep) < 0 && abs(ev.y - downStep) >= 200 && rollToBottom && !loadingState) {
                    setLoadingState(true)
                    //执行RangeInsert需要一定时间，通过协程避免loadingView的insert操作和网络请求数据的insert操作冲突
                    CoroutineScope(Dispatchers.Main).launch {
                        handleFooterView()
                        delay(200)
                        loadMoreData(LOADING_BOTTOM)
                    }
                }

                /**
                 *   手势滑动结束后，无论是否有执行到更新操作
                 *   都重置滚动状态为false
                 * */
                rollToBottom = false
                rollToTop = false
            }

            MotionEvent.ACTION_MOVE -> {
                /**
                 *  当手势向上滑动 && recyclerView已经滑动到底端时
                 *  设置向上滑动状态为true
                 * */
                if ((ev.y - downStep) < 0 && isScrollToBottom()) {
                    rollToBottom = true
                }
                /**
                 *  当手势向下滑动 && recyclerView已经滑动到顶端时
                 *  设置向下滑动状态为true
                 * */
                if ((ev.y - downStep) > 0 && isScrollToTop()) {
                    rollToTop = true
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (childCount > 0) {
            if (getChildAt(0) is RecyclerView) {
                recyclerView = getChildAt(0) as MyRecyclerView
            }
        }
    }

    /**
     *  传入loadData(state: Int)函数对象执行更新操作
     *  state = LOADING_TOP : 执行上拉刷新
     *  state = LOADING_BOTTOM : 执行下拉加载
     *  @param loadData
     *  */
    fun addOnLoadMoreListener(loadData: (state: Int) -> Unit) {
        this.loadMoreData = loadData
    }

    /**
     *  当loadingState为false时，移除所有FooterView
     *  建议该方法在notify数据后调用
     *  避免发生IndexOutOfBoundsException
     *  */
    fun setLoadingState(state: Boolean) {
        loadingState = state
        if (!state) {
            isRefreshing = false
            view?.apply {
                recyclerView?.removeFooterView(this)
                recyclerView?.rAdapter?.apply {
                    notifyItemRangeRemoved(this.itemCount, 1)
                }
            }
            view = null
        }
    }

    /**
     *  当loadingState为true时，向list尾部动态添加footerView，显示loading状态
     *  */
    private fun handleFooterView() {
        if (loadingState) {
            view = LayoutInflater.from(context).inflate(R.layout.loading_footer_view, null)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            view?.apply {
                layoutParams = lp
                recyclerView?.setFooterView(this)
            }

            recyclerView?.rAdapter?.apply {
                notifyItemRangeInserted(this.itemCount, 1)
            }
        }
    }
}
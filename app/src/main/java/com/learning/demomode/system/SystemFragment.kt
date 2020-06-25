package com.learning.demomode.system

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.common.base.BaseFragment
import com.learning.common.weight.MToast
import com.learning.demomode.R
import com.learning.demomode.REQUEST_SYSTEM
import com.learning.demomode.databinding.FragmentSystemBinding
import kotlinx.android.synthetic.main.fragment_system.*
import android.view.View
import com.learning.common.bean.SystemCategoryBean
import com.learning.common.database.DemoDataBase
import com.learning.common.ui.DIRECTION_BOTTOM
import com.learning.common.ui.DISTANCE
import com.learning.common.ui.MyDistanceDividerDecoration
import com.learning.common.weight.Utility

class SystemFragment : BaseFragment<SystemViewModel, FragmentSystemBinding>() {

    private val leftAdapter by lazy { SystemLeftListAdapter(baseViewModel) }
    private val rightAdapter by lazy { SystemRightListAdapter(safeContext) }
    private val systemDB by lazy { DemoDataBase.getDataBase(safeContext) }

    private lateinit var leftLayoutManager: LinearLayoutManager
    private lateinit var rightLayoutManager: LinearLayoutManager

    private var isScrollToBehind = false

    /**
     * 判断是否左侧view点击导致右侧view滑动（防止点击左侧list时，需要逐个更新item背景色）
     * */
    private var currentPosition = 0

    override fun layoutId(): Int = R.layout.fragment_system

    override fun initWithCreate() {
        baseViewModel.setSystemDB(systemDB)

        baseViewModel.getSystemData().observe(this, Observer {
            // 解决recyclerView的item复用，导致更改某一item状态时会导致其他item一同更改的问题
            system_leftList.setItemViewCacheSize(it.size)
            system_titleLayout.visibility = View.VISIBLE
            system_progress.visibility = View.GONE
            system_rightText.text = Utility.compatHtmlToStr(baseViewModel.getListVisitableText(0))

            leftAdapter.updateData(it)
            leftAdapter.notifyItemRangeInserted(leftAdapter.itemCount, it.size)
            rightAdapter.updateData(it)
            rightAdapter.notifyItemRangeInserted(rightAdapter.itemCount, it.size)
        })

        baseViewModel.getCurrentPosition().observe(this, Observer {
            //记录点击的位置，刷新RecyclerView，点击位置的item更改背景色
            currentPosition = it
            leftAdapter.setOnClickPos(it)
            onScrollToPlace(it)
            leftAdapter.notifyDataSetChanged()
        })

        baseViewModel.getRequestError().observe(this, Observer {
            for (key in it.keys) {
                Log.e(
                    TAG,
                    "Network Error [type: $key] -> code : ${it[key]?.statusCode} / msg : ${it[key]?.errorMsg}"
                )

                when (key) {
                    REQUEST_SYSTEM -> {
                        system_progress.visibility = View.GONE
                    }
                }
            }

            MToast.makeText(
                safeContext,
                safeContext.getString(R.string.network_error),
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    override fun initWithViewCreate(savedInstanceState: Bundle?) {
        /**
         *  自定义MyRecyclerView有设定rAdapter
         *  原始RecyclerView没有设定adapter，所以需要在ViewCreate时设置
         *  否则报错No adapter attached; skipping layout
         * */
        with(system_leftList) {
            adapter = leftAdapter
            leftLayoutManager = LinearLayoutManager(safeContext)
            layoutManager = leftLayoutManager
            overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
        }

        with(system_rightList) {
            adapter = rightAdapter
            rightLayoutManager = LinearLayoutManager(safeContext)
            layoutManager = rightLayoutManager

            addItemDecoration(MyDistanceDividerDecoration(DIRECTION_BOTTOM, DISTANCE))
        }
    }

    override fun initWithResume() {
        val leftData = ArrayList(systemDB.systemCategoryDao().getSysCategoryData())
        leftAdapter.updateData(leftData)

        val rightData = ArrayList<SystemCategoryBean>()
        for (bean in systemDB.systemCategoryDao().getSysCategoryData()) {
            bean.children = ArrayList(systemDB.systemChildDao().getSysCategoryData(bean.id))
            rightData.add(bean)
        }
        rightAdapter.updateData(rightData)

        leftAdapter.notifyItemRangeInserted(leftAdapter.itemCount, leftData.size)
        rightAdapter.notifyItemRangeInserted(rightAdapter.itemCount, rightData.size)

        if (baseViewModel.getListVisitableText(0).isNotEmpty()) {
            system_titleLayout.visibility = View.VISIBLE
            system_rightText.text = Utility.compatHtmlToStr(baseViewModel.getListVisitableText(0))
        }

        setOnScrollListenerToList()

        if (Utility.isNetworkAvailable()) {
            baseViewModel.onRequestSystemData()
            if (leftAdapter.itemCount == 0 ||
                rightAdapter.itemCount == 0
            ) {
                system_progress.visibility = View.VISIBLE
            }
        }
    }

    private fun onScrollToPlace(pos: Int) {
        //找到当前RightList可见头尾项的position
        val firstVisiblePos = rightLayoutManager.findFirstVisibleItemPosition()
        val lastVisiblePos = rightLayoutManager.findLastVisibleItemPosition()
        when {
            //当点击的item在当前显示的item之前
            pos <= firstVisiblePos -> {
                system_rightList.smoothScrollToPosition(pos)
            }
            //当点击的item在当前显示的item的中间
            pos <= lastVisiblePos -> {
                val height =
                    system_rightList.getChildAt(pos - firstVisiblePos).top
                system_rightList.smoothScrollBy(0, height)
            }
            //当点击的item在当前显示的item之后
            else -> {
                isScrollToBehind = true
                system_rightList.smoothScrollToPosition(pos)
            }
        }
    }

    /**
     *  判断recyclerView是否已经滑动到底部
     *  根据当前显示区域的高度(Extent)+当前屏幕已经滑过的距离(Offset) 是否>= 整个recyclerView的高度
     *  是 则return true
     * */
    private fun isBottomOfList(recyclerView: RecyclerView): Boolean {
        return (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
    }

    private fun setOnScrollListenerToList() {
        system_rightList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val firstVisiblePos: Int

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    /**
                     *  当点击左侧RecyclerView的item，在右侧view目前显示item后面时(isScrollToBehind是用于判断是否左侧view控制)
                     *  先让右侧view滑动到该pos，通过ScrollListener监听滑动状态
                     *  当滑动停止后，再进行“吸顶”操作将该item移至视图顶部
                     * */
                    if (isScrollToBehind) {
                        isScrollToBehind = false
                        firstVisiblePos =
                            currentPosition - rightLayoutManager.findFirstVisibleItemPosition()

                        if (firstVisiblePos >= 0 && firstVisiblePos <= system_rightList.childCount) {
                            val height =
                                system_rightList?.getChildAt(firstVisiblePos)?.top ?: 0

                            system_rightList?.smoothScrollBy(0, height)
                        } else {
                            baseViewModel.setClickByLeft(false)
                        }
                    } else {
                        /**
                         *  若滑动右侧RecyclerView向上滑动结束，则再向上滑动title的高度
                         *  并重置clickByLeft的状态
                         * */
                        baseViewModel.setClickByLeft(false)
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    if (system_titleLayout.visibility == View.GONE) {
                        system_titleLayout.visibility = View.VISIBLE
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                /**
                 *   右侧RecyclerView在滑动时，一直更新顶部title的文字
                 * */
                val firstVisiblePos = rightLayoutManager.findFirstVisibleItemPosition()
                system_rightText.text = baseViewModel.getListVisitableText(firstVisiblePos)

                /**
                 *  当滑动右侧RecyclerView, 左侧view对应item的背景颜色随着滑动一直变更,
                 * （当点击左侧view来操控右侧view滑动时，不需要执行该步骤）
                 * */
                if (!baseViewModel.isClickByLeft()) {
                    leftAdapter.setOnClickPos(firstVisiblePos)
                    leftAdapter.notifyDataSetChanged()
                    system_leftList.scrollToPosition(firstVisiblePos)
                }

                if (isBottomOfList(recyclerView)) {
                    val height = rightLayoutManager.findLastCompletelyVisibleItemPosition()
                    system_leftList.scrollToPosition(height)
                }
            }
        })
    }
}
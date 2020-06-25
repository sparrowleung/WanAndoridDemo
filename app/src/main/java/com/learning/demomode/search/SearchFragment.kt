package com.learning.demomode.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.learning.common.base.BaseFragment
import com.learning.common.base.BaseViewModel
import com.learning.common.bean.SEARCH_KEYWORD
import com.learning.common.bean.SearchHistoryBean
import com.learning.common.database.DemoDataBase
import com.learning.common.ui.*
import com.learning.common.weight.PerformLinkCenter
import com.learning.common.weight.Utility
import com.learning.demomode.R
import com.learning.demomode.databinding.FragmentSearchBinding
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment<BaseViewModel, FragmentSearchBinding>() {

    private var commandList: ArrayList<String> = ArrayList()
    private val historyAdapter by lazy { SearchHistoryAdapter() }
    private val searchHistoryDao by lazy {
        DemoDataBase.getDataBase(safeContext).searchHistoryDao()
    }

    override fun layoutId(): Int = R.layout.fragment_search

    override fun initWithCreate() {
        setTitle = safeContext.getString(R.string.search)
        onUpdateToolbar()

        commandList.add("面试")
        commandList.add("Studio3")
        commandList.add("动画")
        commandList.add("自定义View")
        commandList.add("性能优化 速度")
        commandList.add("gradle")
        commandList.add("Camera 相机")
        commandList.add("代码混淆 安全")
        commandList.add("逆向 加固")
        commandList.add("协程")
        commandList.add("Jetpack")
        commandList.add("kotlin")

        searchHistoryDao.getHistoryList().observe(this, Observer {
            if (search_history.visibility == View.GONE && it.isNotEmpty()) {
                search_history.visibility = View.VISIBLE
            } else if (it.isEmpty() && search_history.visibility == View.VISIBLE) {
                search_history.visibility = View.GONE
            }

            historyAdapter.updateData(ArrayList(it))
            historyAdapter.notifyDataSetChanged()
        })
    }

    override fun initWithViewCreate(savedInstanceState: Bundle?) {
        with(search_edit) {
            var inputContent: String? = null

            addTextChangedListener {
                inputContent = it?.toString()
            }

            setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER
                                && KeyEvent.ACTION_DOWN == event.action)
                ) {
                    inputContent?.apply {
                        executeSearch(this)
                    }
                }
                true
            }
        }

        with(search_flex) {
            for (str in commandList) {
                val view = TextView(safeContext)

                view.text = str
                view.background = safeContext.getDrawable(R.drawable.flexbox_text_background)

                val lp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )

                lp.setMargins(0, Utility.setPxToDp(5), Utility.setPxToDp(10), Utility.setPxToDp(5))
                view.layoutParams = lp
                view.setOnClickListener {
                    executeSearch(str)
                }

                this.addView(view)
            }
        }

        if (searchHistoryDao.getHistoryData().isEmpty()) {
            search_history.visibility = View.GONE
        }

        with(search_rv) {
            historyAdapter.updateData(ArrayList(searchHistoryDao.getHistoryData()))
            adapter = historyAdapter

            historyAdapter.setDelete { str, date ->
                executeDeleteHistory(str, date)
            }

            historyAdapter.setSearch {
                executeSearch(it)
            }

            addItemDecoration(MyDistanceDividerDecoration(DIRECTION_BOTTOM, DISTANCE))
        }
    }

    override fun initWithResume() {}

    private fun executeDeleteHistory(str: String, date: Long) {
        val historyBean = SearchHistoryBean(date, str)
        searchHistoryDao.delete(historyBean)
    }

    private fun executeSearch(str: String) {
        val bundle = Bundle().apply { putString(SEARCH_KEYWORD, str) }
        val historyBean = SearchHistoryBean(System.currentTimeMillis(), str)

        searchHistoryDao.insert(historyBean)
        PerformLinkCenter.instance.performLinkWithContext(
                safeContext,
                "demo://view/search/detail",
                bundle
        )
    }
}
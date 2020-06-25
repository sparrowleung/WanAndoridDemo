package com.learning.demomode.search

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import com.alibaba.android.arouter.facade.annotation.Route
import com.learning.common.base.BaseActivity
import com.learning.common.router.Router.APP_SEARCH_ACTIVITY
import com.learning.demomode.R

@Route(path = APP_SEARCH_ACTIVITY)
class SearchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setActionBar()

        val fragment = SearchFragment().apply {
            this.arguments = intent.extras
        }
        onAttachFragmentAsSingle(fragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home ->{
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
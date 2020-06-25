package com.learning.demomode.search.detail

import android.os.Bundle
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Route
import com.learning.common.base.BaseActivity
import com.learning.common.router.Router.APP_SEARCH_DETAIL_ACTIVITY
import com.learning.demomode.R

@Route(path = APP_SEARCH_DETAIL_ACTIVITY)
class SearchDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)
        setActionBar()

        val fragment = SearchDetailFragment().apply {
            this.arguments = intent.extras
        }

        onAttachFragmentAsSingle(fragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }
}
package com.learning.common.webview

import android.os.Bundle
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Route
import com.learning.common.R
import com.learning.common.base.BaseActivity
import com.learning.common.router.Router

@Route(path = Router.COMMON_WEB_VIEW)
class CommonWebActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        setActionBar()

        val commonWebFragment = CommonWebFragment()
        intent.extras?.apply {
            commonWebFragment.arguments = this
        }

        onAttachFragmentAsSingle(commonWebFragment)
    }

    fun onPopOutFragment() {
        val backEntry =
            supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1)

        backEntry.apply {
            if (supportFragmentManager.backStackEntryCount == 1) {
                finish()
            } else {
                supportFragmentManager.popBackStackImmediate()
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
            return
        }

        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is IBackPressFunction) {
            fragment.onBackPress()
        } else {
            onPopOutFragment()
        }
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
package com.learning.demomode.system.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.learning.common.base.BaseActivity
import com.learning.common.router.Router.APP_SYSTEM_DETAIL_ACTIVITY
import com.learning.demomode.R
import com.learning.demomode.databinding.ActivitySysDetailBinding

@Route(path = APP_SYSTEM_DETAIL_ACTIVITY)
class SystemDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySysDetailBinding>(this, R.layout.activity_sys_detail)

        val fragment = SystemDetailFragment()
        intent?.apply {
            fragment.arguments = this.extras
        }
        setActionBar()
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
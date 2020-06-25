package com.learning.common.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.learning.common.R

open class BaseActivity : AppCompatActivity() {

    protected lateinit var safeContext: Context

    companion object {
        lateinit var TAG: String
        lateinit var mTitle: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeContext = this
        TAG = this::javaClass.name
    }

    protected fun setActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.apply {
            this.setContentInsetsAbsolute(0 ,0)
            this.contentInsetStartWithNavigation = 0
            setSupportActionBar(this)
        }

        supportActionBar?.apply {
            this.setDisplayHomeAsUpEnabled(true)
            this.setHomeAsUpIndicator(R.drawable.drawable_actionbar_back)
        }
    }


    protected fun onAttachFragmentAsSingle(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commitAllowingStateLoss()
        }

        supportFragmentManager.executePendingTransactions()
    }
}
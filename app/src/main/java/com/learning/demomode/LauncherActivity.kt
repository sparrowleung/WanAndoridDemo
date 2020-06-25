package com.learning.demomode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.learning.common.weight.PerformLinkCenter
import com.learning.demomode.home.MainActivity

class LauncherActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!this.isTaskRoot) {
            intent?.apply {
                if (this.hasCategory(Intent.CATEGORY_LAUNCHER) && this.action.equals(Intent.ACTION_MAIN)) {
                    finish()
                    return
                }
            }
        }

        if (intent != null && intent.dataString?.isNotEmpty() == true) {
            PerformLinkCenter.instance.performLinkWithContext(
                baseContext,
                intent.dataString,
                intent.extras
            )
            finish()
            return
        }

        init()
    }

    private fun init() {
        performToNext()
    }

    private fun performToNext() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
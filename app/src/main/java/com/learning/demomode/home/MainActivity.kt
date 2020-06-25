package com.learning.demomode.home

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentPagerAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.learning.common.weight.MToast
import com.learning.demomode.R
import com.learning.common.base.BaseActivity
import com.learning.common.base.FragmentItem
import com.learning.common.router.Router
import com.learning.common.weight.PerformLinkCenter
import com.learning.demomode.databinding.ActivityMainBinding
import com.learning.demomode.officail.OfficialAccountFragment
import com.learning.demomode.project.ProjectFragment
import com.learning.demomode.system.SystemFragment
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = Router.APP_MAIN_ACTIVITY)
class MainActivity : BaseActivity() {

    private lateinit var mActivityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initData()
        setNavigationView()
    }

    private fun initData() {
        val list = arrayListOf<FragmentItem>()
        list.add(FragmentItem("首页", HomeFragment()))
        list.add(FragmentItem("项目", ProjectFragment()))
        list.add(FragmentItem("体系", SystemFragment()))
        list.add(FragmentItem("公众号", OfficialAccountFragment()))

        with(main_viewPager) {
            adapter = MainPagerAdapter(
                supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                list
            )
            currentItem = 0
        }

        with(main_tabLayout) {
            tabGravity = TabLayout.GRAVITY_CENTER
            tabMode = TabLayout.MODE_FIXED
            isTabIndicatorFullWidth = false
            setupWithViewPager(mActivityMainBinding.mainViewPager)

            addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {

                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {

                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    var position = p0?.position
                    when (position) {
                        0 -> {
                            //Analysis
                        }

                        1 -> {
                            //Analysis
                        }
                    }
                }
            })

            for (i in 0..tabCount) {
                getTabAt(i)?.text = list[i].name
            }
        }

        main_navi_img.setOnClickListener {
            openDrawer()
        }

        main_search_img.setOnClickListener {
            PerformLinkCenter.instance.performLink(safeContext, "demo://view/search")
        }
    }

    private fun setNavigationView() {
        val view = LayoutInflater.from(safeContext).inflate(R.layout.layout_navigation_header, null)
        view.findViewById<TextView>(R.id.header_name).text = "testMode"
        view.findViewById<ImageView>(R.id.header_img).setImageResource(R.mipmap.ic_launcher)

        with(main_navigation) {
            addHeaderView(view)
            setNavigationItemSelectedListener {
                closeDrawer()
                when (it.itemId) {
                    R.id.item_nav_happy_minute -> {
                        MToast.makeText(safeContext, it.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.item_nav_favorite -> {
                        MToast.makeText(safeContext, it.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.item_nav_setting -> {
                        MToast.makeText(safeContext, it.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.item_nav_about -> {
                        MToast.makeText(safeContext, it.title, Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }

    private fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }
}

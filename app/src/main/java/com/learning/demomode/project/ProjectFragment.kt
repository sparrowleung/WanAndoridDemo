package com.learning.demomode.project

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.learning.common.base.BaseFragment
import com.learning.common.bean.ProjectCategoryBean
import com.learning.common.database.DemoDataBase
import com.learning.common.weight.MToast
import com.learning.common.weight.Utility
import com.learning.demomode.R
import com.learning.demomode.REQUEST_PROJECT
import com.learning.demomode.databinding.FragmentProjectBinding
import com.learning.demomode.project.pager.ProjectPagerFragment
import kotlinx.android.synthetic.main.fragment_project.*
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class ProjectFragment : BaseFragment<ProjectViewModel, FragmentProjectBinding>() {

    private val projectDB by lazy { DemoDataBase.getDataBase(safeContext) }

    override fun layoutId(): Int = R.layout.fragment_project

    override fun initWithCreate() {
        baseViewModel.setProjectDB(projectDB)

        baseViewModel.getProjectCategory().observe(this, Observer {
            val fragmentList: ArrayList<Fragment> = ArrayList()
            for (element in it) {
                fragmentList.add(ProjectPagerFragment.getInstance(element.id))
            }

            with(project_viewPager) {
                adapter =
                    ProjectPagerAdapter(
                        childFragmentManager,
                        FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                        fragmentList
                    )
                currentItem = 0
            }

            with(project_tabLayout) {
                tabMode = TabLayout.MODE_SCROLLABLE
                tabGravity = TabLayout.GRAVITY_FILL
                isTabIndicatorFullWidth = false
                setupWithViewPager(project_viewPager)
                for (i in it.indices) {
                    getTabAt(i)?.text = Utility.compatHtmlToStr(it[i].name)
                }
            }

            project_progress.visibility = View.GONE
        })

        baseViewModel.getRequestError().observe(this, Observer {
            for (key in it.keys) {
                Log.e(
                    TAG,
                    "Network Error [type: $key] -> code : ${it[key]?.statusCode} / msg : ${it[key]?.errorMsg}"
                )
                when (key) {
                    REQUEST_PROJECT -> {
                        project_progress.visibility = View.GONE
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

    }

    override fun initWithResume() {
        if (Utility.isNetworkAvailable()) {
            baseViewModel.requestProjectCategory()
            if (projectDB.projectCategoryDao().getProjectCategoryData().isEmpty()) {
                project_progress.visibility = View.VISIBLE
            }
        } else {
            val projectList = projectDB.projectCategoryDao().getProjectCategoryData()
            val fragmentList = ArrayList<Fragment>()
            val tabTextList = ArrayList<String>()

            for (category in projectList) {
                fragmentList.add(ProjectPagerFragment.getInstance(category.id))
                tabTextList.add(category.name)
            }

            with(project_viewPager) {
                adapter =
                    ProjectPagerAdapter(
                        childFragmentManager,
                        FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                        fragmentList
                    )
                currentItem = 0
            }

            with(project_tabLayout) {
                tabMode = TabLayout.MODE_SCROLLABLE
                tabGravity = TabLayout.GRAVITY_FILL
                isTabIndicatorFullWidth = false
                setupWithViewPager(project_viewPager)

                for (i in projectList.indices) {
                    getTabAt(i)?.text = Utility.compatHtmlToStr(tabTextList[i])
                }
            }
        }
    }
}
package com.learning.demomode.project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ProjectPagerAdapter(
    fragmentManager: FragmentManager,
    behavior: Int,
    private var dataList: ArrayList<Fragment> = ArrayList()
) : FragmentPagerAdapter(fragmentManager, behavior) {

    fun setFragmentList(list: ArrayList<Fragment>){
        dataList = list
    }

    override fun getItem(position: Int): Fragment {
        return dataList[position]
    }

    override fun getCount(): Int {
        return dataList.size
    }
}
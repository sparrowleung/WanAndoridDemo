package com.learning.demomode.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.learning.common.base.FragmentItem

class MainPagerAdapter(fragmentManager: FragmentManager, intStyle:Int, private val list: ArrayList<FragmentItem>)
    : FragmentPagerAdapter(fragmentManager, intStyle){

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position].name
    }

    override fun getItem(position: Int): Fragment {
        return list[position].fragment
    }

    override fun getCount(): Int {
        return list.size
    }
}
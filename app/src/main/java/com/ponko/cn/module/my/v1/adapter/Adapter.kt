package com.ponko.cn.module.my.v1.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * 积分商城ViewPager适配器
 */
class Adapter(fm: FragmentManager, val frgs: ArrayList<Fragment>, val id: ArrayList<Int>, val title: ArrayList<String>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment {
        return frgs[p0]
    }

    override fun getCount(): Int {
        return frgs.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

    fun getFragment(pos: Int): Fragment {
        return frgs[pos]
    }
}
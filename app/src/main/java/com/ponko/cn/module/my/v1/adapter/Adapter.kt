package com.ponko.cn.module.my.v1.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
/**
 * 积分商城ViewPager适配器
 */
class Adapter(fm: FragmentManager, val frgs: ArrayList<Fragment>, val title: ArrayList<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment {
        return frgs[p0]
    }

    override fun getCount(): Int {
        return frgs.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}
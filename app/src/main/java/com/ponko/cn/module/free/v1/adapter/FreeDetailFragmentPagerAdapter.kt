package com.ponko.cn.module.free.v1.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * 适配器
 */
class FreeDetailFragmentPagerAdapter(private val frgs: List<Fragment>?, private val titles: List<String>?, private val fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment {
        return frgs!![p0]
    }

    override fun getCount(): Int {
        return frgs?.size!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles!![position]
    }
}

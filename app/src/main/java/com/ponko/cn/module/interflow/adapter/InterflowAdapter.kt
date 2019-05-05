package com.ponko.cn.module.interflow.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class InterflowAdapter(fm: FragmentManager, val frgs: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment {
        return frgs[p0]
    }

    override fun getCount(): Int {
        return frgs.size
    }
}
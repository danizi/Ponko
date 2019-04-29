package com.ponko.cn.module.my

import android.view.View
import com.ponko.cn.R
import com.xm.lib.common.base.mvp.MvpFragment

class MyFrg : MvpFragment<Any>() {
    override fun presenter(): Any {
        return Any()
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_nav_my
    }

    override fun findViews(view: View) {
    }

    override fun initDisplay() {
    }

    override fun iniEvent() {
    }

    override fun iniData() {
    }
}
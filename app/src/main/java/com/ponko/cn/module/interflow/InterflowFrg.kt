package com.ponko.cn.module.interflow

import android.view.View
import com.ponko.cn.R
import com.xm.lib.common.base.mvp.MvpFragment

class InterflowFrg : MvpFragment<Any>() {
    override fun presenter(): Any {
        return Any()
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_nav_interflow
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
package com.ponko.cn.module.common

import android.support.v7.widget.Toolbar
import android.view.View
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.base.mvp.MvpActivity

abstract class PonkoBaseAct<P>: MvpActivity<P>() {

    override fun setContentViewBefore() {}

    override fun findViews() {}

    override fun initDisplay() {}

    override fun iniData() {}

    override fun iniEvent() {}

}
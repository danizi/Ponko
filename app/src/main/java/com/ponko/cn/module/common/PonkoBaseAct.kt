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

    protected fun addBar1(toolbar:Toolbar?,title: String){
        BarUtil.addBar1(this,toolbar,title)
    }

    protected fun addBar2(toolbar:Toolbar?,title: String,barRight:String?="",barRightlistener: View.OnClickListener){
        BarUtil.addBar2(this,toolbar,title,barRight,barRightlistener)
    }

    protected fun addBar3(toolbar:Toolbar?,title: String,barRight:String?="",barRightlistener: View.OnClickListener){
        BarUtil.addBar3(this,toolbar,title,barRight,barRightlistener)
    }

    protected fun addWhiteBar(toolbar:Toolbar?,title: String,barRight:String?="",barRightlistener: View.OnClickListener){
        BarUtil.addWhiteBar(this,toolbar,title,barRight,barRightlistener)
    }

}
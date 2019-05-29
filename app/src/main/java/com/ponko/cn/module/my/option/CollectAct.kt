package com.ponko.cn.module.my.option

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.ponko.cn.R
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.my.constract.CollectContract


/**
 * 收藏
 */
class CollectAct : PonkoBaseAct<CollectContract.Present>(), CollectContract.V {

    /**
     * 收藏UI
     */
    private var ui: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun presenter(): CollectContract.Present {
        return CollectContract.Present(context = this, v = this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_coloct
    }

    override fun findViews() {
        super.findViews()
        if (ui == null) {
            ui = ViewHolder.create(this)
        }
    }

    override fun iniEvent() {
        super.iniEvent()
        ui?.vp?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                p?.pageSelected(p0)
            }
        })
        ui?.ivBack?.setOnClickListener {
            p?.clickBack()
        }
        ui?.btnSection?.setOnClickListener {
            p?.clickSection()
        }
        ui?.btnCourse?.setOnClickListener {
            p?.clickCourse()
        }
        ui?.ivDelete?.setOnClickListener {
            p?.clickDelete()
        }
    }

    override fun iniData() {
        super.iniData()
        val fragments = p?.getFragments()
        if (fragments?.isNotEmpty()!!) {
            ui?.vp?.adapter = CollectContract.M.ViewPagerAdapter(fragments, supportFragmentManager)
        }
    }

    override fun pageSelected(index: Int) {
        when (index) {
            0 -> {
                ui?.btnSection?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk_s)
                ui?.btnCourse?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk)
                ui?.btnSection?.setTextColor(Color.parseColor("#FFFFFF"))
                ui?.btnCourse?.setTextColor(Color.parseColor("#FFFF5A5E")) //红色
            }
            1 -> {
                ui?.btnCourse?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk_s)
                ui?.btnSection?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk)
                ui?.btnCourse?.setTextColor(Color.parseColor("#FFFFFF"))
                ui?.btnSection?.setTextColor(Color.parseColor("#FFFF5A5E")) //红色
            }
        }
    }

    override fun deleteModeDisplay(pageIndex: Int) {

    }

    override fun unDeleteModeDisplay(pageIndex: Int) {

    }

    /**
     * 窗口ui
     */
    private class ViewHolder private constructor(val clTopMenu: ConstraintLayout, val ivBack: AppCompatImageButton, val linearLayout4: LinearLayout, val btnSection: Button, val btnCourse: Button, val ivDelete: AppCompatImageButton, val vp: ViewPager) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val clTopMenu = rootView.findViewById<View>(R.id.cl_top_menu) as ConstraintLayout
                val ivBack = rootView.findViewById<View>(R.id.iv_back) as AppCompatImageButton
                val linearLayout4 = rootView.findViewById<View>(R.id.linearLayout4) as LinearLayout
                val btnSection = rootView.findViewById<View>(R.id.btn_section) as Button
                val btnCourse = rootView.findViewById<View>(R.id.btn_course) as Button
                val ivDelete = rootView.findViewById<View>(R.id.iv_delete) as AppCompatImageButton
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                return ViewHolder(clTopMenu, ivBack, linearLayout4, btnSection, btnCourse, ivDelete, vp)
            }
        }
    }

}

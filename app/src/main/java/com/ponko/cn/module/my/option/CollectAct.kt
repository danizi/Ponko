package com.ponko.cn.module.my.option

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
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
        ui?.btnDelete?.setOnClickListener {
            p?.clickBottomDelete()
        }
        ui?.btnAllSelect?.setOnClickListener {
            p?.clickBottomSelect()
        }
    }

    private var fragments: ArrayList<Fragment>? = null

    override fun iniData() {
        super.iniData()
        fragments = p?.getFragments()
        if (fragments?.isNotEmpty()!!) {
            ui?.vp?.adapter = CollectContract.M.ViewPagerAdapter(fragments!!, supportFragmentManager)
        }
    }

    override fun pageSelected(index: Int) {
        when (index) {
            0 -> {
                ui?.btnSection?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk_s)
                ui?.btnCourse?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk)
                ui?.btnSection?.setTextColor(Color.parseColor("#FFFFFF"))
                ui?.btnCourse?.setTextColor(Color.parseColor("#FFFF5A5E")) //红色
                ui?.vp?.currentItem = 0
            }
            1 -> {
                ui?.btnCourse?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk_s)
                ui?.btnSection?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk)
                ui?.btnCourse?.setTextColor(Color.parseColor("#FFFFFF"))
                ui?.btnSection?.setTextColor(Color.parseColor("#FFFF5A5E")) //红色
                ui?.vp?.currentItem = 1
            }
        }
    }

    override fun deleteModeDisplay(pageIndex: Int) {
        ui?.llBottom?.visibility = View.VISIBLE
        (fragments!![0] as CollectContract.V.CollectSectionFragment).deleteModeDisplay()
        (fragments!![1] as CollectContract.V.CollectCourseFragment).deleteModeDisplay()
    }

    override fun unDeleteModeDisplay(pageIndex: Int) {
        ui?.llBottom?.visibility = View.INVISIBLE
        (fragments!![0] as CollectContract.V.CollectSectionFragment).unDeleteModeDisplay()
        (fragments!![1] as CollectContract.V.CollectCourseFragment).unDeleteModeDisplay()
    }

    override fun selectAll(pageIndex: Int) {
        when(pageIndex){
            0->{
                (fragments!![pageIndex] as CollectContract.V.CollectSectionFragment).selectAll()
            }
            1->{
                (fragments!![pageIndex] as CollectContract.V.CollectCourseFragment).selectAll()
            }
        }
    }

    override fun unselectAll(pageIndex: Int) {
        when(pageIndex){
            0->{
                (fragments!![pageIndex] as CollectContract.V.CollectSectionFragment).unselectAll()
            }
            1->{
                (fragments!![pageIndex] as CollectContract.V.CollectCourseFragment).unselectAll()
            }
        }
    }

    override fun deleteData(pageIndex: Int) {
        when(pageIndex){
            0->{
                (fragments!![pageIndex] as CollectContract.V.CollectSectionFragment).deleteData()
            }
            1->{
                (fragments!![pageIndex] as CollectContract.V.CollectCourseFragment).deleteData()
            }
        }
    }

    /**
     * 窗口ui
     */
    private class ViewHolder private constructor(val clTopMenu: ConstraintLayout, val ivBack: AppCompatImageButton, val linearLayout4: LinearLayout, val btnSection: Button, val btnCourse: Button, val ivDelete: AppCompatImageButton, val vp: ViewPager, val llBottom: LinearLayout, val btnAllSelect: Button, val btnDelete: Button) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val clTopMenu = rootView.findViewById<View>(R.id.cl_top_menu) as ConstraintLayout
                val ivBack = rootView.findViewById<View>(R.id.iv_back) as AppCompatImageButton
                val linearLayout4 = rootView.findViewById<View>(R.id.linearLayout4) as LinearLayout
                val btnSection = rootView.findViewById<View>(R.id.btn_section) as Button
                val btnCourse = rootView.findViewById<View>(R.id.btn_course) as Button
                val ivDelete = rootView.findViewById<View>(R.id.iv_delete) as AppCompatImageButton
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                val llBottom = rootView.findViewById<View>(R.id.ll_bottom) as LinearLayout
                val btnAllSelect = rootView.findViewById<View>(R.id.btn_all_select) as Button
                val btnDelete = rootView.findViewById<View>(R.id.btn_delete) as Button
                return ViewHolder(clTopMenu, ivBack, linearLayout4, btnSection, btnCourse, ivDelete, vp, llBottom, btnAllSelect, btnDelete)
            }
        }
    }


}

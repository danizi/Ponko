package com.ponko.cn.module.interflow.frg

import android.annotation.SuppressLint
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Button
import com.ponko.cn.R
import com.ponko.cn.module.interflow.adapter.InterflowAdapter
import com.ponko.cn.module.interflow.constract.InterflowConstract
import com.xm.lib.common.base.mvp.MvpFragment


class InterflowFrg : MvpFragment<InterflowConstract.Present>(), InterflowConstract.View {

    private var viewHolder: ViewHolder? = null

    override fun presenter(): InterflowConstract.Present {
        return InterflowConstract.Present(context, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_nav_interflow
    }

    override fun findViews(view: View) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(view)
        }
    }

    override fun initDisplay() {

    }

    override fun iniEvent() {
        viewHolder?.vp?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            @SuppressLint("ResourceAsColor")
            override fun onPageSelected(p0: Int) {
                //改变顶部指示器状态
                when (p0) {
                    0 -> {
                        viewHolder?.btnCase?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk_s)
                        viewHolder?.btnActivity?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk)
                        viewHolder?.btnCase?.setTextColor(context?.resources?.getColor(R.color.white)!!)
                        viewHolder?.btnActivity?.setTextColor(context?.resources?.getColor(R.color.red_5e)!!)
                    }
                    1 -> {
                        viewHolder?.btnActivity?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk_s)
                        viewHolder?.btnCase?.setBackgroundResource(R.drawable.interflow_top_nav_btn_bk)
                        viewHolder?.btnActivity?.setTextColor(context?.resources?.getColor(R.color.white)!!)
                        viewHolder?.btnCase?.setTextColor(context?.resources?.getColor(R.color.red_5e)!!)
                    }
                }
            }

        })
        viewHolder?.btnCase?.setOnClickListener {
            viewHolder?.vp?.currentItem = 0
        }
        viewHolder?.btnActivity?.setOnClickListener {
            viewHolder?.vp?.currentItem = 1
        }
    }

    override fun iniData() {
        val frgs = ArrayList<Fragment>()
        frgs.add(CaseFragment())
        frgs.add(ActivityFragment())
        viewHolder?.vp?.adapter = InterflowAdapter(childFragmentManager, frgs)
    }

    /**
     * ViewHolder
     */
    private class ViewHolder private constructor(val clTopMenu: ConstraintLayout, val btnCase: Button, val btnActivity: Button, val vp: ViewPager) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val clTopMenu = rootView.findViewById<View>(R.id.cl_top_menu) as ConstraintLayout
                val btnCase = rootView.findViewById<View>(R.id.btn_case) as Button
                val btnActivity = rootView.findViewById<View>(R.id.btn_activity) as Button
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                return ViewHolder(clTopMenu, btnCase, btnActivity, vp)
            }
        }
    }
}
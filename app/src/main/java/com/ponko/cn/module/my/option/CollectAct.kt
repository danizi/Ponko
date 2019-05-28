package com.ponko.cn.module.my.option

import android.annotation.SuppressLint
import android.os.Bundle
import com.ponko.cn.R
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.my.constract.CollectConstract
import android.support.v4.view.ViewPager
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


/**
 * 收藏
 */
class CollectAct : PonkoBaseAct<CollectConstract.Present>(), CollectConstract.V {

    /**
     * 收藏UI
     */
    private var ui: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun presenter(): CollectConstract.Present {
        return CollectConstract.Present(context = this, v = this)
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

    /**
     * 窗口ui
     */
    private class ViewHolder private constructor(val clTopMenu: ConstraintLayout, val btnSection: Button, val btnCourse: Button, val vp: ViewPager) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val clTopMenu = rootView.findViewById<View>(R.id.cl_top_menu) as ConstraintLayout
                val btnSection = rootView.findViewById<View>(R.id.btn_section) as Button
                val btnCourse = rootView.findViewById<View>(R.id.btn_course) as Button
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                return ViewHolder(clTopMenu, btnSection, btnCourse, vp)
            }
        }
    }

    /**
     * 适配器
     */
    private class viewPagerApt(val frgs: ArrayList<Fragment>, fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(p0: Int): Fragment {
            return frgs[p0]
        }

        override fun getCount(): Int {
            return if (frgs.isEmpty()) {
                0
            } else {
                frgs.size
            }
        }
    }

    /**
     * 收藏章节
     */
    @SuppressLint("ValidFragment")
    private class CollectSectionFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    /**
     * 收藏课程
     */
    @SuppressLint("ValidFragment")
    private class CollectCourseFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }
    }
}

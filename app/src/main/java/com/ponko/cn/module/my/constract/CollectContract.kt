package com.ponko.cn.module.my.constract

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.db.bean.CourseCollectSectionDbBean
import com.ponko.cn.db.bean.CourseCollectSpecialDbBean
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.my.option.collect.CollectListActivity
import com.ponko.cn.module.study.StudyCourseDetailActivity
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.log.BKLog


/**
 * 收藏契约类 - MVP模式
 */
class CollectContract {
    /**
     * 视图层
     */
    interface V {
        /**
         * 滑动到第几个页面
         */
        fun pageSelected(index: Int)

        /**
         * 删除模式页面展示处理
         */
        fun deleteModeDisplay(pageIndex: Int)

        /**
         * 非删除模式页面展示处理
         */
        fun unDeleteModeDisplay(pageIndex: Int)

        /**
         * 选中所有
         */
        fun selectAll(pageIndex: Int)

        /**
         * 不选中所有
         */
        fun unselectAll(pageIndex: Int)

        /**
         * 删除所选数据
         */
        fun deleteData(pageIndex: Int)

        /**
         * 收藏章节Fragment
         */
        @SuppressLint("ValidFragment")
        class CollectSectionFragment : RefreshLoadFrg<Any, ArrayList<CourseCollectSectionDbBean>>() {

            private var collectSections = ArrayList<CourseCollectSectionDbBean>()

            override fun initDisplay() {
                disableLoad = true
                addItemDecoration = false
                viewHolder?.clContent?.setBackgroundColor(Color.WHITE)
                super.initDisplay()
            }

            override fun presenter(): Any {
                return Any()
            }

            override fun bindItemViewHolderData(): BindItemViewHolderBean {
                return BindItemViewHolderBean.create(
                        arrayOf(0),
                        arrayOf(CollectViewHolder::class.java),
                        arrayOf(Any::class.java),
                        arrayOf(R.layout.item_search_result_sub_tv)
                )
            }

            override fun requestMoreApi() {}


            override fun requestRefreshApi() {
                collectSections.clear()
                if(CacheUtil.isUserTypeLogin()){
                    collectSections.addAll(PonkoApp.collectSectionDao?.selectAll()!!)
                }
                requestRefreshSuccess(collectSections)
            }

            override fun multiTypeData(body: ArrayList<CourseCollectSectionDbBean>?): List<Any> {
                return body!!
            }

            override fun adapter(): BaseRvAdapter? {
                return object : BaseRvAdapter() {}
            }

            fun deleteModeDisplay() {
                for (bean in adapter?.data!!) {
                    val courseCollectSectionDbBean = bean as CourseCollectSectionDbBean
                    courseCollectSectionDbBean.isDelete = true
                }
                adapter?.notifyDataSetChanged()
            }

            fun unDeleteModeDisplay() {
                for (bean in adapter?.data!!) {
                    val courseCollectSectionDbBean = bean as CourseCollectSectionDbBean
                    courseCollectSectionDbBean.isDelete = false
                }
                adapter?.notifyDataSetChanged()
            }

            fun selectAll() {
                for (bean in adapter?.data!!) {
                    val courseCollectSectionDbBean = bean as CourseCollectSectionDbBean
                    courseCollectSectionDbBean.isSelect = true
                }
                adapter?.notifyDataSetChanged()
            }

            fun unselectAll() {
                for (bean in adapter?.data!!) {
                    val courseCollectSectionDbBean = bean as CourseCollectSectionDbBean
                    courseCollectSectionDbBean.isSelect = false
                }
                adapter?.notifyDataSetChanged()
            }

            fun deleteData() {
                for (i in (0 until viewHolder?.rv?.childCount!!)) {
                    if (viewHolder?.rv?.getChildAt(i)?.findViewById<CheckBox>(R.id.cb)?.isChecked == true) {
                        val collectSection = collectSections[i]
                        PonkoApp.collectSectionDao?.deleteBySectionId(collectSection.column_section_id)
                        adapter?.data?.remove(collectSection)
                        adapter?.notifyItemRemoved(i)
                        adapter?.notifyItemChanged(i)
                        BKLog.d(collectSection.column_section_name + "需要被删除")
                    }
                }
            }

            /**
             * 課程ViewHolder
             */
            class CollectViewHolder(view: View) : BaseViewHolder(view) {
                private var ui: ViewHolder? = null

                override fun bindData(d: Any, position: Int) {
                    if (ui == null) {
                        ui = ViewHolder.create(itemView)
                    }
                    val context = itemView.context
                    val collectSectionDbBean = d as CourseCollectSectionDbBean
                    if (collectSectionDbBean.isDelete) {
                        ui?.cb?.visibility = View.VISIBLE
                    } else {
                        ui?.cb?.visibility = View.GONE
                    }
                    ui?.cb?.isChecked = collectSectionDbBean.isSelect

                    ui?.tv?.text = collectSectionDbBean.column_section_name
                    itemView.setOnClickListener {
                        if (collectSectionDbBean.isDelete) {
                            ui?.cb?.isChecked = !ui?.cb?.isChecked!!
                        } else {
                            BKLog.d("跳转到小节列表")
                            StudyCourseDetailActivity.start(context, collectSectionDbBean.column_course_id, "", 0L, 0L)
                        }
                    }
                }

                private class ViewHolder private constructor(val tv: TextView, val rlClear: RelativeLayout, val divider: View, val cb: CheckBox) {
                    companion object {

                        fun create(rootView: View): ViewHolder {
                            val tv = rootView.findViewById<View>(R.id.tv) as TextView
                            val rlClear = rootView.findViewById<View>(R.id.rl_clear) as RelativeLayout
                            val divider = rootView.findViewById(R.id.divider) as View
                            val cb = rootView.findViewById<View>(R.id.cb) as CheckBox
                            return ViewHolder(tv, rlClear, divider, cb)
                        }
                    }
                }
            }
        }

        /**
         * 收藏课程Fragment
         */
        @SuppressLint("ValidFragment")
        class CollectCourseFragment : RefreshLoadFrg<Any, ArrayList<CourseCollectSpecialDbBean>>() {

            private var collectSpecialDaos = ArrayList<CourseCollectSpecialDbBean>()

            override fun initDisplay() {
                disableLoad = true
                addItemDecoration = false
                viewHolder?.clContent?.setBackgroundColor(Color.WHITE)
                super.initDisplay()
                viewHolder?.rv?.addItemDecoration(MyItemDecoration.divider(context, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))  //https://www.jianshu.com/p/86aaaa49ed3e
            }

            override fun bindItemViewHolderData(): BindItemViewHolderBean {
                return BindItemViewHolderBean.create(
                        arrayOf(0),
                        arrayOf(CollectCourseViewHolder::class.java),
                        arrayOf(Any::class.java),
                        arrayOf(R.layout.item_collect_course)
                )
            }

            override fun requestMoreApi() {}


            override fun requestRefreshApi() {
                collectSpecialDaos.clear()
                if(CacheUtil.isUserTypeLogin()){
                    collectSpecialDaos.addAll(PonkoApp.collectSpecialDao?.selectAll()!!)
                    //查看章节中是否还有该课程id
                    for (collectSpecial in collectSpecialDaos) {
                        val sections = PonkoApp.collectSectionDao?.selectByCourseId(collectSpecial.column_course_id)
                        if (sections?.isEmpty()!!) {
                            PonkoApp.collectSpecialDao?.deleteByCourseId(collectSpecial.column_course_id)
                            collectSpecialDaos.remove(collectSpecial)
                        }
                    }
                }
                requestRefreshSuccess(collectSpecialDaos)
            }

            override fun multiTypeData(body: ArrayList<CourseCollectSpecialDbBean>?): List<Any> {
                return body!!
            }

            override fun adapter(): BaseRvAdapter? {
                return object : BaseRvAdapter() {}
            }

            override fun presenter(): Any {
                return Any()
            }

            fun deleteModeDisplay() {
                for (bean in adapter?.data!!) {
                    val courseCollectSpecialDbBean = bean as CourseCollectSpecialDbBean
                    courseCollectSpecialDbBean.isDelete = true
                }
                adapter?.notifyDataSetChanged()
            }

            fun unDeleteModeDisplay() {
                for (bean in adapter?.data!!) {
                    val courseCollectSpecialDbBean = bean as CourseCollectSpecialDbBean
                    courseCollectSpecialDbBean.isDelete = false
                }
                adapter?.notifyDataSetChanged()
            }

            fun selectAll() {
                for (bean in adapter?.data!!) {
                    val courseCollectSpecialDbBean = bean as CourseCollectSpecialDbBean
                    courseCollectSpecialDbBean.isSelect = true
                }
                adapter?.notifyDataSetChanged()
            }

            fun unselectAll() {
                for (bean in adapter?.data!!) {
                    val courseCollectSpecialDbBean = bean as CourseCollectSpecialDbBean
                    courseCollectSpecialDbBean.isSelect = false
                }
                adapter?.notifyDataSetChanged()
            }

            fun deleteData() {
                for (i in (0 until viewHolder?.rv?.childCount!!)) {
                    if (viewHolder?.rv?.getChildAt(i)?.findViewById<CheckBox>(R.id.cb)?.isChecked == true) {
                        val collectSpecial = collectSpecialDaos[i]
                        PonkoApp.collectSpecialDao?.deleteByCourseId(collectSpecial.column_course_id)
                        adapter?.data?.remove(collectSpecial)
                        adapter?.notifyItemRemoved(i)
                        adapter?.notifyItemChanged(i)
                        BKLog.d(collectSpecial.column_title + "需要被删除")
                    }
                }
            }

            class CollectCourseViewHolder(view: View) : BaseViewHolder(view) {
                private var ui: ViewHolder? = null
                @SuppressLint("SetTextI18n")
                override fun bindData(d: Any, position: Int) {
                    if (ui == null) {
                        ui = ViewHolder.create(itemView)
                    }
                    val context = itemView.context
                    val courseCollectSpecialDbBean = d as CourseCollectSpecialDbBean
                    if (courseCollectSpecialDbBean.isDelete) {
                        ui?.cb?.visibility = View.VISIBLE
                    } else {
                        ui?.cb?.visibility = View.GONE
                    }
                    ui?.cb?.isChecked = courseCollectSpecialDbBean.isSelect
                    Glide.with(context, courseCollectSpecialDbBean.column_cover, ui?.ivCover)
                    ui?.tvCourseTitle?.text = courseCollectSpecialDbBean.column_title
                    ui?.tvTeacher?.text = "${courseCollectSpecialDbBean.column_teacher}老师"
                    ui?.courseNumber?.text = "共${courseCollectSpecialDbBean.column_num}集 "
                    itemView.setOnClickListener {
                        if (courseCollectSpecialDbBean.isDelete) {
                            ui?.cb?.isChecked = !ui?.cb?.isChecked!!
                        } else {
                            BKLog.d("跳转到小节列表")
                            CollectListActivity.start(context, courseCollectSpecialDbBean.column_title, courseCollectSpecialDbBean.column_course_id)
                        }

                    }
                }


                private class ViewHolder private constructor(val ivCover: ImageView, val tvCourseTitle: TextView, val tvTeacher: TextView, val courseNumber: TextView, val imageView2: ImageView, val cb: CheckBox) {
                    companion object {

                        fun create(rootView: View): ViewHolder {
                            val ivCover = rootView.findViewById<View>(R.id.iv_cover) as ImageView
                            val tvCourseTitle = rootView.findViewById<View>(R.id.tv_course_title) as TextView
                            val tvTeacher = rootView.findViewById<View>(R.id.tv_teacher) as TextView
                            val courseNumber = rootView.findViewById<View>(R.id.course_number) as TextView
                            val imageView2 = rootView.findViewById<View>(R.id.imageView2) as ImageView
                            val cb = rootView.findViewById<View>(R.id.cb) as CheckBox
                            return ViewHolder(ivCover, tvCourseTitle, tvTeacher, courseNumber, imageView2, cb)
                        }
                    }
                }

            }
        }
    }

    /**
     * 数据层
     */
    class M {
        /**
         * 当前显示页面下标
         */
        var pageIndex = 0
        /**
         * 删除模式还是非删除模式
         */
        var isDeleteMode = false
        /**
         * 全选还是非全选
         */
        var isSelectAll = false

        /**
         * 适配器
         */
        class ViewPagerAdapter(val frgs: ArrayList<Fragment>, fm: FragmentManager) : FragmentPagerAdapter(fm) {
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
    }

    /**
     * 控制层
     */
    class Present(private val context: Context, private val v: V) {
        private val m = M()

        /**
         * 滑动到第几个页面
         */
        fun pageSelected(index: Int) {
            m.pageIndex = index
            v.pageSelected(index)
            BKLog.d("ViewPager 滑动到$index")
        }

        /**
         * 点击回退
         */
        fun clickBack() {
            (context as Activity).finish()
            BKLog.d("点击退出")
        }

        /**
         * 点击章节
         */
        fun clickSection() {
            m.pageIndex = 0
            v.pageSelected(m.pageIndex)
            BKLog.d("点击章节")
        }

        /**
         * 点击了课程
         */
        fun clickCourse() {
            m.pageIndex = 1
            v.pageSelected(m.pageIndex)
            BKLog.d("点击课程")
        }

        /**
         * 点击删除
         */
        fun clickDelete() {
            m.isDeleteMode = !m.isDeleteMode
            if (m.isDeleteMode) {
                v.deleteModeDisplay(m.pageIndex)
                BKLog.d("删除模式")
            } else {
                v.unDeleteModeDisplay(m.pageIndex)
                BKLog.d("标准模式")
            }
        }

        /**
         * 删除数据库信息
         */
        fun clickBottomDelete() {
            BKLog.d("点击底部删除")
            v.deleteData(m.pageIndex)
        }

        /**
         * 全选、不全选
         */
        fun clickBottomSelect() {
            BKLog.d("点击底部全选")
            m.isSelectAll = !m.isSelectAll
            if (m.isSelectAll) {
                v.selectAll(m.pageIndex)
            } else {
                v.unselectAll(m.pageIndex)
            }
        }

        /**
         * 获取fragment集合
         */
        fun getFragments(): ArrayList<Fragment>? {
            val fragments = ArrayList<Fragment>()
            fragments.add(V.CollectSectionFragment())
            fragments.add(V.CollectCourseFragment())
            return fragments
        }
    }
}
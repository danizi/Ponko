package com.ponko.cn.module.my.constract

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.module.common.RefreshLoadFrg
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import android.widget.RelativeLayout
import android.widget.TextView
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.db.bean.CourseCollectSectionDbBean


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
         * 收藏章节Fragment
         */
        @SuppressLint("ValidFragment")
        class CollectSectionFragment : RefreshLoadFrg<Any, ArrayList<CourseCollectSectionDbBean>>() {

            override fun initDisplay() {
                viewHolder?.toolbar?.visibility = View.GONE
                disableLoad = true
                disableRefresh = true
                addItemDecoration = false
                super.initDisplay()
            }

            override fun presenter(): Any {
                return Any()
            }


            override fun bindItemViewHolderData(): BindItemViewHolderBean {
                return BindItemViewHolderBean.create(
                        arrayOf(0),
                        arrayOf(ViewHolder::class.java),
                        arrayOf(Any::class.java),
                        arrayOf(R.layout.item_search_result_sub_tv)
                )
            }

            override fun requestMoreApi() {}

            override fun requestRefreshApi() {
                val collectSections = PonkoApp.collectSectionDao?.selectAll()
                requestRefreshSuccess(collectSections)
            }

            override fun multiTypeData(body: ArrayList<CourseCollectSectionDbBean>?): List<Any> {
                return body!!
            }

            override fun adapter(): BaseRvAdapter? {
                return object : BaseRvAdapter() {}
            }

            private class ViewHolder private constructor(val tv: TextView, val rlClear: RelativeLayout, val divider: View) {
                companion object {

                    fun create(rootView: View): ViewHolder {
                        val tv = rootView.findViewById<View>(R.id.tv) as TextView
                        val rlClear = rootView.findViewById<View>(R.id.rl_clear) as RelativeLayout
                        val divider = rootView.findViewById(R.id.divider) as View
                        return ViewHolder(tv, rlClear, divider)
                    }
                }
            }

        }

        /**
         * 收藏课程Fragment
         */
        @SuppressLint("ValidFragment")
        class CollectCourseFragment : MvpFragment<Any>() {
            override fun presenter(): Any {
                return Any()
            }

            override fun getLayoutId(): Int {
                return R.layout.item_my_rv
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


    }

    /**
     * 数据层
     */
    class M {
        /**
         * 当前显示页面下标
         */
        var pageIndex = 0
        var isDeleteMode = false

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
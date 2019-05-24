package com.ponko.cn.module.my.option

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.courseDao
import com.ponko.cn.app.PonkoApp.Companion.courseSpecialDao
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.db.bean.CourseSpecialDbBean
import com.ponko.cn.db.dao.CourseSpecialDao
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.study.StudyCacheActivity
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.log.BKLog

/**
 * 我的页面-缓存
 */
class CacheAct : RefreshLoadAct<Any, List<CourseSpecialDbBean>>() {

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(ItemViewHolder::class.java),
                arrayOf(CourseSpecialDbBean::class.java),
                arrayOf(R.layout.item_course_introduction)
        )
    }

    override fun initDisplay() {
        BarUtil.addBar1(this, viewHolder?.toolbar, "离线缓存")
        disableLoad = true
        addItemDecoration = false
        super.initDisplay()
        viewHolder?.rv?.addItemDecoration(MyItemDecoration.divider(this, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
    }

    override fun requestMoreApi() {

    }

    override fun onResume() {
        super.onResume()
        val datas = courseSpecialDao?.selectAll()
        if (!datas?.isEmpty()!!) {
            for (courseSpecial in datas) {
                //通过专题来寻找课程
                val courses = courseDao?.selectBySpecialId(courseSpecial.special_id)
                //如果课程为空则删除专题
                if (courses?.isEmpty()!!) {
                    courseSpecialDao?.delete(courseSpecial)
                }
            }
        }
        //重新再刷新一次页面
        requestRefreshApi()
    }

    override fun requestRefreshApi() {
        val courseSpecialDao = CourseSpecialDao(PonkoApp.dbHelp?.writableDatabase)
        val data = courseSpecialDao.selectAll()
        requestRefreshSuccess(data)
    }

    override fun multiTypeData(body: List<CourseSpecialDbBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }


    /**
     * 专题列表
     */
    open class ItemViewHolder(view: View) : BaseViewHolder(view) {
        /**
         * 控件ui
         */
        private var viewHolder: ViewHolder? = null

        @SuppressLint("SetTextI18n")
        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }

            val courseSpecialDbBean = d as CourseSpecialDbBean
            val context = itemView.context
            Glide.with(context, courseSpecialDbBean.cover, viewHolder?.ivCover)
            viewHolder?.tvCourseTitle?.text = courseSpecialDbBean.title
            viewHolder?.tvTeacher?.text = "${courseSpecialDbBean.teacher}老师"
            viewHolder?.courseNumber?.text = "共${courseSpecialDbBean.num}集 | 已缓存${PonkoApp.courseDao?.completeCount(courseSpecialDbBean.special_id)}"
            courseSpecialDbBean.special_id
            itemView.setOnClickListener {
                BKLog.d("跳转到专题缓存课程列表")
                CacheListAct.start(
                        context,
                        courseSpecialDbBean.special_id,
                        courseSpecialDbBean.title,
                        courseSpecialDbBean.teacher,
                        courseSpecialDbBean.num,
                        courseSpecialDbBean.duration)
                //ActivityUtil.startActivity(context, Intent(context, CacheListAct::class.java))
            }
        }

        /**
         * 专题 ui
         */
        private class ViewHolder private constructor(val ivCover: ImageView, val tvCourseTitle: TextView, val tvTeacher: TextView, val courseNumber: TextView, val imageView2: ImageView) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val ivCover = rootView.findViewById<View>(R.id.iv_cover) as ImageView
                    val tvCourseTitle = rootView.findViewById<View>(R.id.tv_course_title) as TextView
                    val tvTeacher = rootView.findViewById<View>(R.id.tv_teacher) as TextView
                    val courseNumber = rootView.findViewById<View>(R.id.course_number) as TextView
                    val imageView2 = rootView.findViewById<View>(R.id.imageView2) as ImageView
                    return ViewHolder(ivCover, tvCourseTitle, tvTeacher, courseNumber, imageView2)
                }
            }
        }

    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_cache)
//    }

}

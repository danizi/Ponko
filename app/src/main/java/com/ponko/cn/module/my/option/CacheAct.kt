package com.ponko.cn.module.my.option

import android.support.v7.widget.DividerItemDecoration
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.db.bean.CourseSpecialDbBean
import com.ponko.cn.db.dao.CourseSpecialDao
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog

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
        //disableRefresh = true
        disableLoad = true
        addItemDecoration = false
        super.initDisplay()
        viewHolder?.rv?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun requestMoreApi() {
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


    private class ItemViewHolder(view: View) : BaseViewHolder(view) {
        private var viewHolder: ViewHolder? = null
        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }
            val courseSpecialDbBean = d as CourseSpecialDbBean
            val context = itemView.context
            Glide.with(context, courseSpecialDbBean.cover, viewHolder?.ivCover)
            viewHolder?.tvCourseTitle?.text = courseSpecialDbBean.title
            viewHolder?.tvTeacher?.text = "${courseSpecialDbBean.teacher}老师"
            viewHolder?.courseNumber?.text = "共${courseSpecialDbBean.num}集 | 已缓存0"
            itemView.setOnClickListener {
                BKLog.d("跳转到专题缓存课程列表")
            }
        }


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

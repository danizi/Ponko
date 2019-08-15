package com.ponko.cn.module.free.v1.holder

import android.annotation.SuppressLint
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.CoursesCBean
import com.ponko.cn.bean.WechatBean
import com.ponko.cn.module.study.v1.StudyCourseDetailActivity
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import com.xm.lib.common.base.rv.v1.BaseViewHolder
import com.xm.lib.common.util.NumUtil

/**
 * 微信课程
 */
class WechatViewHolder(view: View?) : BaseViewHolder(view!!) {
    private var ui: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (ui == null) {
            ui = ViewHolder.create(itemView)
        }
        val context = itemView.context
        val wechats = d as WechatBean
        ui?.tvTitle?.text = "微信领取"
        val adapter = object : BaseRvAdapter() {}
        adapter.addItemViewDelegate(0, CourseViewHolder::class.java, Any::class.java, R.layout.item_course_introduction)
        adapter.data?.addAll(wechats.wechat!!)
        ui?.rv?.adapter = adapter
        ui?.rv?.layoutManager = LinearLayoutManager(context)
        ui?.rv?.addItemDecoration(MyItemDecoration.divider(context, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
        ui?.rv?.isFocusableInTouchMode = false
    }

    /**
     * 兑换列表Item ViewHolder
     */
    class CourseViewHolder(view: View) : BaseViewHolder(view) {
        private var ui: UI? = null
        @SuppressLint("SetTextI18n")
        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = UI.create(itemView)
            }
            val exchanged = d as CoursesCBean.Exchanged
            val context = itemView.context
            Glide.with(context, exchanged.image, ui?.ivCover)
            ui?.tvCourseTitle?.text = exchanged.title.toString()

            var teachers = ""
            if (exchanged.teachers != null) {
                for (i in 0..(exchanged.teachers.size - 1)) {
                    teachers += if (i == 0) {
                        exchanged.teachers[i]
                    } else {
                        " | " + exchanged.teachers[i]
                    }
                }
            }
            if (TextUtils.isEmpty(teachers)) {
                ui?.tvTeacher?.visibility = View.GONE
            } else {
                ui?.tvTeacher?.text = "${teachers}老师"
            }
            ui?.courseNumber?.text = "共${exchanged.num}集 | ${NumUtil.getDecimalPoint(exchanged.duration?.toFloat()!! / 60f / 60f)}小时"
            itemView.setOnClickListener {
                //测试使用
                //exchanged.id = "e90b1cbc845411e5a95900163e000c35"
                StudyCourseDetailActivity.start(context, exchanged.id, teachers, exchanged.num.toLong(), exchanged.duration.toLong())
            }
        }

        private class UI private constructor(val ivCover: ImageView, val tvCourseTitle: TextView, val tvTeacher: TextView, val courseNumber: TextView, val imageView2: ImageView) {
            companion object {
                fun create(rootView: View): UI {
                    val ivCover = rootView.findViewById<View>(R.id.iv_cover) as ImageView
                    val tvCourseTitle = rootView.findViewById<View>(R.id.tv_course_title) as TextView
                    val tvTeacher = rootView.findViewById<View>(R.id.tv_teacher) as TextView
                    val courseNumber = rootView.findViewById<View>(R.id.course_number) as TextView
                    val imageView2 = rootView.findViewById<View>(R.id.imageView2) as ImageView
                    return UI(ivCover, tvCourseTitle, tvTeacher, courseNumber, imageView2)
                }
            }
        }
    }

    /**
     * 兑换列表ViewHolder
     */
    private class ViewHolder private constructor(val tvTitle: TextView, val rv: RecyclerView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return ViewHolder(tvTitle, rv)
            }
        }
    }
}
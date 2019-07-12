package com.ponko.cn.module.my.v1.holder

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.InternalCourseCBean
import com.ponko.cn.module.study.v1.StudyCourseDetailActivity
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.util.NumUtil

/**
 * 已兑课程
 */
class ExchangedClassVH(view: View) : BaseViewHolder(view) {

    private var ui: UI? = null
    @SuppressLint("SetTextI18n")
    override fun bindData(d: Any, position: Int) {
        if (ui == null) {
            ui = UI.create(itemView)
        }
        val exchanged = d as InternalCourseCBean
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
            StudyCourseDetailActivity.start(context, exchanged.id, teachers, exchanged.num!!.toLong(), exchanged.duration!!.toLong())
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
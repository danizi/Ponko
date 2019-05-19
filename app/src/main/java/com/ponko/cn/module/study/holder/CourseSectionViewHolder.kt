package com.ponko.cn.module.study.holder

import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.module.study.CourseTypeLinearActivity
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog

class CourseSectionViewHolder(view: View?) : BaseViewHolder(view!!) {


    private class ViewHolder private constructor(val cardView: CardView, val ivCover: ImageView, val tvCourseSection1: TextView, val tvCourseSection2: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val cardView = rootView.findViewById<View>(R.id.cardView) as CardView
                val ivCover = rootView.findViewById<View>(R.id.iv_cover) as ImageView
                val tvCourseSection1 = rootView.findViewById<View>(R.id.tv_course_section1) as TextView
                val tvCourseSection2 = rootView.findViewById<View>(R.id.tv_course_section2) as TextView
                return ViewHolder(cardView, ivCover, tvCourseSection1, tvCourseSection2)
            }
        }
    }

    private var v: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (v == null) {
            v = ViewHolder.create(itemView)
        }

        val typeBean = d as MainCBean.TypesBeanX.TypesBean
        val context = itemView.context
        Glide.with(context, typeBean.picture, v?.ivCover)
        v?.tvCourseSection1?.text = typeBean.title
        v?.tvCourseSection2?.text = typeBean.subtitle2!!
        v?.ivCover?.setOnClickListener {
            BKLog.d("点击了课程专题")
            CourseTypeLinearActivity.start(context,typeBean.title,typeBean.id)
        }
    }

}
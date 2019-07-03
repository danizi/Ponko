package com.ponko.cn.module.study.v1.holder

import android.app.Activity
import android.content.Context
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.module.study.v1.CourseTypeLinearActivity
import com.ponko.cn.module.study.v2.CourseTypeLinearActivityV2
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil

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
        ivivCoverSize(context)
        v?.tvCourseSection1?.text = typeBean.title
        v?.tvCourseSection2?.text = typeBean.subtitle2!!
        itemView.setOnClickListener {
            BKLog.d("点击了课程专题:${typeBean.title}")
            when (PonkoApp.getAppUIVersion(context)) {
                Constants.UI_VERSION_1 -> {
                    CourseTypeLinearActivity.start(context, typeBean.title, typeBean.id)
                }
                Constants.UI_VERSION_2 -> {
                    CourseTypeLinearActivityV2.start(context, typeBean.title, typeBean.id)
                }
            }

        }
    }

    private fun ivivCoverSize(context: Context) {
        var w = ScreenUtil.getNormalWH(context as Activity)[0]
        w = (w - ScreenUtil.dip2px(context, 40)) / 2
        v?.ivCover?.layoutParams?.height = w * 94 / 164
    }

}
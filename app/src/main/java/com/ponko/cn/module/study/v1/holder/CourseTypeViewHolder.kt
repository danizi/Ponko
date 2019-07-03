package com.ponko.cn.module.study.v1.holder

import android.app.Activity
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.CourseTypeBean
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.module.study.v1.CourseTypeGridActivity
import com.ponko.cn.module.study.v1.adapter.CourseSectionAdapter
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.base.rv.decoration.GridItemDecoration
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil

class CourseTypeViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val tvTitle: TextView, val btnPayCourse: Button, val ivDetails: ImageView, val vp: RecyclerView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val btnPayCourse = rootView.findViewById<View>(R.id.btn_pay_course) as Button
                val ivDetails = rootView.findViewById<View>(R.id.iv_details) as ImageView
                val vp = rootView.findViewById<View>(R.id.vp) as RecyclerView
                return ViewHolder(tvTitle, btnPayCourse, ivDetails, vp)
            }
        }
    }

    private var v: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (v == null) {
            v = ViewHolder.create(itemView)
        }
        val typesBeanX = (d as CourseTypeBean).types
        val context = itemView.context
        v?.tvTitle?.text = typesBeanX?.title
        if (typesBeanX?.isIs_vip!!) {
            v?.ivDetails?.visibility = View.GONE
            v?.btnPayCourse?.text = "已购"
            v?.btnPayCourse?.setOnClickListener {
                BKLog.d("点击已购按钮,跳转到课程分类列表")
                CourseTypeGridActivity.start(context, typesBeanX.title, typesBeanX.type_id)
            }
        } else {
            v?.ivDetails?.visibility = View.VISIBLE
            //设置广告尺寸
            ivDetailsSize(context)

            v?.btnPayCourse?.text = "未购"
            Glide.with(context, typesBeanX.avatar, v?.ivDetails)
            v?.btnPayCourse?.setOnClickListener {
                BKLog.d("点击未购按钮")
                IntoTargetUtil.target(context, "pay", typesBeanX.url)
            }
            v?.ivDetails?.setOnClickListener {
                IntoTargetUtil.target(context, "pay", typesBeanX.url)
                BKLog.d("跳转到支付页面")
            }
        }
        v?.tvTitle?.text = typesBeanX.title

        //展示分类课程
        courseSection(context, typesBeanX)
    }

    private fun ivDetailsSize(context: Context) {
        var w = ScreenUtil.getNormalWH(context as Activity)[0]
        w = (w - ScreenUtil.dip2px(context, 30)) / 2
        v?.ivDetails?.layoutParams?.height = w * 113 / 347
        v?.ivDetails?.layoutParams?.height = w * 2 / 3
    }

    private fun courseSection(context: Context, typesBeanX: MainCBean.TypesBeanX) {
        v?.vp?.layoutManager = GridLayoutManager(context, 2)
        v?.vp?.addItemDecoration(GridItemDecoration.Builder(context)
                .setHorizontalSpan(ScreenUtil.dip2px(context, 15).toFloat())
                .setVerticalSpan(ScreenUtil.dip2px(context, 15).toFloat())
                .setColorResource(R.color.white)
                .setShowLastLine(false)
                .build())
        v?.vp?.isFocusableInTouchMode = false
        v?.vp?.requestFocus()
        val adapter = CourseSectionAdapter()
        adapter.data?.addAll(typesBeanX.types)
        adapter.addItemViewDelegate(0, CourseSectionViewHolder::class.java, Any::class.java, R.layout.item_study_course_section)
        v?.vp?.adapter = adapter
    }
}
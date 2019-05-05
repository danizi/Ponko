package com.ponko.cn.module.my.holder

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.MyBean
import com.xm.lib.common.base.rv.BaseViewHolder
import de.hdodenhof.circleimageview.CircleImageView


class MyViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val btnWxUnbind: Button, val clUserInfo: ConstraintLayout, val tvName: TextView, val ivVipNoOrYes: ImageView, val tvVipDes: TextView, val ivCircleHead: CircleImageView, val imageView4: ImageView, val clOther: ConstraintLayout, val llCourse: LinearLayout, val tvCourseNumber: TextView, val tvCourseDes: TextView, val llTime: LinearLayout, val tvTimeNumber: TextView, val tvTimeDes: TextView, val llIntegral: LinearLayout, val tvIntegralNumber: TextView, val tvIntegralDes: TextView, val clOpenInvite: ConstraintLayout, val clOpenRoll: ConstraintLayout, val llOpen: LinearLayout, val clInvite: ConstraintLayout, val llInvite: LinearLayout) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val btnWxUnbind = rootView.findViewById<View>(R.id.btn_wx_unbind) as Button
                val clUserInfo = rootView.findViewById<View>(R.id.cl_user_info) as ConstraintLayout
                val tvName = rootView.findViewById<View>(R.id.tv_name) as TextView
                val ivVipNoOrYes = rootView.findViewById<View>(R.id.iv_vip_no_or_yes) as ImageView
                val tvVipDes = rootView.findViewById<View>(R.id.tv_vip_des) as TextView
                val ivCircleHead = rootView.findViewById<View>(R.id.iv_circle_head) as CircleImageView
                val imageView4 = rootView.findViewById<View>(R.id.imageView4) as ImageView
                val clOther = rootView.findViewById<View>(R.id.cl_other) as ConstraintLayout
                val llCourse = rootView.findViewById<View>(R.id.ll_course) as LinearLayout
                val tvCourseNumber = rootView.findViewById<View>(R.id.tv_course_number) as TextView
                val tvCourseDes = rootView.findViewById<View>(R.id.tv_course_des) as TextView
                val llTime = rootView.findViewById<View>(R.id.ll_time) as LinearLayout
                val tvTimeNumber = rootView.findViewById<View>(R.id.tv_time_number) as TextView
                val tvTimeDes = rootView.findViewById<View>(R.id.tv_time_des) as TextView
                val llIntegral = rootView.findViewById<View>(R.id.ll_integral) as LinearLayout
                val tvIntegralNumber = rootView.findViewById<View>(R.id.tv_integral_number) as TextView
                val tvIntegralDes = rootView.findViewById<View>(R.id.tv_integral_des) as TextView
                val clOpenInvite = rootView.findViewById<View>(R.id.cl_open_invite) as ConstraintLayout
                val clOpenRoll = rootView.findViewById<View>(R.id.cl_open_roll) as ConstraintLayout
                val llOpen = rootView.findViewById<View>(R.id.ll_open) as LinearLayout
                val clInvite = rootView.findViewById<View>(R.id.cl_invite) as ConstraintLayout
                val llInvite = rootView.findViewById<View>(R.id.ll_invite) as LinearLayout
                return ViewHolder(btnWxUnbind, clUserInfo, tvName, ivVipNoOrYes, tvVipDes, ivCircleHead, imageView4, clOther, llCourse, tvCourseNumber, tvCourseDes, llTime, tvTimeNumber, tvTimeDes, llIntegral, tvIntegralNumber, tvIntegralDes, clOpenInvite, clOpenRoll, llOpen, clInvite, llInvite)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val myBeans = MyBean.create()
        val context = itemView.context
    }
}
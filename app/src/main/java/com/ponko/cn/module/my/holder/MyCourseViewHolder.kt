package com.ponko.cn.module.my.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.StoreProfileCMoreBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder


class MyCourseViewHolder(view: View) : BaseViewHolder(view) {


    private class ViewHolder private constructor(val ivCourse: ImageView, val tvCourseName: TextView, val tvIntegralNum: TextView, val tvExchanged: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val ivCourse = rootView.findViewById<View>(R.id.iv_course) as ImageView
                val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                val tvIntegralNum = rootView.findViewById<View>(R.id.tv_integral_num) as TextView
                val tvExchanged = rootView.findViewById<View>(R.id.tv_exchanged) as TextView
                return ViewHolder(ivCourse, tvCourseName, tvIntegralNum, tvExchanged)
            }
        }
    }


    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val storesBean = d as StoreProfileCMoreBean.StoresBean
        val context = itemView.context
        Glide.with(context,storesBean.picture,viewHolder?.ivCourse)
        viewHolder?.tvCourseName?.text = storesBean.name
        viewHolder?.tvIntegralNum?.text = storesBean.scores.toString()
        viewHolder?.tvExchanged?.text = storesBean.expend.toString()

    }
}
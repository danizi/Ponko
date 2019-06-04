package com.ponko.cn.module.my.holder

import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.RemindCBean
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.util.TimeUtil


class MyRemindHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val tvTitle: TextView, val tvTime: TextView, val tvDes: TextView, val llDetail: ConstraintLayout, val textView6: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                val tvDes = rootView.findViewById<View>(R.id.tv_des) as TextView
                val llDetail = rootView.findViewById<View>(R.id.ll_detail) as ConstraintLayout
                val textView6 = rootView.findViewById<View>(R.id.textView6) as TextView
                return ViewHolder(tvTitle, tvTime, tvDes, llDetail, textView6)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val remindCBean = d as RemindCBean
        val context = itemView.context
        if (remindCBean.type.equals("SIMPLE", true)) {
            //不可跳转的
            viewHolder?.llDetail?.visibility = View.GONE
        } else {
            //可跳转的
            viewHolder?.llDetail?.visibility = View.VISIBLE
            itemView.setOnClickListener {
                IntoTargetUtil.target(context, remindCBean.type, remindCBean.url)
            }
        }
        viewHolder?.tvTitle?.text = remindCBean.title
        viewHolder?.tvDes?.text = remindCBean.description
        viewHolder?.tvTime?.text = TimeUtil.unixStr("yyyy/MM/dd HH:mm:ss", remindCBean.createTime)
    }
}
package com.ponko.cn.module.my.holder

import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.OpenCBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog


class MyOpenViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val ivBk: ImageView, val ivLogo: ImageView, val clRight: ConstraintLayout, val tvTitle: TextView, val ivVip: ImageView, val tvExpireIn: TextView, val btnPay: Button, val tvSummaries: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val ivBk = rootView.findViewById<View>(R.id.iv_bk) as ImageView
                val ivLogo = rootView.findViewById<View>(R.id.iv_logo) as ImageView
                val clRight = rootView.findViewById<View>(R.id.cl_right) as ConstraintLayout
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val ivVip = rootView.findViewById<View>(R.id.iv_vip) as ImageView
                val tvExpireIn = rootView.findViewById<View>(R.id.tv_expire_in) as TextView
                val btnPay = rootView.findViewById<View>(R.id.btn_pay) as Button
                val tvSummaries = rootView.findViewById<View>(R.id.tv_summaries) as TextView
                return ViewHolder(ivBk, ivLogo, clRight, tvTitle, ivVip, tvExpireIn, btnPay, tvSummaries)
            }
        }
    }


    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val openCBean = d as OpenCBean
        val context = itemView.context
        Glide.with(context, openCBean.bg_url, viewHolder?.ivBk)
        Glide.with(context, openCBean.logo_url, viewHolder?.ivLogo)
        viewHolder?.tvTitle?.text = openCBean.title
        viewHolder?.tvSummaries?.text = openCBean.summaries.toString()
        viewHolder?.tvSummaries?.setTextColor(Color.parseColor(openCBean.summary_color))
        itemView.setOnClickListener {
            BKLog.d("点击了${openCBean.title}")
        }

    }
}
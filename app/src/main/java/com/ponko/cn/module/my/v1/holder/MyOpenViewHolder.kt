package com.ponko.cn.module.my.holder

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.OpenCBean
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil


class MyOpenViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val ivBk: ImageView, val ivLogo: ImageView, val clRight: ConstraintLayout, val tvTitle: TextView, val ivVip: ImageView, val tvExpireIn: TextView, val btnPay: Button, val tv1: TextView, val tv2: TextView, val tv3: TextView, val tv4: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val ivBk = rootView.findViewById<View>(R.id.iv_bk) as ImageView
                val ivLogo = rootView.findViewById<View>(R.id.iv_logo) as ImageView
                val clRight = rootView.findViewById<View>(R.id.cl_right) as ConstraintLayout
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val ivVip = rootView.findViewById<View>(R.id.iv_vip) as ImageView
                val tvExpireIn = rootView.findViewById<View>(R.id.tv_expire_in) as TextView
                val btnPay = rootView.findViewById<View>(R.id.btn_pay) as Button
                val tv1 = rootView.findViewById<View>(R.id.tv_1) as TextView
                val tv2 = rootView.findViewById<View>(R.id.tv_2) as TextView
                val tv3 = rootView.findViewById<View>(R.id.tv_3) as TextView
                val tv4 = rootView.findViewById<View>(R.id.tv_4) as TextView
                return ViewHolder(ivBk, ivLogo, clRight, tvTitle, ivVip, tvExpireIn, btnPay, tv1, tv2, tv3, tv4)
            }
        }
    }


    private var viewHolder: ViewHolder? = null

    @SuppressLint("SetTextI18n")
    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val openCBean = d as OpenCBean
        val context = itemView.context
        Glide.with(context, openCBean.bg_url, viewHolder?.ivBk)
        Glide.with(context, openCBean.logo_url, viewHolder?.ivLogo)
        viewHolder?.tvTitle?.text = openCBean.title
        viewHolder?.tv1?.text = openCBean.summaries[0]
        viewHolder?.tv2?.text = openCBean.summaries[1]
        viewHolder?.tv3?.text = openCBean.summaries[2]
        viewHolder?.tv4?.text = openCBean.summaries[3]
        viewHolder?.tv1?.setTextColor(Color.parseColor(openCBean.summary_color))
        viewHolder?.tv2?.setTextColor(Color.parseColor(openCBean.summary_color))
        viewHolder?.tv3?.setTextColor(Color.parseColor(openCBean.summary_color))
        viewHolder?.tv4?.setTextColor(Color.parseColor(openCBean.summary_color))
        viewHolder?.btnPay?.setBackgroundColor(Color.parseColor(openCBean.summary_color))
        if (openCBean.expire_in > 0) {
            //订购状态
            viewHolder?.ivVip?.setImageResource(R.mipmap.vip_red)
            viewHolder?.tvExpireIn?.text = "有限期至${TimeUtil.unixStr("yyyy/MM/dd HH:mm:ss", openCBean.expire_in)}"
            viewHolder?.btnPay?.text="续费"
        } else {
            //非订购状态
            viewHolder?.ivVip?.setImageResource(R.mipmap.my_info_no_vip)
            viewHolder?.tvExpireIn?.text = "您尚未开通"
            viewHolder?.btnPay?.text="订购"
        }
        viewHolder?.btnPay?.setOnClickListener {
            BKLog.d("点击了${openCBean.title}")
            IntoTargetUtil.target(context, "pay", openCBean.url)
        }
        itemView.setOnClickListener {
            BKLog.d("点击了${openCBean.title}")
            IntoTargetUtil.target(context, "pay", openCBean.url)
        }
    }
}
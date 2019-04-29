package com.ponko.cn.module.study.holder

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.AdBean2
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.util.ScreenUtil

class AdViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val tvAdTitle: TextView, val tvMore: TextView, val ivAd: ImageView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tvAdTitle = rootView.findViewById<View>(R.id.tv_ad_title) as TextView
                val tvMore = rootView.findViewById<View>(R.id.tv_more) as TextView
                val ivAd = rootView.findViewById<View>(R.id.iv_ad) as ImageView
                return ViewHolder(tvAdTitle, tvMore, ivAd)
            }
        }
    }


    private var v: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (v == null) {
            v = ViewHolder.create(itemView)
        }
        val adBean = d as AdBean2
        val ad = adBean.ad!![position]
        val context = itemView.context

        //图片高度自适应
        val present = adBean.ad!![position].height.toFloat() / adBean.ad!![position].width.toFloat()
        val layoutParams = v?.ivAd?.layoutParams
        layoutParams?.height = ((ScreenUtil.getNormalWH(context as Activity)[0] - ScreenUtil.dip2px(context, 28)) * present).toInt()

        v?.tvAdTitle?.text = ad.title
        Glide.with(context, ad.image, v?.ivAd)
        v?.ivAd?.setOnClickListener {
            IntoTargetUtil.target(context, ad.type, ad.link)
        }
    }
}
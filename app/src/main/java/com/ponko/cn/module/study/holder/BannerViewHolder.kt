package com.ponko.cn.module.study.holder

import android.app.Activity
import android.content.Context
import android.support.constraint.Guideline
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BannerBean
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.module.study.CourseTypeGridActivity
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.util.ScreenUtil
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader

class BannerViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val banner: Banner, val llB2b: LinearLayout, val ivB2b: ImageView, val tvB2b: TextView, val view: View, val llB2c: LinearLayout, val ivB2c: ImageView, val tvB2c: TextView, val guideline7: Guideline) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val banner = rootView.findViewById<View>(R.id.banner) as Banner
                val llB2b = rootView.findViewById<View>(R.id.ll_b2b) as LinearLayout
                val ivB2b = rootView.findViewById<View>(R.id.iv_b2b) as ImageView
                val tvB2b = rootView.findViewById<View>(R.id.tv_b2b) as TextView
                val view = rootView.findViewById(R.id.view) as View
                val llB2c = rootView.findViewById<View>(R.id.ll_b2c) as LinearLayout
                val ivB2c = rootView.findViewById<View>(R.id.iv_b2c) as ImageView
                val tvB2c = rootView.findViewById<View>(R.id.tv_b2c) as TextView
                val guideline7 = rootView.findViewById<View>(R.id.guideline7) as Guideline
                return ViewHolder(banner, llB2b, ivB2b, tvB2b, view, llB2c, ivB2c, tvB2c, guideline7)
            }
        }
    }


    private var v: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (v == null) {
            v = ViewHolder.create(itemView)
        }
        val context = itemView.context
        val bannerBean = d as BannerBean
        //处理横幅
        val banner = bannerBean.scrolls
        if (banner?.isEmpty()!!) {
            v?.banner?.visibility = View.GONE
        } else {
            v?.banner?.visibility = android.view.View.VISIBLE
            v?.banner?.isAutoPlay(true)
            v?.banner?.setDelayTime(1500)
            v?.banner?.setBannerAnimation(Transformer.DepthPage)
            v?.banner?.setIndicatorGravity(BannerConfig.CENTER)
            v?.banner?.setImageLoader(object : ImageLoader() {
                override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                    Glide.with(context, (path as MainCBean.ScrollsBean).picture, imageView)
                }
            })
            v?.banner?.setImages(banner)
            v?.banner?.start()
            //点击横幅
            v?.banner?.setOnBannerListener { position ->
                IntoTargetUtil.target(context, banner[position].link_type, banner[position].link_value)
            }
            //横幅自适应高度，避免图片拉伸 0.344
            val layoutParams = v?.banner?.layoutParams
            layoutParams?.height = (ScreenUtil.getNormalWH(context as Activity)[0] * 0.344f).toInt()
        }

        //处理横幅下面两个按钮
        val b2b = bannerBean.tabbar?.get(0)
        val b2c = bannerBean.tabbar?.get(1)
        Glide.with(context, b2b?.icon_url, v?.ivB2b)
        Glide.with(context, b2c?.icon_url, v?.ivB2c)
        v?.tvB2b?.text = b2b?.icon_text
        v?.tvB2c?.text = b2c?.icon_text
        v?.llB2b?.setOnClickListener {
            if (PonkoApp.mainCBean?.types!![0].isIs_vip) {
                CourseTypeGridActivity.start(context, PonkoApp.mainCBean?.types!![0].title, PonkoApp.mainCBean?.types!![0].type_id)
            } else {
                IntoTargetUtil.target(context, b2b?.link_type, b2b?.link_value)
            }
        }
        v?.llB2c?.setOnClickListener {
            if (PonkoApp.mainCBean?.types!![1].isIs_vip) {
                CourseTypeGridActivity.start(context, PonkoApp.mainCBean?.types!![1].title, PonkoApp.mainCBean?.types!![1].type_id)
            } else {
                IntoTargetUtil.target(context, b2c?.link_type, b2c?.link_value)
            }

        }
    }
}
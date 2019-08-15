package com.ponko.cn.module.study.v2.holder

import android.app.Activity
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.Guideline
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.BannerV2Bean
import com.ponko.cn.bean.Main2CBean
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.v1.BaseViewHolder
import com.xm.lib.common.util.ScreenUtil
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader

class BannerV2ViewHolder(view: View) : BaseViewHolder(view) {

    private var v: UI? = null

    override fun bindData(d: Any, position: Int) {
        if (v == null) {
            v = UI.create(itemView)
        }
        val bannerBean = d as BannerV2Bean
        val context = itemView.context
        v?.clTaber?.visibility = View.GONE
        if (bannerBean.banner.isEmpty()) {
            v?.banner?.visibility = View.GONE
        } else {
            v?.banner?.visibility = android.view.View.VISIBLE
            v?.banner?.isAutoPlay(true)
            v?.banner?.setDelayTime(3000)
            //v?.banner?.setBannerAnimation(Transformer.DepthPage)
            v?.banner?.setIndicatorGravity(BannerConfig.CENTER)
            v?.banner?.setImageLoader(object : ImageLoader() {
                override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                    Glide.with(context, (path as Main2CBean.BannerTopBean).avatar, imageView)
                }
            })
            v?.banner?.setImages(bannerBean.banner)
            v?.banner?.start()
            //点击横幅
            v?.banner?.setOnBannerListener { position ->
                IntoTargetUtil.target(context, bannerBean.banner[position].link_type, bannerBean.banner[position].link_value)
            }
            //横幅自适应高度，避免图片拉伸 0.344
            val layoutParams = v?.banner?.layoutParams
            layoutParams?.height = (ScreenUtil.getNormalWH(context as Activity)[0] * 0.344f).toInt()
        }
    }

    /**
     * UI界面
     */
    private class UI private constructor(val banner: Banner, val clTaber: ConstraintLayout, val llB2b: LinearLayout, val ivB2b: ImageView, val tvB2b: TextView, val view: View, val llB2c: LinearLayout, val ivB2c: ImageView, val tvB2c: TextView, val guideline7: Guideline) {
        companion object {

            fun create(rootView: View): UI {
                val banner = rootView.findViewById<View>(R.id.banner) as Banner
                val clTaber = rootView.findViewById<View>(R.id.cl_taber) as ConstraintLayout
                val llB2b = rootView.findViewById<View>(R.id.ll_b2b) as LinearLayout
                val ivB2b = rootView.findViewById<View>(R.id.iv_b2b) as ImageView
                val tvB2b = rootView.findViewById<View>(R.id.tv_b2b) as TextView
                val view = rootView.findViewById(R.id.view) as View
                val llB2c = rootView.findViewById<View>(R.id.ll_b2c) as LinearLayout
                val ivB2c = rootView.findViewById<View>(R.id.iv_b2c) as ImageView
                val tvB2c = rootView.findViewById<View>(R.id.tv_b2c) as TextView
                val guideline7 = rootView.findViewById<View>(R.id.guideline7) as Guideline
                return UI(banner, clTaber, llB2b, ivB2b, tvB2b, view, llB2c, ivB2c, tvB2c, guideline7)
            }
        }
    }

}
package com.ponko.cn.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.ponko.cn.R
import com.xm.lib.common.helper.TimerHelper
import com.xm.lib.component.TransformationUtilV2
import com.xm.lib.component.XmAdView
import java.lang.Exception


object Glide {
    private val timerHelper = TimerHelper()

    /**
     * 针对一些第一次不显示图片使用
     */
    fun with(context: Context?, path: String?, imageView: ImageView?, delay: Long) {
        // ps:如果使用该方法容易出现UI卡顿，但是能解决第一次不显示问题
        Glide.with(context?.applicationContext).load(path)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.load_img_default)
                .crossFade(300)
                .dontAnimate()
//                .listener(object : RequestListener<String, GlideDrawable> {
//                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
//                        //ToastUtil.showToast(e?.message)
//                        return false
//                    }
//
//                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
//                        return false
//                    }
//                })
                .into(object : SimpleTarget<GlideDrawable>() {
                    //ps:添加回调处理第一次不显示图片问题，但是没有动画了
                    override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>?) {
                        if (resource != null) {
                            imageView?.setImageDrawable(resource)
                        }
                    }
                })

    }

    fun with(context: Context?, path: String?, targetView: View?) {
        // ps:如果使用该方法容易出现UI卡顿，但是能解决第一次不显示问题
        Glide.with(context?.applicationContext).load(path)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.load_img_default)
                .crossFade(300)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        //ToastUtil.showToast(e?.message)
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        return false
                    }
                }).into(object : SimpleTarget<GlideDrawable>() {
                    //ps:添加回调处理第一次不显示图片问题，但是没有动画了
                    override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>?) {
                        val drawable = resource?.current
                        if (Build.VERSION.SDK_INT >= 16) {
                            targetView?.background = drawable
                        } else {
                            targetView?.setBackgroundDrawable(drawable);
                        }

                    }
                })
    }

    fun with(context: Context?, path: String?, imageView: ImageView?, isDefault: Boolean? = true) {
        val requestManager = Glide.with(context?.applicationContext).load(path)
        requestManager
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

        if (isDefault!!) {
            requestManager.placeholder(R.mipmap.load_img_default)
        }

        requestManager.crossFade(300)
                .into(imageView)
    }
}
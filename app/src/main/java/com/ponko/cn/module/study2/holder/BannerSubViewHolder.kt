package com.ponko.cn.module.study2.holder

import android.app.Activity
import android.content.Context
import android.provider.Contacts
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import com.ponko.cn.R
import com.ponko.cn.bean.StudyHomeBannerSubBean
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader


/**
 * 底部小横幅
 */
class BannerSubViewHolder(view: View) : BaseViewHolder(view) {

    private var ui: UI? = null

    override fun bindData(d: Any, position: Int) {
        if (ui == null) {
            ui = UI.create(itemView)
        }
        val context = itemView.context
        val studyHomeBannerSubBean = d as StudyHomeBannerSubBean
        val banner = studyHomeBannerSubBean.images
        if (banner.isEmpty()) {
            ui?.banner?.visibility = View.GONE
        } else {
            ui?.banner?.visibility = android.view.View.VISIBLE
            ui?.banner?.isAutoPlay(true)
            ui?.banner?.setDelayTime(5000)
            //ui?.banner?.setBannerAnimation(Transformer.DepthPage)
            ui?.banner?.setIndicatorGravity(BannerConfig.CENTER)
            ui?.banner?.setImageLoader(object : ImageLoader() {
                override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                    Glide.with(context,path as String, imageView)
                }
            })
            ui?.banner?.setImages(banner)
            ui?.banner?.start()
            ui?.banner?.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(p0: Int) {
                    //设置选中指示器的宽度
                    val indicators = getFieldValueByFieldName("indicatorImages", ui?.banner!!)
                    for (img in indicators!!) {
                        img.layoutParams?.width = ScreenUtil.dip2px(context, 5)
                    }
                    indicators[p0].layoutParams?.width = ScreenUtil.dip2px(context, 10)
                }

                private fun getFieldValueByFieldName(fieldName: String, `object`: Any): ArrayList<ImageView>? {
                    try {
                        val field = `object`.javaClass.getDeclaredField(fieldName)
                        //设置对象的访问权限，保证对private的属性的访问
                        field.isAccessible = true
                        val data = field.get(`object`) as ArrayList<ImageView>
                        BKLog.d("属性值:${field.name}属性名称:$data")
                        return data
                    } catch (e: Exception) {

                        return null
                    }

                }

            })

            ui?.banner?.setOnBannerListener {
                ToastUtil.show("点击推荐 $it")
            }
            //横幅自适应高度，避免图片拉伸 91f / 375f
            val layoutParams = ui?.banner?.layoutParams
            layoutParams?.height = (ScreenUtil.getNormalWH(context as Activity)[0] * (91f / 375f)).toInt()
        }
    }

    private class UI private constructor(val banner: Banner) {
        companion object {

            fun create(rootView: View): UI {
                val banner = rootView.findViewById<View>(R.id.banner) as Banner
                return UI(banner)
            }
        }
    }

}
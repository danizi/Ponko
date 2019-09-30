package com.ponko.cn.module.common.share

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.ShareCBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.OnEnterListener
import com.xm.lib.downloader.utils.CommonUtil
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception


class ShareContract {
    interface V {
        /**
         * 设置分享背景
         * @param bg 图片背景
         */
        fun setShareBg(bg: String?)

        /**
         * 设置分享信息
         * @param qr 二维码
         * @param title1 分享者
         * @param title2 分享价格
         * @param title3 分享说明
         */
        fun setSharerInformation(qr: String?, title1: String?, title2: String?, title3: String?)
    }

    class M {
        var type = ""
        var id = ""
        var shareCBean: ShareCBean? = null

        /**
         * 请求分享接口
         */
        fun requestShareApi(callback: HttpCallBack<ShareCBean>) {
            PonkoApp.shareApi?.share(type, id)?.enqueue(callback)
        }
    }

    class P(val ctx: Context?, val v: V?) {
        private var m = M()
        private var wxShare: WxShare? = null

        init {
            wxShare = WxShare(ctx as Activity)
            wxShare?.init(ShareConfig.Builder().appid(Constants.APP_ID).build())
        }

        /**
         * @param i 0 朋友 1 朋友圈 2小程序
         */
        fun shareTo(shareView: ConstraintLayout?, i: Int) {
            when (i) {
                0 -> {
                    wxShare?.shareImage(getShareCoverBmp(shareView))
                }
                1 -> {
                    wxShare?.shareImage(getShareCoverBmp(shareView), WXSceneTimeline)
                }
                2 -> {
                    val params = m.shareCBean?.params
                    if (params == null) {
                        ToastUtil.show("分享信息params为空，请重新进入页面再试...")
                        return
                    } else {
                        if (TextUtils.isEmpty(params.title))
                            return
//                        if (TextUtils.isEmpty(params.summary))
//                            return
                        if (TextUtils.isEmpty(params.username))
                            return
                        if (TextUtils.isEmpty(params.path))
                            return
                    }
                    if (m.shareCBean != null && !TextUtils.isEmpty(m.shareCBean?.bg)) {
                        DialogUtil.showProcess(ctx!!)
                        com.bumptech.glide.Glide.with(ctx).load(m.shareCBean?.params?.avatar).asBitmap().into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                wxShare?.shareMiniProgram(
                                        resource,
                                        params.title,
                                        ""/*params.summary*/,
                                        params.username,
                                        params.path,
                                        params.type/*WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE*/)
                                DialogUtil.hideProcess()
                            }

                            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                                super.onLoadFailed(e, errorDrawable)
                                DialogUtil.hideProcess()
                            }
                        }) //方法中设置asBitmap可以设置回调类型
                    }
                }
            }
        }

        /**
         * 长按分享图片保存到本地相册中
         */
        fun longClickSaveCover(clCover: ConstraintLayout?) {
            DialogUtil.show(ctx!!, "提示", "分享海报是否保存到本地？", true, object : OnEnterListener {
                override fun onEnter(dlg: AlertDialog) {
                    CommonUtil.saveBmp2Gallery(ctx, getShareCoverBmp(clCover),CommonUtil.md5(m.shareCBean?.bg)/*"poster"*/)
                    dlg.dismiss()
                }
            }, null)
        }

        private fun getShareCoverBmp(view: View?): Bitmap {
            return ViewUtil.getViewBitmap2(view!!, 375.toFloat() / 667)
        }

        fun clear() {

        }

        fun requestShareApi(course: String, id: String) {
            m.type = course
            m.id = id
            m.requestShareApi(object : HttpCallBack<ShareCBean>() {
                override fun onSuccess(call: Call<ShareCBean>?, response: Response<ShareCBean>?) {
                    val shareCBean = response?.body()
                    m.shareCBean = shareCBean
                    //检查参数
                    checkString(shareCBean?.bg!!, "requestShareApi failure ,bk is null")
                    checkString(shareCBean.qr!!, "requestShareApi failure ,qr is null")
                    checkString(shareCBean.title1!!, "requestShareApi failure ,title1 is null")
                    checkString(shareCBean.title2!!, "requestShareApi failure ,title2 is null")
                    checkString(shareCBean.title3!!, "requestShareApi failure ,title3 is null")
                    //设置封面
                    v?.setShareBg(shareCBean.bg)
                    //设置信息
                    v?.setSharerInformation(
                            shareCBean.qr,
                            shareCBean.title1,
                            shareCBean.title2,
                            shareCBean.title3)
                }

                override fun onFailure(call: Call<ShareCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    ToastUtil.show(msg)
                }

                private fun checkString(str: String, errorMsg: String) {
                    if (TextUtils.isEmpty(str)) {
                        throw IllegalAccessException(errorMsg)
                    }
                }
            })
        }
    }
}
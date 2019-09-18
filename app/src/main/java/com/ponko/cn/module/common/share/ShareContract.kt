package com.ponko.cn.module.common.share

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.constant.Constants
import com.ponko.cn.utils.DialogUtil
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.OnEnterListener
import com.xm.lib.downloader.utils.CommonUtil
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare

class ShareContract {
    interface V {}
    class M {
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
                    wxShare?.shareImage(getShareCoverBmp(shareView),WXSceneTimeline)
                }
                2 -> {
                    wxShare?.shareMiniProgram(R.mipmap.ic_launcher, "帮课大学", "描述", "小程序id", "", WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE)
                }
            }
        }

        /**
         * 长按分享图片保存到本地相册中
         */
        fun longClickSaveCover(clCover: ConstraintLayout?) {
            DialogUtil.show(ctx!!, "提示", "分享海报是否保存到本地？", true, object : OnEnterListener {
                override fun onEnter(dlg: AlertDialog) {
                    CommonUtil.saveBmp2Gallery(ctx, getShareCoverBmp(clCover), "poster")
                    dlg.dismiss()
                }
            }, null)
        }

        private fun getShareCoverBmp(view: View?): Bitmap {
            return ViewUtil.getViewBitmap2(view!!, 375.toFloat() / 667)
        }

        fun clear() {

        }

        fun requestShareApi() {

        }


    }
}
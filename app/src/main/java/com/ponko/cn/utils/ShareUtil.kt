package com.ponko.cn.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.XmPopWindow
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare

/**
 * 分享工具类
 */
object ShareUtil {

    /**
     * 分享给朋友
     */
    fun shareFriend(ctx: Context, shareUrl: String?, shareTitle: String?, shareDescription: String?) {
        val wxShare: WxShare? = WxShare(ctx as Activity)
        wxShare?.shareWebPage(R.mipmap.ic_launcher, shareUrl!!, shareTitle!!, shareDescription!!, SendMessageToWX.Req.WXSceneSession)
    }

    /**
     * 分享到朋友圈
     */
    fun shareFriendMoment(ctx: Context, shareUrl: String?, shareTitle: String?, shareDescription: String?) {
        val wxShare: WxShare? = WxShare(ctx as Activity)
        wxShare?.shareWebPage(R.mipmap.ic_launcher, shareUrl!!, shareTitle!!, shareDescription!!, SendMessageToWX.Req.WXSceneTimeline)
    }

    /**
     * 分享弹出框二
     */
    fun showShareDlg2(activity: Activity) {
        val xmPopWindow = com.xm.lib.media.view.XmPopWindow(activity)
        val shareView = LayoutInflater.from(activity).inflate(com.xm.lib.media.R.layout.media_share, null, false)
        val share: ImageView = shareView.findViewById(com.xm.lib.media.R.id.iv_share_wx)
        val friend: ImageView = shareView.findViewById(com.xm.lib.media.R.id.iv_share_friend)
        share.setOnClickListener {
            Toast.makeText(activity, "分享到微信", Toast.LENGTH_SHORT).show()
        }
        friend.setOnClickListener {
            Toast.makeText(activity, "分享到朋友圈", Toast.LENGTH_SHORT).show()
        }
        xmPopWindow.ini(shareView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        xmPopWindow.showAtLocation(com.xm.lib.media.view.XmPopWindow.Location.BOTTOM, com.xm.lib.media.R.style.AnimationBottomFade, activity?.window?.decorView!!, 0, 0)
    }

    /**
     * 弹出分享框
     */
    fun showShareDlg(context: Context?, shareUrl: String?, shareTitle: String?, shareDescription: String?) {
        val shareView = ViewUtil.viewById(context, R.layout.view_share)
        val shareViewHolder = ShareViewHolder.create(shareView!!, context).share(shareUrl, shareTitle, shareDescription)
    }

    /**
     * 分享相关操作ViewHolder
     */
    private class ShareViewHolder private constructor(val ctx: Context?, val rootView: View, val tvRemind: TextView, val flFriend: FrameLayout, val flFriendMoment: FrameLayout, val tvCancel: TextView, val popWindow: XmPopWindow) {
        companion object {
            fun create(rootView: View, context: Context?): ShareViewHolder {
                val tvRemind = rootView.findViewById<View>(R.id.tv_remind) as TextView
                val flFriend = rootView.findViewById<View>(R.id.fl_friend) as FrameLayout
                val flFriendMoment = rootView.findViewById<View>(R.id.fl_friend_moment) as FrameLayout
                val tvCancel = rootView.findViewById<View>(R.id.tv_cancel) as TextView
                val popWindow = XmPopWindow(context)
                popWindow.ini(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                //popWindow.showAtLocation(XmPopWindow.Location.BOTTOM, R.style.AnimationBottomFade, window.decorView, 0, 0)
                return ShareViewHolder(context, rootView, tvRemind, flFriend, flFriendMoment, tvCancel, popWindow)
            }
        }

        var shareTitle: String? = null
        var shareDescription: String? = null
        var shareUrl: String? = null
        var wxShare: WxShare? = WxShare(ctx as Activity)


        fun initEvent() {
            wxShare?.init(ShareConfig.Builder().appid(PonkoApp.APP_ID).build())
            tvCancel.setOnClickListener {
                popWindow.dismiss()
            }
            flFriend.setOnClickListener {
                wxShare?.shareWebPage(R.mipmap.ic_launcher, shareUrl!!, shareTitle!!, shareDescription!!, SendMessageToWX.Req.WXSceneSession)
            }
            flFriendMoment.setOnClickListener {
                wxShare?.shareWebPage(R.mipmap.ic_launcher, shareUrl!!, shareTitle!!, shareDescription!!, SendMessageToWX.Req.WXSceneTimeline)
            }
        }

        /**
         * 弹出分享框
         */
        fun share(shareUrl: String?, shareTitle: String?, shareDescription: String?) {
            this.shareUrl = shareUrl
            this.shareTitle = shareTitle
            this.shareDescription = shareDescription
            initEvent()
            popWindow.showAtLocation(XmPopWindow.Location.BOTTOM, com.xm.lib.media.R.style.AnimationBottomFade, (ctx as Activity).window.decorView, 0, 0)
        }
    }
}
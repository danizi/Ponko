package com.ponko.cn.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.my.option.store.IntegralExchangedAct
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.*
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
import retrofit2.Call
import retrofit2.Response

object DialogUtil {
    @SuppressLint("StaticFieldLeak")
    private var dlg: AlertDialog? = null

    /**
     * 显示苹果风格弹框
     */
    fun show(context: Context, title: String, msg: String, isCancelable: Boolean, enterListener: OnEnterListener?, cancelListener: OnCancelListener?) {
        try {
            if (dlg != null && dlg?.isShowing!!) {
                dlg?.dismiss()
            }
            dlg = newXmIOSDialog(context, title, msg, isCancelable, enterListener, cancelListener)
            dlg?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hide() {
        try {
            if (dlg != null && dlg?.isShowing!!) {
                dlg?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun newXmIOSDialog(context: Context, title: String, msg: String, isCancelable: Boolean, enterListener: OnEnterListener?, cancelListener: OnCancelListener?): AlertDialog {
        val w = ScreenUtil.getNormalWH(context as Activity)[0] - ScreenUtil.dip2px(context, 100)
        return XmIOSDialog(context)
                .setType(Type.GENERAL)
                .setMsg(msg)
                .setTitle(title)
                .setCancelable(isCancelable)
                .setSize(w, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnEnterListener(enterListener)
                .setOnCancelListener(cancelListener)
                .build()
    }

    /**
     * 显示积分弹框
     */
    fun showExchange(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_et_exchange, null, false)
        AlertDialog.Builder(context!!)
                .setTitle("兑换")
                .setView(view)
                .setPositiveButton("确认") { dialog, which ->
                    //获取用户输入
                    val etExchange: EditText = view.findViewById(R.id.et_exchange)
                    val code = etExchange.text.toString()
                    BKLog.d("用户输入兑换码:$code")
                    PonkoApp.myApi?.exchangeCode(code)?.enqueue(object : HttpCallBack<GeneralBean>() {
                        override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                            val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setMessage("恭喜，课程兑换成功。")
                            alertDialog.setNegativeButton("开始学习") { dialog, _ ->
                                dialog.dismiss()
                                BKLog.d("兑换成功，跳转到已兑课程") //PS:跳转到免费页面还是已兑课程
                                //跳转到免费页面
                                context.sendBroadcast(Intent(Constants.ACTION_FREE_REFRESH))
                                //IntoTargetUtil.target(context, "free", 1.toString())
//                                ActivityUtil.startActivity(context, Intent(context, IntegralExchangedAct::class.java))
                                //sendBroadcast(Intent("com.moudle.free.startRefresh"))  //PS:广播通知免费界面刷新
                            }.create().show()
                        }
                    })
                }.show()
    }

    /**
     * 显示分享弹框
     */
    fun showShareImg(context: Context?,bmp:Bitmap) {
        if (bmp==null) {
            ToastUtil.show("分享图片为空。")
            return
        }
        var wxShare: WxShare? = WxShare(context as Activity)
        wxShare?.init(ShareConfig.Builder().appid(PonkoApp.APP_ID).build())
        val popWindow = XmPopWindow(context)
        val shareView = ViewUtil.viewById(context, R.layout.view_share)
        val flFriend = shareView?.findViewById<View>(R.id.fl_friend) as FrameLayout
        val flFriendMoment = shareView.findViewById<View>(R.id.fl_friend_moment) as FrameLayout
        val tvCancel = shareView.findViewById<View>(R.id.tv_cancel) as TextView
        tvCancel.setOnClickListener {
            popWindow.dismiss()
        }
        flFriend.setOnClickListener {
            wxShare?.shareImage(bmp,SendMessageToWX.Req.WXSceneSession)
            popWindow.dismiss()
        }
        flFriendMoment.setOnClickListener {
            wxShare?.shareImage(bmp,SendMessageToWX.Req.WXSceneTimeline)
            popWindow.dismiss()
        }
        popWindow.ini(shareView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        (context as Activity).runOnUiThread {
            popWindow.showAtLocation(XmPopWindow.Location.BOTTOM, com.xm.lib.media.R.style.AnimationBottomFade, (context as Activity).window.decorView, 0, 0)
        }
    }

    /**
     * 显示分享弹框
     */
    fun showShare(context: Context?, shareUrl: String?, shareTitle: String?, shareDescription: String?) {
        if (TextUtils.isEmpty(shareUrl)) {
            ToastUtil.show("分享地址为空。")
            return
        }

        if (TextUtils.isEmpty(shareTitle)) {
            ToastUtil.show("分享标题为空。")
            return
        }

        if (TextUtils.isEmpty(shareDescription)) {
            ToastUtil.show("分享描述为空。")
            return
        }

        var wxShare: WxShare? = WxShare(context as Activity)
        wxShare?.init(ShareConfig.Builder().appid(PonkoApp.APP_ID).build())
        val popWindow = XmPopWindow(context)
        val shareView = ViewUtil.viewById(context, R.layout.view_share)
        val flFriend = shareView?.findViewById<View>(R.id.fl_friend) as FrameLayout
        val flFriendMoment = shareView.findViewById<View>(R.id.fl_friend_moment) as FrameLayout
        val tvCancel = shareView.findViewById<View>(R.id.tv_cancel) as TextView
        tvCancel.setOnClickListener {
            popWindow.dismiss()
        }
        flFriend.setOnClickListener {
            wxShare?.shareWebPage(R.mipmap.ic_launcher, shareUrl!!, shareTitle!!, shareDescription!!, SendMessageToWX.Req.WXSceneSession)
            popWindow.dismiss()
        }
        flFriendMoment.setOnClickListener {
            wxShare?.shareWebPage(R.mipmap.ic_launcher, shareUrl!!, shareTitle!!, shareDescription!!, SendMessageToWX.Req.WXSceneTimeline)
            popWindow.dismiss()
        }
        popWindow.ini(shareView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        (context as Activity).runOnUiThread {
            popWindow.showAtLocation(XmPopWindow.Location.BOTTOM, com.xm.lib.media.R.style.AnimationBottomFade, (context as Activity).window.decorView, 0, 0)
        }
    }

    /**
     * 显示进度
     */
    private var progressDialog: ProgressDialog? = null

    /**
     * 显示进度条
     */
    fun showProcess(context: Context) {
        try {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context)
                progressDialog?.setMessage("请稍等，正在处理中...")
                progressDialog?.setCanceledOnTouchOutside(true)
            }
            if (progressDialog?.isShowing == true) {
                progressDialog?.dismiss()
            }
            progressDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 隐藏进度条
     */
    fun hideProcess() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
        progressDialog = null
    }
}
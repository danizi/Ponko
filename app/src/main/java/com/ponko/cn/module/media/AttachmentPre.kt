package com.ponko.cn.module.media

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatImageButton
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.ponko.cn.app.PonkoApp.Companion.is3GNetTip
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.http.NetworkUtil
import com.xm.lib.component.OnCancelListener
import com.xm.lib.component.OnEnterListener
import com.xm.lib.media.R
import com.xm.lib.media.attachment.BaseAttachmentView
import com.xm.lib.media.base.IXmMediaPlayer
import com.xm.lib.media.event.GestureObserver
import com.xm.lib.media.event.PhoneStateObserver
import com.xm.lib.media.event.PlayerObserver


/**
 * 预览页面
 */
class AttachmentPre(context: Context?, private var preUrl: String? = "") : BaseAttachmentView(context!!) {

    private var ui: UI? = null
    var url: String? = ""
    var isPay: Boolean? = false
    var pos: Int = 0

    init {
        observer = object : PlayerObserver {
            override fun onPrepared(mp: IXmMediaPlayer) {
                super.onPrepared(mp)
                xmVideoView?.mediaPlayer?.start()
                xmVideoView?.unBindAttachmentView(this@AttachmentPre)
            }
        }
        gestureObserver = object : GestureObserver {

        }
        phoneObserver = object : PhoneStateObserver {}
        Glide.with(context).load(preUrl).error(R.mipmap.load_img_default).into(ui?.ivPre)//加载图片
    }

    fun load(vid: String?, preUrl: String?, isPay: Boolean, pos: Int) {
        this.url = vid
        this.preUrl = preUrl
        this.isPay = isPay
        this.pos = pos
        Glide.with(context).load(preUrl).error(R.mipmap.load_img_default).into(ui?.ivPre)//加载图片  //TODO You cannot start a load for a destroyed activity
    }

    override fun layoutId(): Int {
        return R.layout.attachment_pre
    }

    override fun findViews(view: View?) {
        if (ui == null) {
            ui = UI.create(view)
        }
    }

    override fun initEvent() {
        ui?.ivStart?.setOnClickListener {
            if (isPay == true) {
                if (!is3GNetTip && NetworkUtil.is3GNet(context)) {

                    DialogUtil.show(context, "提示", "当前使用是手机流量,是否继续播放？", true, object : OnEnterListener {
                        override fun onEnter(dlg: AlertDialog) {
                            is3GNetTip = true
                            clickIvStart()
                            dlg.dismiss()
                        }
                    }, object : OnCancelListener {
                        override fun onCancel(dlg: AlertDialog) {
                            dlg.dismiss()
                        }
                    })
                } else {
                    clickIvStart()
                }
            } else {
                if (TextUtils.isEmpty(url)) {
                    ToastUtil.show("暂未获取课程信息")
                } else {
                    ToastUtil.show("您尚未订购")
                }
            }
        }
        ui?.ivBack?.setOnClickListener {
            clickBack()
        }
    }

    private fun clickIvStart() {
        if (ui?.ivStart?.visibility == View.VISIBLE) {
            ui?.ivStart?.visibility = View.GONE
            ui?.pbLoading?.visibility = View.VISIBLE
            //获取播放地址
            if (TextUtils.isEmpty(url)) {
                return
            }
            if (url?.startsWith("http")!! || url?.startsWith("https")!!) {
                xmVideoView?.start(url, true, pos)
                xmVideoView?.bringChildToFront(this@AttachmentPre)
            } else {
                MediaUitl.getM3u8Url(url, object : MediaUitl.OnPlayUrlListener {
                    override fun onSuccess(url: String, size: Int?) {
                        xmVideoView?.start(url, true, pos)
                        xmVideoView?.bringChildToFront(this@AttachmentPre)
                    }

                    override fun onFailure() {
                        //ToastUtil.show("获取播放地址失败 - ")
                    }
                })
            }
        }
    }

    private fun clickBack() {
        (context as Activity).finish()
    }

    override fun initDisplay() {

        ui?.ivPre?.visibility = View.VISIBLE
        ui?.ivStart?.visibility = View.VISIBLE
        ui?.pbLoading?.visibility = View.GONE
    }

    fun hideIvBack() {
        ui?.ivBack?.visibility = View.INVISIBLE
    }

    fun showIvBack() {
        ui?.ivBack?.visibility = View.VISIBLE
    }

    /**
     * ui
     */
    private class UI private constructor(val ivPre: ImageView, val ivStart: ImageView, val pbLoading: ProgressBar, val ivBack: AppCompatImageButton) {
        companion object {

            fun create(rootView: View?): UI {
                val ivPre = rootView?.findViewById<View>(R.id.iv_pre) as ImageView
                val ivStart = rootView.findViewById<View>(R.id.iv_start) as ImageView
                val pbLoading = rootView.findViewById<View>(R.id.pb_loading) as ProgressBar
                val ivBack = rootView.findViewById<View>(R.id.iv_back) as AppCompatImageButton
                return UI(ivPre, ivStart, pbLoading, ivBack)
            }
        }
    }

}
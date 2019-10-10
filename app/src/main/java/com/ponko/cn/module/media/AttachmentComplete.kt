package com.ponko.cn.module.media

import android.content.Context
import android.support.v7.widget.AppCompatImageButton
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.ponko.cn.module.media.control.AttachmentControl
import com.xm.lib.media.R
import com.xm.lib.media.attachment.BaseAttachmentView
import com.xm.lib.media.base.IXmMediaPlayer
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.event.GestureObserver
import com.xm.lib.media.event.PhoneStateObserver
import com.xm.lib.media.event.PlayerObserver


class AttachmentComplete(context: Context?) : BaseAttachmentView(context) {
    private var ui: UI? = null

    init {
        observer = object : PlayerObserver {
            override fun onCompletion(mp: IXmMediaPlayer) {
                super.onCompletion(mp)
                //if (xmVideoView?.mediaPlayer?.getDuration() == xmVideoView?.mediaPlayer?.getCurrentPosition()) {
                    xmVideoView?.bringChildToFront(this@AttachmentComplete)
                    this@AttachmentComplete.visibility = View.VISIBLE
                //}
            }

            override fun onPrepared(mp: IXmMediaPlayer) {
                super.onPrepared(mp)
                this@AttachmentComplete.visibility = View.GONE
            }
        }
        gestureObserver = object : GestureObserver {

        }
        phoneObserver = object : PhoneStateObserver {}
    }

    override fun bind(xmVideoView: XmVideoView) {
        super.bind(xmVideoView)
        this.visibility = View.GONE
    }

    override fun layoutId(): Int {
        return R.layout.attachment_complete
    }

    override fun findViews(view: View?) {
        super.findViews(view)
        if (ui == null) {
            ui = UI.create(view)
        }
    }

    override fun initEvent() {
        super.initEvent()
        ui?.ivNext?.setOnClickListener {
            (xmVideoView?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl).next()
            this@AttachmentComplete.visibility = View.GONE
        }
        ui?.ivReplay?.setOnClickListener {
            (xmVideoView?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl).replay()
            this@AttachmentComplete.visibility = View.GONE
        }
    }

    private class UI private constructor(val pb: ProgressBar, val ivReplay: AppCompatImageButton, val ivNext: AppCompatImageButton, val tvDes: TextView) {
        companion object {
            fun create(rootView: View?): UI {
                val pb = rootView?.findViewById<View>(R.id.pb) as ProgressBar
                val ivReplay = rootView.findViewById<View>(R.id.iv_replay) as AppCompatImageButton
                val ivNext = rootView.findViewById<View>(R.id.iv_next) as AppCompatImageButton
                val tvDes = rootView.findViewById<View>(R.id.tv_des) as TextView
                return UI(pb, ivReplay, ivNext, tvDes)
            }
        }
    }
}
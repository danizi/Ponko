package com.ponko.cn.module.media

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.VideoInfoCBean
import com.ponko.cn.module.m3u8downer.core.M3u8Utils
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.log.BKLog
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
    companion object {
        private const val TAG = "AttachmentPre"
    }


    private var ivStart: ImageView? = null
    private var pbLoading: ProgressBar? = null
    var url: String? = ""
    private var ivPre: ImageView? = null

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
        Glide.with(context).load(preUrl).error(R.mipmap.load_img_default).into(ivPre)//加载图片
    }

    fun load(vid: String?, preUrl: String?) {
        this.url = vid
        this.preUrl = preUrl
        Glide.with(context).load(preUrl).error(R.mipmap.ic_launcher).into(ivPre)//加载图片  //TODO You cannot start a load for a destroyed activity
    }

    override fun layoutId(): Int {
        return R.layout.attachment_pre
    }

    override fun findViews(view: View?) {
        ivPre = view?.findViewById(R.id.iv_pre)
        ivStart = view?.findViewById(R.id.iv_start)
        pbLoading = view?.findViewById(R.id.pb_loading)
    }

    override fun initEvent() {
        ivStart?.setOnClickListener {
            if (ivStart?.visibility == View.VISIBLE) {
                ivStart?.visibility = View.GONE
                pbLoading?.visibility = View.VISIBLE
                //获取播放地址
                MediaUitl.getM3u8Url(url, object : MediaUitl.OnPlayUrlListener {
                    override fun onSuccess(url: String, size: Int?) {
                        ToastUtil.show("获取播放地址失败 - ")
                        xmVideoView?.start(url, true)
                        xmVideoView?.bringChildToFront(this@AttachmentPre)
                    }

                    override fun onFailure() {
                        ToastUtil.show("获取播放地址失败 - ")
                    }
                })

            }
        }
    }

    override fun initDisplay() {
        ivPre?.visibility = View.VISIBLE
        ivStart?.visibility = View.VISIBLE
        pbLoading?.visibility = View.GONE
    }
}
package com.ponko.cn.module.study.v2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.module.media.AttachmentPre
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.media.base.XmVideoView

/**
 * 调起播放页面
 */
class MediaAct : AppCompatActivity() {

    companion object {
        /**
         * 启动播放页面
         * @param context 上下文对象
         * @param url  地址
         * @param title 标题
         * @param auto 是否自动播放
         * @param pos  播放位置(单位毫秒)
         */
        fun start(context: Context, url: String, auto: Boolean, pos: Int) {
            val intent = Intent(context, MediaAct::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", auto)
            intent.putExtra("auto_play", auto)
            intent.putExtra("position", pos)
            context.startActivity(intent)
        }
    }

    private var video: XmVideoView? = null

    private var attachmentControl: AttachmentControl? = null
    private var attachmentPre: AttachmentPre? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        video = findViewById<View>(R.id.video) as XmVideoView

        //获取intent传递的值
        val url = intent.getStringExtra("url")
        val title = intent.getStringExtra("title")
        val auto_play = intent.getBooleanExtra("auto_play", false)
        val position = intent.getIntExtra("position", 0)

        //初始化播放器，以及附着页面
        MediaUitl.initXmVideoView(video, this@MediaAct)
        attachmentControl = video?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl
        attachmentPre = video?.attachmentViewMaps!!["attachmentPre"] as AttachmentPre
        attachmentControl?.showLoading()
        attachmentControl?.setTitle(title)
        attachmentControl?.start(url, position, 0)
        attachmentPre?.url = url
        attachmentPre?.isPay = true
    }

    override fun onDestroy() {
        super.onDestroy()
        video?.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        if (CacheUtil.getMediaBackground() == "0") {
            video?.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        video?.onResume()
    }

}

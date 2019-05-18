package com.ponko.cn.module.m3u8downer.core

import android.text.TextUtils

/**
 * 下载任务信息
 */
class M3u8DownTask private constructor(builder: Builder) {

    var vid = ""
    @Deprecated("在下在模块内部使用")
    var m3u8 = ""
    var name = ""
    @Deprecated("在下在模块内部使用,在")
    var fileSize = 0L

    init {
        this.vid = builder.vid
        this.m3u8 = builder.m3u8
        this.name = builder.name
        this.fileSize = builder.fileSize
    }



    class Builder {
        /**
         * 获取m3u8地址的唯一标识
         */
        var vid = ""
        /**
         * 下载m3u8地址
         */
        var m3u8 = ""
        /**
         * 任务名称
         */
        var name = ""
        /**
         * 文件的大小
         */
        var fileSize = 0L

        @Deprecated("")
        fun vid(vid: String): Builder {
            this.vid = vid
            return this
        }

        @Deprecated("使用vid，在Runnable请求视频信息回调中设置")
        fun m3u8(m3u8: String): Builder {
            this.m3u8 = m3u8
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        @Deprecated("在Runnable请求视频信息回调中设置")
        fun fileSize(fileSize: Long): Builder {
            this.fileSize = fileSize
            return this
        }

        fun build(): M3u8DownTask {
            //检查参数是否合法
            checkParameters()
            return M3u8DownTask(this)
        }

        private fun checkParameters() {
            if (TextUtils.isEmpty(vid)) {
                throw NullPointerException("vid is null")
            }
            if (TextUtils.isEmpty(m3u8)) {
                //throw NullPointerException("m3u8 is null")
            }
            if (TextUtils.isEmpty(name)) {
                name = M3u8Utils.m3u8FileName(m3u8)
            }
            if (fileSize <= 0) {
                //throw NullPointerException("m3u8 必须设置文件大小")
            }
        }
    }

    override fun toString(): String {
        return "M3u8DownTask(vid='$vid', m3u8='$m3u8', name='$name', fileSize=$fileSize)"
    }
}
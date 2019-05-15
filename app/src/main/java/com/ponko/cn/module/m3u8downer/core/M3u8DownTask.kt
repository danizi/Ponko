package com.ponko.cn.module.m3u8downer.core

import android.text.TextUtils

/**
 * 下载任务信息
 */
class M3u8DownTask private constructor(builder: Builder) {

    var m3u8 = ""
    var name = ""
    var fileSize = 0L

    init {
        this.m3u8 = builder.m3u8
        this.name = builder.name
        this.fileSize = builder.fileSize
    }

    class Builder {
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

        fun m3u8(m3u8: String): Builder {
            this.m3u8 = m3u8
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

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
            if (TextUtils.isEmpty(m3u8)) {
                throw NullPointerException("m3u8 is null")
            }
            if (TextUtils.isEmpty(name)) {
                name = M3u8Utils.m3u8FileName(m3u8)
            }
            if (fileSize <= 0) {
                throw NullPointerException("m3u8 必须设置文件大小")
            }
        }
    }
}
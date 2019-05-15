package com.ponko.cn.module.m3u8downer.core

import android.text.TextUtils
import com.google.gson.Gson
import com.ponko.cn.module.m3u8downer.core.M3u8Utils.listToStr

/**
 * 下载者
 */
class M3u8DownTasker private constructor(builder: Builder) : IM3u8DownTasker {

    var m3u8DownRunnable: M3u8DownRunnable? = null
    var m3u8DownManager: M3u8DownManager? = null
    var downTask: M3u8DownTask? = null

    init {
        this.m3u8DownManager = builder.m3u8DownManager
        this.downTask = builder.downTask
        this.m3u8DownRunnable = M3u8DownRunnable(this)
    }

    override fun enqueue(listener: OnDownListener?) {
        this.m3u8DownRunnable?.setOnDownListener(object : OnDownListener {

            override fun onStart(m3u8Key: String, m3u8Ts: ArrayList<String>) {
                // 插入数据库
                val daoBean = m3u8DownManager?.dao?.select(downTask?.m3u8!!)
                if (TextUtils.isEmpty(daoBean?.m3u8)) {
                    //数据库中不存在该任务则添加到数据库中
                    daoBean?.m3u8 = downTask?.m3u8!!
                    daoBean?.need_download_ts = listToStr(m3u8Ts)
                    daoBean?.need_download_key = m3u8Key
                    daoBean?.not_download_ts = ""
                    daoBean?.have_download_ts = ""
                    daoBean?.hava_key = 0
                    daoBean?.progress = 0
                    daoBean?.total = downTask?.fileSize?.toInt()!!
                    m3u8DownManager?.dao?.insert(daoBean!!)
                }
            }

            override fun onComplete(url: String) {
                // 移除分发器中已经下载完成的任务，并执行准备队列中的任务
                m3u8DownManager?.dispatcher?.remove(this@M3u8DownTasker)
                // 更新数据库中的进度字段
                val daoBean = m3u8DownManager?.dao?.select(downTask?.m3u8!!)
                if (!TextUtils.isEmpty(daoBean?.m3u8)) {
                    daoBean?.progress = daoBean?.total!!
                    m3u8DownManager?.dao?.update(daoBean)
                }

                // 任务下载完成回调
                listener?.onComplete(downTask?.m3u8!!)
            }

            override fun onProcess(url: String, progress: Int) {
                // 更新数据库中的进度字段
                val daoBean = m3u8DownManager?.dao?.select(downTask?.m3u8!!)
                if (!TextUtils.isEmpty(daoBean?.m3u8)) {
                    daoBean?.progress = progress
                    if(url.endsWith(".key")){
                        daoBean?.hava_key = 1
                        daoBean?.need_download_key = url
                    }else if(url.endsWith(".ts")){
                        daoBean?.have_download_ts +="$url,"
                    }
                    m3u8DownManager?.dao?.update(daoBean!!)
                }
                // 回调任务进度
                listener?.onProcess(downTask?.m3u8!!, progress)
            }

            override fun onError(msg: String) {
                // 回调任务下载错误信息
                listener?.onError(msg)
            }
        })
        m3u8DownManager?.dispatcher?.enqueue(this)
    }

    override fun remove() {
        m3u8DownManager?.dispatcher?.remove(this)
    }

    /**
     * 建造者
     */
    class Builder {
        /**
         * 下载真正实现接口
         */
        var m3u8DownRunnable: M3u8DownRunnable? = null
        /**
         * 下载管理者
         */
        var m3u8DownManager: M3u8DownManager? = null
        /**
         * 下载信息
         */
        var downTask: M3u8DownTask? = null


        fun m3u8DownManager(m3u8DownManager: M3u8DownManager?): Builder {
            this.m3u8DownManager = m3u8DownManager
            return this
        }

        fun downTask(downTask: M3u8DownTask?): Builder {
            this.downTask = downTask
            return this
        }

        fun build(): M3u8DownTasker {
            //检查参数是否合法
            checkParameters()
            return M3u8DownTasker(this)
        }

        private fun checkParameters() {
            if (m3u8DownManager == null) {
                throw NullPointerException("m3u8DownManager is null")
            }
            if (downTask == null) {
                throw NullPointerException("downTask is null")
            }
        }
    }

}
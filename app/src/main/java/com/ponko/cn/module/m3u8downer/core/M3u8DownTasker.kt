package com.ponko.cn.module.m3u8downer.core

import android.text.TextUtils
import com.ponko.cn.module.m3u8downer.core.M3u8Utils.listToStr
import com.ponko.cn.module.m3u8downer.core.M3u8Utils.strToList
import com.xm.lib.common.log.BKLog

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
        // 检查本地数据库未下载ts key 和进度，有缓存则设置
        val cacheBean = m3u8DownManager?.dao?.select(downTask?.m3u8!!)
        if (!TextUtils.isEmpty(cacheBean?.not_download_ts)) {
            this.m3u8DownRunnable?.notDownloadM3u8AnalysisUrls = strToList(cacheBean?.not_download_ts)!!
            this.m3u8DownRunnable?.progress = cacheBean?.progress?.toLong()!!
            BKLog.d("-> 数据库中缓存的信息")
            BKLog.d("m3u8           : ${cacheBean.m3u8}")
            BKLog.d("notDownloadUrl : ${cacheBean.not_download_ts}")
            BKLog.d("progress       : ${cacheBean.progress}")
            BKLog.d("total          : ${cacheBean.total}")
            BKLog.d("-----------------------------------")
        }

        // 下载任务回调
        this.m3u8DownRunnable?.setOnDownListener(object : OnDownListener {

            override fun onStart(m3u8Analysis: ArrayList<String>) {
                // 插入数据库
                val daoBean = m3u8DownManager?.dao?.select(downTask?.m3u8!!)
                if (TextUtils.isEmpty(daoBean?.m3u8)) {
                    //数据库中不存在该任务则添加到数据库中
                    daoBean?.m3u8 = downTask?.m3u8!!
                    daoBean?.need_download_ts = listToStr(m3u8Analysis)!!
                    daoBean?.not_download_ts = listToStr(m3u8Analysis)!!
                    daoBean?.progress = 0
                    daoBean?.total = downTask?.fileSize?.toInt()!!
                    m3u8DownManager?.dao?.insert(daoBean!!)
                    BKLog.d("-> 插入数据库的信息")
                    BKLog.d("m3u8             : ${daoBean?.m3u8}")
                    BKLog.d("need_download_ts : ${daoBean?.need_download_ts}")
                    BKLog.d("not_download_ts  : ${daoBean?.not_download_ts}")
                    BKLog.d("progress         : ${daoBean?.progress}")
                    BKLog.d("total            : ${daoBean?.total}")
                    BKLog.d("-----------------------------------")
                }
            }

            override fun onComplete(url: String) {
                // 移除分发器中已经下载完成的任务，并执行准备队列中的任务
                m3u8DownManager?.dispatcher?.remove(this@M3u8DownTasker)
                // 更新数据库中的进度字段
                val daoBean = m3u8DownManager?.dao?.select(downTask?.m3u8!!)
                if (!TextUtils.isEmpty(daoBean?.m3u8)) {
                    daoBean?.progress = daoBean?.total!!
                    daoBean.not_download_ts = getDownload_ts(daoBean.not_download_ts, url)!!
                    m3u8DownManager?.dao?.update(daoBean)
                    BKLog.d("-> 插入数据库的信息 ps:<>是修改过的值")
                    BKLog.d("m3u8               : ${daoBean?.m3u8}")
                    BKLog.d("need_download_ts   : ${daoBean?.need_download_ts}")
                    BKLog.d("<not_download_ts>  : ${daoBean?.not_download_ts}")
                    BKLog.d("<progress>         : ${daoBean?.progress}")
                    BKLog.d("total              : ${daoBean?.total}")
                    BKLog.d("-----------------------------------")
                }

                // 任务下载完成回调
                listener?.onComplete(downTask?.m3u8!!)
            }

            override fun onProcess(url: String, progress: Int) {
                // 更新数据库中的进度字段
                val daoBean = m3u8DownManager?.dao?.select(downTask?.m3u8!!)
                if (!TextUtils.isEmpty(daoBean?.m3u8)) {
                    daoBean?.progress = progress
                    daoBean?.not_download_ts = getDownload_ts(daoBean?.not_download_ts, url)!!
                    m3u8DownManager?.dao?.update(daoBean!!)
                    BKLog.d("-> 更新数据库的信息 ps:<>是修改过的值")
                    BKLog.d("m3u8               : ${daoBean?.m3u8}")
                    BKLog.d("need_download_ts   : ${daoBean?.need_download_ts}")
                    BKLog.d("<not_download_ts>  : ${daoBean?.not_download_ts}")
                    BKLog.d("<progress>         : ${daoBean?.progress}")
                    BKLog.d("total              : ${daoBean?.total}")
                    BKLog.d("-----------------------------------")
                }
                // 回调任务进度
                listener?.onProcess(downTask?.m3u8!!, progress)
            }

            private fun getDownload_ts(not_download_ts: String?, downloadedUrl: String): String? {
                val notDownloadUrl = strToList(not_download_ts)
                notDownloadUrl?.remove(downloadedUrl)
                return listToStr(notDownloadUrl)
            }

            override fun onError(msg: String) {
                // 回调任务下载错误信息
                listener?.onError(msg)
            }
        })

        // 添加到下载队列中
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
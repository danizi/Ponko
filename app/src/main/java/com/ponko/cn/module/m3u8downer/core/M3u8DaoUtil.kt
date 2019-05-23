package com.ponko.cn.module.m3u8downer.core

import android.text.TextUtils
import com.ponko.cn.module.m3u8downer.core.M3u8Utils.listToStr
import com.ponko.cn.module.m3u8downer.core.M3u8Utils.strToList
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.task.DownTask

/**
 * 数据库操作工具类
 */
object M3u8DaoUtil {

    private const val TAG = "M3u8DaoUtil"

    /**
     * 下载进入队列，更新数据库
     */
    fun updateQueue() {

    }

    /**
     * 下载开始，更新数据库
     */
    fun updateStartDb(m3u8DownManager: M3u8DownManager?, downTask: M3u8DownTask?, m3u8Analysis: ArrayList<String>?) {
        val daoBean = m3u8DownManager?.dao?.select2(downTask?.vid!!)
        if (TextUtils.isEmpty(daoBean?.vid)) {
            //数据库中不存在该任务则添加到数据库中
            daoBean?.m3u8 = downTask?.m3u8!!
            daoBean?.need_download_ts = M3u8Utils.listToStr(m3u8Analysis)!!
            daoBean?.not_download_ts = M3u8Utils.listToStr(m3u8Analysis)!!
            daoBean?.progress = 0
            daoBean?.total = downTask.fileSize.toInt()
            daoBean?.complete = 0
            daoBean?.vid = downTask.vid
            m3u8DownManager?.dao?.insert2(daoBean!!)
            BKLog.d(TAG, "-> 插入数据库的信息")
            BKLog.d(TAG, "m3u8             : ${daoBean?.m3u8}")
            BKLog.d(TAG, "need_download_ts : ${daoBean?.need_download_ts}")
            BKLog.d(TAG, "not_download_ts  : ${daoBean?.not_download_ts}")
            BKLog.d(TAG, "progress         : ${daoBean?.progress}")
            BKLog.d(TAG, "total            : ${daoBean?.total}")
            BKLog.d(TAG, "complete         : ${daoBean?.complete}")
            BKLog.d(TAG, "vid              : ${daoBean?.vid}")
            BKLog.d(TAG, "-----------------------------------")
        }
    }

    /**
     * 下载完成，更新数据库
     */
    fun updateCompleteDb(m3u8DownManager: M3u8DownManager?, downTask: M3u8DownTask?, url: String) {
        val daoBean = m3u8DownManager?.dao?.select2(downTask?.vid!!)
        if (!TextUtils.isEmpty(daoBean?.vid)) {
            daoBean?.progress = daoBean?.total!!
            //daoBean.not_download_ts = getDownload_ts(daoBean.not_download_ts, url)!!
            daoBean.not_download_ts = ""
            daoBean.complete = 1
            m3u8DownManager.dao?.update2(daoBean)
            BKLog.d(TAG, "-> 下载完成，修改数据库信息 ps:<>是修改过的值")
            BKLog.d(TAG, "m3u8               : ${daoBean.m3u8}")
            BKLog.d(TAG, "need_download_ts   : ${daoBean.need_download_ts}")
            BKLog.d(TAG, "<not_download_ts>  : ${daoBean.not_download_ts}")
            BKLog.d(TAG, "<progress>         : ${daoBean.progress}")
            BKLog.d(TAG, "total              : ${daoBean.total}")
            BKLog.d(TAG, "<complete>         : ${daoBean.complete}")
            BKLog.d(TAG, "vid                : ${daoBean.vid}")
            BKLog.d(TAG, "-----------------------------------")
        }
    }


    /**
     * 下载进行中，更新数据库
     */
    fun updateProcessDb(m3u8DownManager: M3u8DownManager?, downTask: M3u8DownTask?, daoBean: M3u8DbContract.DaoBean, progress: Int, url: String) {
        //val daoBean = m3u8DownManager?.dao?.select2(downTask?.vid!!)!!
        if (!TextUtils.isEmpty(daoBean.vid)) {
            // ps:实际计算大小与后台传过来的大小有偏差
            if (daoBean.total > 0 && daoBean.progress > daoBean.total) {
                daoBean.progress = daoBean.total
            } else {
                daoBean.progress = progress
            }
            daoBean.not_download_ts = getDownload_ts(daoBean.not_download_ts, url)!!
            daoBean.complete = 0
            m3u8DownManager?.dao?.update2(daoBean)
            BKLog.d(TAG, "-> 下载中，更新数据库信息 ps:<>是修改过的值")
            BKLog.d(TAG, "m3u8               : ${daoBean.m3u8}")
            BKLog.d(TAG, "need_download_ts   : ${daoBean.need_download_ts}")
            BKLog.d(TAG, "<not_download_ts>  : ${daoBean.not_download_ts}")
            BKLog.d(TAG, "<progress>         : ${daoBean.progress}")
            BKLog.d(TAG, "total              : ${daoBean.total}")
            BKLog.d(TAG, "<complete>         : ${daoBean.complete}")
            BKLog.d(TAG, "vid                : ${daoBean.vid}")
            BKLog.d(TAG, "-----------------------------------")
        }
    }

    /**
     * 获取下载解析的ts信息
     */
    private fun getDownload_ts(not_download_ts: String?, downloadedUrl: String): String? {
        val notDownloadUrl = strToList(not_download_ts)
        notDownloadUrl?.remove(downloadedUrl)
        return listToStr(notDownloadUrl)
    }

    /**
     * 下载错误，更新数据库
     */
    fun updateErrorDb() {

    }

}
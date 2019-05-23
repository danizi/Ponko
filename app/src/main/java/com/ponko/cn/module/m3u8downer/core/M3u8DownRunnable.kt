package com.ponko.cn.module.m3u8downer.core

import com.ponko.cn.module.m3u8downer.core.M3u8Utils.writeLocal
import com.ponko.cn.module.media.MediaUitl
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.DownManager
import com.xm.lib.downloader.enum_.DownErrorType
import com.xm.lib.downloader.event.DownObserver
import com.xm.lib.downloader.task.DownTask
import com.xm.lib.downloader.task.DownTasker
import okhttp3.*
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 真正下载Runnable接口
 */
class M3u8DownRunnable(private val m3u8DownTasker: M3u8DownTasker) : Runnable, IM3u8DownRunnable {
    /**
     * 下载管理器
     */
    private var downManager: DownManager? = null
    /**
     * 线程是否运行标志位
     */
    var isRuning = AtomicBoolean(false)
    /**
     * 下载监听
     */
    private var listener: OnDownListener? = null
    /**
     * 未下载完成的解析url
     */
    var notDownloadM3u8AnalysisUrls: ArrayList<String>? = ArrayList<String>()
    /**
     * 下载进度
     */
    var progress: Long = 0L
    /**
     * 任务是否下载完成
     */
    var isComplete: Boolean? = false

    init {
        downManager = DownManager.createDownManager(m3u8DownTasker.m3u8DownManager?.context!!)
    }

    override fun run() {
        isRuning = AtomicBoolean(true)
        MediaUitl.getM3u8Url(m3u8DownTasker.downTask?.vid, object : MediaUitl.OnPlayUrlListener {

            override fun onFailure() {
                callbackError("请检查您的网络")
            }

            override fun onSuccess(url: String, size: Int?) {
                m3u8DownTasker.downTask?.m3u8 = url
                //m3u8 = url
                analysisM3u8(url)
            }
        })
    }

    private fun analysisM3u8(m3u8: String) {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url(m3u8).get().build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                BKLog.e("解析m3u8的ts key失败")
                callbackError("解析m3u8失败")
            }

            override fun onResponse(call: Call, response: Response) {
                val (stream1, stream2) = M3u8Utils.copyInputStream(response.body()?.byteStream())
                val (m3u8Key, m3u8Ts) = M3u8Utils.analysis(stream1)
                val path = m3u8DownTasker.m3u8DownManager?.path!!
                val dir = "${m3u8DownTasker.m3u8DownManager?.dir!!}/${M3u8Utils.m3u8Unique(m3u8)}"
                writeLocal(stream2, m3u8, path, dir, m3u8Key, m3u8Ts)

                //解析地址存入集合
                val m3u8AnalysisUrls = ArrayList<String>()
                m3u8AnalysisUrls.add(m3u8Key)
                m3u8AnalysisUrls.addAll(m3u8Ts)
                down(path, dir, m3u8AnalysisUrls, m3u8Ts)
            }
        })
    }

    private fun down(path: String, dir: String, m3u8AnalysisUrls: ArrayList<String>, m3u8Ts: ArrayList<String>) {
        // 配置缓存路径
        downManager?.downConfig()?.path = path
        downManager?.downConfig()?.dir = dir

        //任务已经完成回调下载成功
        if (isComplete == true) {
            callBackComplete("")
            return
        }

        // 开始下载 PS:本来是要传m3u8AnalysisUrls 但是下载回调解析key下载完成，回调状态不同步，所有暂时先传入m3u8Ts
        callBackStart(m3u8Ts)

        // 获取下载进度
        if (!notDownloadM3u8AnalysisUrls?.isEmpty()!!) {
            m3u8AnalysisUrls.clear()
            m3u8AnalysisUrls.addAll(notDownloadM3u8AnalysisUrls!!)
        }

        // 加入下载队列中
        for (url in m3u8AnalysisUrls) {
            val task = DownTask()
            task.url = url
            downManager?.createDownTasker(task)?.enqueue()
        }
        downManager?.downObserverable()?.registerObserver(object : DownObserver {
            override fun onComplete(tasker: DownTasker, total: Long) {
                if (tasker.task.url == m3u8AnalysisUrls[m3u8AnalysisUrls.size - 1]) {
                    //回调下载完成
                    callBackComplete(tasker.task.url)
                    //listener?.onComplete(m3u8)
                    //BKLog.d(tasker.task.fileName + "total:$total thread:${Looper.getMainLooper() == Looper.getMainLooper()}")
                } else {
                    //回调下載進度
                    callBackProcess(tasker.task.url, total)
                }
            }

            override fun onError(tasker: DownTasker, typeError: DownErrorType, s: String) {
                callbackError(s)
            }

            override fun onProcess(tasker: DownTasker, process: Long, total: Long, present: Float) {

            }

            override fun onPause(tasker: DownTasker) {

            }

            override fun onDelete(tasker: DownTasker) {

            }
        })
    }

    /**
     * 任务下载进度回调
     */
    private fun callBackProcess(analysisUrl: String, total: Long) {
        progress += total
        listener?.onProcess(m3u8DownTasker.downTask?.vid!!, analysisUrl, progress.toInt())
        //m3u8DownTasker.m3u8DownManager?.listener?.onProcess(m3u8, progress.toInt())
    }

    /**
     * 任务下载错误回调
     */
    private fun callbackError(error: String) {
        listener?.onError(m3u8DownTasker.downTask?.vid!!, "", error)
        //m3u8DownTasker.m3u8DownManager?.listener?.onError(m3u8, "解析m3u8失败")
    }

    /**
     * 任务下载完成回调
     */
    private fun callBackComplete(analysisUrl: String) {
        listener?.onComplete(m3u8DownTasker.downTask?.vid!!, analysisUrl)
        //m3u8DownTasker.m3u8DownManager?.listener?.onComplete(m3u8)
    }

    /**
     * 任务准备回调
     */
    private fun callBackStart(m3u8Ts: ArrayList<String>) {
        listener?.onStart(m3u8DownTasker.downTask?.vid!!, "", m3u8Ts)
        //m3u8DownTasker.m3u8DownManager?.listener?.onStart(m3u8,m3u8Ts)
    }

    override fun stop() {
        isRuning.set(false)
        downManager?.pauseAllDownTasker()
    }

    override fun setOnDownListener(listener: OnDownListener?) {
        this.listener = listener
    }
}

/**
 * 回调监听
 *
 */
interface OnDownListener {

    /**
     * 开始下载
     * @param vid 视频唯一ID
     * @param url m3u8
     * @param m3u8Analysis m3u8解析的key,ts
     */
    fun onStart(vid: String, url: String, m3u8Analysis: ArrayList<String>)

    /**
     * 任务加入队列 PS ： M3u8DownTasker中才用到
     * @param vid 视频唯一ID
     * @param url m3u8
     */
    fun onQueue(vid: String?, url: String?)

    /**
     * 下载完成回调
     * @param vid 视频唯一ID
     * @param url m3u8
     */
    fun onComplete(vid: String, url: String)

    /**
     * 下载进度回调
     * @param vid 视频唯一ID
     * @param url m3u8
     * @param progress 进度
     */
    fun onProcess(vid: String, url: String, progress: Int)

    /**
     * 下载错误
     * @param vid 视频唯一ID
     * @param url m3u8
     * @param msg 下载错误信息
     */
    fun onError(vid: String, url: String, msg: String)


}
package com.ponko.cn.module.m3u8downer.core

import android.os.Looper
import com.ponko.cn.module.m3u8downer.core.M3u8Utils.writeLocal
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
     * 需要下载的m3u8地址
     */
    private var m3u8 = ""
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
        //解析m3u8地址中的 key 和ts
        m3u8 = m3u8DownTasker.downTask?.m3u8!!
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
                writeLocal(stream2, m3u8,path, dir, m3u8Key, m3u8Ts)

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
    fun callBackProcess(analysisUrl: String, total: Long) {
        progress += total
        listener?.onProcess(analysisUrl, progress.toInt())
        //m3u8DownTasker.m3u8DownManager?.listener?.onProcess(m3u8, progress.toInt())
    }

    /**
     * 任务下载错误回调
     */
    private fun callbackError(error: String) {
        listener?.onError("", error)
        //m3u8DownTasker.m3u8DownManager?.listener?.onError(m3u8, "解析m3u8失败")
    }

    /**
     * 任务下载完成回调
     */
    private fun callBackComplete(analysisUrl: String) {
        listener?.onComplete(analysisUrl)
        //m3u8DownTasker.m3u8DownManager?.listener?.onComplete(m3u8)
    }

    /**
     * 任务准备回调
     */
    private fun callBackStart(m3u8Ts: ArrayList<String>) {
        listener?.onStart("", m3u8Ts)
        //m3u8DownTasker.m3u8DownManager?.listener?.onStart(m3u8,m3u8Ts)
    }

    override fun stop() {
        isRuning.set(false)
        downManager?.pauseAllDownTasker()
    }

    override fun setOnDownListener(listener: OnDownListener?) {
        this.listener = listener
    }

//    fun exit() {
//        isRuning.set(false)
//        downManager?.pauseAllDownTasker()
//    }
}

///**
// * m3u8解析bean
// */
//private class m3u8AnalysisBean {
//    var key = ""
//    var m3u8ts = ArrayList<String>()
//}

/**
 * 回调监听
 *
 */
interface OnDownListener {

    /**
     * 开始下载
     * @param url m3u8
     * @param m3u8Analysis m3u8解析的key,ts
     */
    fun onStart(url: String, m3u8Analysis: ArrayList<String>)

    /**
     * 下载完成回调
     * @param url m3u8
     */
    fun onComplete(url: String)

    /**
     * 下载进度回调
     * @param url m3u8
     * @param progress 进度
     */
    fun onProcess(url: String, progress: Int)

    /**
     * 下载错误
     * @param url m3u8
     * @param msg 下载错误信息
     */
    fun onError(url: String, msg: String)


}
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
     * 解析的地址列表
     */
    private val m3u8AnalysisUrls = ArrayList<String>()


    init {
        downManager = DownManager.createDownManager(m3u8DownTasker.m3u8DownManager?.context!!)
    }

    override fun run() {
        isRuning = AtomicBoolean(true)
        //解析m3u8地址中的 key 和ts
        val m3u8 = m3u8DownTasker.downTask?.m3u8!!
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url(m3u8).get().build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                BKLog.d("请求m3u8失败")
            }

            override fun onResponse(call: Call, response: Response) {
                val (stream1, stream2) = M3u8Utils.copyInputStream(response.body()?.byteStream())
                val (m3u8Key, m3u8Ts) = M3u8Utils.analysis(stream1)
                writeLocal(stream2, m3u8, "XmDown", m3u8Key, m3u8Ts)
                down("XmDown/${M3u8Utils.m3u8FileName(m3u8)}", m3u8Key, m3u8Ts)
            }
        })
    }

    private fun down(dir: String, m3u8Key: String, m3u8Ts: ArrayList<String>) {
        // 配置缓存路径
        downManager?.downConfig()?.dir = dir

        // 下载地址集合 todo 检查本地数据库未下载ts key
        val bean= m3u8DownTasker.m3u8DownManager?.dao?.select(m3u8DownTasker.downTask?.m3u8!!)
        m3u8AnalysisUrls.add(m3u8Key)
        m3u8AnalysisUrls.addAll(m3u8Ts)

        // 加入下载队列中
        for (url in m3u8AnalysisUrls) {
            val task = DownTask()
            task.url = url
            downManager?.createDownTasker(task)?.enqueue()
        }

        // 获取下载进度
        var progress = 0L
        downManager?.downObserverable()?.registerObserver(object : DownObserver {
            override fun onComplete(tasker: DownTasker, total: Long) {
                if (tasker.task.url.endsWith("${m3u8Ts.size - 1}.ts")) {
                    //所有解析地址已经下载完成
                    listener?.onComplete(tasker.task.url)
                    BKLog.d(tasker.task.fileName + "total:$total thread:${Looper.getMainLooper() == Looper.getMainLooper()}")
                } else {
                    //下载
                    progress += total
                    listener?.onProcess(tasker.task.url, progress.toInt())
                }
            }

            override fun onError(tasker: DownTasker, typeError: DownErrorType, s: String) {
                listener?.onError("")
            }

            override fun onProcess(tasker: DownTasker, process: Long, total: Long, present: Float) {

            }

            override fun onPause(tasker: DownTasker) {

            }

            override fun onDelete(tasker: DownTasker) {

            }
        })

        // 开始下载
        listener?.onStart(m3u8Key, m3u8Ts)
    }

    override fun stop() {
        downManager?.pauseAllDownTasker()
    }

    override fun setOnDownListener(listener: OnDownListener?) {
        this.listener = listener
    }
}

/**
 * m3u8解析bean
 */
private class m3u8AnalysisBean {
    var key = ""
    var m3u8ts = ArrayList<String>()
}

/**
 * 回调监听
 */
interface OnDownListener {

    /**
     * 开始下载
     * @param m3u8Key m3u8解析的key
     * @param m3u8Ts m3u8解析的ts
     */
    fun onStart(m3u8Key: String, m3u8Ts: ArrayList<String>)

    /**
     * 下载完成回调
     * @param url m3u8文件中的ts或者key url地址
     */
    fun onComplete(url: String)

    /**
     * 下载进度回调
     * @param url m3u8文件中的ts或者key url地址
     * @param progress 进度
     */
    fun onProcess(url: String, progress: Int)

    /**
     * 下载错误
     * @param msg 下载错误信息
     */
    fun onError(msg: String)


}
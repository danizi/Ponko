package com.ponko.cn.module.m3u8downer.core

import android.content.Context
import android.os.Environment
import com.ponko.cn.app.PonkoApp.Companion.dbHelp
import java.io.File

/**
 * 下载任务管理器
 */
class M3u8DownManager internal constructor(builder: Builder?, context: Context) : IM3u8DownManager {

    constructor(context: Context) : this(Builder(context), context)

    var context: Context? = null
    var dispatcher: M3u8Dispatcher? = null
    var dao: M3u8DbContract.Dao? = null
    var path: String = ""
    var dir: String? = ""

    /**
     * 可接受所有任务的回调
     */
    var listener: OnDownListener? = null

    init {
        this.context = context
        this.dispatcher = builder?.dispatcher
        this.dao = builder?.dao
        this.path = builder?.path!!
        this.dir = builder.dir
    }

    override fun newTasker(downTask: M3u8DownTask): M3u8DownTasker {
        return M3u8DownTasker.Builder()
                .m3u8DownManager(this)
                .downTask(downTask)
                .build()
    }

    override fun isRun(url: String): Boolean {
        for (runningQueue in dispatcher?.runningQueue!!) {
            if (url == runningQueue.downTask?.vid) {
                return true
            }
            if (url == runningQueue.downTask?.m3u8) {
                return true
            }
        }
        return false
    }

    override fun isReady(vid: String): Boolean {
        //先判断运行队列中该任务
        if (isRun(vid)) {
            return false
        }
        for (readyQueue in dispatcher?.readyQueue!!) {
            if (vid == readyQueue.downTask?.vid) {
                return true
            }
            if (vid == readyQueue.downTask?.m3u8) {
                return true
            }
        }
        return false
    }

    override fun pause(vid: String, url: String) {
        //从队列中移除该任务，并执行下一个队列任务
        //dispatcher?.remove(url)
        //暂停回调
        listener?.onPause(vid, url)

        for (tasker in dispatcher?.runningQueue?.iterator()!!) {
            if (url == tasker.downTask?.m3u8) {
                tasker.m3u8DownRunnable?.stop()
                dispatcher?.runningQueue?.remove(tasker)
                dispatcher?.remove(tasker)
                return
            }
        }
        for (tasker in dispatcher?.readyQueue?.iterator()!!) {
            if (url == tasker.downTask?.m3u8) {
                dispatcher?.readyQueue?.remove(tasker)
                tasker.m3u8DownRunnable?.stop()
                return
            }
        }
    }

    fun pauseCurrent() {
        for (tasker in dispatcher?.runningQueue?.iterator()!!) {
            tasker.m3u8DownRunnable?.stop()
            dispatcher?.runningQueue?.remove(tasker)
            listener?.onPause(tasker.downTask?.vid!!, tasker.downTask?.m3u8!!)
        }
    }

    override fun pause(urls: List<String>) {
        for (url in urls) {
            pause("", url)
        }
    }

    override fun resume(downTask: M3u8DownTask) {
        val tasker = M3u8DownTasker.Builder()
                .m3u8DownManager(this)
                .downTask(downTask)
                .build()
        tasker.enqueue(null)
    }

    override fun resume(downTasks: List<M3u8DownTask>) {
        for (task in downTasks) {
            resume(task)
        }
    }

    override fun delete(url: String) {
        dispatcher?.remove(url)
        //todo 删除文件缓存以及数据库中的信息
    }

    override fun delete(urls: List<String>) {
        for (url in urls) {
            delete(url)
        }
    }

    /**
     * 建造者
     */
    class Builder(context: Context) {
        /**
         * 上下文对象
         */
        var context: Context? = null
        /**
         * 下载任务分发器
         */
        var dispatcher: M3u8Dispatcher? = null
        /**
         *  数据库操作Dao，缓存进度
         */
        var dao: M3u8DbContract.Dao? = null
        /**
         *  数据库创建帮助类
         */
        private var dbHelp: M3u8DBHelp? = null
        /**
         * 路径
         */
        var path: String? = ""
        /**
         *  文件目录
         */
        var dir: String? = ""

        init {
            this.dispatcher = M3u8Dispatcher()
            this.dbHelp = M3u8DBHelp(context, "M3u8Down.db", null, 100)
            this.path = Environment.getExternalStorageDirectory().canonicalPath
            this.dir = "XmDown"
            dao = M3u8DbContract.Dao(dbHelp?.writableDatabase)
        }
    }
}


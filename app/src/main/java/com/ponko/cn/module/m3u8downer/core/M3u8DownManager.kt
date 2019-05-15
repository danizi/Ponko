package com.ponko.cn.module.m3u8downer.core

import android.content.Context

/**
 * 下载任务管理器
 */
class M3u8DownManager internal constructor(builder: Builder?, context: Context) : IM3u8DownManager {

    constructor(context: Context) : this(Builder(context), context)

    var context: Context? = null
    var dispatcher: M3u8Dispatcher? = null
    var dao: M3u8DbContract.Dao? = null

    init {
        this.context = context
        this.dispatcher = builder?.dispatcher
        this.dao = builder?.dao
    }

    override fun newTasker(downTask: M3u8DownTask): M3u8DownTasker {
        return M3u8DownTasker.Builder()
                .m3u8DownManager(this)
                .downTask(downTask)
                .build()
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
        private var dbHelp: M3u8DBHelp? = null

        init {
            dispatcher = M3u8Dispatcher()
            dao = M3u8DbContract.Dao(dbHelp?.writableDatabase)
            this.dbHelp = M3u8DBHelp(context, "M3u8Down.db", null, 100)
        }
    }
}


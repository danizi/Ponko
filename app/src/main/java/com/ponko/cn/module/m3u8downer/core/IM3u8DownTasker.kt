package com.ponko.cn.module.m3u8downer.core

interface IM3u8DownTasker {
    /**
     * 添加任务到分发器中
     */
    fun enqueue(listener: OnDownListener?)

    /**
     * 任务完成，移除分发器中的该任务
     */
    fun remove()
}
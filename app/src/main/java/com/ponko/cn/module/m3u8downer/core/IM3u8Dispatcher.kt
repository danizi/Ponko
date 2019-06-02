package com.ponko.cn.module.m3u8downer.core

interface IM3u8Dispatcher {
    /**
     * 入下载队列
     * @param tasker 需要被添加的下载者
     */
    fun enqueue(tasker: M3u8DownTasker?)

    /**
     * 移除队列
     * @param tasker 需要被移除的下载者
     */
    fun remove(tasker: M3u8DownTasker?)
}
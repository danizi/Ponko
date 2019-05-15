package com.ponko.cn.module.m3u8downer.core

interface IM3u8DownRunnable {
    /**
     * 停止线程运行
     */
    fun stop()

    /**
     * 下载监听
     */
    fun setOnDownListener(listener: OnDownListener?)
}
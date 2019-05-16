package com.ponko.cn.module.m3u8downer.core

/**
 * 管理器接口
 */
interface IM3u8DownManager {
    /**
     * 实例一个下载者
     * @param downTask 下载信息
     */
    fun newTasker(downTask: M3u8DownTask): M3u8DownTasker

    /**
     * 任务是否在下载
     */
    fun isRun(url: String): Boolean

    /**
     * 暂停任务
     * @param url m3u8地址
     */
    fun pause(url: String)

    /**
     * 暂停所有任务
     */
    fun pause(urls: List<String>)

    /**
     * 恢复任务
     * @param url m3u8地址
     */
    fun resume(downTask: M3u8DownTask)

    /**
     * 恢复所有任务
     */
    fun resume(downTasks: List<M3u8DownTask>)

    /**
     * 删除任务
     * @param url m3u8地址
     */
    fun delete(url: String)

    /**
     * 删除所有任务
     */
    fun delete(urls: List<String>)
}
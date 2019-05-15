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
}
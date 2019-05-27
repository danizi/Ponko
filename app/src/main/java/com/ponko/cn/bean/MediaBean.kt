package com.ponko.cn.bean

/**
 * 视频播放播放
 */
class MediaBean(var mediaInfos: List<MediaInfo>) {

    /**
     * 视频信息
     */
    class MediaInfo(var isPay: Boolean?, var isFree: Boolean?, var name: String?, var vid: String?, var duration: Int?, var progress_duration: Int?, var avatar: String, var select: Boolean? = false)

    /**
     * 分享信息
     */
    class ShareBean(
            /**
             * 分享地址
             */
            var url: String?,
            /**
             * 分享标题
             */
            var title: String?,
            /**
             * 分享说明
             */
            var description: String?)

    /**
     * 配置信息
     */
    class ConfigBean {
        /**
         * 0.5 / 0.75 / 1.0 / 1.25 / 1.5 / 20
         */
        var speed: Float? = 1f
        /**
         * 不开启 播放当前 自定义
         */
        var timerType = ""
        /**
         * 自动连播 列表循环 单集循环 播放暂停
         */
        var playType = ""
        /**
         * 适应 填充 16:9 4:3
         */
        var surfaceTypeSize = ""
    }
}
package com.ponko.cn.constant

object Constants {
    /**
     * 接口BaseApi
     */
    const val BASE_API = "https://api.tradestudy.cn/v3/"
    /**
     * 测试服务器
     */
//    const val BASE_API = "http://192.168.253.160:8092/v3/"
    /**
     * 微信id
     */
    var APP_ID = "wxd37fb8ce51a02360"
    /**
     * bugly id
     */
    val BUG_APP_ID = "c70a31a0a0"
    /**
     * 签到成功广播
     */
    const val ACTION_SIGN_SUCCESS = "action_sign_success"
    /**
     * 码兑换成功免费页面刷新广播
     */
    const val ACTION_FREE_REFRESH= "action_free_refresh"
    /**
     * 点击免费视频详情页面Item广播
     */
    const val ACTION_CLICK_FREE_PLAY_ITEM = "action_click_free_play_item"
    /**
     * 点击搜索记录广播
     */
    const val ACTION_CLICK_SEARCH_RECORD_ITEM = "action_click_search_record_item"
    /**
     * 点击搜索老师广播
     */
    const val ACTION_CLICK_SEARCH_TEACHER_ITEM = "action_click_search_teacher_item"
    /**
     * 下载所有下载广播
     */
    const val ACTION_DOWN_ALL = "broadcast.action.down.all"
    /**
     * 暂停所有下载广播
     */
    const val ACTION_DOWN_All_PAUSE = "broadcast.action.pause.all"
    /**
     * 点击了item 运行- >暂停 处理广播
     */
    const val ACTION_DOWN_CLICK_PAUSE = "broadcast.action.pause"
    /**
     * 点击了item 队列- >运行处理广播
     */
    const val ACTION_DOWN_CLICK_READY = "broadcast.action.ready"
    /**
     * 图片延迟加载时间设定
     */
    @Deprecated("")
    const val LOAD_IMAGE_DELAY = 3000L

}
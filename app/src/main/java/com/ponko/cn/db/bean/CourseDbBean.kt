package com.ponko.cn.db.bean

class CourseDbBean {
    /**
     * 用户唯一标识
     */
    var column_uid = ""
    /**
     * 专题id  PS : 关联缓存专题表，是一对多的关系
     */
    var column_special_id = ""
    /**
     * 课程id
     */
    var column_course_id = ""
    /**
     * 课程封面
     */
    var column_cover = ""
    /**
     * 课程标题
     */
    var column_title = ""
    /**
     * 课程视频大小 单位（B）
     */
    var column_total = 0
    /**
     * 课程下载进度 单位（B）
     */
    var column_progress = 0
    /**
     * 是否下载完成
     */
    var column_complete = 0       //是否下载完成
    /**
     * 课程视频m3u8地址 ps:需用通过vid解析获取
     */
    var column_m3u8_url = ""
    /**
     * key ts 地址
     */
    @Deprecated("过时")
    var column_key_ts_url = ""
    /**
     * 缓存课程路径
     */
    var column_down_path = ""     //下载路径
    /**
     * 缓存课程状态( 下载中 暂停 队列 错误 ...)
     */
    var column_state = ""
    /**
     * 视频播放id ps:通过接口获取真实的m3u8地址
     */
    var column_vid = ""

    override fun toString(): String {
        return "CourseDbBean(column_uid='$column_uid', column_special_id='$column_special_id', column_course_id='$column_course_id', column_cover='$column_cover', column_title='$column_title', column_total=$column_total, column_progress=$column_progress, column_complete=$column_complete, column_m3u8_url='$column_m3u8_url', column_key_ts_url='$column_key_ts_url', column_down_path='$column_down_path', column_state='$column_state', column_vid='$column_vid')"
    }


}
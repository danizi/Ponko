package com.ponko.cn.db.bean

class CourseDbBean {
     var column_uid = ""                 //用户唯一标识
     var column_special_id = ""   //专题id         PS : 关联缓存专题表，是一对多的关系
     var column_course_id = ""     //课程id
     var column_cover = ""             //封面
     var column_title = ""             //课程标题
     var column_total = 0             //单个课程大小 单位（B）
     var column_progress = 0       //单个课程下载进度 单位（B）
     var column_complete = 0       //是否下载完成
     var column_m3u8_url = ""       //下载的m3u8地址
     var column_key_ts_url = ""   //key ts 地址
     var column_down_path = ""     //下载路径
     var column_state = ""      //课程状态( 下载中 暂停 队列 错误 )
}
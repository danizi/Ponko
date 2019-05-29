package com.ponko.cn.db.bean

class CourseCollectSpecialDbBean {
    /**
     * 用户唯一标识
     */
    var column_uid = ""
    /**
     * 课程id
     */
    var column_course_id = ""
    /**
     * 专题老师
     */
    var column_teacher = ""
    /**
     * 专题课程数量
     */
    var column_num = ""
    /**
     * 专题封面
     */
    var column_cover = ""
    /**
     * 专题标题
     */
    var column_title = ""

    override fun toString(): String {
        return "CourseCollectSpecialDbBean(column_uid='$column_uid', column_course_id='$column_course_id', column_teacher='$column_teacher', column_num='$column_num', column_cover='$column_cover', column_title='$column_title')"
    }
}
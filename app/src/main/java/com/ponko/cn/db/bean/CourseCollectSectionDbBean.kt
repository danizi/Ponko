package com.ponko.cn.db.bean

class CourseCollectSectionDbBean {
    /**
     * 用户唯一标识
     */
    var column_uid=""
    /**
     * 课程id
     */
    var column_course_id=""
    /**
     * 小节id
     */
    var column_section_id=""
    /**
     * 小节名称
     */
    var column_section_name=""

    override fun toString(): String {
        return "CourseCollectSectionDbBean(column_uid='$column_uid', column_course_id='$column_course_id', column_section_id='$column_section_id', column_section_name='$column_section_name')"
    }


}
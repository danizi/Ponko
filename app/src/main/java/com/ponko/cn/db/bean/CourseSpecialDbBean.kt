package com.ponko.cn.db.bean

/**
 * 实体
 */
class CourseSpecialDbBean {
    /**
     * 登录app用户唯一标识
     */
    var uid = ""
    /**
     * 专题唯一标识
     */
    var special_id = ""
    /**
     * 专题老师
     */
    var teacher = ""
    /**
     * 专题课程总数量
     */
    var num = -1
    /**
     * 专题封面
     */
    var cover = ""
    /**
     * 专题标题
     */
    var title = ""
    /**
     * 专题课程总时长
     */
    var duration=-1

    override fun toString(): String {
        return "CourseSpecialDbBean(uid='$uid', special_id='$special_id', teacher='$teacher', num=$num, cover='$cover', title='$title', duration=$duration)"
    }


}
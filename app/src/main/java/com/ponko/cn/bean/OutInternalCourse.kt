package com.ponko.cn.bean

/**
 * Created by 取个佛系名字：如去 on 2018/7/5.
 * 外层Course
 */
@Deprecated("CourseAllCBean替代了")
class OutInternalCourse {

    var id: String? = null
    var parentId: String? = null
    var title: String? = null

    var newer = false

    var courses : List<InternalCourse>? = null
}
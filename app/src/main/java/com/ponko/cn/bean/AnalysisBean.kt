package com.ponko.cn.bean

/**
 * Created by 取个佛系名字：如去 on 2018/2/22.
 */
@Deprecated("")
class AnalysisBean {

    var types: MutableList<Type>? = null
    var activities: MutableList<Analysis>? = null
    var url: String? = null


    class Type {
        var id: String? = null
        var name: String? = null
        var sort = 0
    }

    class Analysis {
        var typeId: String? = null
        var id: String? = null
        var type: String? = null
        var targetType: String? = null
        var title: String? = null
        var url: String? = null
        var createTime = 0L
        var brief: String? = null
    }

}
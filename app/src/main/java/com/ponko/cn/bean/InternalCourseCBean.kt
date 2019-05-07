package com.ponko.cn.bean

class InternalCourseCBean {
    /**
     * {
    "duration": 1216,
    "newer": false,
    "image": "http://cdn.tradestudy.cn/upload/product/20170204/81c345a25f223263241a03f1a2c30365.jpg",
    "teachers": [],
    "num": 2,
    "id": "ba37e08ea26611e6b8aa00163e000c35",
    "title": "Seven steps to improve your confidence in business"
    }
     */

    var duration: Long? = 0
    var newer = false
    var image: String? = null
    var teachers = emptyArray<String>()
    var num: Long? = 0
    var id: String? = null
    var title: String? = null
    var parentId:String?=null //父级id
    var parentTitle:String?=null //父级Title
    var isFirst = false //是否第一个元素
}
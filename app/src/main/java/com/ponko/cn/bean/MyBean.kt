package com.ponko.cn.bean

class MyBean {

    companion object {
        fun create(): ArrayList<MyListBean> {
            val listBean = MyListBean()
        }
    }

    var infos = ArrayList<MyListBean>()


    class MyListBean {
        var icon: Int = 0
        var des: String = ""
    }

}
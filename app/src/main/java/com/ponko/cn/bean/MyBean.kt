package com.ponko.cn.bean

import com.ponko.cn.R

class MyBean(val listBeans: ArrayList<MyListBean>) {
    companion object {
        val icons = arrayOf(
                R.mipmap.me_service_shop,
                R.mipmap.me_my_cache,
                R.mipmap.me_my_colect,
                R.mipmap.me_my_history,
                R.mipmap.me_my_remind,
                R.mipmap.me_my_rank,
                R.mipmap.me_service_bk,
                R.mipmap.me_service_bk_ed,
                R.mipmap.me_service_advisory,
                R.mipmap.me_service_problem
        )
        val des = arrayOf(
                "积分商城",
                "缓存",
                "收藏",
                "历史",
                "提醒",
                "学习排行",
                "BK码兑换",
                "已兑课程",
                "咨询",
                "常见问题"
        )

        fun create(): MyBean {
            val listBeans = ArrayList<MyListBean>()
            for (i in 0..(icons.size - 1)) {
                listBeans.add(MyListBean(icons[i], des[i]))
            }
            return MyBean(listBeans)
        }
    }

    class MyListBean(val icon: Int = 0, val des: String = "")
}
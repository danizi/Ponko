package com.ponko.cn.bean

import com.ponko.cn.R

class MyBean(val listBeans: ArrayList<MyListBean>) {
    companion object {
        val icons = arrayOf(
                R.mipmap.my_shop_2,
                R.mipmap.my_cache_2,
                R.mipmap.my_colect_2,
                R.mipmap.my_history_2,
                R.mipmap.my_remind_2,
                R.mipmap.my_rank_2,
                R.mipmap.my_bk_2,
                R.mipmap.my_bk_ed_2,
                R.mipmap.my_advisory_2,
                R.mipmap.my_problem,
                R.mipmap.me_check
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
                "常见问题",
                "检查更新"
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
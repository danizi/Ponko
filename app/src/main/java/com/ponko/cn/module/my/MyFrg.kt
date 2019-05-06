package com.ponko.cn.module.my

import com.ponko.cn.R
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.MyBean
import com.ponko.cn.bean.MyTopBean
import com.ponko.cn.bean.ProfileCBean
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.my.adapter.MyAdapter
import com.ponko.cn.module.my.constract.MyConstract
import com.ponko.cn.module.my.holder.MyViewHolder
import com.ponko.cn.module.my.holder.MyViewHolder2
import com.xm.lib.common.base.rv.BaseRvAdapter

class MyFrg : RefreshLoadFrg<MyConstract.Present, ProfileCBean>(), MyConstract.View {

    override fun presenter(): MyConstract.Present {
        return MyConstract.Present(context, this)
    }

    override fun adapter(): BaseRvAdapter? {
        return MyAdapter()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1),
                arrayOf(MyViewHolder::class.java, MyViewHolder2::class.java),
                arrayOf(MyTopBean::class.java, MyBean::class.java),
                arrayOf(R.layout.item_my_top, R.layout.item_my_rv)
                )
    }

    override fun requestMoreApi() {

    }

    override fun requestRefreshApi() {
        p?.requestMyInfoApi()
    }

    override fun requestMyInfoApiSuccess(body: ProfileCBean?) {
        requestRefreshSuccess(body)
    }

    override fun multiTypeData(body: ProfileCBean?): List<Any> {
        val data = ArrayList<Any>()
        if (body != null) {
            data.add(body)
        }
        data.add(MyBean.create())
        return data
    }
}
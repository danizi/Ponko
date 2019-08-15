package com.ponko.cn.module.my.option

import android.view.View
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.OpenCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.holder.MyOpenViewHolder
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response

class OpenRollActivity : RefreshLoadAct<Any, List<OpenCBean>>() {

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(MyOpenViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_my_open_roll)
        )
    }

    override fun initDisplay() {
        addItemDecoration = false
        super.initDisplay()
        addBar2("开通学籍", "", View.OnClickListener { })
        viewHolder?.clContent?.setBackgroundColor(resources?.getColor(R.color.divider)!!)
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        PonkoApp.myApi?.products()?.enqueue(object : HttpCallBack<List<OpenCBean>>() {
            override fun onSuccess(call: Call<List<OpenCBean>>?, response: Response<List<OpenCBean>>?) {
                requestRefreshSuccess(response?.body())
            }

            override fun onFailure(call: Call<List<OpenCBean>>?, msg: String?) {
                super.onFailure(call, msg)
                requestRefreshFailure()
            }
        })
    }

    override fun multiTypeData(body: List<OpenCBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }
}

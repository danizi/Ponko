package com.ponko.cn.module.interflow.frg

import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.ActivityCBean
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.interflow.adapter.SubActivityAdapter
import com.ponko.cn.module.interflow.holder.SubActivityHolder
import com.xm.lib.common.base.rv.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response

/**
 * 活动
 */
class ActivityFragment : RefreshLoadFrg<Any, List<ActivityCBean>>() {

    override fun initDisplay() {
        addItemDecoration = false
        super.initDisplay()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(SubActivityHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_interflow_activity))
    }

    override fun requestMoreApi() {
        PonkoApp.interflowApi?.getParties(++page, 30)?.enqueue(object : HttpCallBack<ArrayList<ActivityCBean>>() {
            override fun onSuccess(call: Call<ArrayList<ActivityCBean>>?, response: Response<ArrayList<ActivityCBean>>?) {
                requestMoreSuccess(response?.body()!!)
            }
        })
    }

    override fun requestRefreshApi() {
        page = 1
        PonkoApp.interflowApi?.getParties(1, 30)?.enqueue(object : HttpCallBack<ArrayList<ActivityCBean>>() {
            override fun onSuccess(call: Call<ArrayList<ActivityCBean>>?, response: Response<ArrayList<ActivityCBean>>?) {
                requestRefreshSuccess(response?.body())
            }
        })
    }

    override fun multiTypeData(body: List<ActivityCBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return SubActivityAdapter()
    }

    override fun presenter(): Any {
        return Any()
    }
}
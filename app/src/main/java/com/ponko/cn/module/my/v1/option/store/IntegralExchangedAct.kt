package com.ponko.cn.module.my.option.store

import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.ExchangedHistoriesCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.holder.MyExchangedViewHolder
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response

/**
 * 已兑记录
 */
class IntegralExchangedAct : RefreshLoadAct<Any, ArrayList<ExchangedHistoriesCBean>>() {

    override fun initDisplay() {
        addItemDecoration = false
        super.initDisplay()
        addBar1("已兑记录")
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(MyExchangedViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_my_store_exchange_record)
        )
    }

    override fun requestMoreApi() {
        PonkoApp.myApi?.integralObtainExchanged(++page)?.enqueue(object : HttpCallBack<ArrayList<ExchangedHistoriesCBean>>() {
            override fun onSuccess(call: Call<ArrayList<ExchangedHistoriesCBean>>?, response: Response<ArrayList<ExchangedHistoriesCBean>>?) {
                requestMoreSuccess(response?.body())
            }

            override fun onFailure(call: Call<ArrayList<ExchangedHistoriesCBean>>?, msg: String?) {
                super.onFailure(call, msg)
                requestMoreFailure()
            }
        })
    }

    override fun requestRefreshApi() {
        PonkoApp.myApi?.integralObtainExchanged()?.enqueue(object : HttpCallBack<ArrayList<ExchangedHistoriesCBean>>() {
            override fun onSuccess(call: Call<ArrayList<ExchangedHistoriesCBean>>?, response: Response<ArrayList<ExchangedHistoriesCBean>>?) {
                requestRefreshSuccess(response?.body())
            }

            override fun onFailure(call: Call<ArrayList<ExchangedHistoriesCBean>>?, msg: String?) {
                super.onFailure(call, msg)
                requestRefreshFailure()
            }
        })
    }

    override fun multiTypeData(body: ArrayList<ExchangedHistoriesCBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }
}

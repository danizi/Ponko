package com.ponko.cn.module.my.option

import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.RemindCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.my.holder.MyRemindHolder
import com.xm.lib.common.base.rv.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response

class RemindAct : RefreshLoadAct<Any, ArrayList<RemindCBean>>() {

    override fun initDisplay() {
        addBar1("消息提醒")
        super.initDisplay()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(MyRemindHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_my_msg_remind)
        )
    }

    override fun requestMoreApi() {
        PonkoApp.myApi?.getRemindList(++page)?.enqueue(object : HttpCallBack<ArrayList<RemindCBean>>() {
            override fun onSuccess(call: Call<ArrayList<RemindCBean>>?, response: Response<ArrayList<RemindCBean>>?) {
                requestMoreSuccess(response?.body())
            }
        })
    }

    override fun requestRefreshApi() {
        PonkoApp.myApi?.getRemindList()?.enqueue(object : HttpCallBack<ArrayList<RemindCBean>>() {
            override fun onSuccess(call: Call<ArrayList<RemindCBean>>?, response: Response<ArrayList<RemindCBean>>?) {
                requestRefreshSuccess(response?.body())
            }
        })
    }

    override fun multiTypeData(body: ArrayList<RemindCBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_remind)
//    }
}

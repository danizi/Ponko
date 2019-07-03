package com.ponko.cn.module.my.option

import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.HelpCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.holder.MyProblemViewHolder
import com.xm.lib.common.base.rv.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response

class ProblemAct : RefreshLoadAct<Any, MutableList<HelpCBean>>() {

    override fun initDisplay() {
        addBar1("常见问题")
        addItemDecoration = false
        disableLoad = true
        super.initDisplay()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(MyProblemViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_my_problem)
        )
    }

    override fun requestMoreApi() {

    }

    override fun requestRefreshApi() {
        PonkoApp.myApi?.getHelpList()?.enqueue(object : HttpCallBack<MutableList<HelpCBean>>() {
            override fun onSuccess(call: Call<MutableList<HelpCBean>>?, response: Response<MutableList<HelpCBean>>?) {
                requestRefreshSuccess(response?.body())
            }

            override fun onFailure(call: Call<MutableList<HelpCBean>>?, msg: String?) {
                super.onFailure(call, msg)
                requestRefreshFailure()
            }
        })
    }

    override fun multiTypeData(body: MutableList<HelpCBean>?): List<Any> {
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
//        setContentView(R.layout.activity_problem)
//    }
}

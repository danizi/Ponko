package com.ponko.cn.module.my.option

import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.LearnRankingBottomBean
import com.ponko.cn.bean.LearnRankingCBean
import com.ponko.cn.bean.LearnRankingGeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.holder.MyLearnRankingBottomHolder
import com.ponko.cn.module.my.holder.MyLearnRankingGeneralHolder
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response

class LearnRankingAct : RefreshLoadAct<Any, LearnRankingCBean>() {

    override fun initDisplay() {
        disableLoad=true
        addBar1("本周学习排行版")
        super.initDisplay()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1),
                arrayOf(MyLearnRankingGeneralHolder::class.java, MyLearnRankingBottomHolder::class.java),
                arrayOf(LearnRankingGeneralBean::class.java, LearnRankingBottomBean::class.java),
                arrayOf(R.layout.item_my_learn_ranking, R.layout.item_my_learn_ranking_bttom)
        )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        PonkoApp.myApi?.learnRanking()?.enqueue(object : HttpCallBack<LearnRankingCBean>() {
            override fun onSuccess(call: Call<LearnRankingCBean>?, response: Response<LearnRankingCBean>?) {
                requestRefreshSuccess(response?.body())
            }

            override fun onFailure(call: Call<LearnRankingCBean>?, msg: String?) {
                super.onFailure(call, msg)
                requestRefreshFailure()
            }
        })
    }

    override fun multiTypeData(body: LearnRankingCBean?): List<Any> {
        val data = ArrayList<Any>()
        if (!body?.ranking?.isEmpty()!!) {
            data.add(LearnRankingGeneralBean(body.ranking))
        }
//        if (body.oneself != null) {
//            data.add(LearnRankingBottomBean(body.oneself))
//        }
        return data
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_ranking)
//    }
}

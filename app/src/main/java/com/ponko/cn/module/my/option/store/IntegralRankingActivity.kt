package com.ponko.cn.module.my.option.store

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.*
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.holder.MyRankFooterViewHolder
import com.ponko.cn.module.my.holder.MyRankGenenalViewHolder
import com.ponko.cn.module.my.holder.MyRankHeadViewHolder
import com.xm.lib.common.base.rv.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response

class IntegralRankingActivity : RefreshLoadAct<Any, RankingV2>() {
    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0,1,2,3),
                arrayOf(MyRankHeadViewHolder::class.java,MyRankGenenalViewHolder::class.java,MyRankFooterViewHolder::class.java),
                arrayOf(IntegralRankHeadBean::class.java,IntegralRankGenenalBean::class.java,IntegralRankFooterBean::class.java),
                arrayOf(R.layout.item_my_store_integral_rank_head,R.layout.item_my_store_integral_rank_genenal,R.layout.item_my_store_integral_rank_footer)
                )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        PonkoApp.myApi?.rank()?.enqueue(object :HttpCallBack<RankingV2>(){
            override fun onSuccess(call: Call<RankingV2>?, response: Response<RankingV2>?) {
            requestRefreshSuccess(response?.body())
            }
        })
    }

    override fun multiTypeData(body: RankingV2?): List<Any> {
        val data = ArrayList<Any>()
        //前三名次
        //中间名次
        //我的名次
        if(body?.mine!=null){
            data.add(body.mine)
        }
        return data
    }

    override fun adapter(): BaseRvAdapter? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun presenter(): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_integral_ranking)
//    }
}

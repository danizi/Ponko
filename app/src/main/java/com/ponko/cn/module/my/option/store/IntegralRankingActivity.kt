package com.ponko.cn.module.my.option.store

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

    override fun initDisplay() {
        super.initDisplay()
        addBar1("积分排行版")
        viewHolder?.clContent?.setBackgroundColor(resources.getColor(R.color.white))
        disableLoad=true
        isFocusableInTouchMode()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0,1,2),
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
        if(!body?.all?.isEmpty()!! && body.all.size>3){
            val integralRankHeadBean = IntegralRankHeadBean()
            integralRankHeadBean.ranking.add(body.all!![0])
            integralRankHeadBean.ranking.add(body.all!![1])
            integralRankHeadBean.ranking.add(body.all!![2])
            data.add(integralRankHeadBean)
        }

        //中间名次
        if(!body.all?.isEmpty()!! && body.all.size>3){
            val integralRankGenenalBean = IntegralRankGenenalBean()
            for (i in 2..(body.all.size-1)){
                integralRankGenenalBean.ranking.add(body.all!![i])
            }
            data.add(integralRankGenenalBean)
        }
        //我的名次
        if(body.mine!=null){
            data.add(IntegralRankFooterBean(body.mine))
        }
        return data
    }

    override fun adapter(): BaseRvAdapter? {
        return object :BaseRvAdapter(){}
    }

    override fun presenter(): Any {
        return Any()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_integral_ranking)
//    }
}

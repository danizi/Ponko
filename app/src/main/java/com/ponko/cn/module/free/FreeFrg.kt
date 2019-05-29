package com.ponko.cn.module.free

import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.*
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.free.adapter.FreeAdapter
import com.ponko.cn.module.free.constract.FreeConstract
import com.ponko.cn.module.free.holder.ExchangedViewHolder
import com.ponko.cn.module.free.holder.TrialViewHolder
import com.ponko.cn.module.free.holder.WechatViewHolder
import com.ponko.cn.utils.DialogUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.log.BKLog

class FreeFrg : RefreshLoadFrg<FreeConstract.Present, CoursesCBean>(), FreeConstract.View {

    override fun presenter(): FreeConstract.Present {
        return FreeConstract.Present(context, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_base_refresh_load
    }

    override fun initDisplay() {
        super.initDisplay()
        disableLoad = true
        addBar3("免费体验", "兑换", View.OnClickListener {
            BKLog.d("弹出兑换码框")
            DialogUtil.showExchange(context)
        })
    }


    override fun requestFreeApiSuccess(body: CoursesCBean?) {
        requestRefreshSuccess(body)
    }

    override fun multiTypeData(body: CoursesCBean?): List<Any> {
        val multiData = ArrayList<Any>()

        //p?.testWechat(body)
        if (body?.wechat?.isNotEmpty()!!) {
            multiData.add(WechatBean(body.wechat))
        }
        //p?.testExchanged(body)
        if (body.exchanged?.isNotEmpty()!!) {
            multiData.add(ExchangedBean(body.exchanged))
        }
        if (body.trial.isNotEmpty()) {
            multiData.add(TrialBean(body.trial[0]))
            multiData.add(TrialBean(body.trial[1]))
        }
        return multiData
    }


    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1, 2),
                arrayOf(WechatViewHolder::class.java, ExchangedViewHolder::class.java, TrialViewHolder::class.java),
                arrayOf(WechatBean::class.java, ExchangedBean::class.java, TrialBean::class.java),
                arrayOf(R.layout.item_free_exchange_list, R.layout.item_free_exchange_list, R.layout.item_free_course_type)
        )
    }

    override fun requestMoreApi() {

    }

    override fun requestRefreshApi() {
        p?.requestFreeApi()
    }

    override fun adapter(): BaseRvAdapter? {
        return FreeAdapter()
    }
}
package com.ponko.cn.module.my.option.store

import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.storeProfileBean
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.MyTaskBean
import com.ponko.cn.bean.MyTaskSignBean
import com.ponko.cn.bean.StoreTaskBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.holder.MyTaskSignViewHolder
import com.ponko.cn.module.my.holder.MyTaskViewHolder
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.SDKVersionUtil.hasL
import retrofit2.Call
import retrofit2.Response

/**
 * 赚积分
 */
class IntegralTaskActivity : RefreshLoadAct<Any, StoreTaskBean>() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initDisplay() {
        disableLoad = true
        addItemDecoration = false
        isFocusableInTouchMode()
        super.initDisplay()
        addBar2("赚积分", "规则", View.OnClickListener {
            BKLog.d("点击规则")
            IntoTargetUtil.target(this, "url", storeProfileBean?.score_rule)
        })
        viewHolder?.toolbar?.setBackgroundColor(Color.parseColor("#EFF7FE"))
        if (hasL()) {
            viewHolder?.toolbar?.elevation = 0f
        }
        addItemDecoration = false
        com.jaeger.library.StatusBarUtil.setColor(this, Color.parseColor("#EFF7FE"), 0)
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1),
                arrayOf(MyTaskSignViewHolder::class.java, MyTaskViewHolder::class.java),
                arrayOf(MyTaskSignBean::class.java, MyTaskBean::class.java),
                arrayOf(R.layout.item_my_store_integral_sign, R.layout.item_my_store_integral_task_rv)
        )
    }

    override fun requestMoreApi() {
        PonkoApp.myApi?.tasks(++page)?.enqueue(object : HttpCallBack<StoreTaskBean>() {
            override fun onSuccess(call: Call<StoreTaskBean>?, response: Response<StoreTaskBean>?) {
                requestMoreSuccess(response?.body())
            }

            override fun onFailure(call: Call<StoreTaskBean>?, msg: String?) {
                super.onFailure(call, msg)
                requestMoreFailure()
            }
        })
    }

    override fun requestRefreshApi() {
        //Ps:我的页面已经预先请求了
        if (PonkoApp.signInfo != null) {
            requestRefreshSuccess(PonkoApp.signInfo)
        }
    }

    override fun multiTypeData(body: StoreTaskBean?): List<Any> {
        val data = ArrayList<Any>()
        data.add(MyTaskSignBean(body?.isCompleted, body?.days, body?.scores))
        data.add(MyTaskBean(body?.tasks!!))
        return data
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }
}

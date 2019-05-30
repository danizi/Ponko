package com.ponko.cn.module.my.option.store

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.StoreObtainLogBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.holder.MyIntegralRecordViewHolder
import com.xm.lib.common.base.rv.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response
import android.widget.TextView
import android.widget.LinearLayout
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil

/**
 * 积分获取记录
 */
class IntegralRecordActivity : RefreshLoadAct<Any, StoreObtainLogBean>() {
    /**
     * 积分记录UI
     */
    private var v: ViewHolder? = null

    override fun initDisplay() {
        addItemDecoration = false
        super.initDisplay()
        addBar1("积分获取记录")
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_integral_record
    }

    override fun findViews() {
        super.findViews()
        if (v == null) {
            v = ViewHolder.create(this)
        }
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(MyIntegralRecordViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_my_store_integral_record))
    }

    override fun requestMoreApi() {
        PonkoApp.myApi?.integralObtainRecord(++page)?.enqueue(object : HttpCallBack<StoreObtainLogBean>() {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(call: Call<StoreObtainLogBean>?, response: Response<StoreObtainLogBean>?) {
                val storeObtainLogBean = response?.body()
//                if (!storeObtainLogBean?.list?.isEmpty()!!) {
                    requestMoreSuccess(storeObtainLogBean)
//                } else {
//                    ToastUtil.show("数据已全部加载完成")
//                    viewHolder?.srl?.finishLoadMore(0)
//                }
            }

            override fun onFailure(call: Call<StoreObtainLogBean>?, msg: String?) {
                super.onFailure(call, msg)
                requestMoreFailure()
            }
        })
    }

    override fun requestRefreshApi() {
        PonkoApp.myApi?.integralObtainRecord()?.enqueue(object : HttpCallBack<StoreObtainLogBean>() {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(call: Call<StoreObtainLogBean>?, response: Response<StoreObtainLogBean>?) {
                val storeObtainLogBean = response?.body()
                requestRefreshSuccess(storeObtainLogBean)
                v?.tvIntegralExpiration?.text = "当前有${storeObtainLogBean?.soon_expired_scores}积分将在${TimeUtil.unixStr("yyy-MM-dd", storeObtainLogBean?.soon_expired_time!!)}日过期，"
                v?.llIntegralExpiration?.setOnClickListener {
                    BKLog.d("顶部黄色提示")
                    finish()
                }
            }

            override fun onFailure(call: Call<StoreObtainLogBean>?, msg: String?) {
                super.onFailure(call, msg)
                requestRefreshFailure()
            }
        })
    }

    override fun multiTypeData(body: StoreObtainLogBean?): List<Any> {
        return body?.list!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

    /**
     * ui
     */
    private class ViewHolder private constructor(val llIntegralExpiration: LinearLayout, val tvIntegralExpiration: TextView) {
        companion object {

            fun create(act: AppCompatActivity): ViewHolder {
                val llIntegralExpiration = act.findViewById<View>(R.id.ll_integral_expiration) as LinearLayout
                val tvIntegralExpiration = act.findViewById<View>(R.id.tv_integral_expiration) as TextView
                return ViewHolder(llIntegralExpiration, tvIntegralExpiration)
            }
        }
    }


}

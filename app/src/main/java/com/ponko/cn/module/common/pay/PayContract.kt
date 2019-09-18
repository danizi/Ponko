package com.ponko.cn.module.common.pay

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.bean.ItemPayLessonBean
import com.ponko.cn.bean.ItemPayRightsBean
import com.ponko.cn.bean.ItemPaymentBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.share.ShareAct
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.pay.AbsPay
import com.xm.lib.pay.Channel
import com.xm.lib.pay.OnPayListener
import com.xm.lib.pay.wx.WxPay
import retrofit2.Call
import retrofit2.Response

class PayContract {

    interface V {

        /**
         * 设置RecyclerView适配器
         */
        fun setAdapter(dates: ArrayList<Any>)

        /**
         * 请求接口数据成功
         */
        fun requestPayApiSuccess(data: ArrayList<Any>)

        /**
         * 请求支付接口失败
         */
        fun requestPayApiFailure(msg: String?)

    }

    class M {
        fun requestPayApi(callback: HttpCallBack<GeneralBean>) {
            callback.onSuccess(null, null)
        }
    }

    class P(val context: Context?, val v: V?) {
        private var payment = 0
        private var m = M()
        private var adapter: BaseRvAdapterV2? = null

        private var pay: AbsPay? = null
        private var order = ""

        init {


        }

        fun clickPay() {
            when (payment) {
                0 -> {
                    pay = WxPay(context as Activity)

                }
                1 -> {
                    pay = WxPay(context as Activity)
                }
            }
            pay?.pay(Channel.GENERAL, order, object : OnPayListener {
                override fun onSuccess() {
                    ToastUtil.show("支付成功")
                }

                override fun onFailure() {
                    ToastUtil.show("支付失败")
                }

                override fun onCancel() {
                    ToastUtil.show("支付取消")
                }
            })
            (context as Activity).startActivity(Intent(context, ShareAct::class.java))
        }

        fun refresh() {
            requestPayApi()
        }

        fun getMultiTypeData(body: Response<GeneralBean>?): ArrayList<Any> {
            val dates = ArrayList<Any>()
            dates.add(ItemPayLessonBean())
            dates.add(ItemPayRightsBean())
            dates.add(ItemPaymentBean())
            return dates
        }

        fun requestPayApi() {
            m.requestPayApi(object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    //设置RecyclerView适配器
                    val data = getMultiTypeData(response)
                    v?.setAdapter(data)
                    v?.requestPayApiSuccess(data)
                }

                override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.requestPayApiFailure(msg)
                }
            })
        }

        /**
         * @param type 0 微信 1 阿里
         */
        fun paymentSelect(type: Int) {
            payment = type
        }
    }

}
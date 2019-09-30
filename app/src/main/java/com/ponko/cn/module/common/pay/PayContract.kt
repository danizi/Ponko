package com.ponko.cn.module.common.pay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.google.gson.Gson
import com.ponko.cn.MainActivity
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.*
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.OnEnterListener
import com.xm.lib.pay.AbsPay
import com.xm.lib.pay.Channel
import com.xm.lib.pay.OnPayListener
import com.xm.lib.pay.PayConfig
import com.xm.lib.pay.ali.AliPay
import com.xm.lib.pay.wx.WxPay
import com.xm.lib.pay.wx.uikit.Constants
import retrofit2.Call
import retrofit2.Response

class PayContract {

    interface V {
        /**
         * 设置RecyclerView适配器
         */
        fun setAdapter(dates: ArrayList<Any>)

        /**
         * 刷新RecyclerView数据
         */
        fun refreshRvData(data: ArrayList<Any>)

        /**
         * 请求接口数据成功
         */
        fun requestPayApiSuccess(data: ArrayList<Any>)

        /**
         * 请求支付接口失败
         */
        fun requestPayApiFailure(msg: String?)

        /**
         * 状态页面显示
         * @param type VIEW_STATE_LOADING 加载
         *              VIEW_STATE_NODATA  无数据
         *              VIEW_STATE_ERROR   错误
         *              VIEW_STATE_HIDE    隐藏
         */
        fun statePage(type: Int)

        /**
         * 显示登录页面对话框
         */
        fun showLoginTipDlg(s: String)
    }

    class M {

        fun requestPayApi(productId: String, callback: HttpCallBack<ProductInfoCBean>) {
            PonkoApp.payApi?.productInfo(productId)?.enqueue(callback)
        }

        fun createProductOrder(productId: String, payWay: String, callback: HttpCallBack<OrderCBean>) {
            PonkoApp.payApi?.createProductOrder(payWay, productId)?.enqueue(callback)
        }

        fun orderFinish(sn: String, callback: HttpCallBack<GeneralBean>) {
            PonkoApp.payApi?.orderFinish(sn)?.enqueue(callback)
        }
    }

    class P(val context: Context?, val v: V?) {
        private var payment = 0
        private var m = M()
        private var adapter: BaseRvAdapterV2? = null

        private var pay: AbsPay? = null
        private var order = ""
        private var payWay = "weiXin"
        private var productId: String = ""

        private val checkOrderDelayed5 = 5000L
        private val checkOrderDelayed3 = 3000L
        private val checkOrderErrorMsg = "您支付的款项我们已收到，但因为网络原因导致服务开通失败，请点击按钮重试。我们不会向您重复收取费用，请放心点击。"
        private val checkOrderLoadingMsg = "正在开通服务..."
        private var checkOrderHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                if (msg?.what == 1) {
                    val sn = msg.obj as String
                    if (context == null) {
                        ToastUtil.show("context is null")
                        return
                    }
                    m.orderFinish(sn, object : HttpCallBack<GeneralBean>() {
                        override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                            val generalBean = response?.body()
                            DialogUtil.hideProcess()
                            DialogUtil.show(context, "提示", generalBean?.msg!!, false, object : OnEnterListener {
                                override fun onEnter(dlg: AlertDialog) {
                                    dlg.dismiss()
                                    context.startActivity(Intent(context, MainActivity::class.java))
                                }
                            }, null)
                        }

                        override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                            super.onFailure(call, msg)
                            DialogUtil.show(context, "提示", checkOrderErrorMsg, false, object : OnEnterListener {
                                override fun onEnter(dlg: AlertDialog) {
                                    dlg.dismiss()
                                    DialogUtil.hideProcess()
                                    check(sn, checkOrderDelayed3)
                                }
                            }, null)
                        }
                    })
                }

            }
        }

        companion object {
            const val VIEW_STATE_LOADING = 0x00
            const val VIEW_STATE_NODATA = 0x01
            const val VIEW_STATE_ERROR = 0x02
            const val VIEW_STATE_HIDE = 0x03
        }

        /**
         * 将支付结果通知后台
         * @param sn 创建订单接口返回实体的key字段
         * @param sleep 延时执行检查接口事件 单位ms
         */
        private fun check(sn: String?, sleep: Long) {
            if (!TextUtils.isEmpty(sn)) {
                DialogUtil.showProcess(context!!, checkOrderLoadingMsg, false)
                //Thread.sleep(sleep)
                val msg = checkOrderHandler.obtainMessage()
                msg.what = 1
                msg.obj = sn
                checkOrderHandler.sendMessageDelayed(msg, sleep)

            } else {
                ToastUtil.show("创建订单key为空，请联系客服处理")
            }
        }

        fun clickPay() {
            if (CacheUtil.isUserTypeLogin()) {
                checkProductId()
                m.createProductOrder(productId, payWay, object : HttpCallBack<OrderCBean>() {
                    override fun onSuccess(call: Call<OrderCBean>?, response: Response<OrderCBean>?) {
                        val orderCBean = response?.body()
                        val order = getOrder(orderCBean)
                        if (!TextUtils.isEmpty(order)) {
                            BKLog.d("order:$order")
                            getPay()?.pay(Channel.GENERAL, order, object : OnPayListener {
                                override fun onSuccess() {
                                    //ToastUtil.show("支付成功")
                                    check(orderCBean?.key, checkOrderDelayed5)
                                }

                                override fun onFailure() {
                                    ToastUtil.show("支付失败")
                                }

                                override fun onCancel() {
                                    ToastUtil.show("支付取消")
                                }
                            })
                        } else {
                            BKLog.e("订单参数为空，请检查下获取订单方法")
                        }
                    }

                    /**
                     * 获取订单
                     */
                    private fun getOrder(orderCBean: OrderCBean?): String {
                        when (payWay) {
                            "weiXin" -> {
                                order = Gson().toJson(orderCBean?.wechat)
                            }
                            "alipay" -> {
                                order = orderCBean?.alipay!!
                            }
                        }
                        BKLog.d("创建订单:" + orderCBean.toString())
                        return order
                    }

                    /**
                     * 获取支付实例
                     */
                    private fun getPay(): AbsPay? {
                        when (payment) {
                            0 -> {
                                pay = WxPay(context as Activity)
                                pay?.init(PayConfig.Builder().appid(Constants.APP_ID).build())
                            }
                            1 -> {
                                pay = AliPay(context as Activity)
                            }
                        }
                        return pay
                    }

                    override fun onFailure(call: Call<OrderCBean>?, msg: String?) {
                        super.onFailure(call, msg)
                        BKLog.e("请求订单失败:$msg")
                    }
                })
            } else {
                v?.showLoginTipDlg("亲，请先登录或者注册账号....")
            }
        }

        /**
         * 刷新请求接口
         */
        fun refresh() {
            checkProductId()
            m.requestPayApi(productId, object : HttpCallBack<ProductInfoCBean>() {
                override fun onSuccess(call: Call<ProductInfoCBean>?, response: Response<ProductInfoCBean>?) {
                    //设置RecyclerView适配器
                    val data = getMultiTypeData(response)
                    v?.refreshRvData(data)
                    BKLog.d("刷新数据成功")
                }

                override fun onFailure(call: Call<ProductInfoCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.requestPayApiFailure(msg)
                    v?.statePage(VIEW_STATE_ERROR)
                }
            })
        }

        private fun getMultiTypeData(body: Response<ProductInfoCBean>?): ArrayList<Any> {
            val dates = ArrayList<Any>()
            val productInfoCBean = body?.body()
            if (!productInfoCBean?.list?.isEmpty()!!) {
                val itemPayLessonBean = ItemPayLessonBean()
                val itemPayRightsBean = ItemPayRightsBean()
                val itemPaymentBean = ItemPaymentBean()
                for (listBeanX in productInfoCBean.list) {
                    val type = listBeanX.type
                    when {
                        type.equals("course", true) -> {
                            itemPayLessonBean.title = listBeanX.title
                            itemPayLessonBean.list.addAll(listBeanX.list)
                            if (productInfoCBean.header != null) {
                                itemPayLessonBean.header = productInfoCBean.header
                            }
                            if (itemPayLessonBean.list.isNotEmpty()) {
                                dates.add(itemPayLessonBean)
                            }
                        }
                        type.equals("summary", true) -> {
                            itemPayRightsBean.title = listBeanX.title
                            itemPayRightsBean.list.addAll(listBeanX.list)
                            if (itemPayRightsBean.list.isNotEmpty()) {
                                dates.add(itemPayRightsBean)
                            }
                        }
                        type.equals("pay", true) -> {
                            itemPaymentBean.title = listBeanX.title
                            itemPaymentBean.list.addAll(listBeanX.list)
                            if (itemPaymentBean.list.isNotEmpty()) {
                                dates.add(itemPaymentBean)
                            }
                        }
                    }
                }
                if (productInfoCBean.footer != null) {
                    dates.add(productInfoCBean.footer)
                }
            } else {
                BKLog.e("getMultiTypeData(...) 封装数据失败")
            }
            return dates
        }

        /**
         * 请求支付页面接口
         */
        fun requestPayApi() {
            checkProductId()
            v?.statePage(VIEW_STATE_LOADING)
            m.requestPayApi(productId, object : HttpCallBack<ProductInfoCBean>() {
                override fun onSuccess(call: Call<ProductInfoCBean>?, response: Response<ProductInfoCBean>?) {
                    //设置RecyclerView适配器
                    val data = getMultiTypeData(response)
                    v?.setAdapter(data)
                    v?.requestPayApiSuccess(data)
                    v?.statePage(VIEW_STATE_HIDE)
                }

                override fun onFailure(call: Call<ProductInfoCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.requestPayApiFailure(msg)
                    v?.statePage(VIEW_STATE_ERROR)
                }
            })
        }

        /**
         * @param type 0 微信 1 阿里
         */
        fun paymentSelect(type: Int) {
            this.payment = type
            this.payWay = ""
            when (type) {
                0 -> {
                    payWay = "weiXin"
                }
                1 -> {
                    payWay = "alipay"
                }
            }
        }

        /**
         * 设置订单参数
         */
        fun setProductId(productId: String?) {
            this.productId = productId!!
            checkProductId()
        }

        private fun checkProductId() {
            if (TextUtils.isEmpty(productId)) {
                throw IllegalAccessException("productId is null call requestPayApi Failure")
            }
        }
    }
}
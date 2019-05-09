package com.ponko.cn

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.ponko.cn.constant.Constant
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ViewUtil
import android.support.v7.widget.AppCompatButton
import android.widget.LinearLayout
import com.google.gson.Gson
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.APP_ID
import com.ponko.cn.bean.AddressBean
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.bean.OrderCBean
import com.ponko.cn.http.HttpCallBack
import com.xm.lib.pay.AbsPay
import com.xm.lib.pay.Channel
import com.xm.lib.pay.OnPayListener
import com.xm.lib.pay.ali.AliPay
import com.xm.lib.pay.wx.WxPay
import com.xm.lib.share.AbsShare
import com.xm.lib.share.wx.WxShare
import retrofit2.Call
import retrofit2.Response


/**
 * 分享 & 支付 & 普通网页 & 积分兑换(exchange)
 */
class WebAct : PonkoBaseAct<Any>() {
    companion object {
        private const val TAG = "WebAct"
        fun start(context: Context?, link_type: String?, link_value: String?, title: String? = "") {
            val intent = Intent(context, WebAct::class.java)
            intent.putExtra("title", title)
            intent.putExtra("link_type", link_type)
            intent.putExtra("link_value", link_value)
            context?.startActivity(intent)
        }

        fun startExChange(context: Context?, link_type: String?, link_value: String?, title: String?, id: String?, needScore: String, aggregateScore: String) {
            val intent = Intent(context, WebAct::class.java)
            intent.putExtra("title", title)
            intent.putExtra("link_type", link_type)
            intent.putExtra("link_value", link_value)
            intent.putExtra("need_score", needScore)
            intent.putExtra("exchange_product_id", id)
            intent.putExtra("aggregate_score", aggregateScore)
            context?.startActivity(intent)
        }
    }

    private var linkType: String = ""
    private var linkValue: String = ""
    private var viewHolder: ViewHolder? = null

    override fun presenter(): Any {
        return Any()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun findViews() {
        super.findViews()
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
    }

    override fun initDisplay() {
        super.initDisplay()
        addBar(this, viewHolder?.toolbar)
        initWebView()
    }

    private var exchangeViewHolder: ExchangeViewHolder? = null
    private var payViewHolder: PayViewHolder? = null
    private var tvBarRight: TextView? = null
    private var tvTitle: TextView? = null
    private fun addBar(context: Context?, toolbar: Toolbar?) {
        val barView = ViewUtil.viewById(context, R.layout.bar_2, null)
        toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar?.visibility = View.VISIBLE
        val flBack: FrameLayout? = barView?.findViewById(R.id.fl_back) as FrameLayout
        tvTitle = barView.findViewById(R.id.tv_title) as TextView
        tvBarRight = barView.findViewById(R.id.tv_bar_right) as TextView
        flBack?.setOnClickListener {
            (context as Activity).finish()
        }
        tvBarRight?.setOnClickListener {
            share()
        }
        tvTitle?.text = title
        tvBarRight?.text = "分享"
        tvBarRight?.visibility = View.GONE
    }

    private var productId: String = ""

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    private fun initWebView() {
        val javascriptInterfaceName = "local_obj"
        val javascriptShare = "javascript:window.$javascriptInterfaceName.getShare(" +
                "document.getElementById('share_title').value," +
                "document.getElementById('share_description').value," +
                "document.getElementById('share_link').value" +
                ");"
        val javascriptPayProductid = "javascript:window.$javascriptInterfaceName.getValueById(" +
                "document.getElementById('productId').value" +
                ");"
        val javascriptPayGuide = "javascript:window.local_obj.getProductGuide(" +
                "document.getElementById('productGuideHave').value," +
                "document.getElementById('productGuideATitle').value," +
                "document.getElementById('productGuideAUrl').value," +
                "document.getElementById('productGuideBTitle').value," +
                "document.getElementById('productGuideBUrl').value," +
                "document.getElementById('productGuideCTitle').value" +
                ")"

        val webView = viewHolder?.web
        val progressBar = viewHolder?.process
        val webSettings = webView?.settings
        webSettings?.cacheMode = WebSettings.LOAD_DEFAULT //默认浏览器缓存
        webSettings?.javaScriptEnabled = true  //支持js
        webSettings?.useWideViewPort = true  //
        //支持获取手势焦点，输入用户名、密码或其他
        webView?.requestFocusFromTouch()
        webSettings?.blockNetworkImage = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        //监听
        webView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                handleProgressBar(newProgress)
                super.onProgressChanged(view, newProgress)
            }

            private fun handleProgressBar(newProgress: Int) {
                BKLog.i(TAG, "newProgress == >  $newProgress")
                if (newProgress == 0)
                    progressBar?.visibility = View.VISIBLE

                progressBar?.progress = newProgress

                if (newProgress >= 100)
                    progressBar?.visibility = View.GONE
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                if (!TextUtils.isEmpty(title)) {
                    BKLog.d(TAG, "onReceivedTitle$title")
                    //setTitle(title)
                    if (title.startsWith("http") || title.startsWith("https")) else tvTitle?.text = title
                }
                super.onReceivedTitle(view, title)
            }
        }
        webView?.webViewClient = object : WebViewClient() {
            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                view.loadUrl(request.url.toString())
                return true
            }

            override fun onPageFinished(view: WebView, finishUrl: String) {
                BKLog.d(TAG, "onPageFinished finished:$finishUrl")
                view.loadUrl(javascriptShare)
                view.loadUrl(javascriptPayProductid)
                view.loadUrl(javascriptPayGuide)
                super.onPageFinished(view, finishUrl)
                progressBar?.visibility = View.GONE
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                BKLog.d(TAG, "onReceivedError error  $error")
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()  // 接受所有网站的证书
            }
        }

        //加载网页并且注入对象
        title = intent.getStringExtra("title")    //ps:可选项，如果web回调中能获取标题则直接获取
        if (!TextUtils.isEmpty(title)) {
            tvTitle?.text = title
        }

        linkType = intent.getStringExtra("link_type")
        linkValue = intent.getStringExtra("link_value")

        when (linkType) {
            "exchange" -> {
                viewHolder?.flBottomBtn?.visibility = View.VISIBLE
                val view = ViewUtil.viewById(this, R.layout.web_bottom_exchange)
                exchangeViewHolder = ExchangeViewHolder.create(view!!)
                exchangeViewHolder?.btnExchange?.setOnClickListener {
                    enterExchange()
                }
                viewHolder?.flBottomBtn?.addView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
            "pay" -> {
                viewHolder?.flBottomBtn?.visibility = View.VISIBLE
                val view = ViewUtil.viewById(this, R.layout.web_bottom_pay)
                payViewHolder = PayViewHolder.create(view!!)
                payViewHolder?.btnPay?.setOnClickListener {
                    enterPay()
                }
                viewHolder?.flBottomBtn?.addView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
            "url" -> {
                viewHolder?.flBottomBtn?.visibility = View.GONE
            }
        }

        val heads = HashMap<String, String>()
        heads["x-tradestudy-client-version"] = "3.4.6"
        heads["x-tradestudy-client-device"] = "android_phone"
        heads["x-tradestudy-access-key-id"] = "c"
        heads["x-tradestudy-access-token"] = Constant.TOKEN
        viewHolder?.web?.loadUrl(loadUrl(linkValue), heads)
        webView?.addJavascriptInterface(InJavaScriptLocalObj(object : InJavaScriptLocalObj.OnInJavaScriptLocalObjListener {
            override fun onShare(t: String, des: String, l: String) {
                this@WebAct.runOnUiThread {
                    BKLog.d(TAG, "t:$t des:$des l:$l")
                    tvBarRight?.visibility = View.VISIBLE
                }

            }

            override fun onProductId(value: String) {
                this@WebAct.runOnUiThread {
                    BKLog.d(TAG, "productId:$value")
                    productId = value
                    tvBarRight?.visibility = View.GONE
                }
            }

            override fun onPayGuide(have: String?, guideATitle: String?/*课程表*/, guideAUrl: String, guideBTitle: String/*老师介绍*/, guideBUrl: String, guideCTitle: String) {
                this@WebAct.runOnUiThread {
                    BKLog.d(TAG, "have:$have guideATitle:$guideATitle guideAUrl:$guideAUrl guideBTitle:$guideBTitle guideBUrl:$guideBUrl guideCTitle:$guideCTitle")
                    tvBarRight?.visibility = View.GONE
                    when (have) {
                        "0" -> {
                            //只显示支付按钮
                            payViewHolder?.btnPay?.visibility = View.VISIBLE
                        }
                        "1" -> {
                            //显示课程信息课程介绍
                            payViewHolder?.btnCourse?.visibility = View.VISIBLE
                            payViewHolder?.btnIntroduce?.visibility = View.VISIBLE
                            payViewHolder?.btnPay?.visibility = View.VISIBLE
                            payViewHolder?.btnCourse?.setOnClickListener {
                                start(this@WebAct, "url", guideAUrl, guideATitle)
                            }
                            payViewHolder?.btnIntroduce?.setOnClickListener {
                                start(this@WebAct, "url", guideBUrl, guideBTitle)
                            }
                        }
                    }
                }
            }
        }), javascriptInterfaceName)
    }

    private fun share() {
        val absShare = WxShare(this)
        absShare.init(APP_ID)
        absShare.shareWebPage()
    }

    private fun enterPay() {
        BKLog.d("点击支付,弹出支付框选项")
        val payProductId = intent.getStringExtra("pay_product_id")
        AlertDialog.Builder(this).setItems(arrayOf("微信", "支付宝")) { dialog, which ->

            var absPay: AbsPay? = null
            var payWay = ""
            when (which) {
                0 -> {
                    payWay = "weiXin"
                    absPay = WxPay(this)
                    absPay.init(APP_ID)
                    BKLog.d("点击了微信支付")
                }
                1 -> {
                    payWay = "alipay"
                    absPay = AliPay(this)
                    BKLog.d("点击了支付宝")
                }
            }
            PonkoApp.payApi?.createProductOrder(payWay, productId)?.enqueue(object : HttpCallBack<OrderCBean>() {
                override fun onSuccess(call: Call<OrderCBean>?, response: Response<OrderCBean>?) {
                    val orderCBean = response?.body()
                    orderCBean?.wechat

                    var order = ""
                    when (payWay) {
                        "weiXin" -> {
                            order = Gson().toJson(orderCBean?.wechat)
                        }
                        "alipay" -> {
                            order = orderCBean?.alipay!!
                        }
                    }

                    absPay?.pay(Channel.GENERAL, order, object : OnPayListener {
                        override fun onSuccess() {
                            BKLog.d("支付成功")
                        }

                        override fun onFailure() {
                            BKLog.d("支付失败")
                        }

                        override fun onCancel() {
                            BKLog.d("支付取消")
                        }
                    })
                    dialog.dismiss()
                }
            })
        }.show()
    }

    private fun enterExchange() {
        BKLog.d("点击积分兑换")
        //1 判断积分是否足够
        val totalScores = intent.getStringExtra("aggregate_score")
        val needScores = intent.getStringExtra("need_score")
        val exchangeProductId = intent.getStringExtra("exchange_product_id")
        if (totalScores.toInt() > needScores.toInt()) {
            //2 判断地址是否填写
            PonkoApp.myApi?.getAddress()?.enqueue(object : HttpCallBack<AddressBean>() {
                override fun onSuccess(call: Call<AddressBean>?, response: Response<AddressBean>?) {
                    var isCanExchange = true
                    val error = when {
                        TextUtils.isEmpty(response?.body()?.tel) -> {
                            exchangeViewHolder?.btnExchange?.isEnabled = false
                            isCanExchange = false
                            "电话号码为空"
                        }
                        TextUtils.isEmpty(response?.body()?.recipient) -> {
                            isCanExchange = false
                            "收货人为空"
                        }
                        TextUtils.isEmpty(response?.body()?.address) -> {
                            isCanExchange = false
                            "收货地址为空"
                        }
                        else -> {
                            "其他错误"
                        }
                    }

                    if (isCanExchange) {
                        exchangeViewHolder?.btnExchange?.isEnabled = true
                        PonkoApp.myApi?.exchangeProduct(exchangeProductId)?.enqueue(object : HttpCallBack<GeneralBean>() {
                            override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                                BKLog.d("兑换成功")
                            }
                        })
                    } else {
                        exchangeViewHolder?.btnExchange?.isEnabled = false
                        BKLog.d("兑换失败:$error")
                    }
                }
            })
        }
    }

    private fun loadUrl(linkValue: String): String {
        return if (linkValue.startsWith("http", true) || linkValue.startsWith("https", true)) {
            linkValue
        } else {
            "https://api.tradestudy.cn/v3$linkValue"
        }
    }

    private class ViewHolder private constructor(val toolbar: Toolbar, val process: ProgressBar, val web: WebView, val flBottomBtn: FrameLayout) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val toolbar = rootView.findViewById<View>(R.id.toolbar) as Toolbar
                val process = rootView.findViewById<View>(R.id.process) as ProgressBar
                val web = rootView.findViewById<View>(R.id.web) as WebView
                val flBottomBtn = rootView.findViewById<View>(R.id.fl_bottom_btn) as FrameLayout
                return ViewHolder(toolbar, process, web, flBottomBtn)
            }
        }
    }

    private class ExchangeViewHolder private constructor(val btnExchange: AppCompatButton) {
        companion object {

            fun create(rootView: View): ExchangeViewHolder {
                val btnExchange = rootView.findViewById<View>(R.id.btn_exchange) as AppCompatButton
                return ExchangeViewHolder(btnExchange)
            }
        }
    }

    private class PayViewHolder private constructor(val btnCourse: AppCompatButton, val btnIntroduce: AppCompatButton, val btnPay: AppCompatButton) {
        companion object {

            fun create(ll_pay: View): PayViewHolder {
                val btnCourse = ll_pay.findViewById<View>(R.id.btn_course) as AppCompatButton
                val btnIntroduce = ll_pay.findViewById<View>(R.id.btn_introduce) as AppCompatButton
                val btnPay = ll_pay.findViewById<View>(R.id.btn_pay) as AppCompatButton
                return PayViewHolder(btnCourse, btnIntroduce, btnPay)
            }
        }
    }

    class InJavaScriptLocalObj(val listener: OnInJavaScriptLocalObjListener?) {

        interface OnInJavaScriptLocalObjListener {
            fun onShare(t: String, des: String, l: String)
            fun onProductId(value: String)
            fun onPayGuide(have: String?, guideATitle: String?, guideAUrl: String, guideBTitle: String, guideBUrl: String, guideCTitle: String)
        }

        /**
         * @param t 分享标题
         * @param des 分享描述
         * @param link 分享地址
         */
        @JavascriptInterface
        fun getShare(t: String, des: String, l: String) {

            listener?.onShare(t, des, l)
        }

        /**
         * @param value 支付产品ID
         */
        @JavascriptInterface
        fun getValueById(value: String) {
            listener?.onProductId(value)
        }

        /**
         * @param have 1代表网页还有老师介绍选项 ，0 则没有
         */
        @JavascriptInterface
        fun getProductGuide(have: String?, guideATitle: String?, guideAUrl: String, guideBTitle: String, guideBUrl: String, guideCTitle: String) {
            listener?.onPayGuide(have, guideATitle, guideAUrl, guideBTitle, guideBUrl, guideCTitle)
        }
    }
}

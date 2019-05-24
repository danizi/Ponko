package com.ponko.cn

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.APP_ID
import com.ponko.cn.bean.AddressBean
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.bean.OrderCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.utils.CacheUtil.getToken
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.common.util.TimerHelper
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.XmPopWindow
import com.xm.lib.component.XmStateView
import com.xm.lib.pay.AbsPay
import com.xm.lib.pay.Channel
import com.xm.lib.pay.OnPayListener
import com.xm.lib.pay.PayConfig
import com.xm.lib.pay.ali.AliPay
import com.xm.lib.pay.wx.WxPay
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
import retrofit2.Call
import retrofit2.Response


/**
 * 分享 & 支付 & 普通网页 & 积分兑换(exchange)
 */
class WebAct : PonkoBaseAct<Any>() {
    companion object {
        private const val TAG = "WebAct"
        /**
         * 普通的跳转使用该方法
         * @param context     上下文对象
         * @param link_type   网页类型
         * @param link_value  网页操作值
         * @param title       标题
         */
        fun start(context: Context?, link_type: String?, link_value: String?, title: String? = "") {
            val intent = Intent(context, WebAct::class.java)
            intent.putExtra("title", title)
            intent.putExtra("link_type", link_type)
            intent.putExtra("link_value", link_value)
            context?.startActivity(intent)
        }

        /**
         * 积分兑换使用该方法
         * @param context     上下文对象
         * @param link_type   网页类型
         * @param link_value  网页操作值
         * @param title       标题
         * @param id          兑换产品id
         * @param needScore   需要的积分
         * @param needScore   用户总积分
         */
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

    private var viewHolder: ViewHolder? = null
    private var exchangeViewHolder: ExchangeViewHolder? = null
    private var payViewHolder: PayViewHolder? = null
    private var shareViewHolder: ShareViewHolder? = null

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

            //底部兑换积分
            val exchangeView = ViewUtil.viewById(this, R.layout.web_bottom_exchange)
            exchangeViewHolder = ExchangeViewHolder.create(exchangeView!!)

            //底部支付按钮，其中根据获取页面值，判断是否添加“老师介绍”、“老师课程”按钮
            val payView = ViewUtil.viewById(this, R.layout.web_bottom_pay)
            payViewHolder = PayViewHolder.create(this, payView!!)

            //分享底部弹出框
            val shareView = ViewUtil.viewById(this, R.layout.view_share)
            shareViewHolder = ShareViewHolder.create(shareView!!, this)
        }
    }

    override fun initDisplay() {
        super.initDisplay()
        viewHolder?.addBar(this, View.OnClickListener {
            BKLog.d("点击分享")
            shareViewHolder?.share()
        })
        viewHolder?.initWebView()
    }

    override fun iniEvent() {
        super.iniEvent()
        viewHolder?.initEvent()
        exchangeViewHolder?.initEvent() //监听兑换按钮
        payViewHolder?.initEvent()      //监听支付按钮
        shareViewHolder?.initEvent()    //监听分享按钮 PS：在顶部栏右边触发
    }

    override fun iniData() {
        super.iniData()
        viewHolder?.iniData(this, exchangeViewHolder, payViewHolder, shareViewHolder)
        viewHolder?.linkType = intent?.getStringExtra("link_type")!!
        viewHolder?.linkValue = intent?.getStringExtra("link_value")!!
        when (viewHolder?.linkType) {
            "exchange" -> {
                exchangeViewHolder?.totalScores = intent.getStringExtra("aggregate_score")
                exchangeViewHolder?.needScores = intent.getStringExtra("need_score")
                exchangeViewHolder?.exchangeProductId = intent.getStringExtra("exchange_product_id")
            }
            "pay" -> {
                try {
                    payViewHolder?.payProductId = intent.getStringExtra("pay_product_id")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            "url" -> {
                viewHolder?.flBottomBtn?.visibility = View.GONE
            }
        }
        //payViewHolder?.iniData(this)
        //shareViewHolder?.iniData(this)
    }

    /**
     * 窗口UI
     */
    private class ViewHolder private constructor(val toolbar: Toolbar, val process: ProgressBar, val web: WebView, val flBottomBtn: FrameLayout, val viewState: XmStateView) {
        companion object {

            const val javascriptInterfaceName = "local_obj"

            fun create(rootView: AppCompatActivity): ViewHolder {
                val toolbar = rootView.findViewById<View>(R.id.toolbar) as Toolbar
                val process = rootView.findViewById<View>(R.id.process) as ProgressBar
                val web = rootView.findViewById<View>(R.id.web) as WebView
                val flBottomBtn = rootView.findViewById<View>(R.id.fl_bottom_btn) as FrameLayout
                val viewState = rootView.findViewById<View>(R.id.view_state) as XmStateView
                return ViewHolder(toolbar, process, web, flBottomBtn, viewState)
            }
        }

        private var url: String = ""
        private var tvBarRight: TextView? = null
        private var tvTitle: TextView? = null
        private var act: Activity? = null
        private var title: String? = null
        var linkType: String = ""
        var linkValue: String = ""

        fun initEvent() {

        }

        @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
        fun initWebView() {
            //初始化webview
            val javascriptShare = ShareViewHolder.javascriptShare
            val javascriptPayProductid = PayViewHolder.javascriptPayProductid
            val javascriptPayGuide = PayViewHolder.javascriptPayGuide

            //
            val webView: WebView? = web
            val progressBar: ProgressBar? = process
            val webSettings = webView?.settings
            webSettings?.cacheMode = WebSettings.LOAD_DEFAULT //默认浏览器缓存
            webSettings?.javaScriptEnabled = true  //支持js
            webSettings?.useWideViewPort = true  //
            webView?.requestFocusFromTouch()//支持获取手势焦点，输入用户名、密码或其他
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

                    if (newProgress >= 100) {
                        progressBar?.visibility = View.GONE
                    }
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
                    BKLog.e(TAG, "onReceivedError error  $error")
                    super.onReceivedError(view, request, error)
                }

                override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                    handler.proceed()  // 接受所有网站的证书
                }
            }
        }

        fun iniData(act: Activity? = null, exchangeViewHolder: ExchangeViewHolder?, payViewHolder: PayViewHolder?, shareViewHolder: ShareViewHolder?) {
            if (act != null) {
                this.act = act
                //获取intent中携带的信息
                title = act.intent.getStringExtra("title")    //ps:可选项，如果web回调中能获取标题则直接获取
                if (!TextUtils.isEmpty(title)) {
                    tvTitle?.text = title
                }
                linkType = act.intent?.getStringExtra("link_type")!!
                linkValue = act.intent?.getStringExtra("link_value")!!

                //根据linkType底部展示不同的按钮
                webViewBittomDisplay(exchangeViewHolder, payViewHolder, shareViewHolder)

                //加载网页
                web.loadUrl(loadUrl(linkValue), heads())
                //注入java对象
                web.addJavascriptInterface(InJavaScriptLocalObj(object : InJavaScriptLocalObj.OnInJavaScriptLocalObjListener {
                    override fun onShare(t: String, des: String, l: String) {
                        act.runOnUiThread {
                            BKLog.d(TAG, "t:$t des:$des l:$l")
                            tvBarRight?.visibility = View.VISIBLE
                            shareViewHolder?.shareTitle = t
                            shareViewHolder?.shareDescription = des
                            shareViewHolder?.shareUrl = l
                        }
                    }

                    override fun onProductId(value: String) {
                        act.runOnUiThread {
                            BKLog.d(TAG, "productId:$value")
                            payViewHolder?.payProductId = value
                            tvBarRight?.visibility = View.GONE
                            payViewHolder?.btnPay?.visibility = View.VISIBLE
                        }
                    }

                    override fun onPayGuide(have: String?, guideATitle: String?/*课程表*/, guideAUrl: String, guideBTitle: String/*老师介绍*/, guideBUrl: String, guideCTitle: String/*支付按钮显示*/) {
                        act.runOnUiThread {
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
                                    payViewHolder?.btnPay?.text = guideCTitle
                                    payViewHolder?.btnCourse?.setOnClickListener {
                                        start(act, "url", guideAUrl, guideATitle)
                                    }
                                    payViewHolder?.btnIntroduce?.setOnClickListener {
                                        start(act, "url", guideBUrl, guideBTitle)
                                    }
                                }
                            }
                        }
                    }
                }), javascriptInterfaceName)
            }
        }

        private fun webViewBittomDisplay(exchangeViewHolder: ExchangeViewHolder?, payViewHolder: PayViewHolder?, shareViewHolder: ShareViewHolder?) {
            when (linkType) {
                "exchange" -> {
                    flBottomBtn.visibility = View.VISIBLE
                    flBottomBtn.addView(exchangeViewHolder?.rootView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                }
                "pay" -> {
                    flBottomBtn.visibility = View.VISIBLE
                    flBottomBtn.addView(payViewHolder?.rootView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                }
                "url" -> {
                    flBottomBtn.visibility = View.GONE
                }
            }
        }

        private fun heads(): Map<String, String> {
            val heads = HashMap<String, String>()
            heads["x-tradestudy-client-version"] = "3.4.6"
            heads["x-tradestudy-client-device"] = "android_phone"
            heads["x-tradestudy-access-key-id"] = "c"
            heads["x-tradestudy-access-token"] = getToken()!!
            return heads
        }

        private fun loadUrl(linkValue: String): String {
            return if (linkValue.startsWith("http", true) || linkValue.startsWith("https", true)) {
                linkValue
            } else {
                "https://api.tradestudy.cn/v3$linkValue"
            }
        }

        fun addBar(context: Context, rightListener: View.OnClickListener) {
            //添加顶部标题栏
            val barView = ViewUtil.viewById(context, R.layout.bar_2, null)
            toolbar.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            toolbar.visibility = View.VISIBLE
            val flBack: FrameLayout? = barView?.findViewById(R.id.fl_back) as FrameLayout
            tvTitle = barView.findViewById(R.id.tv_title) as TextView
            tvBarRight = barView.findViewById(R.id.tv_bar_right) as TextView
            flBack?.setOnClickListener {
                (context as Activity).finish()
            }
            tvBarRight?.setOnClickListener(rightListener)

            tvTitle?.text = title
            tvBarRight?.text = "分享"
            tvBarRight?.visibility = View.GONE
        }
    }

    /**
     * 积分兑换相关操作ViewHolder
     */
    private class ExchangeViewHolder private constructor(val rootView: View, val btnExchange: AppCompatButton) {
        companion object {
            private const val javascriptInterfaceName = "local_obj"

            fun create(rootView: View): ExchangeViewHolder {
                val btnExchange = rootView.findViewById<View>(R.id.btn_exchange) as AppCompatButton
                return ExchangeViewHolder(rootView, btnExchange)
            }
        }

        private var act: Activity? = null
        var totalScores = ""
        var needScores = ""
        var exchangeProductId = ""

        fun initEvent() {
            btnExchange.setOnClickListener {
                enterExchange()
            }
        }

//        fun iniData(act: Activity? = null) {
//            if (act != null) {
//                this.act = act
//                try {
//                    totalScores = act.intent.getStringExtra("aggregate_score")
//                    needScores = act.intent.getStringExtra("need_score")
//                    exchangeProductId = act.intent.getStringExtra("exchange_product_id")
//                }catch (e:Exception){
//                    e.printStackTrace()
//                }
//            }
//        }

        private fun enterExchange() {
            BKLog.d("点击积分兑换")
            //1 判断积分是否足够

            if (totalScores.toInt() > needScores.toInt()) {
                //2 判断地址是否填写
                PonkoApp.myApi?.getAddress()?.enqueue(object : HttpCallBack<AddressBean>() {
                    override fun onSuccess(call: Call<AddressBean>?, response: Response<AddressBean>?) {
                        var isCanExchange = true
                        val error = when {
                            TextUtils.isEmpty(response?.body()?.tel) -> {
                                btnExchange.isEnabled = false
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
                                ""
                            }
                        }

                        if (isCanExchange) {
                            btnExchange.isEnabled = true
                            AlertDialog.Builder(act!!)
                                    .setTitle("提示")
                                    .setMessage("亲爱的用户，是否兑换？")
                                    .setPositiveButton("确定") { dialog, which ->
                                        PonkoApp.myApi?.exchangeProduct(exchangeProductId)?.enqueue(object : HttpCallBack<GeneralBean>() {
                                            override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                                                BKLog.d("兑换成功")
                                            }
                                        })
                                    }
                                    .setNegativeButton("取消") { dialog, which -> dialog?.dismiss() }
                                    .create()
                                    .show()


                        } else {
                            btnExchange.isEnabled = false
                            AlertDialog.Builder(act!!)
                                    .setTitle("提示")
                                    .setMessage("亲爱的用户，您 $error ，请完善您的个人信息")
                                    .setPositiveButton("确定") { dialog, which ->
                                        BKLog.d("跳转到个人信息页面")
                                    }
                                    .setNegativeButton("取消") { dialog, which -> dialog?.dismiss() }
                                    .create()
                                    .show()
                            BKLog.d("兑换失败:$error")
                        }
                    }
                })
            }
        }

    }

    /**
     * 支付相关操作ViewHolder
     */
    private class PayViewHolder private constructor(var act: Activity?, val rootView: View, val btnCourse: AppCompatButton, val btnIntroduce: AppCompatButton, val btnPay: AppCompatButton) {
        companion object {
            private const val javascriptInterfaceName = "local_obj"
            val javascriptPayProductid = "javascript:window.$javascriptInterfaceName.getValueById(" +
                    "document.getElementById('productId').value" +
                    ");"
            const val javascriptPayGuide = "javascript:window.$javascriptInterfaceName.getProductGuide(" +
                    "document.getElementById('productGuideHave').value," +
                    "document.getElementById('productGuideATitle').value," +
                    "document.getElementById('productGuideAUrl').value," +
                    "document.getElementById('productGuideBTitle').value," +
                    "document.getElementById('productGuideBUrl').value," +
                    "document.getElementById('productGuideCTitle').value" +
                    ")"

            fun create(act: Activity?, ll_pay: View): PayViewHolder {
                val btnCourse = ll_pay.findViewById<View>(R.id.btn_course) as AppCompatButton
                val btnIntroduce = ll_pay.findViewById<View>(R.id.btn_introduce) as AppCompatButton
                val btnPay = ll_pay.findViewById<View>(R.id.btn_pay) as AppCompatButton
                return PayViewHolder(act, ll_pay, btnCourse, btnIntroduce, btnPay)
            }
        }

        var payProductId: String = ""

        fun initEvent() {
            btnPay.setOnClickListener {
                enterPay()
            }
        }

        private fun enterPay() {
            BKLog.d("点击支付,弹出支付框选项")
            AlertDialog.Builder(act!!).setItems(arrayOf("微信", "支付宝")) { dialog, which ->

                var absPay: AbsPay? = null
                var payWay = ""
                when (which) {
                    0 -> {
                        payWay = "weiXin"
                        absPay = WxPay(act!!)
                        absPay.init(PayConfig.Builder().appid(APP_ID).build())
                        BKLog.d("点击了微信支付")
                    }
                    1 -> {
                        payWay = "alipay"
                        absPay = AliPay(act!!)
                        BKLog.d("点击了支付宝")
                    }
                }
                PonkoApp.payApi?.createProductOrder(payWay, payProductId)?.enqueue(object : HttpCallBack<OrderCBean>() {
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

//        fun iniData(act: Activity? = null) {
//            if (act != null) {
//                this.act = act
//                if (TextUtils.isEmpty(payProductId)) {
//                    try {
//                        payProductId = act.intent.getStringExtra("pay_product_id")
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        }
    }

    /**
     * 分享相关操作ViewHolder
     */
    private class ShareViewHolder private constructor(val ctx: Context?, val rootView: View, val tvRemind: TextView, val flFriend: FrameLayout, val flFriendMoment: FrameLayout, val tvCancel: TextView, val popWindow: XmPopWindow) {
        companion object {

            private const val javascriptInterfaceName = "local_obj"
            const val javascriptShare = "javascript:window.$javascriptInterfaceName.getShare(" +
                    "document.getElementById('share_title').value," +
                    "document.getElementById('share_description').value," +
                    "document.getElementById('share_link').value" +
                    ");"

            fun create(rootView: View, context: Context?): ShareViewHolder {
                val tvRemind = rootView.findViewById<View>(R.id.tv_remind) as TextView
                val flFriend = rootView.findViewById<View>(R.id.fl_friend) as FrameLayout
                val flFriendMoment = rootView.findViewById<View>(R.id.fl_friend_moment) as FrameLayout
                val tvCancel = rootView.findViewById<View>(R.id.tv_cancel) as TextView
                val popWindow = XmPopWindow(context)
                popWindow.ini(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                //popWindow.showAtLocation(XmPopWindow.Location.BOTTOM, R.style.AnimationBottomFade, window.decorView, 0, 0)
                return ShareViewHolder(context, rootView, tvRemind, flFriend, flFriendMoment, tvCancel, popWindow)
            }
        }

        var shareTitle: String? = null
        var shareDescription: String? = null
        var shareUrl: String? = null
        var wxShare: WxShare? = WxShare(ctx as Activity)

        fun initEvent() {
            wxShare?.init(ShareConfig.Builder().appid(APP_ID).build())
            tvCancel.setOnClickListener {
                popWindow.dismiss()
            }
            flFriend.setOnClickListener {
                wxShare?.shareWebPage(R.mipmap.ic_launcher, shareUrl!!, shareTitle!!, shareDescription!!, SendMessageToWX.Req.WXSceneSession)
            }
            flFriendMoment.setOnClickListener {
                wxShare?.shareWebPage(R.mipmap.ic_launcher, shareUrl!!, shareTitle!!, shareDescription!!, SendMessageToWX.Req.WXSceneTimeline)
            }
        }

        fun share() {
            popWindow.showAtLocation(XmPopWindow.Location.BOTTOM, com.xm.lib.media.R.style.AnimationBottomFade, (ctx as Activity).window.decorView, 0, 0)
        }
    }

    /**
     * 注入到网页中的对象
     */
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

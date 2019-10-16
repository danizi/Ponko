package com.ponko.cn.module.web

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.APP_ID
import com.ponko.cn.bean.*
import com.ponko.cn.constant.Constants.BASE_API
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.common.pay.PayAct
import com.ponko.cn.module.common.share.ShareAct
import com.ponko.cn.module.my.option.InviteFriendRecordActivity
import com.ponko.cn.module.my.option.acount.AddressActivity
import com.ponko.cn.module.web.WebContract.V.PayViewHolder.Companion.javascriptPayGuide
import com.ponko.cn.module.web.WebContract.V.PayViewHolder.Companion.javascriptPayProductid
import com.ponko.cn.module.web.WebContract.V.ShareViewHolder.Companion.javascriptShare
import com.ponko.cn.utils.*
import com.ponko.cn.utils.CacheUtil.getToken
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.OnCancelListener
import com.xm.lib.component.OnEnterListener
import com.xm.lib.component.XmPopWindow
import com.xm.lib.component.XmStateView
import com.xm.lib.pay.AbsPay
import com.xm.lib.pay.Channel
import com.xm.lib.pay.OnPayListener
import com.xm.lib.pay.PayConfig
import com.xm.lib.pay.ali.AliPay
import com.xm.lib.pay.wx.WxPay
import com.xm.lib.share.AbsShare
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


/**
 * 分享 & 支付 & 普通网页 & 积分兑换(exchange)
 */
open class WebAct : PonkoBaseAct<WebContract.Present>(), WebContract.V {
    companion object {
        const val TAG = "WebAct"
        /**
         * 普通的跳转使用该方法
         * @param context     上下文对象
         * @param link_type   网页类型
         * @param link_value  网页操作值
         * @param title       标题
         */
        fun start(context: Context?, link_type: String?, link_value: String?, title: String? = "", lunMode: Int? = Intent.FLAG_ACTIVITY_SINGLE_TOP) {
            val intent = Intent(context, WebAct::class.java)
            intent.flags = lunMode!!
            intent.putExtra("title", title)
            intent.putExtra("link_type", link_type)
            intent.putExtra("link_value", link_value)
            intent.putExtra("from", link_value)
            context?.startActivity(intent)
        }

        fun startFromInvite(context: Context?, link_type: String?, link_value: String?, title: String? = "", lunMode: Int? = Intent.FLAG_ACTIVITY_SINGLE_TOP) {
            val intent = Intent(context, WebAct::class.java)
            intent.flags = lunMode!!
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
        fun startExChange(context: Context?, link_type: String?, link_value: String?, title: String?, id: String?, needScore: String, aggregateScore: String, total: Int?, isVirtualProduct: Boolean? = true) {
            val intent = Intent(context, WebAct::class.java)
            intent.putExtra("title", title)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("link_type", link_type)
            intent.putExtra("link_value", link_value)
            intent.putExtra("need_score", needScore)
            intent.putExtra("exchange_product_id", id)
            intent.putExtra("aggregate_score", aggregateScore)
            intent.putExtra("total", total)
            intent.putExtra("isVirtualProduct", isVirtualProduct)
            context?.startActivity(intent)
        }
    }

    override fun onDestroy() {
        if (mainUI?.web != null) {
            val parent = mainUI?.web?.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(mainUI?.web)
            }

            mainUI?.web?.stopLoading()
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mainUI?.web?.settings?.javaScriptEnabled = false;
            mainUI?.web?.clearHistory()
            mainUI?.web?.clearView()
            mainUI?.web?.removeAllViews()
            mainUI?.web?.destroy()
        }
        mainUI = null
        exchangeUI = null
        payUI = null
        shareUI = null
        super.onDestroy()
    }

    /**
     * 窗口UI
     */
    protected var mainUI: ViewHolder? = null
    /**
     * 积分兑换UI处理
     */
    private var exchangeUI: WebContract.V.ExchangeViewHolder? = null
    /**
     * 支付UI处理
     */
    private var payUI: WebContract.V.PayViewHolder? = null
    /**
     * 分享UI处理
     */
    private var shareUI: WebContract.V.ShareViewHolder? = null

    override fun presenter(): WebContract.Present {
        return WebContract.Present(context = this, v = this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun findViews() {
        super.findViews()
        if (mainUI == null) {
            mainUI = ViewHolder.create(this)

            //底部兑换积分
            val exchangeView = ViewUtil.viewById(this, R.layout.web_bottom_exchange)
            exchangeUI = WebContract.V.ExchangeViewHolder.create(this, exchangeView!!)

            //底部支付按钮，其中根据获取页面值，判断是否添加“老师介绍”、“老师课程”按钮
            val payView = ViewUtil.viewById(this, R.layout.web_bottom_pay)
            payUI = WebContract.V.PayViewHolder.create(this, payView!!)

            //分享底部弹出框
            val shareView = ViewUtil.viewById(this, R.layout.view_share)
            shareUI = WebContract.V.ShareViewHolder.create(shareView!!, this)
        }
    }

    override fun initDisplay() {
        super.initDisplay()
        mainUI?.addBar(this, View.OnClickListener {
            BKLog.d("点击分享")
            shareUI?.share()
        })
        mainUI?.initWebView()
    }

    override fun iniEvent() {
        super.iniEvent()
        mainUI?.initEvent()
        exchangeUI?.initEvent() //监听兑换按钮
        payUI?.initEvent()      //监听支付按钮
        shareUI?.initEvent()    //监听分享按钮 PS：在顶部栏右边触发
        mainUI?.web?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                //按返回键操作并且能回退网页
                if (keyCode == KeyEvent.KEYCODE_BACK && mainUI?.web?.canGoBack()!!) {
                    //后退
                    mainUI?.web?.goBack()
                    return@OnKeyListener true
                }
            }
            false
        })
    }

    override fun iniData() {
        super.iniData()
        mainUI?.iniData(this, exchangeUI, payUI, shareUI)
        mainUI?.linkType = intent?.getStringExtra("link_type")!!
        mainUI?.linkValue = intent?.getStringExtra("link_value")!!
        when (mainUI?.linkType) {
            "exchange" -> {
                exchangeUI?.title = intent.getStringExtra("title")
                exchangeUI?.totalScores = intent.getStringExtra("aggregate_score")
                exchangeUI?.total = intent.getIntExtra("total", 0)
                exchangeUI?.needScores = intent.getStringExtra("need_score")
                exchangeUI?.exchangeProductId = intent.getStringExtra("exchange_product_id")
                exchangeUI?.isVirtualProduct = intent.getBooleanExtra("isVirtualProduct", false)
            }
            "pay" -> {
                try {
                    payUI?.payProductId = intent.getStringExtra("pay_product_id")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            "url" -> {
                mainUI?.flBottomBtn?.visibility = View.GONE
                if (mainUI?.linkValue?.contains("invite", true)!!) {
                    mainUI?.addBar2(this, "邀请好友", "邀请记录", View.OnClickListener {
                        BKLog.d("点击邀请记录")
                        InviteFriendRecordActivity.start(this)
                    })
                }
            }
        }
        //payUI?.iniData(this)
        //shareUI?.iniData(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (mainUI?.androidObj?.link_type.equals("camera_album")) {
            CameraUtil2.onActivityResult(this, requestCode, resultCode, data, object : OnCameraListener {
                override fun onAlbum() {
                    DialogUtil.showProcess(this@WebAct)
                }

                override fun onCamera() {
                    DialogUtil.showProcess(this@WebAct)
                }
                override fun onResult(type: Int, filePath: String, bmp: Bitmap?) {
                    val json = JSONObject()
                    json.put("type", "type")
                    json.put("token", "token ")
                    val urlbase64=com.xm.lib.component.CommonUtil.bitmapToBase64(bmp)
                    json.put("value", urlbase64)
                    val j = json.toString()
                    BKLog.d("result:$j")
                    mainUI?.web?.loadUrl("javascript:phone_callback($j)")
                    DialogUtil.hideProcess()
                }
            })
        }
    }


    /**
     * 窗口UI
     */
    open class ViewHolder private constructor(val toolbar: Toolbar, val process: ProgressBar, val web: WebView, val flBottomBtn: FrameLayout, val viewState: XmStateView) {
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

        var androidObj: WebContract.M.AndroidObj? = null
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
            val javascriptShare = WebContract.V.ShareViewHolder.javascriptShare
            val javascriptPayProductid = WebContract.V.PayViewHolder.javascriptPayProductid
            val javascriptPayGuide = WebContract.V.PayViewHolder.javascriptPayGuide

            //
            val webView: WebView? = web
            val progressBar: ProgressBar? = process
            val webSettings = webView?.settings
            webSettings?.cacheMode = WebSettings.LOAD_DEFAULT //默认浏览器缓存
            webSettings?.javaScriptEnabled = true  //支持js
            webSettings?.useWideViewPort = true  //

            webSettings?.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            webSettings?.useWideViewPort = true
            webSettings?.loadWithOverviewMode = true
            webSettings?.setGeolocationEnabled(true)
            webSettings?.domStorageEnabled = false
            webView?.requestFocus()
            webView?.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY


            webView?.requestFocusFromTouch()//支持获取手势焦点，输入用户名、密码或其他
            webSettings?.blockNetworkImage = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            //监听
            initWebViewListener(webView, progressBar)
        }

        /**
         * WebView监听
         */
        private fun initWebViewListener(webView: WebView?, progressBar: ProgressBar?) {
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

                override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                    val builder = AlertDialog.Builder(act!!)
                    builder.setTitle("提示")
                    builder.setMessage(message)
                    val dlg = builder.create()
                    dlg.show()
                    result?.confirm()
                    //return super.onJsAlert(view, url, message, result)
                    return true
                }
            }
            webView?.webViewClient = object : WebViewClient() {

                @TargetApi(Build.VERSION_CODES.N)
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    view.loadUrl(request.url.toString(), heads(request.url.toString()))
                    return true
                }

                override fun onPageFinished(view: WebView, finishUrl: String) {
                    BKLog.d(TAG, "onPageFinished finished:$finishUrl")
                    view.loadUrl(javascriptShare)
                    view.loadUrl(javascriptPayProductid)
                    view.loadUrl(javascriptPayGuide)
                    super.onPageFinished(view, finishUrl)
                    progressBar?.visibility = View.GONE

                    if (finishUrl.startsWith("tel:")) {
                        web.loadUrl(webView?.url)
                        val callPhone = finishUrl.replace("//", "")
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(callPhone))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        act?.startActivity(intent)

                    }
                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    BKLog.e(TAG, "onReceivedError error  $error")
                    viewState.showError("加载失败", View.OnClickListener { })
                    super.onReceivedError(view, request, error)
                }

                override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                    handler.proceed()  // 接受所有网站的证书
                }
            }
        }

        /**
         * 设置数据
         */
        @SuppressLint("AddJavascriptInterface", "JavascriptInterface")
        fun iniData(act: Activity? = null, exchangeViewHolder: WebContract.V.ExchangeViewHolder?, payViewHolder: WebContract.V.PayViewHolder?, shareViewHolder: WebContract.V.ShareViewHolder?) {
            if (act != null) {
                this.act = act
                //获取intent中携带的信息
                title = act.intent.getStringExtra("title")    //ps:可选项，如果web回调中能获取标题则直接获取
                if (!TextUtils.isEmpty(title)) {
                    tvTitle?.text = title
                }
                linkType = act.intent?.getStringExtra("link_type")!!
                linkValue = act.intent?.getStringExtra("link_value")!!   //todo 有为null的分享
                //根据linkType底部展示不同的按钮
                webViewBottomDisplay(exchangeViewHolder, payViewHolder, shareViewHolder)
                //加载网页
                web.loadUrl(loadUrl(linkValue), heads(linkValue))
                //注入java对象
                web.addJavascriptInterface(WebContract.M.InJavaScriptLocalObj(object : WebContract.M.InJavaScriptLocalObj.OnInJavaScriptLocalObjListener {
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
                        if (PonkoApp.getLocalVersion(act) < 345) {
                            act.runOnUiThread {
                                BKLog.d(TAG, "productId:$value")
                                payViewHolder?.payProductId = value
                                tvBarRight?.visibility = View.GONE
                                payViewHolder?.btnPay?.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onPayGuide(have: String?, guideATitle: String?/*课程表*/, guideAUrl: String, guideBTitle: String/*老师介绍*/, guideBUrl: String, guideCTitle: String/*支付按钮显示*/) {
                        if (PonkoApp.getLocalVersion(act) < 345) {
                            act.runOnUiThread {
                                BKLog.d(TAG, "have:$have guideATitle:$guideATitle guideAUrl:$guideAUrl guideBTitle:$guideBTitle guideBUrl:$guideBUrl guideCTitle:$guideCTitle")
                                tvBarRight?.visibility = View.GONE
                                when (have) {
                                    "0" -> {
                                        //只显示支付按钮
                                        payViewHolder?.btnPay?.visibility = View.GONE
                                    }
                                    "1" -> {
                                        //显示课程信息课程介绍
                                        payViewHolder?.btnCourse?.visibility = View.VISIBLE
                                        payViewHolder?.btnIntroduce?.visibility = View.VISIBLE
                                        payViewHolder?.btnPay?.visibility = View.GONE
                                        payViewHolder?.btnPay?.text = guideCTitle
                                        payViewHolder?.btnCourse?.setOnClickListener {
                                            start(act, "url", guideAUrl, guideATitle, Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }
                                        payViewHolder?.btnIntroduce?.setOnClickListener {
                                            start(act, "url", guideBUrl, guideBTitle, Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }
                                        payViewHolder?.btnPay?.text = guideCTitle
                                    }
                                }
                            }
                        }
                    }
                }), javascriptInterfaceName)
                androidObj = WebContract.M.AndroidObj(act, web, null, object : WebContract.M.AndroidObj.JSCallback {
                    override fun onIsShowPayBtn(show: Boolean) {
                        if (show) {
                            payViewHolder?.btnPay?.visibility = View.GONE
                        } else {
                            payViewHolder?.btnPay?.visibility = View.VISIBLE
                        }
                    }
                })
                web.addJavascriptInterface(androidObj, WebContract.M.AndroidObj.javascriptInterfaceName)
            }
        }

        private fun webViewBottomDisplay(exchangeViewHolder: WebContract.V.ExchangeViewHolder?, payViewHolder: WebContract.V.PayViewHolder?, shareViewHolder: WebContract.V.ShareViewHolder?) {
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

        private fun heads(linkValue: String): Map<String, String> {
            val heads = HashMap<String, String>()
            if (loadUrl(linkValue).startsWith(BASE_API, true)) {
                heads["x-tradestudy-client-version"] = PonkoApp.getLocalVersion2(act!!)
                heads["x-tradestudy-client-device"] = "android_phone"
                heads["x-tradestudy-access-key-id"] = "c"
                heads["x-tradestudy-access-token"] = getToken()!!
                BKLog.e(TAG, "网页添加请求头:")
            } else {
                BKLog.e(TAG, "非法地址$linkValue")
            }
            return heads
        }

        private fun loadUrl(linkValue: String): String {
            return when {
                linkValue.startsWith(BASE_API, true) -> linkValue
                linkValue.startsWith("/") -> "$BASE_API$linkValue"
                else -> linkValue
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

        fun addBar2(context: Context?, title: String, barRight: String, rightListener: View.OnClickListener) {
            if (toolbar.childCount > 0) {
                toolbar.removeAllViews()
            }
            val barView = ViewUtil.viewById(context, R.layout.bar_2, null)
            toolbar.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            toolbar.visibility = View.VISIBLE
            val flBack: FrameLayout? = barView?.findViewById(R.id.fl_back) as FrameLayout
            val tvTitle: TextView? = barView.findViewById(R.id.tv_title) as TextView
            val tvBarRight: TextView? = barView.findViewById(R.id.tv_bar_right) as TextView
            flBack?.setOnClickListener {
                (context as Activity).finish()
            }
            tvBarRight?.setOnClickListener(rightListener)
            tvTitle?.text = title
            tvBarRight?.text = barRight
        }
    }
}

/**
 * 契约类
 */
class WebContract {
    /**
     * 视图层
     */
    interface V {
        /**
         * 积分兑换相关操作ViewHolder
         */
        class ExchangeViewHolder private constructor(val act: Activity, val rootView: View, val btnExchange: AppCompatButton) {
            companion object {
                private const val javascriptInterfaceName = "local_obj"

                fun create(act: Activity, rootView: View): ExchangeViewHolder {
                    val btnExchange = rootView.findViewById<View>(R.id.btn_exchange) as AppCompatButton
                    return ExchangeViewHolder(act, rootView, btnExchange)
                }
            }

            var title = ""
            var total = 0
            var totalScores = ""
            var needScores = ""
            var exchangeProductId = ""
            var isVirtualProduct: Boolean = true

            fun initEvent() {
                if (!TextUtils.isEmpty(totalScores) && !TextUtils.isEmpty(needScores)) {
                    if (totalScores.toInt() < needScores.toInt()) {
                        btnExchange.isEnabled = false
                        btnExchange.text = "您的积分不足"
                    }
                }
                if (total == 0) {
                    btnExchange.isEnabled = false
                    btnExchange.text = "商品不足"
                }
                btnExchange.setOnClickListener {
                    if (isVirtualProduct) {
                        //虚拟商品兑换
                        virtualProductExchange()
                    } else {
                        //实物商品兑换
                        enterExchange()
                    }
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

            private fun virtualProductExchange() {
                if (totalScores.toInt() > needScores.toInt()) {
                    DialogUtil.show(act, "提示", "当前《${title}》课程，所需${needScores}积分，是否兑换？", false, object : OnEnterListener {
                        override fun onEnter(dlg: AlertDialog) {
                            PonkoApp.myApi?.exchangeProduct(exchangeProductId)?.enqueue(object : HttpCallBack<GeneralBean>() {
                                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                                    //ToastUtil.show("兑换成功")
                                    val message = response?.body()?.message
                                    if (TextUtils.isEmpty(message)) {
                                        DialogUtil.show(act, "", "兑换成功", true, null, null)
                                    } else {
                                        DialogUtil.show(act, "", message!!, true, null, null)
                                    }
                                    dlg.dismiss()
                                }

                                override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                                    super.onFailure(call, msg)
                                    //ToastUtil.show("兑换失败")
                                    var errorMsg = "兑换失败"
                                    errorMsg = if (TextUtils.isEmpty(exchangeProductId)) {
                                        "兑换失败,兑换产品id为空！"
                                    } else {
                                        "兑换失败,$msg"
                                    }
                                    DialogUtil.show(act, "", errorMsg, true, null, null)
                                    dlg.dismiss()
                                }
                            })
                        }
                    }, object : OnCancelListener {
                        override fun onCancel(dlg: AlertDialog) {
                            dlg.dismiss()
                        }

                    })
                }
            }

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

                            //兑换
                            if (isCanExchange) {
                                DialogUtil.showProcess(act)
                                btnExchange.isEnabled = true
                                BKLog.d("exchangeProductId: $exchangeProductId")
                                //请求接口地址
                                PonkoApp.myApi?.getAddress()?.enqueue(object : HttpCallBack<AddressBean>() {
                                    override fun onSuccess(call: Call<AddressBean>?, response: Response<AddressBean>?) {
                                        val d = response?.body()
                                        val sb = StringBuilder()
                                                .append("当前《${title}》课程，所需${needScores}积分。\n")
                                                .append("收件人地址 ：${d?.address}\n")
                                                .append("收件人电话 ：${d?.tel}\n")
                                                .append("收件人姓名 ：${d?.recipient}\n")
                                                .append("亲爱的用户，是否兑换？")
                                        DialogUtil.hideProcess()
                                        AlertDialog.Builder(act)
                                                .setTitle("提示")
                                                .setMessage(sb.toString())
                                                .setPositiveButton("确定") { dialog, which ->
                                                    PonkoApp.myApi?.exchangeProduct(exchangeProductId)?.enqueue(object : HttpCallBack<GeneralBean>() {
                                                        override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                                                            //ToastUtil.show("兑换成功")
                                                            val message = response?.body()?.message
                                                            if (TextUtils.isEmpty(message)) {
                                                                DialogUtil.show(act, "", "兑换成功", true, null, null)
                                                            } else {
                                                                DialogUtil.show(act, "", message!!, true, null, null)
                                                            }
                                                        }

                                                        override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                                                            super.onFailure(call, msg)
                                                            //ToastUtil.show("兑换失败")
                                                            var errorMsg = "兑换失败"
                                                            errorMsg = if (TextUtils.isEmpty(exchangeProductId)) {
                                                                "兑换失败,兑换产品id为空！"
                                                            } else {
                                                                "兑换失败,$msg"
                                                            }
                                                            DialogUtil.show(act, "", errorMsg, true, null, null)
                                                        }
                                                    })
                                                }
                                                .setNegativeButton("取消") { dialog, which -> dialog?.dismiss() }
                                                .setNeutralButton("修改地址") { dialog, which ->
                                                    ActivityUtil.startActivity(act, Intent(act, AddressActivity::class.java))
                                                }
                                                .create()
                                                .show()
                                    }
                                })

                            } else {
                                //btnExchange.isEnabled = false
                                //btnExchange.text = error
                                AlertDialog.Builder(act)
                                        .setTitle("提示")
                                        .setMessage("亲爱的用户，您$error，请完善您的个人信息。")
                                        .setPositiveButton("确定") { dialog, which ->
                                            BKLog.d("跳转到个人信息页面")
                                            //act.finish()
                                            ActivityUtil.startActivity(act, Intent(act, AddressActivity::class.java))
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
        class PayViewHolder private constructor(var act: Activity?, val rootView: View, val btnCourse: AppCompatButton, val btnIntroduce: AppCompatButton, val btnPay: AppCompatButton) {
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
                btnCourse.setOnClickListener {
                    BKLog.d("点击课程")

                }
                btnIntroduce.setOnClickListener {
                    BKLog.d("点击介绍")
                }
                btnPay.setOnClickListener {
                    BKLog.d("点击支付")
                    enterPay()
                }
            }

            private fun enterPay() {
                BKLog.d("点击支付,弹出支付框选项")
                if (CacheUtil.isUserTypeLogin()) {
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
                        DialogUtil.showProcess(act!!)
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
                                        if (CacheUtil.getStudyUI() == "2"/*1 旧版 2 新版学习Fragment*/) {
                                            DialogUtil.show(act!!, "提示", PonkoApp.main2CBean?.tips?.pay_success!!, true, null, null)
                                            DialogUtil.hideProcess()
                                        }
                                    }

                                    override fun onFailure() {
                                        BKLog.d("支付失败")
                                        ToastUtil.show("支付失败，请重新支付。")
                                        DialogUtil.hideProcess()
                                    }

                                    override fun onCancel() {
                                        BKLog.d("支付取消")
                                        DialogUtil.hideProcess()
                                    }
                                })
                                dialog.dismiss()
                            }

                            override fun onFailure(call: Call<OrderCBean>?, msg: String?) {
                                super.onFailure(call, msg)
                                DialogUtil.hideProcess()
                                ToastUtil.show("请求支付订单失败，请重新支付。")
                            }
                        })
                    }.show()
                } else {
                    ToastUtil.show("亲，请先登录账号....")
                }
            }
        }

        /**
         * 分享相关操作ViewHolder
         */
        class ShareViewHolder private constructor(val ctx: Context?, val rootView: View, val tvRemind: TextView, val flFriend: FrameLayout, val flFriendMoment: FrameLayout, val tvCancel: TextView, val popWindow: XmPopWindow) {
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
    }

    /**
     * 数据层
     */
    class M {
        /**
         * 注入到网页中的对象
         */
        open class InJavaScriptLocalObj(private val listener: OnInJavaScriptLocalObjListener?) {

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

        /**
         * 一个总的类进行处理
         */
        class AndroidObj(private val act: Activity?, private val webView: WebView?, private val listener: OnInJavaScriptLocalObjListener?, private val jsCallback: JSCallback?) : InJavaScriptLocalObj(listener) {

            companion object {
                private const val TAG = "AndroidObj"
                const val javascriptInterfaceName = "android"
            }

            /**
             * 支付
             */
            private var payObj: AndroidPayObj? = null
            /**
             * 分享
             */
            private var shareObj: AndroidShareObj? = null
            /**
             * 跳转
             */
            private var gotoObj: AndroidGotoObj? = null
            var link_type: String = ""
            var link_value: String = ""

            init {
                payObj = AndroidPayObj(act, webView)
                shareObj = AndroidShareObj(act, webView)
                gotoObj = AndroidGotoObj(act, webView)
            }

            @JavascriptInterface
            fun action(json: String?) {
                BKLog.d("JSON:$json")
                val linkBean = Gson().fromJson(json, LinkBean::class.java)
                link_type = linkBean.type

                val jArray = JSONArray()
                jArray.put(linkBean.link)
                if (jArray.length() > 0) {
                    link_value = Gson().toJson(linkBean.link)
                } else {
                    link_value = linkBean.link.toString()
                }
                when (link_type) {
                    "JS_PAY" -> {
                        pay(link_type, link_value)
                    }
                    "JS_SHARE" -> {
                        share(link_type, linkBean.link as Map<String, String>)
                    }
                    "JS_EXCHANGE" -> {
                        excahnge(link_type, link_value)
                    }
                    "JS_CALL" -> {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$link_value"))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        act?.startActivity(intent)
                    }
                    "back" -> {
                        act?.finish()
                    }
                    "mp" -> {
                        val intent = Intent(act, ShareAct::class.java)
                        // 解析数据json串{"type": "type", "value": "slfjsdlkfjsldkf"}
                        val jsonObject = JSONObject(link_value)
                        val type = jsonObject.getString("type")
                        val id = jsonObject.getString("value")
                        intent.putExtra("type", type)
                        intent.putExtra("id", id)
                        ActivityUtil.startActivity(act, intent)
                    }
                    else -> {
                        goto(link_type, link_value)
                    }
                }
            }

            private fun pay(link_type: String?, link_value: String?) {
                if (!TextUtils.isEmpty(link_value)) {
                    payObj?.pay(link_type!!, link_value!!)
                } else {
                    BKLog.e(TAG, "pay link_value is null")
                }
            }

            private fun share(link_type: String?, link_value: String?) {
                if (!TextUtils.isEmpty(link_value)) {
                    val shareBean = Gson().fromJson(link_value, ShareBean::class.java)
                    shareObj?.share(link_type!!, shareBean.title, shareBean.desc, shareBean.url)
                } else {
                    BKLog.e(TAG, "share link_value is null")
                }
            }

            private fun share(link_type: String?, link_value: Map<String, String>?) {
                if (link_value?.isEmpty()!!) {
                    BKLog.e(TAG, "share link_value is null")
                } else {
                    shareObj?.share(link_type!!, link_value.get("title")!!, link_value.get("desc")!!, link_value.get("url")!!)
                }
            }

            private fun goto(link_type: String?, link_value: String?) {
                gotoObj?.goto(link_type, link_value!!)
//                if (!TextUtils.isEmpty(link_value)) {
//                    gotoObj?.goto(link_type, link_value!!)
//                } else {
//                    BKLog.e(TAG, "link_value is null")
//                }
            }

            private fun excahnge(link_type: String?, link_value: String?) {
                BKLog.d("点击积分兑换")
                val exchangeProductId: String? = link_value
//                //1 判断积分是否足够
//                if (totalScores.toInt() > needScores.toInt()) {
                //2 判断地址是否填写
                PonkoApp.myApi?.getAddress()?.enqueue(object : HttpCallBack<AddressBean>() {
                    override fun onSuccess(call: Call<AddressBean>?, response: Response<AddressBean>?) {
                        var isCanExchange = true
                        val error = when {
                            TextUtils.isEmpty(response?.body()?.tel) -> {
//                                    btnExchange.isEnabled = false
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

                        //兑换
                        if (isCanExchange) {
                            DialogUtil.showProcess(act!!)
//                                btnExchange.isEnabled = true
                            BKLog.d("exchangeProductId: $exchangeProductId")
                            //请求接口地址
                            PonkoApp.myApi?.getAddress()?.enqueue(object : HttpCallBack<AddressBean>() {
                                override fun onSuccess(call: Call<AddressBean>?, response: Response<AddressBean>?) {
                                    val d = response?.body()
                                    val sb = StringBuilder()
                                            .append("收件人地址 ：${d?.address}\n")
                                            .append("收件人电话 ：${d?.tel}\n")
                                            .append("收件人姓名 ：${d?.recipient}\n")
                                            .append("亲爱的用户，是否兑换？")
                                    DialogUtil.hideProcess()
                                    AlertDialog.Builder(act)
                                            .setTitle("提示")
                                            .setMessage(sb.toString())
                                            .setPositiveButton("确定") { dialog, which ->
                                                PonkoApp.myApi?.exchangeProduct(exchangeProductId!!)?.enqueue(object : HttpCallBack<GeneralBean>() {
                                                    override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                                                        //ToastUtil.show("兑换成功")
                                                        DialogUtil.show(act, "", "兑换成功", true, null, null)
                                                    }

                                                    override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                                                        super.onFailure(call, msg)
                                                        //ToastUtil.show("兑换失败")
                                                        DialogUtil.show(act, "", "兑换失败", true, null, null)
                                                    }
                                                })
                                            }
                                            .setNegativeButton("取消") { dialog, which -> dialog?.dismiss() }
                                            .create()
                                            .show()
                                }
                            })

                        } else {
                            //btnExchange.isEnabled = false
                            //btnExchange.text = error
                            AlertDialog.Builder(act!!)
                                    .setTitle("提示")
                                    .setMessage("亲爱的用户，您$error，请完善您的个人信息。")
                                    .setPositiveButton("确定") { dialog, which ->
                                        BKLog.d("跳转到个人信息页面")
                                        //act.finish()
                                        ActivityUtil.startActivity(act, Intent(act, AddressActivity::class.java))
                                    }
                                    .setNegativeButton("取消") { dialog, which -> dialog?.dismiss() }
                                    .create()
                                    .show()
                            BKLog.d("兑换失败:$error")
                        }
                    }
                })
//                }
            }

            fun showAndroidPayButton(show: Boolean) {
                jsCallback?.onIsShowPayBtn(show)
            }

            /*===============================*
             * 回调监听
             *===============================*/
            interface JSCallback {
                fun onIsShowPayBtn(show: Boolean)
            }
        }

        @Deprecated("统一使用AndroidObj")
        class AndroidPayObj(private val act: Activity?, private val webView: WebView?) {
            private var aliPay: AbsPay? = null
            private var wxPay: AbsPay? = null

            init {
                if (act != null) {
                    aliPay = AliPay(act)
                    wxPay = WxPay(act)
                    wxPay?.init(PayConfig.Builder().build())
                }
            }

            /**
             * 支付
             * @param type      weiXin / alipay 过时了
             * @param productId 产品id
             */
            @JavascriptInterface
            fun pay(payWay: String, productId: String) {
                if (CacheUtil.isUserTypeLogin() || true) {
                    if (act == null) {
                        BKLog.e("支付失败，activity is null")
                        return
                    }

                    if (TextUtils.isEmpty(productId)) {
                        BKLog.e("支付失败，productId is null")
                        return
                    }

                    PayAct.star(act, productId)

//                    AlertDialog.Builder(act!!).setItems(arrayOf("微信", "支付宝")) { dialog, which ->
//
//                        var absPay: AbsPay? = null
//                        var payWay = payWay
//                        when (which) {
//                            0 -> {
//                                payWay = "weiXin"
//                                absPay = WxPay(act)
//                                absPay.init(PayConfig.Builder().appid(APP_ID).build())
//                                BKLog.d("点击了微信支付")
//                            }
//                            1 -> {
//                                payWay = "alipay"
//                                absPay = AliPay(act)
//                                BKLog.d("点击了支付宝")
//                            }
//                        }
//                        PonkoApp.payApi?.createProductOrder(payWay, productId)?.enqueue(object : HttpCallBack<OrderCBean>() {
//                            override fun onSuccess(call: Call<OrderCBean>?, response: Response<OrderCBean>?) {
//                                val orderCBean = response?.body()
//                                orderCBean?.wechat
//
//                                var order = ""
//                                when (payWay) {
//                                    "weiXin" -> {
//                                        order = Gson().toJson(orderCBean?.wechat)
//                                    }
//                                    "alipay" -> {
//                                        order = orderCBean?.alipay!!
//                                    }
//                                }
//
//                                absPay?.pay(Channel.GENERAL, order, object : OnPayListener {
//                                    override fun onSuccess() {
//                                        BKLog.d("支付成功")
//                                        if (CacheUtil.getStudyUI() == "2"/*新版本*/ && !webView?.url?.contains("web/agent/info")!!) {
//                                            DialogUtil.show(act, "提示", PonkoApp.main2CBean?.tips?.pay_success!!, false, object : OnEnterListener {
//                                                override fun onEnter(dlg: AlertDialog) {
//                                                    dlg.dismiss()
//                                                }
//                                            }, null)
//                                        }
//                                        act.runOnUiThread {
//                                            webView?.loadUrl("javascript:callback_pay_success()")
//                                        }
//                                    }
//
//                                    override fun onFailure() {
//                                        act.runOnUiThread {
//                                            webView?.loadUrl("javascript:callback_pay_failed()")
//                                        }
//                                        BKLog.d("支付失败")
//                                    }
//
//                                    override fun onCancel() {
//                                        act.runOnUiThread {
//                                            webView?.loadUrl("javascript:callback_pay_canceled()")
//                                        }
//                                        BKLog.d("支付取消")
//                                    }
//                                })
//                                dialog.dismiss()
//                            }
//                        })
//                    }.show()
                } else {
                    ToastUtil.show("亲，请先登录账号....")
                }
            }
        }

        @Deprecated("统一使用AndroidObj")
        class AndroidShareObj(private val act: Activity?, web: WebView?) {

            private var share: AbsShare? = null

            init {
                share = WxShare(act!!)
                share?.init(ShareConfig.Builder().build())
            }

            /**
             * 分享
             * @param type friendCircle朋友圈,friend朋友
             * @param shareTitle
             * @param shareDescription
             * @param shareUrl
             */
            @JavascriptInterface
            fun share(type: String, shareTitle: String, shareDescription: String, shareUrl: String) {
                DialogUtil.showShare(act, shareUrl, shareTitle, shareDescription)
            }
        }

        @Deprecated("统一使用AndroidObj")
        class AndroidGotoObj(val context: Context?, web: WebView?) {
            /**
             * 跳转
             */
            @JavascriptInterface
            fun goto(link_type: String?, link_value: String?) {
                IntoTargetUtil.target(context, link_type, link_value)
            }
        }
    }

    /**
     * 数据控制层
     */
    class Present(context: Context?, v: V?) {}
}

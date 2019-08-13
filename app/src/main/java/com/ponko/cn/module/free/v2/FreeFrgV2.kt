package com.ponko.cn.module.free.v2

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.module.web.WebAct
import com.ponko.cn.module.web.WebContract
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.constant.Constants
import com.ponko.cn.constant.Constants.URL_FREE
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.DialogUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.http.NetworkUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.component.XmStateView

/**
 * 免费模块页面 - V2
 */
class FreeFrgV2 : MvpFragment<Any>() {
    /**
     * 窗口UI
     */
    private var ui: UI? = null

    override fun presenter(): Any {
        return Any()
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_free_v2
    }

    override fun findViews(view: View) {
        ui = UI.create(view)

    }

    override fun initDisplay() {
        ui?.addBar(context!!, View.OnClickListener { })
        ui?.initWebView(context as Activity)
    }

    override fun iniEvent() {

    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ui?.web?.onPause()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ui?.web?.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ui?.web?.resumeTimers()
        ui?.web?.destroy()
    }

    override fun iniData() {
        ui?.initWebView(context as Activity)
        ui?.iniData(context as Activity, null, null, null)
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ui?.web?.onPause()
            }
            ui?.web?.loadUrl("javascript:view_did_disappear()")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ui?.web?.onResume()
            }
        }
    }

    fun keyBack() {
        ui?.keyBack()
    }

    fun isLandscepe():Boolean{
        return ScreenUtil.isLandscape(context!!)
    }

    /**
     * 窗口UI
     */
    private class UI private constructor(val toolbar: Toolbar, val process: ProgressBar, val web: WebView, val flBottomBtn: FrameLayout, val viewState: XmStateView, val smartRefreshLayout: SmartRefreshLayout) {
        companion object {

            const val javascriptInterfaceName = "local_obj"

            fun create(rootView: View): UI {
                val toolbar = rootView.findViewById<View>(R.id.toolbar) as Toolbar
                val process = rootView.findViewById<View>(R.id.process) as ProgressBar
                val web = rootView.findViewById<View>(R.id.web) as WebView
                val flBottomBtn = rootView.findViewById<View>(R.id.fl_bottom_btn) as FrameLayout
                val viewState = rootView.findViewById<View>(R.id.view_state) as XmStateView
                val smartRefreshLayout = rootView.findViewById<View>(R.id.srl) as SmartRefreshLayout

                return UI(toolbar, process, web, flBottomBtn, viewState, smartRefreshLayout)
            }
        }

        private var tvBarRight: TextView? = null
        private var tvTitle: TextView? = null
        private var act: Activity? = null
        private var title: String? = null
        var linkType: String = ""
        var linkValue: String = ""

        fun initEvent() {

        }

        @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
        fun initWebView(act: Activity?) {
            //初始化webview
            val webView: WebView? = web
            val progressBar: ProgressBar? = process
            val webSettings = webView?.settings
            webSettings?.cacheMode = WebSettings.LOAD_DEFAULT //默认浏览器缓存
            webSettings?.javaScriptEnabled = true  //支持js
            webSettings?.useWideViewPort = true  //
            webView?.requestFocusFromTouch()//支持获取手势焦点，输入用户名、密码或其他
            webSettings?.useWideViewPort = true // 关键点
            webSettings?.allowFileAccess = true // 允许访问文件
            webSettings?.setSupportZoom(true) // 支持缩放
            webSettings?.loadWithOverviewMode = true
            webSettings?.cacheMode = WebSettings.LOAD_NO_CACHE // 不加载缓存内容
            webSettings?.blockNetworkImage = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            smartRefreshLayout.setOnRefreshListener {
                //web.reload()
                web.loadUrl(loadUrl(linkValue), heads(linkValue))
            }

            //监听
            initWebViewListener(act, webView, progressBar)
        }


        var customView: View? = null
        var fullscreenContainer: FullscreenHolder? = null
        lateinit var customViewCallback: WebChromeClient.CustomViewCallback

        /**
         * WebView监听
         */
        private fun initWebViewListener(act: Activity?, webView: WebView?, progressBar: ProgressBar?) {
            webView?.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    handleProgressBar(newProgress)
                    super.onProgressChanged(view, newProgress)
                }

                private fun handleProgressBar(newProgress: Int) {
                    BKLog.i(WebAct.TAG, "newProgress == >  $newProgress")
                    if (newProgress == 0)
                        progressBar?.visibility = View.VISIBLE

                    progressBar?.progress = newProgress

                    if (newProgress >= 100) {
                        progressBar?.visibility = View.GONE
                    }
                }

                override fun onReceivedTitle(view: WebView, title: String) {
                    if (!TextUtils.isEmpty(title)) {
                        BKLog.d(WebAct.TAG, "onReceivedTitle$title")
                        //setTitle(title)
                        if (title.startsWith("http") || title.startsWith("https")) else {
                            if (title != "找不到网页") {
                                tvTitle?.text = title
                            } else {
                                tvTitle?.text = ""
                            }
                        }
                    }
                    super.onReceivedTitle(view, title)
                }

                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    super.onShowCustomView(view, callback)
                    showCustomView(view, callback)
                }

                override fun onHideCustomView() {
                    super.onHideCustomView()
                    hideCustomView()
                }
            }

            webView?.webViewClient = object : WebViewClient() {
                @TargetApi(Build.VERSION_CODES.N)
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    view.loadUrl(request.url.toString())
                    return true
                }

                override fun onPageFinished(view: WebView, finishUrl: String) {
                    BKLog.d(WebAct.TAG, "onPageFinished finished:$finishUrl")
                    view.loadUrl(WebContract.V.ShareViewHolder.javascriptShare)
                    view.loadUrl(WebContract.V.PayViewHolder.javascriptPayProductid)
                    view.loadUrl(WebContract.V.PayViewHolder.javascriptPayGuide)


                    super.onPageFinished(view, finishUrl)
                    progressBar?.visibility = View.GONE

                    if (!NetworkUtil.isNetworkConnected(web.context)) {
                        viewState.showError("加载失败", View.OnClickListener { web.reload() })
                    } else {
                        viewState.hide()
                        smartRefreshLayout.finishRefresh(0)
                    }
                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    BKLog.e(WebAct.TAG, "onReceivedError error  $error")
                    viewState.showError("加载失败", View.OnClickListener { web.reload() })
                    super.onReceivedError(view, request, error)
                }

                override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                    handler.proceed()  // 接受所有网站的证书
                }


            }
        }

        fun keyBack() {
            /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
            if (customView != null) {
                hideCustomView()
            } else if (web.canGoBack()) {
                web.goBack()
            }
        }

        private fun setStatusBarVisibility(visible: Boolean) {
            val flag = if (visible) 0 else WindowManager.LayoutParams.FLAG_FULLSCREEN
            act?.window?.setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        /**
         * 全屏
         */
        private fun showCustomView(view: View?, callback: WebChromeClient.CustomViewCallback?) {
            val COVER_SCREEN_PARAMS = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            //if a view already exists then immediately terminate the new one
            if (customView != null) {
                callback?.onCustomViewHidden()
                return
            }
            ScreenUtil.setLandscape(act)
            //act?.getWindow()?.getDecorView()

            val decor = act?.window?.decorView as FrameLayout
            fullscreenContainer = FullscreenHolder(act!!, View.OnClickListener {
                hideCustomView()
            })
            fullscreenContainer?.addView(view, COVER_SCREEN_PARAMS)
            decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS)
            customView = view
            setStatusBarVisibility(false)
            customViewCallback = callback!!

            fullscreenContainer?.getChildAt(0)?.bringToFront()
        }

        /**
         * 小窗口
         */
        private fun hideCustomView() {
            if (customView == null) {
                return
            }
            ScreenUtil.setPortrait(act)
            setStatusBarVisibility(true)
            val decor = act?.window?.decorView as FrameLayout
            decor.removeView(fullscreenContainer)
            fullscreenContainer = null
            customView = null
            customViewCallback.onCustomViewHidden()
            web.visibility = View.VISIBLE
        }

        /** 全屏容器界面  */
        @SuppressLint("InflateParams", "ViewConstructor")
        class FullscreenHolder(ctx: Context, val listener: View.OnClickListener) : FrameLayout(ctx) {
            var back: AppCompatImageButton? = null


            init {
                setBackgroundColor(ctx.resources.getColor(android.R.color.black))
                val view = LayoutInflater.from(ctx).inflate(R.layout.attachment_control_landscape, null)
                addView(view)
                view.findViewById<ConstraintLayout>(R.id.cl_landscape_bottom).visibility = View.GONE
                view.findViewById<TextView>(R.id.tv_title).visibility = View.GONE
                view.findViewById<AppCompatImageButton>(R.id.iv_more).visibility = View.GONE
                back = view.findViewById<AppCompatImageButton>(R.id.iv_back)
            }

            override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
                BKLog.d("")
                if ((0 < ev?.rawX!! && ev.rawX < 140) && (0 < ev.rawY && ev.rawY < 140)) {
                    listener.onClick(back)
                }
                return super.dispatchTouchEvent(ev)
            }

            fun backToFront() {
                back?.bringToFront()
            }
        }

        /**
         * 设置数据
         */
        @SuppressLint("AddJavascriptInterface")
        fun iniData(act: Activity? = null, exchangeViewHolder: WebContract.V.ExchangeViewHolder?, payViewHolder: WebContract.V.PayViewHolder?, shareViewHolder: WebContract.V.ShareViewHolder?) {
            if (act != null) {
                this.act = act
                //获取intent中携带的信息
                title = act.intent.getStringExtra("title")    //ps:可选项，如果web回调中能获取标题则直接获取
                if (!TextUtils.isEmpty(title)) {
                    tvTitle?.text = title
                }
//                linkType = act.intent?.getStringExtra("link_type")!!
//                linkValue = act.intent?.getStringExtra("link_value")!!   //todo 有为null的分享

                linkValue = URL_FREE
                //根据linkType底部展示不同的按钮
                webViewBottomDisplay(exchangeViewHolder, payViewHolder, shareViewHolder)
                //加载网页
                web.loadUrl(loadUrl(linkValue), heads(linkValue))
                //注入java对象
                web.addJavascriptInterface(WebContract.M.InJavaScriptLocalObj(object : WebContract.M.InJavaScriptLocalObj.OnInJavaScriptLocalObjListener {
                    override fun onShare(t: String, des: String, l: String) {
                        act.runOnUiThread {
                            BKLog.d(WebAct.TAG, "t:$t des:$des l:$l")
                            tvBarRight?.visibility = View.VISIBLE
                            shareViewHolder?.shareTitle = t
                            shareViewHolder?.shareDescription = des
                            shareViewHolder?.shareUrl = l
                        }
                    }

                    override fun onProductId(value: String) {
                        if (PonkoApp.getLocalVersion(act) < 345) {
                            act.runOnUiThread {
                                BKLog.d(WebAct.TAG, "productId:$value")
                                payViewHolder?.payProductId = value
                                tvBarRight?.visibility = View.GONE
                                payViewHolder?.btnPay?.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onPayGuide(have: String?, guideATitle: String?/*课程表*/, guideAUrl: String, guideBTitle: String/*老师介绍*/, guideBUrl: String, guideCTitle: String/*支付按钮显示*/) {
                        if (PonkoApp.getLocalVersion(act) < 345) {
                            act.runOnUiThread {
                                BKLog.d(WebAct.TAG, "have:$have guideATitle:$guideATitle guideAUrl:$guideAUrl guideBTitle:$guideBTitle guideBUrl:$guideBUrl guideCTitle:$guideCTitle")
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
                                            WebAct.start(act, "url", guideAUrl, guideATitle, Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }
                                        payViewHolder?.btnIntroduce?.setOnClickListener {
                                            WebAct.start(act, "url", guideBUrl, guideBTitle, Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }
                                        payViewHolder?.btnPay?.text = guideCTitle
                                    }
                                }
                            }
                        }
                    }
                }), javascriptInterfaceName)
                web.addJavascriptInterface(WebContract.M.AndroidObj(act, web, null, object : WebContract.M.AndroidObj.JSCallback {
                    override fun onIsShowPayBtn(show: Boolean) {
                        if (show) {
                            payViewHolder?.btnPay?.visibility = View.GONE
                        } else {
                            payViewHolder?.btnPay?.visibility = View.VISIBLE
                        }
                    }
                }), WebContract.M.AndroidObj.javascriptInterfaceName)
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
            heads["x-tradestudy-client-version"] = PonkoApp.getLocalVersion2(act!!)
            heads["x-tradestudy-client-device"] = "android_phone"
            heads["x-tradestudy-access-key-id"] = "c"
            heads["x-tradestudy-access-token"] = CacheUtil.getToken()!!
            return heads
        }

        private fun loadUrl(linkValue: String): String {
            return when {
                linkValue.startsWith(Constants.BASE_API, true) -> linkValue
                linkValue.startsWith("/") -> "${Constants.BASE_API}$linkValue"
                else -> linkValue
            }
        }

        fun addBar(context: Context, rightListener: View.OnClickListener) {
            BarUtil.addBar3(context, toolbar, "免费体验", "兑换", View.OnClickListener {
                BKLog.d("弹出兑换码框")
                DialogUtil.showExchange(context)
            })
        }
    }
}
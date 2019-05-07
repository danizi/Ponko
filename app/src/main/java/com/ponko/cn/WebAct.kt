package com.ponko.cn

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.ponko.cn.module.common.PonkoBaseAct
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ViewUtil

/**
 * 分享 & 支付 & 普通网页 & 积分兑换(exchange)
 */
class WebAct : PonkoBaseAct<Any>() {
    companion object {
        private const val TAG = "WebAct"
        fun start(context: Context?, link_type: String?, link_value: String?) {
            val intent = Intent(context, WebAct::class.java)
            intent.putExtra("link_type", link_type)
            intent.putExtra("link_value", link_value)
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

    private var tvBarRight: TextView? = null
    private var tvTitle: TextView?=null
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

        }
        tvTitle?.text = title
        tvBarRight?.text = "分享"
        tvBarRight?.visibility = View.GONE
    }

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
                    tvTitle?.text=title
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
        linkType = intent.getStringExtra("link_type")
        linkValue = intent.getStringExtra("link_value")
        val heads = HashMap<String, String>()
        heads["x-tradestudy-client-version"] = "3.4.6"
        heads["x-tradestudy-client-device"] = "android_phone"
        heads["x-tradestudy-access-key-id"] = "c"
        heads["x-tradestudy-access-token"] = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjdhV3lyTFlZaHdDZXYwSGgyL2x1cmdUL244S1lOa2hMY3J1YkR2MisrbWZabU1ZY01iRXUydngyWVB4cWZNQWVYWUpkaGU3TGFtbG1aM3VYQlZmRHBRPT0iLCJwaG9uZSI6IjE1MDc0NzcwNzA4IiwiaWQiOiI2NTc4M2IxNWQ0NzcxMWU4OGI0NDAyNDJhYzEzMDAwMyIsInRva2VuIjoiOWRiMTk5ZTBjNjhiNGVmNmI1Y2QzMWJkZTM3ZDU3NWUifQ.WpcxOMaEG-MZZlcNRzrhExbyaIzkllPjXXLlypFvKNw"
        viewHolder?.web?.loadUrl(loadUrl(linkValue), heads)
        webView?.addJavascriptInterface(InJavaScriptLocalObj(object : InJavaScriptLocalObj.OnInJavaScriptLocalObjListener {
            override fun onShare(t: String, des: String, l: String) {
                BKLog.d(TAG, "t:$t des:$des l:$l")
                tvBarRight?.visibility = View.VISIBLE
            }

            override fun onProductId(value: String) {
                BKLog.d(TAG, "productId:$value")
                tvBarRight?.visibility = View.GONE
            }

            override fun onPayGuide(have: String?, guideATitle: String?, guideAUrl: String, guideBTitle: String, guideBUrl: String, guideCTitle: String) {
                BKLog.d(TAG, "have:$have guideATitle:$guideATitle guideAUrl:$guideAUrl guideBTitle:$guideBTitle guideBUrl:$guideBUrl guideCTitle:$guideCTitle")
                tvBarRight?.visibility = View.GONE
            }
        }), javascriptInterfaceName)
    }

    private fun loadUrl(linkValue: String): String {
        return if (linkValue.startsWith("http", true) || linkValue.startsWith("https", true)) {
            linkValue
        } else {
            "https://api.tradestudy.cn/v3$linkValue"
        }
    }

    private class ViewHolder private constructor(val toolbar: Toolbar, val process: ProgressBar, val web: WebView) {
        companion object {

            fun create(act: AppCompatActivity): ViewHolder {
                val toolbar = act.findViewById<View>(R.id.toolbar) as Toolbar
                val process = act.findViewById<View>(R.id.process) as ProgressBar
                val web = act.findViewById<View>(R.id.web) as WebView
                return ViewHolder(toolbar, process, web)
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

package com.ponko.cn.app

import android.app.Application
import com.ponko.cn.api.*
import com.xm.lib.common.http.RetrofitClient
import java.io.File

class PonkoApp : Application() {

    companion object {
        var retrofitClient: RetrofitClient? = null
        var loginApi: LoginApi? = null
        var studyApi: StudyApi? = null
        var freeApi: FreeApi? = null
        var interflowApi: InterflowApi? = null
        var myApi: MyApi? = null
    }

    override fun onCreate() {
        super.onCreate()
        initNetWork()
    }

    private fun initNetWork() {
        val heads = HashMap<String, String>()
        heads["x-tradestudy-client-version"] = "3.4.6"
        heads["x-tradestudy-client-device"] = "android_phone"
        heads["x-tradestudy-access-key-id"] = "c"
        heads["x-tradestudy-access-token"] = "token"
        retrofitClient = RetrofitClient.intance
                .setBaseUrl("https://api.tradestudy.cn/v3/")
                .setHttpCacheDirectory(File(cacheDir, "ponko"))
                .setTimeout(15000)
                .setHeaders(heads)
                .createRetrofit(app = this)
        loginApi = retrofitClient?.retrofit?.create(LoginApi::class.java)
        studyApi = retrofitClient?.retrofit?.create(StudyApi::class.java)
        freeApi = retrofitClient?.retrofit?.create(FreeApi::class.java)
        interflowApi = retrofitClient?.retrofit?.create(InterflowApi::class.java)
        myApi = retrofitClient?.retrofit?.create(MyApi::class.java)
    }

}
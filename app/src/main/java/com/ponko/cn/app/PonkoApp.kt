package com.ponko.cn.app

import android.app.Application
import com.ponko.cn.api.*
import com.ponko.cn.bean.StoreTaskBean
import com.ponko.cn.constant.Constant.BASE_API
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
        var adApi: AdApi? = null
        var UI_DEBUG = true

        var signInfo: StoreTaskBean? = null
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
        heads["x-tradestudy-access-token"] = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IllRSmtrSVJZcitDMkl5aVZ0TTg5bXJVbTZIODhsOGw1ZEtzcXBpaHV2VzFoZHQ5V1ZJV1l1cWlISnp3YjJTQnRlbVJuUmZ5UHRHcXVsWTlrb3VqY21BPT0iLCJwaG9uZSI6IjE1MDc0NzcwNzA4IiwiaWQiOiI2NTc4M2IxNWQ0NzcxMWU4OGI0NDAyNDJhYzEzMDAwMyIsInRva2VuIjoiOGI5NTk4NGJkOGRjNGQ5ZjkwYzkxYTBmNDc5YWY1NWIifQ._Qx3x2tj62o3XN-UYXaRspf8mXYa88RksDBpcj-S0CA"
        retrofitClient = RetrofitClient.intance
                .setBaseUrl(BASE_API)
                .setHttpCacheDirectory(File(cacheDir, "ponko"))
                .setTimeout(4000)
                .setHeaders(heads)
                .createRetrofit(app = this)
        loginApi = retrofitClient?.retrofit?.create(LoginApi::class.java)
        studyApi = retrofitClient?.retrofit?.create(StudyApi::class.java)
        freeApi = retrofitClient?.retrofit?.create(FreeApi::class.java)
        interflowApi = retrofitClient?.retrofit?.create(InterflowApi::class.java)
        myApi = retrofitClient?.retrofit?.create(MyApi::class.java)
        adApi = retrofitClient?.retrofit?.create(AdApi::class.java)
    }

}
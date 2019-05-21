package com.ponko.cn.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.ponko.cn.api.*
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.bean.StoreTaskBean
import com.ponko.cn.constant.Constant.BASE_API
import com.ponko.cn.db.PonkoDBHelp
import com.ponko.cn.db.dao.CourseDao
import com.ponko.cn.db.dao.CourseSpecialDao
import com.ponko.cn.module.m3u8downer.core.M3u8DownManager
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.common.base.ActManager
import com.xm.lib.common.http.RetrofitClient
import com.xm.lib.common.log.BKLog
import java.io.File

class PonkoApp : Application() {
    companion object {
        var APP_ID = "wxd37fb8ce51a02360"
        var app: Application? = null
        var mainCBean: MainCBean? = null
        var signInfo: StoreTaskBean? = null
        var UI_DEBUG = false

        var retrofitClient: RetrofitClient? = null
        var loginApi: LoginApi? = null
        var studyApi: StudyApi? = null
        var freeApi: FreeApi? = null
        var interflowApi: InterflowApi? = null
        var myApi: MyApi? = null
        var adApi: AdApi? = null
        var payApi: PayApi? = null
        var searchApi: SearchApi? = null

        var dbHelp: PonkoDBHelp? = null
        var courseSpecialDao: CourseSpecialDao? = null
        var courseDao: CourseDao? = null

        var activityManager = ActManager()
        @SuppressLint("StaticFieldLeak")
        var m3u8DownManager: M3u8DownManager? = null
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        //日志开关
        iniLog()
        //初始化网络请求
        initNetWork()
        //初始化窗口管理
        initActivityManager()
        //初始化数据库
        initDb()
        //初始化m3u8文件下载器
        initDownManager()
    }

    private fun iniLog() {
        BKLog.LEVEL = BKLog.E
    }

    private fun initDownManager() {
        m3u8DownManager = M3u8DownManager(this)
    }

    private fun initNetWork() {
        //ps 请求头的顺序要一致哟，不然请求某些接口会出错
        val heads = LinkedHashMap<String, String>()
        heads["x-tradestudy-access-key-id"] = "c"
        heads["x-tradestudy-client-version"] = "3.4.6"
        heads["x-tradestudy-client-device"] = "android_phone"
        heads["x-tradestudy-access-token"] = CacheUtil.getToken()!!
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
        payApi = retrofitClient?.retrofit?.create(PayApi::class.java)
        searchApi = retrofitClient?.retrofit?.create(SearchApi::class.java)
    }

    private fun initActivityManager() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
                activityManager.onActivityPaused(activity)
            }

            override fun onActivityResumed(activity: Activity?) {
                activityManager.onActivityResumed(activity)
            }

            override fun onActivityStarted(activity: Activity?) {
                activityManager.onActivityStarted(activity)
            }

            override fun onActivityDestroyed(activity: Activity?) {
                activityManager.onActivityDestroyed(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                activityManager.onActivitySaveInstanceState(activity, outState)
            }

            override fun onActivityStopped(activity: Activity?) {
                activityManager.onActivityStopped(activity)
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                activityManager.onActivityCreated(activity, savedInstanceState)
            }
        })
    }

    private fun initDb() {
        dbHelp = PonkoDBHelp(this, "Ponko.db", null, 101)
        courseSpecialDao = CourseSpecialDao(PonkoApp.dbHelp?.writableDatabase)
        courseDao = CourseDao(PonkoApp.dbHelp?.writableDatabase)
    }
}
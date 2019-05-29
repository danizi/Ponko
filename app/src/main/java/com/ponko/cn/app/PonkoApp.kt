package com.ponko.cn.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.ponko.cn.MainActivity
import com.ponko.cn.R
import com.ponko.cn.api.*
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.bean.StoreTaskBean
import com.ponko.cn.constant.Constant.BASE_API
import com.ponko.cn.constant.Constant.BUG_APP_ID
import com.ponko.cn.db.PonkoDBHelp
import com.ponko.cn.db.dao.CourseDao
import com.ponko.cn.db.dao.CourseSpecialDao
import com.ponko.cn.module.m3u8downer.core.M3u8DownManager
import com.ponko.cn.utils.CacheUtil
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.Beta.largeIconId
import com.xm.lib.common.base.ActManager
import com.xm.lib.common.http.RetrofitClient
import com.xm.lib.common.log.BKLog
import com.xm.lib.statistics.Statistics
import java.io.File


class PonkoApp : MultiDexApplication() {
    companion object {
        var APP_ID = "wxd37fb8ce51a02360"
        var app: Application? = null
        var mainCBean: MainCBean? = null
        @Deprecated("还是接口代替，不缓存在内存中了")
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

        fun getLocalVersion(ctx: Context): Int {
            var localVersion = 0
            try {
                val packageInfo = ctx.applicationContext
                        .packageManager
                        .getPackageInfo(ctx.packageName, 0)
                localVersion = packageInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return localVersion
        }
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
        //初始化bugly
        initBugly()
    }

    private fun initBugly() {
//        Beta.autoInit = true
//        Beta.autoCheckUpgrade = true
//        Beta.upgradeCheckPeriod = 60 * 1000 * 60 * 24 * 2 //2天检查一次
//        Beta.upgradeCheckPeriod = 1 //2天检查一次
//        Beta.initDelay = 30 * 1000//30秒延迟检查
//        Beta.initDelay = 1//1秒延迟检查
//        Beta.largeIconId = R.mipmap.ic_launcher //设置通知栏大图标
//        Beta.smallIconId = R.mipmap.ic_launcher //设置通知栏小图标
//        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        Beta.enableNotification = true
//        Beta.canShowApkInfo = true
//        Beta.canShowUpgradeActs.add(MainActivity::class.java)
//        Beta.showInterruptedStrategy = false
        //Statistics.initCrashReport(applicationContext, BUG_APP_ID)   //ps:把这个配置打开会导致无法弹出升级框
        Statistics.initBugly(applicationContext, BUG_APP_ID)
        Statistics.initUpgradeCheck( R.mipmap.ic_launcher,MainActivity::class.java)
    }

    private fun iniLog() {
        BKLog.LEVEL = BKLog.D
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
                .setHttpCacheDirectory(File(cacheDir, "PonkoCache"))
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
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
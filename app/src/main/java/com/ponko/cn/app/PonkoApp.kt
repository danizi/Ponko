package com.ponko.cn.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.ponko.cn.BuildConfig
import com.ponko.cn.MainActivity
import com.ponko.cn.R
import com.ponko.cn.api.*
import com.ponko.cn.bean.Main2CBean
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.bean.StoreProfileBean
import com.ponko.cn.bean.StoreTaskBean
import com.ponko.cn.constant.Constants.BASE_API
import com.ponko.cn.constant.Constants.BUG_APP_ID
import com.ponko.cn.constant.Constants.UI_VERSION_1
import com.ponko.cn.constant.Constants.UI_VERSION_2
import com.ponko.cn.db.PonkoDBHelp
import com.ponko.cn.db.dao.CourseCollectSectionDao
import com.ponko.cn.db.dao.CourseCollectSpecialDao
import com.ponko.cn.db.dao.CourseDao
import com.ponko.cn.db.dao.CourseSpecialDao
import com.ponko.cn.module.m3u8downer.core.M3u8DownManager
import com.ponko.cn.utils.CacheUtil
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.xm.lib.common.base.ActManager
import com.xm.lib.common.http.RetrofitClient
import com.xm.lib.common.log.BKLog
import com.xm.lib.statistics.Statistics
import java.io.File
import java.util.*


class PonkoApp : MultiDexApplication() {
    companion object {
        var app: Application? = null
        var APP_ID = "wxd37fb8ce51a02360"
        var UI_DEBUG = false
        var UI_AD_DEBUG = false

        /**
         * 临时变量
         */
        @Deprecated("还是接口代替，不缓存在内存中了")
        var signInfo: StoreTaskBean? = null
        var mainCBean: MainCBean? = null
        var main2CBean: Main2CBean? = null
        var storeProfileBean: StoreProfileBean? = null

        /**
         * 接口
         */
        var retrofitClient: RetrofitClient? = null
        var loginApi: LoginApi? = null
        var studyApi: StudyApi? = null
        var freeApi: FreeApi? = null
        var interflowApi: InterflowApi? = null
        var myApi: MyApi? = null
        var adApi: AdApi? = null
        var payApi: PayApi? = null
        var searchApi: SearchApi? = null

        /**
         * 数据库
         */
        var dbHelp: PonkoDBHelp? = null
        var courseSpecialDao: CourseSpecialDao? = null
        var courseDao: CourseDao? = null
        var collectSectionDao: CourseCollectSectionDao? = null
        var collectSpecialDao: CourseCollectSpecialDao? = null

        /**
         * 窗口管理
         */
        var activityManager = ActManager()

        /**
         * 下载管理
         */
        @SuppressLint("StaticFieldLeak")
        var m3u8DownManager: M3u8DownManager? = null

        /**
         * 是否是3G网络提示标志
         */
        var is3GNetTip = false

        fun getLocalVersion(ctx: Context): Int {
            var localVersion = 0
            var localVersionName = ""
            try {
                val packageInfo = ctx.applicationContext
                        .packageManager
                        .getPackageInfo(ctx.packageName, 0)
                localVersion = packageInfo.versionCode
                localVersionName = packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return localVersion
        }

        fun getLocalVersion2(ctx: Context): String {
            var localVersion = 0
            var localVersionName = ""
            try {
                val packageInfo = ctx.applicationContext
                        .packageManager
                        .getPackageInfo(ctx.packageName, 0)
                localVersion = packageInfo.versionCode
                localVersionName = packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return localVersionName
        }

        fun isB2bVip(): Boolean {
            return if (CacheUtil.getStudyUI() == "2") {
                false
            } else {
                (!PonkoApp.mainCBean?.types?.isEmpty()!! && PonkoApp.mainCBean?.types!![0].isIs_vip)
            }
        }

        fun isB2CVip(): Boolean {
            return if (CacheUtil.getStudyUI() == "2") {
                false
            } else {
                (!PonkoApp.mainCBean?.types?.isEmpty()!! && PonkoApp.mainCBean?.types!![1].isIs_vip)
            }
        }

        /**
         * v1 ：3.5.0 版本之前
         * v2 ：3.5.0 版本及以后的
         */
        fun getAppUIVersion(context: Context): String {
            return if (getLocalVersion(context) < 350) {
                UI_VERSION_1
            } else {
                UI_VERSION_2
            }
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
        //内存检查
        if (BuildConfig.IS_DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return
            }
            LeakCanary.install(this)


        }
    }

    private fun initBugly() {
        //Statistics.initCrashReport(applicationContext, BUG_APP_ID)   //ps:把这个配置打开会导致无法弹出升级框
        Statistics.initBugly(applicationContext, BUG_APP_ID)
        Statistics.initUpgradeCheck(R.mipmap.ic_launcher, MainActivity::class.java)
        if (BuildConfig.IS_TINKER_DEVELOPMENT) {
            //热更新是否是开发设备
            Bugly.setIsDevelopmentDevice(applicationContext, true)
        }
    }

    private fun iniLog() {
        if (BuildConfig.IS_DEBUG) {
            BKLog.LEVEL = BKLog.D
        } else {
            BKLog.LEVEL = BKLog.W
        }
    }

    private fun initDownManager() {
        m3u8DownManager = M3u8DownManager(this)
    }

    private fun initNetWork() {
        //ps 请求头的顺序要一致哟
        val heads = LinkedHashMap<String, String>()
        heads["x-tradestudy-access-key-id"] = "c"
        heads["x-tradestudy-client-version"] = getLocalVersion2(this)
        heads["x-tradestudy-client-device"] = "android_phone"
        heads["x-tradestudy-access-token"] = CacheUtil.getToken()!!
        retrofitClient = RetrofitClient.intance
                .setBaseUrl(BASE_API)
                .setHttpCacheDirectory(File(cacheDir, "PonkoCache"))
                .setTimeout(6000)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
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
    }

    private fun initDb() {
        dbHelp = PonkoDBHelp(this, "Ponko.db", null, 101)
        courseSpecialDao = CourseSpecialDao(PonkoApp.dbHelp?.writableDatabase)
        courseDao = CourseDao(PonkoApp.dbHelp?.writableDatabase)
        collectSectionDao = CourseCollectSectionDao(PonkoApp.dbHelp?.writableDatabase)
        collectSpecialDao = CourseCollectSpecialDao(PonkoApp.dbHelp?.writableDatabase)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        // 安装tinker
        Beta.installTinker()
    }
}
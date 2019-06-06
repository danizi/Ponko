package com.ponko.cn.module.study2

import android.content.Context
import com.google.gson.Gson
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.Main2CBean
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.CacheUtil
import retrofit2.Call
import retrofit2.Response

/**
 * 新版学习页面契约类 - MVP模式
 */
class StudyContract2 {
    /**
     * 视图层
     */
    interface V {
        fun requestStudyApiSuccess(body: Main2CBean?)
    }

    /**
     * 数据层
     */
    class M {
        /**
         * 请求学习界面首页接口
         */
        fun requestStudyApi(callback: HttpCallBack<Main2CBean>) {
            PonkoApp.studyApi?.main2()?.enqueue(callback)
        }
    }

    /**
     * 控制层
     */
    class Present(private val context: Context?, private val view: V?) {
        private val m = M()

        /**
         * 请求新版学习首页接口
         */
        fun requestStudyApi() {
            m.requestStudyApi(object : HttpCallBack<Main2CBean>() {
                override fun onSuccess(call: Call<Main2CBean>?, response: Response<Main2CBean>?) {
                    PonkoApp.main2CBean = response?.body()
                    CacheUtil.putPolyvConfig(Gson().toJson(PonkoApp.main2CBean?.polyv))
                    view?.requestStudyApiSuccess(response?.body())
                }
            })
        }
    }
}
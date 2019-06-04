package com.ponko.cn.module.study.constract

import android.content.Context
import com.google.gson.Gson
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.CacheUtil.putPolyvConfig
import retrofit2.Call
import retrofit2.Response

/**
 * 学习页面契约类 - MVP模式
 */
class StudyContract {
    interface View {
        fun requestStudyApiSuccess(body: MainCBean?)
        fun requestStudyApiFailure()
    }

    class Model {
        /**
         * 请求学习界面首页接口
         */
        fun requestStudyApi(callback: HttpCallBack<MainCBean>) {
            PonkoApp.studyApi?.main()?.enqueue(callback)
        }
    }

    class Present(private val context: Context?, private val view: View?) {
        private val model = Model()
        fun requestStudyApi() {
            model.requestStudyApi(object : HttpCallBack<MainCBean>() {
                override fun onSuccess(call: Call<MainCBean>?, response: Response<MainCBean>?) {
                    PonkoApp.mainCBean = response?.body()
                    putPolyvConfig(Gson().toJson(PonkoApp.mainCBean?.polyv))
                    view?.requestStudyApiSuccess(response?.body())
                }

                override fun onFailure(call: Call<MainCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    view?.requestStudyApiFailure()
                }
            })
        }
    }
}
package com.ponko.cn.module.study.constract

import android.content.Context
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.http.HttpCallBack
import retrofit2.Call
import retrofit2.Response

class StudyContract {
    interface View {
        fun requestStudyApiSuccess(body: MainCBean?)
    }

    class Model {
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
                    view?.requestStudyApiSuccess(response?.body())
                }
            })
        }
    }
}
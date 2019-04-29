package com.ponko.cn.module.free.constract

import android.content.Context
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.CoursesCBean
import com.ponko.cn.http.HttpCallBack
import retrofit2.Call
import retrofit2.Response

class FreeConstract {
    interface View {
        fun requestFreeApiSuccess(body: CoursesCBean?)
    }

    class Model {
        fun requestFreeApi(callback: HttpCallBack<CoursesCBean>) {
            PonkoApp.freeApi?.freeHome()?.enqueue(callback)
        }
    }

    class Present(private val context: Context?, private val view: View?) {
        private val model = Model()

        fun requestFreeApi() {
            model.requestFreeApi(object : HttpCallBack<CoursesCBean>() {
                override fun onSuccess(call: Call<CoursesCBean>?, response: Response<CoursesCBean>?) {
                    view?.requestFreeApiSuccess(response?.body())
                }
            })
        }
    }
}
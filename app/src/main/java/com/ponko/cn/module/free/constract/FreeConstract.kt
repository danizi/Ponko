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


         fun testWechat(body: CoursesCBean?) {
            for (i in 0..3) {
                val exchanged = CoursesCBean.Exchanged()
                exchanged.duration = ""
                exchanged.image = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2437088410,3383681288&fm=27&gp=0.jpg"
                exchanged.num = 10.0
                exchanged.id = ""
                exchanged.title = ""
                exchanged.duration = "60"
                //exchanged.teachers.add("may")
                body?.wechat?.add(exchanged)
            }
        }

         fun testExchanged(body: CoursesCBean?) {
            for (i in 0..10) {
                val exchanged = CoursesCBean.Exchanged()
                exchanged.duration = ""
                exchanged.image = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2437088410,3383681288&fm=27&gp=0.jpg"
                exchanged.num = 10.0
                exchanged.id = ""
                exchanged.title = ""
                exchanged.duration = "60"
                //exchanged.teachers.add("may")
                body?.exchanged?.add(exchanged)
            }
        }
    }
}
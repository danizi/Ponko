package com.ponko.cn.module.my.v1.constract

import android.content.Context
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.InternalCourseCBean
import com.ponko.cn.http.HttpCallBack
import retrofit2.Call
import retrofit2.Response

class IntegralExchangedClassContract {

    interface V {
        fun requestExchangedCourseApiSuccess(body: MutableList<InternalCourseCBean>?)
        fun requestExchangedCourseApiFailure()
    }

    class M {
        fun requestExchangedCourseApi(callback: HttpCallBack<MutableList<InternalCourseCBean>>) {
            PonkoApp.myApi?.exchangedCourse()?.enqueue(callback)
        }
    }

    class P(private val context: Context, private val v: V) {
        private val m = M()

        fun requestExchangedCourseApi() {
            m.requestExchangedCourseApi(object : HttpCallBack<MutableList<InternalCourseCBean>>() {
                override fun onSuccess(call: Call<MutableList<InternalCourseCBean>>?, response: Response<MutableList<InternalCourseCBean>>?) {
                    v.requestExchangedCourseApiSuccess(response?.body())
                }

                override fun onFailure(call: Call<MutableList<InternalCourseCBean>>?, msg: String?) {
                    super.onFailure(call, msg)
                    v.requestExchangedCourseApiFailure()
                }
            })
        }
    }
}
package com.ponko.cn.module.my.constract

import android.content.Context
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.ProfileCBean
import com.ponko.cn.http.HttpCallBack
import retrofit2.Call
import retrofit2.Response

class MyConstract {
    interface View {
        fun requestMyInfoApiSuccess(body: ProfileCBean?)
    }

    class Model {
        fun requestMyInfoApi(callback: HttpCallBack<ProfileCBean>) {
            PonkoApp.myApi?.basic_info()?.enqueue(callback)
        }
    }

    class Present(private val context: Context?, private val view: View?) {
        private val model = Model()

        fun requestMyInfoApi() {
            model.requestMyInfoApi(object : HttpCallBack<ProfileCBean>() {
                override fun onSuccess(call: Call<ProfileCBean>?, response: Response<ProfileCBean>?) {
                    view?.requestMyInfoApiSuccess(response?.body())
                }
            })
        }
    }
}
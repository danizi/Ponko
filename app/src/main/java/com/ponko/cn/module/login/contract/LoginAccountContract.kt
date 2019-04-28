package com.ponko.cn.module.login.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.ActivityUtil
import retrofit2.Call
import retrofit2.Response

class LoginAccountContract {
    interface View {}
    class Model {
        fun requestLoginApi(callback: HttpCallBack<Any>) {
            PonkoApp.loginApi?.login("", "", "")?.enqueue(callback)
        }
    }

    class Present(private val context: Context, private val view: View) {
        private val model = Model()
        fun clickClose() {
            (context as Activity).finish()
        }

        fun clickFindPwd() {
            ActivityUtil.startActivity(context, Intent(context, null)) //todo 找回密码
        }

        fun clickEnter() {
            model.requestLoginApi(object : HttpCallBack<Any>() {
                override fun onSuccess(call: Call<Any>?, response: Response<Any>?) {
                    PonkoApp.retrofitClient?.headers?.set("", "")   //设置请求Token
                }
            })
        }
    }
}
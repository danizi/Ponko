package com.ponko.cn.module.login.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import com.ponko.cn.MainActivity
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.login.LoginFindPwdAct
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.common.log.BKLog
import retrofit2.Call
import retrofit2.Response

class LoginAccountContract {
    interface View {}
    class Model {
        fun requestLoginApi(phone: String, pwd: String, callback: HttpCallBack<GeneralBean>) {
            PonkoApp.loginApi?.login(phone, pwd, "")?.enqueue(callback)
        }
    }

    class Present(private val context: Context?, private val view: View?) {
        private val model = Model()
        fun clickClose() {
            (context as Activity).finish()
        }

        fun clickFindPwd() {
            ActivityUtil.startActivity(context, Intent(context, LoginFindPwdAct::class.java))
        }

        fun clickEnter(phone: String, pwd: String) {
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(context, "手机号码为空", Toast.LENGTH_SHORT).show()
                return
            }
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(context, "密码为空", Toast.LENGTH_SHORT).show()
                return
            }
            model.requestLoginApi(phone, pwd, object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    BKLog.d("登录成功")
                    val token = response?.body()?.token
                    CacheUtil.putToken(token)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    ActivityUtil.startActivity(context, intent)
                }
            })
        }
    }
}
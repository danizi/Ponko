package com.ponko.cn.module.login.contract

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import com.ponko.cn.MainActivity
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import retrofit2.Call
import retrofit2.Response

class LoginByCodeContract {
    interface V {}

    class M {
        fun sendLoginCode(phone: String, callback: HttpCallBack<GeneralBean>) {
            PonkoApp.loginApi?.loginSms(phone)?.enqueue(callback)
        }

        fun login(phone: String, code: String, callback: HttpCallBack<GeneralBean>) {
            val map = HashMap<String, String>()
            map["phone"] = phone
            map["code"] = code
//            if (!CacheUtil.isUserTypeLogin()) {
//                map.put("deviceId", UDIDUtil.getUDID(PonkoApp.app))
//            }
            PonkoApp.loginApi?.login2(map)?.enqueue(callback)
        }
    }

    class P(val context: Context, val v: V) {
        private val m = M()

        fun clickEnter(phone: String, code: String) {
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(context, "手机号码为空", Toast.LENGTH_SHORT).show()
                return
            }
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(context, "验证码未空", Toast.LENGTH_SHORT).show()
                return
            }

            DialogUtil.showProcess(context)
            m.login(phone, code, object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    val token = response?.body()?.token
                    CacheUtil.putToken(token)
                    CacheUtil.putUserTypeLogin()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    ActivityUtil.startActivity(context, intent)
                    DialogUtil.hideProcess()
                }
            })
        }

        fun clickSendCode(phone: String) {
            m.sendLoginCode(phone, object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    ToastUtil.show("发送短信成功，请注意查收。")
                }
            })
        }
    }
}
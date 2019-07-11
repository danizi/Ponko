package com.ponko.cn.http

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.module.login.LoginStartAct
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.DialogUtil
import com.xm.lib.common.http.RetrofitClient
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.OnEnterListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

abstract class HttpCallBack<T> : RetrofitClient.BaseCallback<T>() {
    private val TAG = "HttpCallBack"

    override fun onFailure(call: Call<T>?, msg: String?) {
        BKLog.e(TAG, "HttpCallBack onFailure msg$msg")

        if (!TextUtils.isEmpty(msg)) {

            //登录相关点击按钮都有相应提示，主要是没有网络
            val a = PonkoApp.activityManager.getTopActivity()!!
            val className = a::class.java.simpleName
            BKLog.d("className->$className")

            if (!TextUtils.isEmpty(className) && className.contains("Login")) {
                showErrorDlg(className, msg!!)
            } else {
                for (error in errorCodeMaps) {
                    if (msg == error.value) {
                        showErrorDlg(className, msg)
                        break
                    }
                }
            }
        }
    }

    private fun showErrorDlg(className: String, msg: String) {
        val isCancelable = !(msg == errorCodeMaps["InvalidAccessToken"] || msg == errorCodeMaps["RepeatOnline"] || msg == errorCodeMaps["InvalidPhoneCode"])
        if(msg == errorCodeMaps["RepeatOnline"] && !CacheUtil.isUserTypeLogin()) return

        DialogUtil.show(
                PonkoApp.activityManager.getTopActivity()!!,
                "提示"
                , msg
                , isCancelable,
                object : OnEnterListener {
                    override fun onEnter(dlg: AlertDialog) {

                        //用户进入主页，然后再其他终端修改密码时，弹出提示回到登录页面
                        if (!className.contains("Login") && msg == errorCodeMaps["TokenPasswordDoesNotMatch"]) {
                            val act = PonkoApp.activityManager.getTopActivity()
                            val intent = Intent(act, LoginStartAct::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            ActivityUtil.startActivity(act, intent)
                        }

                        //用户多端同时登录
                        if ((msg == errorCodeMaps["InvalidAccessToken"] || msg == errorCodeMaps["RepeatOnline"]) && CacheUtil.isUserTypeLogin()) {
                            val act = PonkoApp.activityManager.getTopActivity()
                            val intent = Intent(act, LoginStartAct::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            ActivityUtil.startActivity(act, intent)
                        }
                        dlg.dismiss()
                    }
                }
                , null)
    }

    override fun errorMsg(response: Response<T>?) {
        val errorString = response?.errorBody()?.string()
        if (TextUtils.isEmpty(errorString)) {
            BKLog.e(TAG, "errorString is empty")
            onFailure(null, "请检查您的网络...")
            return
        }
        val jsonObject = JSONObject(errorString)
        val code = jsonObject.get("code") as String
        val url = response?.raw()?.request()?.url().toString()
        var msg = httpErrorCodeMaps(url)[code]
        if (TextUtils.isEmpty(msg)) {
            msg = errorString
            BKLog.d("未處理服務器返回的消息errorString:$msg")
        }
        BKLog.d(TAG, "url ->$url code -> $code msg ->$msg ")
        onFailure(null, msg)
    }

    private val errorCodeMaps = HashMap<String, String>()
    private fun httpErrorCodeMaps(url: String): HashMap<String, String> {
        errorCodeMaps["InvalidAccessToken"] = "无效令牌，请重新登录"
        errorCodeMaps["InvalidAccessKeyId"] = "key无效"
        errorCodeMaps["PhoneNotFound"] = "未注册"
        errorCodeMaps["TokenPasswordDoesNotMatch"] = "密码不正确"
        errorCodeMaps["PasswordDoesNotMatch"] = "密码不正确"
        errorCodeMaps["RepeatOnline"] = "账号已经在其他设备登录，请重新登录"
        errorCodeMaps["AccountAlreadyExists"] = "账号已经在其他设备登录，该手机账号已经绑定其他微信"
        errorCodeMaps["RepeatExchange"] = "不可以重复兑换"
        errorCodeMaps["CDKeyNotFound"] = "兑换码不存在"
        errorCodeMaps["CouponNotFound"] = "兑换码不存在"
        errorCodeMaps["InvalidPhoneCode"] = "无效验证码"
        return errorCodeMaps
    }
}
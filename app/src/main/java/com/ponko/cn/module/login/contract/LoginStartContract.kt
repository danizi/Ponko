package com.ponko.cn.module.login.contract

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.ponko.cn.MainActivity
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.APP_ID
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.bean.OauthBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.login.LoginAccountAct
import com.ponko.cn.module.login.LoginRegisterAct
import com.ponko.cn.module.login.LoginWxAct
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.UDIDUtil
import com.xm.lib.media.broadcast.BroadcastManager
import com.xm.lib.share.AbsShare
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class LoginStartContract {
    interface View {
        fun showTouristDlg()
    }

    class Model {
        fun requestTourist() {

        }
    }

    class Present(private val context: Context, private val v: View) {

        fun joinMainPageByTourist() {
            val uuid = UDIDUtil.getUDID(context)
            PonkoApp.loginApi?.touristsSignIn(uuid)?.enqueue(object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    val token = response?.body()?.token
                    CacheUtil.putToken(token)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    ActivityUtil.startActivity(context, intent)
                }
            })
        }

        private var broadcastManager: BroadcastManager? = null
        private var wxAuthBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                fun unbind(oauthBean: OauthBean?, code: String) {
                    val userInfo = oauthBean?.info.toString()
                    BKLog.d("未綁定微信，$userInfo")
                    //区分注册过还是未注册过
                    if (TextUtils.isEmpty(userInfo)) {
                        //未注册跳转到微信注册页面
                        val intent = Intent(context, LoginWxAct::class.java)
                        intent.putExtra("code", code)
                        ActivityUtil.startActivity(context, intent)
                    } else {
                        //已注册直接调用绑定接口
                        val params = HashMap<String, String>()
                        params["token"] = oauthBean?.info?.unionId!!
                        params["type"] = "wechat"
                        PonkoApp.loginApi?.wechatBind(params)?.enqueue(object : HttpCallBack<GeneralBean>() {
                            override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                                BKLog.d("已注册账号 - 綁定微信成功，$userInfo")
                                val token = oauthBean.token
                                CacheUtil.putToken(token)
                                val intent = Intent(context, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                ActivityUtil.startActivity(context, intent)
                            }
                        })
                    }
                }

                fun bind(oauthBean: OauthBean?) {
                    val userInfo = oauthBean?.info.toString()
                    BKLog.d("已綁定微信，$userInfo")
                    val token = oauthBean?.token
                    CacheUtil.putToken(token)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    ActivityUtil.startActivity(context, intent)
                }

                if (intent?.action == "COMMAND_SENDAUTH") {
                    val code = intent.getStringExtra("code")
                    BKLog.d("WechatAuth code:$code")
                    PonkoApp.loginApi?.weChatOauth("wechat", code)?.enqueue(object : HttpCallBack<OauthBean>() {
                        override fun onSuccess(call: Call<OauthBean>?, response: Response<OauthBean>?) {
                            when (response?.body()?.status) {
                                "404" -> {
                                    //没有绑定微信
                                    unbind(response.body(), code)
                                }
                                "" -> {
                                    //绑定了微信
                                    bind(response.body())
                                }
                            }
                        }
                    })
                }
            }
        }

        fun onCreate() {
            broadcastManager = BroadcastManager.create(context)
            broadcastManager?.registerReceiver("COMMAND_SENDAUTH", wxAuthBroadcastReceiver)
        }

        fun onDestroy() {
            broadcastManager?.unRegisterReceiver(wxAuthBroadcastReceiver)
        }

        fun clickWxLogin() {
            val wxShare = WxShare(context as Activity)
            wxShare.init(ShareConfig.Builder().appid(APP_ID).build())
            wxShare.oauth()
        }

        fun clickAccountLogin() {
            ActivityUtil.startActivity(context, Intent(context, LoginAccountAct::class.java))
        }

        fun clickRegister() {
            ActivityUtil.startActivity(context, Intent(context, LoginRegisterAct::class.java))
        }


    }
}
package com.ponko.cn.module.login.contract

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
import com.ponko.cn.utils.DialogUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.UDIDUtil
import com.xm.lib.media.broadcast.BroadcastManager
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
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

        /**
         * 游客模式进入主页
         */
        fun joinMainPageByTourist() {
            val uuid = UDIDUtil.getUDID(context)
            DialogUtil.hide()
            DialogUtil.showProcess(context)
            PonkoApp.loginApi?.touristsSignIn(uuid)?.enqueue(object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    val token = response?.body()?.token
                    CacheUtil.putToken(token)
                    CacheUtil.putUserTypeTourist()
                    DialogUtil.hideProcess()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    ActivityUtil.startActivity(context, intent)
                }

                override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    DialogUtil.hideProcess()
                }
            })
        }

        private var broadcastManager: BroadcastManager? = null
        private var wxAuthBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "COMMAND_SENDAUTH") {
                    //微信授权码
                    val code = intent.getStringExtra("code")
                    BKLog.d("微信授权码:$code")
                    PonkoApp.loginApi?.weChatOauth("wechat", code)?.enqueue(object : HttpCallBack<OauthBean>() {
                        override fun onSuccess(call: Call<OauthBean>?, response: Response<OauthBean>?) {
                            when (response?.body()?.status) {
                                "404" -> {
                                    //没有绑定微信
                                    BKLog.d("没有绑定微信-执行绑定操作-${response.body()}")
                                    unbind(response.body(), code)
                                }
                                "" -> {
                                    //绑定了微信
                                    BKLog.d("绑定了微信-直接进入应用")
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
            CacheUtil.putToken("")
            val wxShare = WxShare(context as Activity)
            wxShare.init(ShareConfig.Builder().appid(APP_ID).build())
            wxShare.oauth()
        }

        /**
         * 绑定微信操作
         */
        private fun bind(oauthBean: OauthBean?) {
            val userInfo = oauthBean?.info.toString()
            BKLog.d("已綁定微信，$userInfo")
            val token = oauthBean?.token
            CacheUtil.putToken(token)
            CacheUtil.putUserTypeWx()
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            ActivityUtil.startActivity(context, intent)
        }

        /**
         * 未绑定微信操作
         */
        private fun unbind(oauthBean: OauthBean?, code: String) {
            val userInfo = oauthBean?.info.toString()
            LoginWxAct.start(context, code, oauthBean?.info?.unionId)

        }

        fun clickAccountLogin() {
            ActivityUtil.startActivity(context, Intent(context, LoginAccountAct::class.java))
        }

        fun clickRegister() {
            ActivityUtil.startActivity(context, Intent(context, LoginRegisterAct::class.java))
        }


    }
}
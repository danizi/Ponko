package com.ponko.cn.module.my.constract

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.ProfileCBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.http.HttpCallBack
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.broadcast.BroadcastManager
import retrofit2.Call
import retrofit2.Response

/**
 * 我的页面 - 契约类 - MVP
 */
class MyConstract {
    /**
     * 视图层
     */
    interface View {
        fun requestMyInfoApiSuccess(body: ProfileCBean?)
        fun requestMyInfoApiFailure()
        fun refreshTop(body: ProfileCBean?)
    }

    /**
     * 数据层
     */
    class Model {
        fun requestMyInfoApi(callback: HttpCallBack<ProfileCBean>) {
            PonkoApp.myApi?.basic_info()?.enqueue(callback)
        }
    }

    /**
     * 控制层
     */
    class Present(private val context: Context?, private val view: View?) {
        private val model = Model()

        /**
         * 请求我的页面接口
         */
        fun requestMyInfoApi() {
            model.requestMyInfoApi(object : HttpCallBack<ProfileCBean>() {
                override fun onSuccess(call: Call<ProfileCBean>?, response: Response<ProfileCBean>?) {
                    view?.requestMyInfoApiSuccess(response?.body())
                }

                override fun onFailure(call: Call<ProfileCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    view?.requestMyInfoApiFailure()
                }
            })
        }

        /**
         * 广播管理器
         */
        private var broadcastManager: BroadcastManager? = null
        /**
         * 刷新广播
         */
        private var refreshBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Constants.ACTION_SIGN_SUCCESS) {
                    BKLog.d("用户签到成功，刷新我的页面")
                    requestMyInfoApi()
                }
            }
        }

        /**
         * 注册刷新广播
         */
        fun registerRefreshBroadcast() {
            broadcastManager = BroadcastManager.create(context)
            broadcastManager?.registerReceiver(Constants.ACTION_SIGN_SUCCESS, refreshBroadcastReceiver)
        }

        /**
         * 取消刷新广播
         */
        fun unRegisterRefreshBroadcast() {
            broadcastManager?.unRegisterReceiver(refreshBroadcastReceiver)
        }

        /**
         * 刷新顶部内容
         */
        fun refreshTop() {
            model.requestMyInfoApi(object : HttpCallBack<ProfileCBean>() {
                override fun onSuccess(call: Call<ProfileCBean>?, response: Response<ProfileCBean>?) {
                    view?.refreshTop(response?.body())
                }

                override fun onFailure(call: Call<ProfileCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                }
            })
        }
    }
}
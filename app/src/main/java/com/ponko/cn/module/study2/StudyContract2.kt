package com.ponko.cn.module.study2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.Main2CBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.media.broadcast.BroadcastManager
import retrofit2.Call
import retrofit2.Response

/**
 * 新版学习页面契约类 - MVP模式
 */
class StudyContract2 {
    /**
     * 视图层
     */
    interface V {
        fun requestStudyApiSuccess(body: Main2CBean?)
        fun requestStudyApiFailure()
        fun showMsgTip(i: Int)
        fun hideMsgTip()
    }

    /**
     * 数据层
     */
    class M {
        /**
         * 请求学习界面首页接口
         */
        fun requestStudyApi(callback: HttpCallBack<Main2CBean>) {
            PonkoApp.studyApi?.main2()?.enqueue(callback)
        }
    }

    /**
     * 控制层
     */
    class Present(private val context: Context?, private val view: V?) {
        private val m = M()

        /**
         * 请求新版学习首页接口
         */
        fun requestStudyApi() {
            m.requestStudyApi(object : HttpCallBack<Main2CBean>() {
                override fun onSuccess(call: Call<Main2CBean>?, response: Response<Main2CBean>?) {
                    PonkoApp.main2CBean = response?.body()
                    CacheUtil.putPolyvConfig(Gson().toJson(PonkoApp.main2CBean?.polyv))
                    view?.requestStudyApiSuccess(response?.body())
                    if (response?.body()?.msg_count!! > 0) {
                        view?.showMsgTip(response.body()?.msg_count!!)
                    } else {
                        view?.hideMsgTip()
                    }
                }

                override fun onFailure(call: Call<Main2CBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    view?.requestStudyApiFailure()
                }
            })
        }

        private var broadcastManager: BroadcastManager? = null
        private val tipBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Constants.ACTION_SHOW_MSG_TIP -> {
                        val account = intent.getIntExtra("msg", -1)
                        view?.showMsgTip(account)
                    }
                    Constants.ACTION_HIDE_MSG_TIP -> {
                        view?.hideMsgTip()
                    }
                }
            }
        }

        fun registerTipReceiver() {
            if (broadcastManager == null) {
                broadcastManager = BroadcastManager.create(context)
            }
            broadcastManager?.registerReceiver(Constants.ACTION_SHOW_MSG_TIP, tipBroadcastReceiver)
            broadcastManager?.registerReceiver(Constants.ACTION_HIDE_MSG_TIP, tipBroadcastReceiver)
        }

        fun unRegisterTipReceiver() {
            broadcastManager?.unRegisterReceiver(tipBroadcastReceiver)
        }
    }
}
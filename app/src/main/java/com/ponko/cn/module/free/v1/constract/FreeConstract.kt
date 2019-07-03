package com.ponko.cn.module.free.v1.constract

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.CoursesCBean
import com.ponko.cn.constant.Constants.ACTION_FREE_REFRESH
import com.ponko.cn.http.HttpCallBack
import com.xm.lib.media.broadcast.BroadcastManager
import retrofit2.Call
import retrofit2.Response

class FreeConstract {
    interface View {
        fun requestFreeApiSuccess(body: CoursesCBean?)
        fun requestFreeApiFailure()
    }

    class Model {
        fun requestFreeApi(callback: HttpCallBack<CoursesCBean>) {
            PonkoApp.freeApi?.freeHome()?.enqueue(callback)
        }
    }

    class Present(private val context: Context?, private val view: View?) {
        private val model = Model()
        private val isDebug = PonkoApp.UI_DEBUG
        private var broadcastManager: BroadcastManager? = null
        private val refreshBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == ACTION_FREE_REFRESH) {
                    requestFreeApi()
                }
            }
        }

        fun registerRefreshBroadcast() {
            if (broadcastManager == null) {
                broadcastManager = BroadcastManager.create(context)
            }
            broadcastManager?.registerReceiver(ACTION_FREE_REFRESH, refreshBroadcastReceiver)
        }

        fun unRegisterRefreshBroadcast() {
            broadcastManager?.unRegisterReceiver(refreshBroadcastReceiver)
        }

        fun requestFreeApi() {
            model.requestFreeApi(object : HttpCallBack<CoursesCBean>() {
                override fun onSuccess(call: Call<CoursesCBean>?, response: Response<CoursesCBean>?) {
                    view?.requestFreeApiSuccess(response?.body())
                }

                override fun onFailure(call: Call<CoursesCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    view?.requestFreeApiFailure()
                }
            })
        }

        fun testWechat(body: CoursesCBean?) {
            if (isDebug) {
                for (i in 0..3) {
                    val exchanged = CoursesCBean.Exchanged()
                    exchanged.duration = ""
                    exchanged.image = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2437088410,3383681288&fm=27&gp=0.jpg"
                    exchanged.num = 10.0
                    exchanged.id = ""
                    exchanged.title = ""
                    exchanged.duration = "60"
                    //exchanged.teachers.add("may")
                    body?.wechat?.add(exchanged)
                }
            }

        }

        fun testExchanged(body: CoursesCBean?) {
            if (isDebug) {
                for (i in 0..10) {
                    val exchanged = CoursesCBean.Exchanged()
                    exchanged.duration = ""
                    exchanged.image = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2437088410,3383681288&fm=27&gp=0.jpg"
                    exchanged.num = 10.0
                    exchanged.id = ""
                    exchanged.title = ""
                    exchanged.duration = "60"
                    //exchanged.teachers.add("may")
                    body?.exchanged?.add(exchanged)
                }
            }
        }
    }
}
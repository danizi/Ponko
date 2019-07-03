package com.ponko.cn.module.my.constract

import android.content.Context
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.bean.RemindCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.DialogUtil
import retrofit2.Call
import retrofit2.Response

class RemindContract {
    interface V {
        fun requestCleanRemindApiSuccess()
        fun requestCleanRemindApiFailure()
        fun remindListApiSuccess(body: ArrayList<RemindCBean>?)
        fun remindListApiFailure()
        fun remindListMoreApiSuccess(body: ArrayList<RemindCBean>?)
        fun remindListMoreApiFailure()
    }

    class M {
        /**
         * 获取消息列表接口
         */
        fun remindListApi(page: Int, callback: HttpCallBack<ArrayList<RemindCBean>>) {
            PonkoApp.myApi?.getRemindList(page)?.enqueue(callback)
        }

        /**
         * 删除消息列表接口
         */
        fun requestCleanRemind(ids: String? = "", callback: HttpCallBack<GeneralBean>) {
            PonkoApp.myApi?.cleanRemind(ids)?.enqueue(callback)
        }

        /**
         * 读取消息
         */
        fun requstReadRemind(id: String, callback: HttpCallBack<GeneralBean>) {
            PonkoApp.myApi?.readRemind(id)?.enqueue(callback)
        }
    }

    class Present(val context: Context, val v: V?) {
        private val m = M()
        private var page = 1

        /**
         * 删除所有
         */
        fun clearAll() {
            DialogUtil.showProcess(context)
            m.requestCleanRemind("", object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    v?.requestCleanRemindApiSuccess()
                    DialogUtil.hideProcess()
                }

                override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.requestCleanRemindApiFailure()
                    DialogUtil.hideProcess()
                }
            })
        }

        /**
         * 请求消息列表
         */
        fun remindListApi() {
            page = 1
            m.remindListApi(page, object : HttpCallBack<ArrayList<RemindCBean>>() {
                override fun onSuccess(call: Call<ArrayList<RemindCBean>>?, response: Response<ArrayList<RemindCBean>>?) {
                    v?.remindListApiSuccess(response?.body())
                }

                override fun onFailure(call: Call<ArrayList<RemindCBean>>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.remindListApiFailure()
                }
            })
        }

        /**
         * 请求更多
         */
        fun remindListMoreApi() {
            m.remindListApi(++page, object : HttpCallBack<ArrayList<RemindCBean>>() {
                override fun onSuccess(call: Call<ArrayList<RemindCBean>>?, response: Response<ArrayList<RemindCBean>>?) {
                    v?.remindListMoreApiSuccess(response?.body())
                }

                override fun onFailure(call: Call<ArrayList<RemindCBean>>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.remindListMoreApiFailure()
                }
            })
        }

    }
}
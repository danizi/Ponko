package com.ponko.cn.module.my.v2.constract

import android.content.Context
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.bean.ReadMsg
import com.ponko.cn.bean.RemindCBeanV2
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.DialogUtil
import retrofit2.Call
import retrofit2.Response

class RemindContractV2 {
    interface V {
        fun requestCleanRemindApiSuccess()
        fun requestCleanRemindApiFailure()
        fun remindListApiSuccess(body: RemindCBeanV2?)
        fun remindListApiFailure()
        fun remindListMoreApiSuccess(body: RemindCBeanV2?)
        fun remindListMoreApiFailure()
    }

    class M {
        /**
         * 获取消息列表接口
         */
        fun remindListApiV2(page: Int, callback: HttpCallBack<RemindCBeanV2>) {
            PonkoApp.myApi?.getRemindListV2(page)?.enqueue(callback)
        }

        /**
         * 读取消息
         */
        fun requstReadRemindV2(id: String, callback: HttpCallBack<ReadMsg>) {
            PonkoApp.myApi?.readRemindV2(id)?.enqueue(callback)
        }

        /**
         * 删除消息列表接口
         */
        fun requestCleanRemind(ids: String? = "", callback: HttpCallBack<GeneralBean>) {
            PonkoApp.myApi?.cleanRemind(ids)?.enqueue(callback)
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
            m.remindListApiV2(page, object : HttpCallBack<RemindCBeanV2>() {
                override fun onSuccess(call: Call<RemindCBeanV2>?, response: Response<RemindCBeanV2>?) {
                    v?.remindListApiSuccess(response?.body())
                }

                override fun onFailure(call: Call<RemindCBeanV2>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.remindListApiFailure()
                }
            })
        }

        /**
         * 请求更多
         */
        fun remindListMoreApi() {
            m.remindListApiV2(++page, object : HttpCallBack<RemindCBeanV2>() {
                override fun onSuccess(call: Call<RemindCBeanV2>?, response: Response<RemindCBeanV2>?) {
                    v?.remindListMoreApiSuccess(response?.body())
                }

                override fun onFailure(call: Call<RemindCBeanV2>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.remindListMoreApiFailure()
                }
            })
        }
    }
}
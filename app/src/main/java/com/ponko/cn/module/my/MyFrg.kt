package com.ponko.cn.module.my

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.*
import com.ponko.cn.constant.Constants
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.my.adapter.MyAdapter
import com.ponko.cn.module.my.constract.MyConstract
import com.ponko.cn.module.my.holder.MyViewHolder
import com.ponko.cn.module.my.holder.MyViewHolder2
import com.ponko.cn.module.study2.StudyContract2
import com.ponko.cn.utils.AnimUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import retrofit2.Call
import retrofit2.Response

/**
 * 我的页面
 */
class MyFrg : RefreshLoadFrg<MyConstract.Present, ProfileCBean>(), MyConstract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p?.registerRefreshBroadcast()
    }

    override fun onDestroy() {
        super.onDestroy()
        p?.unRegisterRefreshBroadcast()
    }

    override fun onResume() {
        super.onResume()
        //重新请求页面
        requestRefreshApi()
        //重新请求积分任务接口
        PonkoApp.myApi?.tasks()?.enqueue(object : HttpCallBack<StoreTaskBean>() {
            override fun onSuccess(call: Call<StoreTaskBean>?, response: Response<StoreTaskBean>?) {
                val storeTaskBean = response?.body()
                PonkoApp.signInfo = storeTaskBean
            }
        })
        //再次请求一下接口
        StudyContract2.M().requestStudyApi(object : HttpCallBack<Main2CBean>() {
            override fun onSuccess(call: Call<Main2CBean>?, response: Response<Main2CBean>?) {
                val msg = response?.body()?.msg_count!!
                if (msg > 0) {
                    BKLog.d("有提醒消息，提醒图标晃动")
                    val intent = Intent(Constants.ACTION_SHOW_MSG_TIP)
                    intent.putExtra("msg", msg)
                    context?.sendBroadcast(Intent(Constants.ACTION_SHOW_MSG_TIP))
                } else {
                    context?.sendBroadcast(Intent(Constants.ACTION_HIDE_MSG_TIP))
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initDisplay() {
        disableLoad = true
        addItemDecoration = false
        super.initDisplay()
        viewHolder?.clContent?.setBackgroundColor(context?.resources?.getColor(R.color.white)!!)
        isFocusableInTouchMode()
    }

    override fun presenter(): MyConstract.Present {
        return MyConstract.Present(context, this)
    }

    override fun adapter(): BaseRvAdapter? {
        return MyAdapter()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1),
                arrayOf(MyViewHolder::class.java, MyViewHolder2::class.java),
                arrayOf(MyTopBean::class.java, MyBean::class.java),
                arrayOf(R.layout.item_my_top, R.layout.item_my_rv)
        )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        p?.requestMyInfoApi()
    }

    override fun requestMyInfoApiSuccess(body: ProfileCBean?) {
        requestRefreshSuccess(body)
    }

    override fun requestMyInfoApiFailure() {
        requestRefreshFailure()
    }

    override fun multiTypeData(body: ProfileCBean?): List<Any> {
        val data = ArrayList<Any>()
        if (body != null) {
            data.add(body)
        }
        data.add(MyBean.create())
        return data
    }
}
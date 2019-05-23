package com.ponko.cn.module.my

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.*
import com.ponko.cn.constant.Constant
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.my.adapter.MyAdapter
import com.ponko.cn.module.my.constract.MyConstract
import com.ponko.cn.module.my.holder.MyViewHolder
import com.ponko.cn.module.my.holder.MyViewHolder2
import com.ponko.cn.utils.AnimUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.broadcast.BroadcastManager
import retrofit2.Call
import retrofit2.Response

class MyFrg : RefreshLoadFrg<MyConstract.Present, ProfileCBean>(), MyConstract.View {

    /**
     * 广播管理器
     */
    private var broadcastManager: BroadcastManager? = null
    /**
     * 刷新广播
     */
    private var refreshBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Constant.ACTION_SIGN_SUCCESS) {
                BKLog.d("用户签到成功，刷新我的页面")
                iniData()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broadcastManager?.registerReceiver(Constant.ACTION_SIGN_SUCCESS, refreshBroadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcastManager?.unRegisterReceiver(refreshBroadcastReceiver)
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

    override fun requestMoreApi() {

    }

    override fun requestRefreshApi() {
        p?.requestMyInfoApi()
    }

    override fun requestMyInfoApiSuccess(body: ProfileCBean?) {
        requestRefreshSuccess(body)
    }

    override fun multiTypeData(body: ProfileCBean?): List<Any> {
        val data = ArrayList<Any>()
        if (body != null) {
            data.add(body)
        }
        data.add(MyBean.create())
        return data
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initDisplay() {
        disableLoad = true
        addItemDecoration = false
        super.initDisplay()
        viewHolder?.clContent?.setBackgroundColor(context?.resources?.getColor(R.color.white)!!)
        isFocusableInTouchMode()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        BKLog.d("显示状态：$isVisibleToUser")
        PonkoApp.myApi?.tasks()?.enqueue(object : HttpCallBack<StoreTaskBean>() {
            override fun onSuccess(call: Call<StoreTaskBean>?, response: Response<StoreTaskBean>?) {
                val storeTaskBean = response?.body()
                PonkoApp.signInfo = storeTaskBean
            }
        })
    }
}
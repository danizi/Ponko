package com.ponko.cn.module.my.v1

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.*
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.my.adapter.MyAdapter
import com.ponko.cn.module.my.constract.MyConstract
import com.ponko.cn.module.my.holder.MyViewHolder
import com.ponko.cn.module.my.holder.MyViewHolder2
import com.xm.lib.common.base.rv.BaseRvAdapter
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
        //重新请求页面  PS:这里会频繁的创建对象，请求接口生成页面过程中会不断的创建对象，导致内存不断上涨。改用只更新0位置的内容，即更新积分数量。
        //requestRefreshApi()
        p?.refreshTop()
        //重新请求积分任务接口
        PonkoApp.myApi?.tasks()?.enqueue(object : HttpCallBack<StoreTaskBean>() {
            override fun onSuccess(call: Call<StoreTaskBean>?, response: Response<StoreTaskBean>?) {
                val storeTaskBean = response?.body()
                PonkoApp.signInfo = storeTaskBean
            }
        })
    }

    override fun refreshTop(body: ProfileCBean?) {
        if (adapter?.data?.isEmpty()!!)
            return
        adapter?.data!![0] = body!!
        adapter?.notifyItemChanged(0)
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
package com.ponko.cn.module.my

import android.support.v7.widget.DividerItemDecoration
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.MyBean
import com.ponko.cn.bean.ProfileCBean
import com.ponko.cn.module.common.RefreshViewHolder
import com.ponko.cn.module.my.adapter.MyAdapter
import com.ponko.cn.module.my.constract.MyConstract
import com.ponko.cn.module.my.holder.MyViewHolder
import com.ponko.cn.module.my.holder.MyViewHolder2
import com.xm.lib.common.base.mvp.MvpFragment


class MyFrg : MvpFragment<MyConstract.Present>(), MyConstract.View {

    private var viewHolder: RefreshViewHolder? = null
    private var adapter: MyAdapter = MyAdapter()

    override fun presenter(): MyConstract.Present {
        return MyConstract.Present(context, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_nav_my
    }

    override fun findViews(view: View) {
        if (viewHolder == null) {
            viewHolder = RefreshViewHolder.create(view)
        }
    }

    override fun initDisplay() {
        viewHolder?.addItemDecoration(context, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder)
    }

    override fun iniEvent() {
        viewHolder?.srl?.setOnRefreshListener {
            //请求加载数据
            p?.requestMyInfoApi()
        }
        viewHolder?.isCanLoad(viewHolder?.rv, viewHolder?.srl)
    }

    override fun iniData() {
        viewHolder?.srl?.autoRefresh()
    }

    override fun requestMyInfoApiSuccess(body: ProfileCBean?) {
        adapter.addItemViewDelegate(0, MyViewHolder::class.java, MyTopBean::class.java, R.layout.item_my_top)
        adapter.addItemViewDelegate(1, MyViewHolder2::class.java, MyBean::class.java, R.layout.item_my_rv)
        adapter.data = multiTypeData(body)
        //设置适配器
        viewHolder?.rv?.adapter = adapter
        viewHolder?.srl?.finishRefresh()
    }

    private fun multiTypeData(body: ProfileCBean?): List<Any> {
        val data = ArrayList<Any>()
        if (body != null) {
            data.add(body)
        }
        data.add(MyBean.create())
        return data
    }


    private class MyTopBean(val profileCBean: ProfileCBean)
}
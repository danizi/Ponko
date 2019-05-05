package com.ponko.cn.module.free

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.R.id.view
import com.ponko.cn.bean.*
import com.ponko.cn.module.free.adapter.FreeAdapter
import com.ponko.cn.module.free.constract.FreeConstract
import com.ponko.cn.module.free.holder.ExchangedViewHolder
import com.ponko.cn.module.free.holder.TrialViewHolder
import com.ponko.cn.module.free.holder.WechatViewHolder
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.base.rv.decoration.MyItemDecoration

class FreeFrg : MvpFragment<FreeConstract.Present>(), FreeConstract.View {


    private var srl: SmartRefreshLayout? = null
    private var rv: RecyclerView? = null
    private var adapter: FreeAdapter? = FreeAdapter()

    override fun presenter(): FreeConstract.Present {
        return FreeConstract.Present(context, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_nav_free
    }

    override fun findViews(view: View) {
        srl = view.findViewById(R.id.srl) as SmartRefreshLayout
        rv = view.findViewById(R.id.rv) as RecyclerView
    }

    override fun initDisplay() {
        srl?.isEnableRefresh = true
        srl?.isEnableLoadMore = false
        rv?.layoutManager = LinearLayoutManager(context)
        rv?.addItemDecoration(MyItemDecoration.divider(context!!, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder))  //https://www.jianshu.com/p/86aaaa49ed3e
    }

    override fun iniEvent() {
        srl?.setOnRefreshListener {
            //请求加载数据
            p?.requestFreeApi()
        }
    }

    override fun iniData() {
        srl?.autoRefresh()
    }

    override fun requestFreeApiSuccess(body: CoursesCBean?) {
        adapter?.data = multiTypeData(body)
        adapter?.addItemViewDelegate(0, WechatViewHolder::class.java, WechatBean::class.java, R.layout.item_course_introduction)
        adapter?.addItemViewDelegate(1, ExchangedViewHolder::class.java, ExchangedBean::class.java, R.layout.item_course_introduction)
        adapter?.addItemViewDelegate(2, TrialViewHolder::class.java, TrialBean::class.java, R.layout.item_free_course_type)
        rv?.adapter = adapter
        srl?.finishRefresh()
    }

    private fun multiTypeData(body: CoursesCBean?): List<Any> {
        val multiData = ArrayList<Any>()
        if (body?.wechat?.isNotEmpty()!!) {
            multiData.add(WechatBean(body.wechat))
        }
        if (body.exchanged?.isNotEmpty()!!) {
            multiData.add(ExchangedBean(body.exchanged))
        }
        if (body.trial.isNotEmpty()) {
            multiData.add(TrialBean(body.trial[0]))
            multiData.add(TrialBean(body.trial[1]))
        }
        return multiData
    }
}
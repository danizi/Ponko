package com.ponko.cn.module.free

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.R.id.view
import com.ponko.cn.bean.CoursesCBean
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.module.free.constract.FreeConstract
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.base.rv.decoration.MyItemDecoration

class FreeFrg : MvpFragment<FreeConstract.Present>(), FreeConstract.View {


    private var srl: SmartRefreshLayout? = null
    private var rv: RecyclerView? = null
    private var adapter: FreeAdapter? = null

    override fun presenter(): FreeConstract.Present {
        return FreeConstract.Present(context, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_nav_free
    }

    override fun findViews(view: View) {
    }

    override fun initDisplay() {
        srl?.autoRefresh(1000)
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

    }

    override fun requestFreeApiSuccess(body: CoursesCBean?) {
        adapter?.data = multiTypeData(body)
        adapter?.addItemViewDelegate(0, WechatViewHolder::class.java, WechatBean::class.java, R.layout.item_course_introduction)
        adapter?.addItemViewDelegate(1, ExchangedViewHolder::class.java, ExchangedBean::class.java, R.layout.item_course_introduction)
        adapter?.addItemViewDelegate(2, TrialViewHolder::class.java, WechatBean::class.java, R.layout.item_free_course_type)
        rv?.adapter = adapter
    }

    private fun multiTypeData(body: CoursesCBean?): List<Any> {
        val multiData = ArrayList<Any>()
        if (body?.wechat != null) {
            multiData.add(WechatBean(body.wechat))
        }
        if (body?.exchanged != null) {
            multiData.add(ExchangedBean(body.exchanged))
        }
        if (body?.trial != null) {
            multiData.add(TrialBean(body.trial))
        }
        return multiData
    }

    class FreeViewHolder(view: View?) : BaseViewHolder(view!!) {
        override fun bindData(d: Any, position: Int) {

        }
    }

    class FreeAdapter : BaseRvAdapter()

    class WechatBean(val wechat: List<CoursesCBean.Exchanged>?)
    class ExchangedBean(val exchanged: List<CoursesCBean.Exchanged>?)
    class TrialBean(val trial: List<CoursesCBean.TrialBean>?)

    class WechatViewHolder(view: View?) : BaseViewHolder(view!!) {
        override fun bindData(d: Any, position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class ExchangedViewHolder(view: View?) : BaseViewHolder(view!!) {
        override fun bindData(d: Any, position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class TrialViewHolder(view: View?) : BaseViewHolder(view!!) {
        override fun bindData(d: Any, position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

}
package com.ponko.cn.module.my.v1.option.store

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ponko.cn.R
import com.ponko.cn.bean.StoreProfileCMoreBean
import com.ponko.cn.module.my.holder.MyBookViewHolder
import com.ponko.cn.module.my.holder.MyCourseViewHolder
import com.ponko.cn.module.my.v1.constract.ExchangeContract
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.XmStateView
import android.widget.AdapterView



/**
 * ViewPager Fragment页面
 */
@SuppressLint("ValidFragment")
open class ExchangeFrg : MvpFragment<ExchangeContract.Present>(), ExchangeContract.V {

    companion object {
        /**
         * @param cid   课程id、 一般是1 2 3....
         * @param type  列表的类型 一种线性一种网格
         * @param vp    viewpager
         * @param sv
         */
        fun create(cid: Int, type: String/*, vp: ViewPager?, sv: NestedScrollView?*/): ExchangeFrg {
            val fragment = ExchangeFrg()
            val bundle = Bundle()
            bundle.putString("cid", cid.toString())
            bundle.putString("type", type) //暂时提供两种类型列表 书籍和课程
            fragment.arguments = bundle
            return fragment
        }
    }

    private var page: Int = 1
    private var rv: RecyclerView? = null
    //    private var viewState: XmStateView? = null
    private var type: String = "书籍"
    private var cid: String = ""
    private var adapter = object : BaseRvAdapter() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            BKLog.d("StoreAct", "onCreateViewHolder viewType:$viewType")
            return super.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            BKLog.d("StoreAct", "onBindViewHolder holder:$holder position:$position")
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.frg_rv
    }

    override fun initDisplay() {
//        viewState?.showLoading("加载中...")
    }

    override fun findViews(view: View) {
        rv = view.findViewById(R.id.rv)
//        viewState = view.findViewById(R.id.view_state)
    }

    override fun iniEvent() {
        //RecycerView加载更多加这些配置，viewpager第二个页面就会刷不出数据
        rv?.isFocusableInTouchMode = false
//            rv?.isNestedScrollingEnabled = false
//            rv?.requestFocus()
        rv?.setHasFixedSize(true)
    }

    override fun iniData() {
        cid = arguments?.getString("cid")!!
        type = arguments?.getString("type")!!
        p?.reqeustExchangeRefreshApi(cid)
    }

    private var vp: ViewPager? = null
    private var listener: OnRefreshListener? = null
    fun reqeustExchangeRefreshApi(vp: ViewPager?, listener: OnRefreshListener) {
        p?.reqeustExchangeRefreshApi(cid)
        this.vp = vp
        this.listener = listener
    }

    fun reqeustExchangeMoreApi(vp: ViewPager?) {
        p?.reqeustExchangeMoreApi(cid, ++page)
        this.vp = vp
    }

    override fun reqeustExchangeRefreshApiSuccess(body: ArrayList<StoreProfileCMoreBean>?) {
        adapter.data?.clear()
        adapter.data?.addAll(body!![0].stores!!)
        when (body!![0].layout) {
            "bar" -> {
                adapter.addItemViewDelegate(0, MyCourseViewHolder::class.java, Any::class.java, R.layout.item_my_store_course)
                val linearLayoutManager = LinearLayoutManager(context)
                linearLayoutManager.isSmoothScrollbarEnabled = true
                linearLayoutManager.isAutoMeasureEnabled = true
                rv?.layoutManager = linearLayoutManager

            }
            else -> {
                adapter.addItemViewDelegate(0, MyBookViewHolder::class.java, Any::class.java, R.layout.item_my_store_book)
                val gridLayoutManager = GridLayoutManager(context, 2)
                gridLayoutManager.isSmoothScrollbarEnabled = true
                gridLayoutManager.isAutoMeasureEnabled = true
                rv?.layoutManager = gridLayoutManager
            }
        }
        rv?.adapter = adapter
        this.vp?.requestLayout()
        //刷新成功
        listener?.onSuccess()
        //隐藏状态页面
//        viewState?.hide()
    }

    override fun reqeustExchangeMoreApiSuccess(body: ArrayList<StoreProfileCMoreBean>?) {
        val positionStart = adapter.data?.size
        val itemCount = body!![0].stores!!.size
        adapter.data?.addAll(body[0].stores!!)
        adapter.notifyItemRangeInserted(positionStart!!, itemCount)
        adapter.notifyItemChanged(positionStart, itemCount)
//            adapter.notifyDataSetChanged()
        this.vp?.requestLayout()
    }

    override fun presenter(): ExchangeContract.Present {
        return ExchangeContract.Present(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        clear()
    }


    interface OnRefreshListener {
        fun onSuccess()
        fun onFailure()
    }
}


package com.ponko.cn.module.common

import android.app.Activity
import android.os.Build
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.utils.BarUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.XmStateView


/**
 * 刷新加载页面抽象Fragment
 */
abstract class RefreshLoadFrg<P, D> : MvpFragment<P>() {
    protected var page = 1
    private var viewHolder: ViewHolder? = null
    protected var adapter: BaseRvAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_base_refresh_load
    }

    override fun findViews(view: View) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(view)
        }
    }

    override fun initDisplay() {
        viewHolder?.srl?.isEnableRefresh = true
        viewHolder?.rv?.layoutManager = LinearLayoutManager(context)
        viewHolder?.rv?.addItemDecoration(MyItemDecoration.divider(context, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder))  //https://www.jianshu.com/p/86aaaa49ed3e
        viewHolder?.viewState?.showLoading("正在加载中...")
    }

    override fun iniEvent() {
        viewHolder?.srl?.setOnLoadMoreListener {
            requestMoreApi()    //请求加载更多数据
        }
        viewHolder?.srl?.setOnRefreshListener {
            requestRefreshApi() //请求刷新数据
        }
    }

    override fun iniData() {
        //viewHolder?.srl?.autoRefresh(0)  //PS:展现效果没有原生的好。
        requestRefreshApi() //请求刷新数据
        adapter = adapter()
        bindItemViewHolder(bindItemViewHolderData())
    }


    //////////////////////////
    // RecyclerView 绑定item的ViewHolder
    //////////////////////////
    abstract fun bindItemViewHolderData(): BindItemViewHolderBean
    private fun bindItemViewHolder(bindItemViewHolderBean: BindItemViewHolderBean) {
        for (i in 0..(bindItemViewHolderBean.viewType.size - 1)) {
            adapter?.addItemViewDelegate(bindItemViewHolderBean.viewType[i], bindItemViewHolderBean.viewHolderClass[i], bindItemViewHolderBean.beanClass[i], bindItemViewHolderBean.itemViewLayoutId[i])
        }
    }


    //////////////////////////
    // 加载
    //////////////////////////
    abstract fun requestMoreApi()
    open fun requestMoreSuccess(body: D?) {
        val data = body as ArrayList<Any>
        adapter?.data?.addAll(data)
        val positionStart = adapter?.data?.size
        val itemCount = body.size
        adapter?.notifyItemRangeChanged(positionStart!!, itemCount)
        viewHolder?.srl?.finishLoadMore()
    }
    open fun requestMoreFailure() {

    }


    //////////////////////////
    // 刷新
    //////////////////////////
    abstract fun requestRefreshApi()
    open fun requestRefreshSuccess(body: D?) {
        viewHolder?.srl?.finishRefresh()
        adapter?.data?.clear()
        adapter?.data?.addAll(multiTypeData(body))
        //设置适配器
        viewHolder?.rv?.adapter = adapter
        viewHolder?.viewState?.hide()
        viewHolder?.isCanLoad(viewHolder?.rv, viewHolder?.srl) //判断RecyclerView内容的长度是否可以触发上拉加载
    }
    open fun requestRefreshFailure() {

    }
    abstract fun multiTypeData(body: D?): List<Any>
    abstract fun adapter(): BaseRvAdapter?

    //////////////////////////
    // 标题栏,PS:可以放到工具类中,以便以后复用
    //////////////////////////
    protected fun addSearchBar(searchListener:View.OnClickListener,historyListener:View.OnClickListener,helpListener:View.OnClickListener){
//        val barView = ViewUtil.viewById(context,R.layout.bar_search,null)
//        viewHolder?.toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
//        viewHolder?.toolbar?.visibility=View.VISIBLE
//        val tvSearch: TextView? = barView?.findViewById(R.id.tv_search) as TextView
//        val ivHistory: ImageView? =  barView.findViewById(R.id.iv_history) as ImageView
//        val ivHelp: ImageView? = barView.findViewById(R.id.iv_help) as ImageView
//        tvSearch?.setOnClickListener(searchListener)
//        ivHistory?.setOnClickListener(historyListener)
//        ivHelp?.setOnClickListener(helpListener)
        BarUtil.addSearchBar(context,viewHolder?.toolbar,searchListener,historyListener,helpListener)
    }

    protected fun addBar1(title: String){
//        val barView = ViewUtil.viewById(context,R.layout.bar_1,null)
//        viewHolder?.toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
//        viewHolder?.toolbar?.visibility=View.VISIBLE
//        val flBack: FrameLayout? = barView?.findViewById(R.id.fl_back) as FrameLayout
//        val tvTitle: TextView? = barView.findViewById(R.id.tv_title) as TextView
//        flBack?.setOnClickListener {
//            (context as Activity).finish()
//        }
//        tvTitle?.text = title
        BarUtil.addBar1(context,viewHolder?.toolbar,title)
    }

    protected fun addBar2(title: String,barRight:String?="",barRightlistener:View.OnClickListener){
//        val barView = ViewUtil.viewById(context,R.layout.bar_2,null)
//        viewHolder?.toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
//        viewHolder?.toolbar?.visibility=View.VISIBLE
//        val flBack: FrameLayout? = barView?.findViewById(R.id.fl_back) as FrameLayout
//        val tvTitle: TextView? = barView.findViewById(R.id.tv_title) as TextView
//        val tvBarRight: TextView? = barView.findViewById(R.id.tv_bar_right) as TextView
//        flBack?.setOnClickListener {
//            (context as Activity).finish()
//        }
//        tvBarRight?.setOnClickListener(barRightlistener)
//        tvTitle?.text=title
//        tvBarRight?.text=barRight
        BarUtil.addBar2(context,viewHolder?.toolbar,title,barRight,barRightlistener)
    }

    protected fun addBar3(title: String,barRight:String?="",barRightlistener:View.OnClickListener){
//        val barView = ViewUtil.viewById(context,R.layout.bar_3,null)
//        viewHolder?.toolbar?.addView(barView,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
//        viewHolder?.toolbar?.visibility=View.VISIBLE
//        val tvTitle: TextView? = barView?.findViewById(R.id.tv_title) as TextView
//        val tvBarRight: TextView? = barView.findViewById(R.id.tv_bar_right) as TextView
//        tvBarRight?.setOnClickListener(barRightlistener)
//        tvTitle?.text=title
//        tvBarRight?.text=barRight
        BarUtil.addBar3(context,viewHolder?.toolbar,title,barRight,barRightlistener)
    }

    /**
     * 刷新的ViewHolder
     */
    private class ViewHolder private constructor(val toolbar: Toolbar, val srl: SmartRefreshLayout, val rv: RecyclerView, val viewState: XmStateView) {
        companion object {

            fun create(view: View): ViewHolder {
                val toolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
                val srl = view.findViewById<View>(R.id.srl) as SmartRefreshLayout
                val rv = view.findViewById<View>(R.id.rv) as RecyclerView
                val viewState = view.findViewById<View>(R.id.view_state) as XmStateView
                return ViewHolder(toolbar, srl, rv, viewState)
            }
        }

        fun isCanLoad(rv: RecyclerView?, srl: SmartRefreshLayout?) {
            srl?.isEnableLoadMore = true
            val vto = rv?.viewTreeObserver
            vto?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val heigh = rv.computeVerticalScrollExtent()
                    if (heigh < srl?.measuredHeight!!) {
                        BKLog.d("template.measuredHeight" + heigh + "srl.measuredHeight" + srl.measuredHeight)
                        srl.isEnableLoadMore = false
                    }
                    if (vto.isAlive) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            vto.removeOnGlobalLayoutListener(this)
                        }
                    }
                }
            })
        }
    }
}
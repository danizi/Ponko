package com.ponko.cn.module.common

import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewTreeObserver
import com.ponko.cn.R
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.ToastUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.base.rv.decoration.MyDividerItemDecoration
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.XmStateView


/**
 * 刷新加载页面抽象Fragment
 */
abstract class RefreshLoadFrg<P, D> : MvpFragment<P>() {
    protected var page = 1
    protected var viewHolder: ViewHolder? = null
    protected var adapter: BaseRvAdapter? = null
    protected var disableRefresh = false
    protected var disableLoad = false
    protected var addItemDecoration = true

    override fun getLayoutId(): Int {
        return R.layout.activity_base_refresh_load
    }

    override fun findViews(view: View) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(view)
        }
    }

    override fun initDisplay() {
        viewHolder?.srl?.isEnableRefresh = false
        viewHolder?.srl?.isEnableLoadMore = false
        viewHolder?.rv?.layoutManager = LinearLayoutManager(context)
        if (addItemDecoration) {
            viewHolder?.rv?.addItemDecoration(MyDividerItemDecoration.divider(context, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder))  //https://www.jianshu.com/p/86aaaa49ed3e
        }
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
        adapter = adapter()
        requestRefreshApi() //请求刷新数据
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
        ToastUtil.show("加载更多失败...")
    }


    //////////////////////////
    // 刷新
    //////////////////////////
    abstract fun requestRefreshApi()

    open fun requestRefreshSuccess(body: D?) {
        viewHolder?.srl?.finishRefresh()
        adapter?.data?.clear()
        adapter?.data?.addAll(multiTypeData(body))
        if (multiTypeData(body).isEmpty()) {
            viewHolder?.viewState?.showNoData("暂无数据")
        } else {
            //设置适配器
            viewHolder?.rv?.adapter = adapter
            viewHolder?.viewState?.hide()
            if (!disableRefresh) {
                viewHolder?.srl?.isEnableRefresh = true
            }
            if (!disableLoad) {
                viewHolder?.isCanLoad(viewHolder?.rv, viewHolder?.srl) //判断RecyclerView内容的长度是否可以触发上拉加载
            }
        }
    }

    open fun requestRefreshFailure() {
        viewHolder?.viewState?.showError("请求数据失败请重试....", View.OnClickListener {
            requestRefreshApi()
        })
    }

    abstract fun multiTypeData(body: D?): List<Any>
    abstract fun adapter(): BaseRvAdapter?

    //////////////////////////
    // 标题栏,PS:可以放到工具类中,以便以后复用
    //////////////////////////
    @Deprecated("")
    protected fun addSearchBar(searchListener: View.OnClickListener, historyListener: View.OnClickListener, helpListener: View.OnClickListener) {
        BarUtil.addSearchBar(context, viewHolder?.toolbar, searchListener, historyListener, helpListener)
    }

    @Deprecated("")
    protected fun addBar1(title: String) {
        BarUtil.addBar1(context, viewHolder?.toolbar, title)
    }

    @Deprecated("")
    protected fun addBar2(title: String, barRight: String? = "", barRightlistener: View.OnClickListener) {
        BarUtil.addBar2(context, viewHolder?.toolbar, title, barRight, barRightlistener)
    }

    @Deprecated("")
    protected fun addBar3(title: String, barRight: String? = "", barRightlistener: View.OnClickListener) {
        BarUtil.addBar3(context, viewHolder?.toolbar, title, barRight, barRightlistener)
    }

    protected fun isFocusableInTouchMode() {
        viewHolder?.rv?.isFocusableInTouchMode = false
        viewHolder?.rv?.requestFocus()
    }

    /**
     * 刷新的ViewHolder
     */
    class ViewHolder private constructor(val toolbar: Toolbar, val srl: SmartRefreshLayout, val rv: RecyclerView, val viewState: XmStateView, val clContent: ConstraintLayout) {
        companion object {

            fun create(view: View): ViewHolder {
                val toolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
                val srl = view.findViewById<View>(R.id.srl) as SmartRefreshLayout
                val rv = view.findViewById<View>(R.id.rv) as RecyclerView
                val viewState = view.findViewById<View>(R.id.view_state) as XmStateView
                val clContent = view.findViewById<View>(R.id.cl_content) as ConstraintLayout
                return ViewHolder(toolbar, srl, rv, viewState, clContent)
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
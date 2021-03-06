package com.ponko.cn.module.common

import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
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
import com.xm.lib.common.base.rv.decoration.MyDividerItemDecoration
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.XmStateView
import java.lang.ref.WeakReference


/**
 * 刷新加载抽象activity
 */
abstract class RefreshLoadAct<P, D> : PonkoBaseAct<P>() {

    protected var page = 1
    protected var viewHolder: ViewHolder? = null
    protected var adapter: BaseRvAdapter? = null
    protected var disableRefresh = false
    protected var disableLoad = false
    protected var addItemDecoration = true

    override fun setContentViewBefore() {
        super.setContentViewBefore()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_base_refresh_load
    }

    override fun findViews() {
        if (viewHolder == null) {
            val weakReference = WeakReference(this)
            val activity = weakReference.get()!!
            viewHolder = ViewHolder.create(activity)
        }
    }

    override fun initDisplay() {
        viewHolder?.srl?.isEnableRefresh = false
        viewHolder?.srl?.isEnableLoadMore = false
        viewHolder?.rv?.layoutManager = LinearLayoutManager(this)
        if (addItemDecoration) {
            viewHolder?.rv?.addItemDecoration(MyDividerItemDecoration.divider(this, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder))  //https://www.jianshu.com/p/86aaaa49ed3e
        }
        viewHolder?.viewState?.showLoading("正在加载中...")
    }

    override fun iniData() {
        //viewHolder?.srl?.autoRefresh(0)  //PS:展现效果没有原生的好。
        requestRefreshApi() //请求刷新数据
//        adapter = adapter()
//        bindItemViewHolder(bindItemViewHolderData())
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
//        val data = body as ArrayList<Any>
        val data = multiTypeData(body)
        if (multiTypeData(body).isEmpty()) {
            ToastUtil.show("数据已全部加载")
            viewHolder?.srl?.finishLoadMore(0)
            //viewHolder?.viewState?.showNoData("暂无数据...")
        } else {
            adapter?.data?.addAll(data)
            val positionStart = adapter?.data?.size
//            val itemCount = body.size
            val itemCount = data.size
            adapter?.notifyItemRangeChanged(positionStart!!, itemCount)
            viewHolder?.srl?.finishLoadMore()
        }
    }

    open fun requestMoreFailure() {
//        viewHolder?.viewState?.showError("请求数据失败请重试....", View.OnClickListener {
//            requestMoreApi()
//        })
        ToastUtil.show("加载更多失败...")
        viewHolder?.srl?.finishLoadMore()
    }


    //////////////////////////
    // 刷新
    //////////////////////////
    abstract fun requestRefreshApi()

    open fun requestRefreshSuccess(body: D?) {
        viewHolder?.srl?.finishRefresh()

        if (multiTypeData(body).isEmpty()) {
            viewHolder?.viewState?.showNoData("暂无数据")
        } else {
            adapter = adapter()
            bindItemViewHolder(bindItemViewHolderData())
            viewHolder?.rv?.adapter = adapter
            adapter?.data?.clear()
            adapter?.data?.addAll(multiTypeData(body))
            //设置适配器
            viewHolder?.viewState?.hide()
            if (!disableRefresh) {
                viewHolder?.srl?.isEnableRefresh = true
            }
            if (!disableLoad) {
                viewHolder?.isCanLoad(viewHolder?.rv, viewHolder?.srl) //判断RecyclerView内容的长度是否可以触发上拉加载
            }
        }
    }

    open fun requestRefreshFailure(msg: String = "请求数据失败请重试....") {
        viewHolder?.srl?.finishRefresh(0)
        viewHolder?.viewState?.showError(msg, View.OnClickListener {
            requestRefreshApi()
        })
    }

    abstract fun multiTypeData(body: D?): List<Any>
    abstract fun adapter(): BaseRvAdapter?
    override fun iniEvent() {
        viewHolder?.srl?.setOnLoadMoreListener {
            requestMoreApi()    //请求加载更多数据
        }
        viewHolder?.srl?.setOnRefreshListener {
            requestRefreshApi() //请求刷新数据
        }
    }

    protected fun isFocusableInTouchMode() {
        viewHolder?.rv?.isFocusableInTouchMode = false
        viewHolder?.rv?.requestFocus()
    }

    @Deprecated("直接使用BarUtil", ReplaceWith("BarUtil.addBar1(this, viewHolder?.toolbar, title)", "com.ponko.cn.utils.BarUtil"))
    protected fun addBar1(title: String) {
        BarUtil.addBar1(this, viewHolder?.toolbar, title)
    }

    @Deprecated("直接使用BarUtil", ReplaceWith("BarUtil.addBar2(this, viewHolder?.toolbar, title, barRight, barRightlistener)", "com.ponko.cn.utils.BarUtil"))
    protected fun addBar2(title: String, barRight: String? = "", barRightlistener: View.OnClickListener) {
        BarUtil.addBar2(this, viewHolder?.toolbar, title, barRight, barRightlistener)
    }

    @Deprecated("直接使用BarUtil", ReplaceWith("BarUtil.addBar3(this, viewHolder?.toolbar, title, barRight, barRightlistener)", "com.ponko.cn.utils.BarUtil"))
    protected fun addBar3(title: String, barRight: String? = "", barRightlistener: View.OnClickListener) {
        BarUtil.addBar3(this, viewHolder?.toolbar, title, barRight, barRightlistener)
    }

    @Deprecated("直接使用BarUtil", ReplaceWith("BarUtil.addWhiteBar(this, viewHolder?.toolbar, title, barRight, barRightlistener)", "com.ponko.cn.utils.BarUtil"))
    protected fun addWhiteBar(title: String, barRight: String? = "", barRightlistener: View.OnClickListener) {
        BarUtil.addWhiteBar(this, viewHolder?.toolbar, title, barRight, barRightlistener)
    }

    /**
     * 基类ViewHolder
     */
    class ViewHolder private constructor(val toolbar: Toolbar, val srl: SmartRefreshLayout, val rv: RecyclerView, val viewState: XmStateView, val clContent: ConstraintLayout) {
        companion object {

            fun create(act: AppCompatActivity): ViewHolder {
                val toolbar = act.findViewById<View>(R.id.toolbar) as Toolbar
                val srl = act.findViewById<View>(R.id.srl) as SmartRefreshLayout
                val rv = act.findViewById<View>(R.id.rv) as RecyclerView
                val viewState = act.findViewById<View>(R.id.view_state) as XmStateView
                val clContent = act.findViewById<View>(R.id.cl_content) as ConstraintLayout
                return ViewHolder(toolbar, srl, rv, viewState, clContent)
            }
        }

        /**
         * 检查RecyclerView内容是否大于刷新组件的高度 ，如果小于则不能进行底部加载操作
         */
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

    /**
     * 销毁回收资源
     */
    override fun onDestroy() {
        super.onDestroy()
        viewHolder = null
    }
}
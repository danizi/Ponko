package com.ponko.cn.utils

import android.annotation.SuppressLint
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.ponko.cn.bean.StoreProfileBean
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil

/**
 * 帮助类
 */
@SuppressLint("StaticFieldLeak")
object RecyclerViewHelp {
    private var TAG = "RecyclerViewHelp"
    var rv: RecyclerView? = null
    private var page: Int = 0
    private var dividerSize: Int = 0
    private var dividerSizeDefuat: Int = 25
    var isLoad: Boolean = false
    private var listener: OnBottomListener? = null

    fun bind(recycylerView: RecyclerView?, dividerSize: Int) {
        this.rv = recycylerView
        this.dividerSize = dividerSize
        rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //未滑动到底部直接返回当前函数
                if (!isSlideBottom()) {
                    return
                }

                val rvEndBottom = recyclerView.bottom
                val itemHeight = recyclerView.getChildAt(recyclerView.childCount - 1)?.measuredHeight
                val itemEndBottom = recyclerView.getChildAt(recyclerView.childCount - 1)?.bottom
                val dividerSpace = ScreenUtil.dip2px(recyclerView.context!!, dividerSize + dividerSizeDefuat) //dividerSizeDefuat的左右是减少滑动底部的距离

                if ((rvEndBottom - dividerSize) <= (itemEndBottom!! + dividerSpace) && !isLoad) {
                    /*
                     * 你可能要做的
                     * 1 到达底部，删除底部数据
                     * 2 请求更多数据，并添加一个底部数据，最后调用adapter.notifyDataSetChanged刷新所有数据
                     * 3 累加加载页数
                     */
                    listener?.onStern()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun isSlideBottom(): Boolean {
        val FirstVisiblePosition = (rv?.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        val lastVisiblePosition = (rv?.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
        BKLog.d(TAG, "FirstVisiblePosition:" + FirstVisiblePosition + "lastVisiblePosition:" + lastVisiblePosition)
        //item 没有数据
        if (rv?.layoutManager?.itemCount!! <= 0)
            return false
        //最后一个可见位置小于item的size
        if (lastVisiblePosition < (rv?.layoutManager?.itemCount!! - 1))
            return false
        //“总”Item size数小于“可见”Item size
        if (rv?.layoutManager!!.itemCount < rv?.layoutManager!!.childCount)
            return false
        return true
    }

    /**
     * 添加数据并刷新UI界面
     * @param oldDatas 源数据集合
     * @param datas    需要添加的数据集合
     * @param scrollY  竖向滑动距离
     */
    fun addDataAndNotify(datas: ArrayList<StoreProfileBean.ListBean.StoresBean>?, addDatas: ArrayList<StoreProfileBean.ListBean.StoresBean>?, scrollY: Int) {

        if (addDatas?.isNotEmpty()!! && datas?.isNotEmpty()!!) {
            val position = datas?.size!!
            rv?.adapter?.notifyItemInserted(position)

            datas?.addAll(addDatas)

            rv?.adapter?.notifyItemRangeChanged(position, (datas.size - 1))

            smoothScrollBy(0, scrollY)
            isLoad = false
        }
    }

    /**
     * 设置rv滑动距离
     */
    fun smoothScrollBy(scrollX: Int, scrollY: Int) {
        rv?.smoothScrollBy(scrollX, scrollY)
    }

    /**
     * 添加底部监听
     */
    fun addOnBottomListener(listener: OnBottomListener) {
        this.listener = listener
    }
}

interface OnBottomListener {
    fun onStern()
}
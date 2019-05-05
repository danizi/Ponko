package com.ponko.cn.module

import android.content.Context
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import com.ponko.cn.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.log.BKLog

class RefreshViewHolder private constructor(val srl: SmartRefreshLayout, val rv: RecyclerView) {
    companion object {
        fun create(rootView: View): RefreshViewHolder {
            val srl = rootView.findViewById<View>(R.id.srl) as SmartRefreshLayout
            val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
            return RefreshViewHolder(srl, rv)
        }
    }

    fun addItemDecoration(context: Context?, orientation: Int, id: Int) {
        srl.isEnableRefresh = true
        rv.layoutManager = LinearLayoutManager(context)
        //rv.addItemDecoration(MyItemDecoration.divider(context, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder))  //https://www.jianshu.com/p/86aaaa49ed3e
        rv.addItemDecoration(MyItemDecoration.divider(context!!, orientation, id))
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
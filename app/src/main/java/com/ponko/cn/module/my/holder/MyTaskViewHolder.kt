package com.ponko.cn.module.my.holder

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.ponko.cn.R
import com.xm.lib.common.base.rv.BaseViewHolder
import android.support.v7.widget.RecyclerView
import com.ponko.cn.bean.MyTaskBean
import com.ponko.cn.bean.StoreTaskBean
import com.xm.lib.common.base.rv.BaseRvAdapter


class MyTaskViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val view2: View, val tvTitle: TextView, val rv: RecyclerView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val view2 = rootView.findViewById(R.id.view2) as View
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return ViewHolder(view2, tvTitle, rv)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val context = itemView.context
        val myTaskBean = d as MyTaskBean
        val adapter = object : BaseRvAdapter() {}
        adapter.addItemViewDelegate(0, ItemTaskViewHolder::class.java, Any::class.java, R.layout.item_my_store_integral_task)
        adapter.data?.addAll(myTaskBean.tasks)
        viewHolder?.rv?.adapter = adapter
        viewHolder?.rv?.layoutManager = LinearLayoutManager(context)
    }

    private class ItemTaskViewHolder(view: View) : BaseViewHolder(view) {

        private class ViewHolder private constructor(val tvIntegralNum: TextView, val tvDes1: TextView, val tvDes2: TextView, val tvState: TextView) {
            companion object {

                fun create(rootView: android.support.constraint.ConstraintLayout): ViewHolder {
                    val tvIntegralNum = rootView.findViewById<View>(R.id.tv_integral_num) as TextView
                    val tvDes1 = rootView.findViewById<View>(R.id.tv_des1) as TextView
                    val tvDes2 = rootView.findViewById<View>(R.id.tv_des2) as TextView
                    val tvState = rootView.findViewById<View>(R.id.tv_state) as TextView
                    return ViewHolder(tvIntegralNum, tvDes1, tvDes2, tvState)
                }
            }
        }

        override fun bindData(d: Any, position: Int) {

        }
    }
}
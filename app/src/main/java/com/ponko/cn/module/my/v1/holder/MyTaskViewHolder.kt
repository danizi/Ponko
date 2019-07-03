package com.ponko.cn.module.my.holder

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.MyTaskBean
import com.ponko.cn.bean.StoreTaskBean
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder

/**
 * 签到任务展示
 */
class MyTaskViewHolder(view: View) : BaseViewHolder(view) {
    /**
     * 签到任务展示 UI
     */
    private var viewHolder: ViewHolder? = null

    /**
     * 数据绑定
     */
    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val context = itemView.context
        val myTaskBean = d as MyTaskBean
        val adapter = object : BaseRvAdapter() {}
        adapter.addItemViewDelegate(0, ItemTaskViewHolder::class.java, StoreTaskBean.TasksBean::class.java, R.layout.item_my_store_integral_task)
        adapter.data?.addAll(myTaskBean.tasks)
        viewHolder?.rv?.adapter = adapter
        viewHolder?.rv?.layoutManager = LinearLayoutManager(context)
        viewHolder?.rv?.isFocusableInTouchMode = false
        viewHolder?.rv?.requestFocus()
    }

    /**
     * 任务item
     */
    open class ItemTaskViewHolder(view: View) : BaseViewHolder(view) {

        private var ui: ViewHolder? = null
        private var context: Context? = null
        private var tasksBean: StoreTaskBean.TasksBean? = null

        @SuppressLint("SetTextI18n")
        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = ViewHolder.create(itemView)
            }
            context = itemView.context
            tasksBean = d as StoreTaskBean.TasksBean
            ui?.tvIntegralNum?.text = "+" + tasksBean?.scores.toString()
            ui?.tvDes1?.text = tasksBean?.name
            ui?.tvDes2?.text = tasksBean?.summary

            if (tasksBean?.isCompleted!!) {
                taskComplete()
            } else {
                taskUnComplete()
            }
        }

        /**
         * 任务完成展示
         */
        private fun taskComplete() {
            ui?.tvIntegralNum?.isEnabled = false
            ui?.tvDes1?.isEnabled = false
            ui?.tvDes2?.isEnabled = false
            ui?.tvState?.isEnabled = false
            ui?.tvState?.text = "已完成"
        }

        /**
         * 任务未完成展示
         */
        private fun taskUnComplete() {
            ui?.tvIntegralNum?.isEnabled = true
            ui?.tvDes1?.isEnabled = true
            ui?.tvDes2?.isEnabled = true
            ui?.tvState?.isEnabled = true
            ui?.tvState?.text = "立即前往"
            itemView.setOnClickListener {
                IntoTargetUtil.target(context, tasksBean?.linkType, tasksBean?.linkValue)
            }
        }

        /**
         * 任务item UI
         */
        private class ViewHolder private constructor(val tvIntegralNum: TextView, val tvDes1: TextView, val tvDes2: TextView, val tvState: TextView) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val tvIntegralNum = rootView.findViewById<View>(R.id.tv_integral_num) as TextView
                    val tvDes1 = rootView.findViewById<View>(R.id.tv_des1) as TextView
                    val tvDes2 = rootView.findViewById<View>(R.id.tv_des2) as TextView
                    val tvState = rootView.findViewById<View>(R.id.tv_state) as TextView
                    return ViewHolder(tvIntegralNum, tvDes1, tvDes2, tvState)
                }
            }
        }
    }

    /**
     * 签到任务展示 UI
     */
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
}
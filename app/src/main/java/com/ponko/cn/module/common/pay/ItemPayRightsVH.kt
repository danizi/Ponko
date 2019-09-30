package com.ponko.cn.module.common.pay

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ItemPayRightsBean
import com.xm.lib.common.base.rv.decoration.MyDividerItemDecoration
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2
import com.xm.lib.common.log.BKLog

/**
 * 入学权益ViewHolder
 */
class ItemPayRightsVH(view: View) : BaseViewHolderV2(view) {

    private var ui: UI? = null

    override fun onBind(data: Any) {
        if (ui == null) {
            ui = UI.create(itemView)
        }
        val ctx = itemView.context
        if (data is ItemPayRightsBean) {
            val itemPayRightsBean = data as ItemPayRightsBean

            //设置标题
            ui?.tvTitle?.text = itemPayRightsBean.title

//            if (itemPayRightsBean.list.isEmpty()) {
//                itemView.visibility = View.GONE
//                return
//            }

            //设置嵌套RecyclerView数据，即购买课程
            val adapter = BaseRvAdapterV2.Builder()
                    .addHolderFactory(ItemPayRightsSubVH.Factory())
                    .addDataResouce(itemPayRightsBean.list)
                    .build()
            ui?.rv?.isFocusableInTouchMode = false
            ui?.rv?.layoutManager = LinearLayoutManager(ctx)
            ui?.rv?.addItemDecoration(MyDividerItemDecoration.divider(ctx, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
            ui?.rv?.adapter = adapter
        } else {
            BKLog.e("data not ItemPayRightsBean")
        }
    }

    override fun onClick(v: View?) {

    }

    class Factory : BaseViewHolderV2.Factory() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return ItemPayRightsVH(getView(parent.context, parent, R.layout.item_pay_rights))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(ItemPayRightsBean::class.java, "ItemPayRightsVH")
        }

    }

    private class UI private constructor(val tvTitle: TextView, val divider: View, val rv: RecyclerView) {
        companion object {

            fun create(rootView: View): UI {
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val divider = rootView.findViewById(R.id.divider) as View
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return UI(tvTitle, divider, rv)
            }
        }
    }
}
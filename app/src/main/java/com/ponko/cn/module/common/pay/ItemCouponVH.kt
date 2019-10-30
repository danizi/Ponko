package com.ponko.cn.module.common.pay

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ItemPayCouponsBean
import com.ponko.cn.module.web.WebAct
import com.xm.lib.common.base.rv.decoration.MyDividerItemDecoration
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2
import com.xm.lib.common.log.BKLog

/**
 * 折扣劵VH
 */
class ItemCouponVH(val view: View, private val listener: ItemCouponSubVH.IItemCouponListener) : BaseViewHolderV2(view) {

    private var ui: UI? = null

    private var adapter: BaseRvAdapterV2? = null

    override fun onBind(data: Any) {
        if (ui == null) {
            ui = UI.create(itemView)
        }
        val ctx = itemView.context
        if (data is ItemPayCouponsBean) {
            val itemPayCouponsBean = data as ItemPayCouponsBean

            //设置标题
            ui?.tvTitle?.text = itemPayCouponsBean.title

            //更多是否显示
            if (itemPayCouponsBean.isMoreHave) {
                ui?.tvMore?.visibility = View.VISIBLE
                ui?.tvMore?.text = itemPayCouponsBean.moreText
                ui?.tvMore?.setOnClickListener {
                    WebAct.start(ctx, "url", itemPayCouponsBean.moreUrl)
                }
            } else {
                ui?.tvMore?.visibility = View.INVISIBLE
            }

            //设置嵌套RecyclerView数据，即购买课程
            if (adapter == null) {
                adapter = BaseRvAdapterV2.Builder()
                        .addHolderFactory(ItemCouponSubVH.Factory(listener))
                        .addDataResouce(itemPayCouponsBean.list)
                        .build()
                ui?.rv?.isFocusableInTouchMode = false
                ui?.rv?.layoutManager = LinearLayoutManager(ctx)
                ui?.rv?.addItemDecoration(MyDividerItemDecoration.divider(ctx, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
                ui?.rv?.adapter = adapter
            }else{
                adapter?.getDataSource()?.clear()
                adapter?.getDataSource()?.addAll(itemPayCouponsBean.list)
            }
        } else {
            BKLog.e("data not ItemPayCouponsBean")
        }
    }

    override fun onClick(v: View?) {

    }

    class Factory(private val listener: ItemCouponSubVH.IItemCouponListener) : BaseViewHolderV2.Factory() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return ItemCouponVH(getView(parent.context, parent, R.layout.item_pay_coupon), listener)
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(ItemPayCouponsBean::class.java, "ItemCouponVH")
        }
    }

    private class UI private constructor(val tvTitle: TextView, val tvMore: TextView, val divider: View, val rv: RecyclerView) {
        companion object {

            fun create(rootView: View): UI {
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val tvMore = rootView.findViewById<View>(R.id.tv_more) as TextView
                val divider = rootView.findViewById(R.id.divider) as View
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return UI(tvTitle, tvMore, divider, rv)
            }
        }
    }
}
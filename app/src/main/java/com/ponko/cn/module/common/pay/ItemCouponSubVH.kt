package com.ponko.cn.module.common.pay

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ProductInfoCBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2
import com.xm.lib.common.log.BKLog


/**
 * 子类
 */
class ItemCouponSubVH(val view: View, val listener: IItemCouponListener) : BaseViewHolderV2(view) {

    private var ui: UI? = null
    private var adapter: BaseRvAdapterV2? = null
    override fun onBind(data: Any) {}

    override fun onBind(adapter: BaseRvAdapterV2, data: Any, position: Int) {
        super.onBind(adapter, data, position)
        this.adapter = adapter
        if (data is ProductInfoCBean.ListBeanX.ListBean) {
            if (ui == null) {
                ui = UI.create(itemView)
            }
            val curBean = data as ProductInfoCBean.ListBeanX.ListBean
            Glide.with(itemView.context, curBean.icon, ui?.iv, 0)
            ui?.des?.text = curBean.name
            ui?.ivCheck?.isEnabled = !curBean.defaultX!!
        } else {
            BKLog.e("data not ListBean")
        }
    }

    override fun onClick(v: View?) {
        val bean = adapter?.getDataSource()!![pos] as ProductInfoCBean.ListBeanX.ListBean
        for (data in adapter?.getDataSource()!!) {
            val d = data as ProductInfoCBean.ListBeanX.ListBean
            d.defaultX = bean.name == d.name
        }
        adapter?.notifyDataSetChanged()
        listener.select(bean.id, adapter?.getDataSource()!!,adapter)
    }

    interface IItemCouponListener {
        fun select(tid: String, dataSource: ArrayList<Any>,adapter:BaseRvAdapterV2?)
    }

    class Factory(val listener: IItemCouponListener) : BaseViewHolderV2.Factory() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return ItemCouponSubVH(getView(parent.context, parent, R.layout.item_pay_coupon_sub), listener)
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(Any::class.java, "ItemCouponSubVH")
        }
    }

    private class UI private constructor(val iv: ImageView, val des: TextView, val ivCheck: ImageView) {
        companion object {

            fun create(rootView: View): UI {
                val iv = rootView.findViewById<View>(R.id.iv) as ImageView
                val des = rootView.findViewById<View>(R.id.des) as TextView
                val ivCheck = rootView.findViewById<View>(R.id.iv_check) as ImageView
                return UI(iv, des, ivCheck)
            }
        }
    }

}
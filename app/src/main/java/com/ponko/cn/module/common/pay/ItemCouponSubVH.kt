package com.ponko.cn.module.common.pay

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2


/**
 * 子类
 */
class ItemCouponSubVH(val view: View) : BaseViewHolderV2(view) {

    override fun onBind(data: Any) {

    }

    override fun onClick(v: View?) {

    }

    class Factory : BaseViewHolderV2.Factory() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return ItemCouponSubVH(getView(parent.context, parent, R.layout.item_pay_coupon_sub))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(Any::class.java, "ItemCouponSubVH")
        }
    }

    private class UI private constructor(val iv: ImageView, val des: TextView, val ivCheck: ImageView) {
        companion object {

            fun create(rootView: android.support.constraint.ConstraintLayout): UI {
                val iv = rootView.findViewById<View>(R.id.iv) as ImageView
                val des = rootView.findViewById<View>(R.id.des) as TextView
                val ivCheck = rootView.findViewById<View>(R.id.iv_check) as ImageView
                return UI(iv, des, ivCheck)
            }
        }
    }

}
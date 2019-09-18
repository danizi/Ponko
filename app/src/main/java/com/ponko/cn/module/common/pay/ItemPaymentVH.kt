package com.ponko.cn.module.common.pay

import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ItemPaymentBean
import com.ponko.cn.module.web.WebAct
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2


/**
 * 支付方式ViewHolder
 */
class ItemPaymentVH(view: View, val listener: IPaymentListener?) : BaseViewHolderV2(view) {

    private var ui: UI? = null

    override fun onBind(data: Any) {
        if (ui == null) {
            ui = UI.create(itemView)
        }

        ui?.ivAliCheck?.isEnabled = true
        ui?.ivWxCheck?.isEnabled = false

        ui?.clWx?.setOnClickListener {
            ui?.ivAliCheck?.isEnabled = true
            ui?.ivWxCheck?.isEnabled = false
            listener?.select(0)
        }
        ui?.clAli?.setOnClickListener {
            ui?.ivWxCheck?.isEnabled = true
            ui?.ivAliCheck?.isEnabled = false
            listener?.select(1)
        }
        ui?.tvPayAgreement?.setOnClickListener {
            WebAct.start(itemView.context, "url", "https://www.baidu.com/")
        }

    }

    override fun onClick(v: View?) {

    }

    class Factory(val listener: IPaymentListener?) : BaseViewHolderV2.Factory() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return ItemPaymentVH(getView(parent.context, parent, R.layout.item_pay_ment), listener)
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(ItemPaymentBean::class.java, "ItemPaymentVH")
        }
    }

    private class UI private constructor(
            val tvTitle: TextView,
            val divider: View,
            val clWx: ConstraintLayout,
            val ivWx: ImageView,
            val ivWxCheck: ImageView,
            val divider2: View,
            val clAli: ConstraintLayout,
            val ivAli: ImageView,
            val ivAliCheck: ImageView,
            val tvPayAgreement: TextView) {
        companion object {

            fun create(rootView: View): UI {
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val divider = rootView.findViewById(R.id.divider) as View
                val clWx = rootView.findViewById<View>(R.id.cl_wx) as ConstraintLayout
                val ivWx = rootView.findViewById<View>(R.id.iv_wx) as ImageView
                val ivWxCheck = rootView.findViewById<View>(R.id.iv_wx_check) as ImageView
                val divider2 = rootView.findViewById(R.id.divider2) as View
                val clAli = rootView.findViewById<View>(R.id.cl_ali) as ConstraintLayout
                val ivAli = rootView.findViewById<View>(R.id.iv_ali) as ImageView
                val ivAliCheck = rootView.findViewById<View>(R.id.iv_ali_check) as ImageView
                val tvPayAgreement = rootView.findViewById<View>(R.id.tv_pay_agreement) as TextView
                return UI(tvTitle, divider, clWx, ivWx, ivWxCheck, divider2, clAli, ivAli, ivAliCheck, tvPayAgreement)
            }
        }
    }

    /**
     * 支付类型
     */
    interface IPaymentListener {
        /**
         * @param type 0 微信 1 阿里
         */
        fun select(type: Int)
    }
}
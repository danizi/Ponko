package com.ponko.cn.module.common.pay

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ProductInfoCBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.module.web.WebAct
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2


class ItemPayAgreementVH(view: View) : BaseViewHolderV2(view) {

    private var ui: UI? = null

    override fun onBind(data: Any) {
        if (ui == null) {
            ui = UI.create(itemView)
        }
        val footer = data as ProductInfoCBean.FooterBean
        //底部协议
        val url = if (footer.url?.startsWith("http", true)!! || footer?.url?.startsWith("https", true)!!) {
            footer.url!!
        } else {
            Constants.BASE_API + footer.url!!
        }
        ui?.tvPayAgreement?.text = footer.text
        ui?.tvPayAgreement?.setOnClickListener {
            WebAct.start(itemView.context, "url", url)
        }
    }

    override fun onClick(v: View?) {

    }

    class Factory : BaseViewHolderV2.Factory() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return ItemPayAgreementVH(getView(parent.context, parent, R.layout.item_pay_agreement))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(ProductInfoCBean.FooterBean::class.java, "ItemPayAgreementVH")
        }
    }

    private class UI private constructor(val tvPayAgreement: TextView) {
        companion object {

            fun create(rootView: View): UI {
                val tvPayAgreement = rootView.findViewById<View>(R.id.tv_pay_agreement) as TextView
                return UI(tvPayAgreement)
            }
        }
    }

}
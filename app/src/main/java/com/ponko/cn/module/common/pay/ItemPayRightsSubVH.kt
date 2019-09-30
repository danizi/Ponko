package com.ponko.cn.module.common.pay

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ProductInfoCBean
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2


/**
 * 课程
 */
class ItemPayRightsSubVH(view: View) : BaseViewHolderV2(view) {

    private var ui: UI? = null
    override fun onBind(data: Any) {
        if (ui == null) {
            ui = UI.create(itemView)
        }
        if (data is ProductInfoCBean.ListBeanX.ListBean) {
            val listBean = data as ProductInfoCBean.ListBeanX.ListBean
            ui?.tvEntranceRights?.text = listBean.text
        }
    }

    override fun onClick(v: View?) {

    }

    class Factory : BaseViewHolderV2.Factory() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return ItemPayRightsSubVH(getView(parent.context, parent, R.layout.item_pay_sub_rights))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(Any::class.java, "ItemPayLessonSubVH")
        }
    }

    private class UI private constructor(val tvEntranceRights: TextView) {
        companion object {

            fun create(rootView: View): UI {
                val tvEntranceRights = rootView.findViewById<View>(R.id.tv_entrance_rights) as TextView
                return UI(tvEntranceRights)
            }
        }
    }

}
package com.ponko.cn.module.common.pay

import android.view.View
import android.view.ViewGroup
import com.ponko.cn.R
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2

/**
 * 课程
 */
class ItemPayLessonSubVH(view: View) : BaseViewHolderV2(view) {

    override fun onBind(data: Any) {

    }

    override fun onClick(v: View?) {

    }

    class Factory : BaseViewHolderV2.Factory() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return ItemPayLessonSubVH(getView(parent.context, parent, R.layout.item_pay_sub_course))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(Any::class.java, "ItemPayLessonSubVH")
        }
    }

}
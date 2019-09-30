package com.ponko.cn.module.common.pay

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ProductInfoCBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2
import com.xm.lib.common.log.BKLog


/**
 * 课程
 */
class ItemPayLessonSubVH(view: View) : BaseViewHolderV2(view) {

    private var ui: UI? = null

    override fun onBind(data: Any) {
        if (ui == null) {
            ui = UI.create(itemView)
        }
        if (data is ProductInfoCBean.ListBeanX.ListBean) {
            val listBean = data as ProductInfoCBean.ListBeanX.ListBean
            Glide.with(itemView.context, listBean.avatar, ui?.ivHead)
            ui?.tvPrice?.text = listBean.price
            ui?.tvCourseName?.text = listBean.title
        } else {
            BKLog.e("data not ListBean")
        }
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

    private class UI private constructor(val ivHead: ImageView, val tvCourseName: TextView, val tvPrice: TextView) {
        companion object {

            fun create(rootView: View): UI {
                val ivHead = rootView.findViewById<View>(R.id.iv_head) as ImageView
                val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                val tvPrice = rootView.findViewById<View>(R.id.tv_price) as TextView
                return UI(ivHead, tvCourseName, tvPrice)
            }
        }
    }

}
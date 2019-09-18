package com.ponko.cn.module.common.pay

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ItemPayLessonBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.decoration.MyDividerItemDecoration
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2
import com.xm.lib.component.CircleImageView


/**
 * 入学课程ViewHolder
 */
class ItemPayLessonVH(view: View) : BaseViewHolderV2(view) {

    private var ui: UI? = null

    override fun onBind(data: Any) {
        if (ui == null) {
            ui = UI.create(itemView)
        }
        val ctx = itemView.context
        val itemPayLessonBean = data as ItemPayLessonBean

        //设置个人信息
        Glide.with(ctx, "", ui?.ivHead)
        ui?.tvName?.text = "学员名称"
        ui?.tvPhone?.text = "150****0708"

        //设置嵌套RecyclerView数据，即购买课程
        ui?.rv?.layoutManager = LinearLayoutManager(ctx)
        ui?.rv?.addItemDecoration( MyDividerItemDecoration.divider(ctx, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
        val adapter = BaseRvAdapterV2.Builder()
                .addHolderFactory(ItemPayLessonSubVH.Factory())
                .addDataResouce(itemPayLessonBean.subDatas)
                .build()
        ui?.rv?.adapter = adapter

    }

    override fun onClick(v: View?) {

    }


    class Factory : BaseViewHolderV2.Factory() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return ItemPayLessonVH(getView(parent.context, parent, R.layout.item_pay_lesson))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(ItemPayLessonBean::class.java, "ItemPayLessonVH")
        }
    }

    private class UI private constructor(
            val clHead: ConstraintLayout,
            val ivHead: CircleImageView,
            val tvName: TextView,
            val tvPhone: TextView,
            val tvTitle: TextView,
            val divider: View,
            val rv: RecyclerView) {
        companion object {

            fun create(rootView: View): UI {
                val clHead = rootView.findViewById<View>(R.id.cl_head) as ConstraintLayout
                val ivHead = rootView.findViewById<View>(R.id.iv_head) as CircleImageView
                val tvName = rootView.findViewById<View>(R.id.tv_name) as TextView
                val tvPhone = rootView.findViewById<View>(R.id.tv_phone) as TextView
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val divider = rootView.findViewById(R.id.divider) as View
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return UI(clHead, ivHead, tvName, tvPhone, tvTitle, divider, rv)
            }
        }
    }
}
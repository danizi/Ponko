package com.ponko.cn.module.interflow.holder

import android.view.View
import android.widget.ImageView
import com.ponko.cn.R
import com.ponko.cn.bean.ActivityCBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder

class SubActivityHolder(view: View?) : BaseViewHolder(view!!) {

    private class ViewHolder private constructor(val ivActivity: ImageView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val ivActivity = rootView.findViewById<View>(R.id.iv_activity) as ImageView
                return ViewHolder(ivActivity)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val activityCBean = d as ActivityCBean
        val context = itemView.context
        Glide.with(context, activityCBean.image, viewHolder?.ivActivity)
        itemView.setOnClickListener {

        }
    }
}
package com.ponko.cn.module.interflow.holder

import android.app.Activity
import android.view.View
import android.widget.ImageView
import com.ponko.cn.R
import com.ponko.cn.bean.ActivityCBean
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.v1.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil

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
        //图片高度自适应
        val present = 155f / 347f
        val layoutParams = viewHolder?.ivActivity?.layoutParams
        layoutParams?.height = ((ScreenUtil.getNormalWH(context as Activity)[0] - ScreenUtil.dip2px(context, 30)) * present).toInt()
        viewHolder?.ivActivity?.layoutParams = layoutParams

        itemView.setOnClickListener {
            BKLog.d("点击广告")
            IntoTargetUtil.target(context,"url", activityCBean.url)
        }
    }
}
package com.ponko.cn.module.free.holder

import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.CoursesCBean
import com.ponko.cn.module.free.FreeDetailsAct
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog

class CourseViewHolder(view: View?) : BaseViewHolder(view!!) {

    private class ViewHolder private constructor(val clVideo: ConstraintLayout, val ivAvatar: ImageView, val ivPlayPause: ImageView, val tvTitle: TextView, val clDetail: LinearLayout, val tvDes: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val clVideo = rootView.findViewById<View>(R.id.cl_video) as ConstraintLayout
                val ivAvatar = rootView.findViewById<View>(R.id.iv_avatar) as ImageView
                val ivPlayPause = rootView.findViewById<View>(R.id.iv_play_pause) as ImageView
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val clDetail = rootView.findViewById<View>(R.id.cl_detail) as LinearLayout
                val tvDes = rootView.findViewById<View>(R.id.tv_des) as TextView
                return ViewHolder(clVideo, ivAvatar, ivPlayPause, tvTitle, clDetail, tvDes)
            }
        }
    }


    private var v: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (v == null) {
            v = ViewHolder.create(itemView)
        }
        val context = itemView.context
        val listBean = d as CoursesCBean.TrialBean.ListBean
        v?.tvTitle?.text = listBean.title
        v?.tvDes?.text = listBean.summary
        v?.clDetail?.setOnClickListener {
            BKLog.d("详情页面")
            FreeDetailsAct.start(context, listBean.id)
        }
        v?.clVideo?.setOnClickListener {
            BKLog.d("详情页面")
            FreeDetailsAct.start(context, listBean.id)
        }
        Glide.with(context, listBean.avatar, v?.ivAvatar)
    }
}
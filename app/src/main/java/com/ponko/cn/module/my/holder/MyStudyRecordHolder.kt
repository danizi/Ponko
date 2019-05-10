package com.ponko.cn.module.my.holder

import android.annotation.SuppressLint
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.RecordCBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil

class MyStudyRecordHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val clDivider: ConstraintLayout, val ivIcon: ImageView, val tvDate: TextView, val ivCover: ImageView, val tvTitle: TextView, val tvRecord: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val clDivider = rootView.findViewById<View>(R.id.cl_divider) as ConstraintLayout
                val ivIcon = rootView.findViewById<View>(R.id.iv_icon) as ImageView
                val tvDate = rootView.findViewById<View>(R.id.tv_date) as TextView
                val ivCover = rootView.findViewById<View>(R.id.iv_cover) as ImageView
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val tvRecord = rootView.findViewById<View>(R.id.tv_record) as TextView
                return ViewHolder(clDivider, ivIcon, tvDate, ivCover, tvTitle, tvRecord)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    @SuppressLint("SetTextI18n")
    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val historyRecordBean = d as RecordCBean.HistoryRecordBean
        val context = itemView.context
        val p = (historyRecordBean.position.toFloat() * 100f / historyRecordBean.durationForSecond.toFloat()).toInt()
        Glide.with(context, historyRecordBean.avatar, viewHolder?.ivCover)
        viewHolder?.tvTitle?.text = historyRecordBean.sectionName
        viewHolder?.tvRecord?.text = "${TimeUtil.hhmmss(historyRecordBean.durationForSecond * 1000L)} | 已学习 $p %"
        itemView.setOnClickListener {
            BKLog.d("跳转到课程中...")
        }
    }
}
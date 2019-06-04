package com.ponko.cn.module.interflow.holder

import android.view.View
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.AnalysisCBean
import com.ponko.cn.constant.Constants.BASE_API
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil


class SubCaseViewHolder(view: View?) : BaseViewHolder(view!!) {

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val caseBean = d as AnalysisCBean.ActivitiesBean
        val context = itemView.context
        viewHolder?.tvTitle?.text = caseBean.title
        viewHolder?.tvSummary?.text = caseBean.brief
        viewHolder?.tvTime?.text = TimeUtil.yyyyMMdd(caseBean.createTime)
        itemView.setOnClickListener {
            BKLog.d("点击案例"+caseBean.targetType+caseBean.url)

            IntoTargetUtil.target(context,caseBean.targetType, BASE_API + "analysis/detail?id=${caseBean.id}")
        }
    }

    private class ViewHolder private constructor(val tvTitle: TextView, val tvSummary: TextView, val tvTime: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val tvSummary = rootView.findViewById<View>(R.id.tv_summary) as TextView
                val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                return ViewHolder(tvTitle, tvSummary, tvTime)
            }
        }
    }

}
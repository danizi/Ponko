package com.ponko.cn.module.interflow.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ponko.cn.R
import com.ponko.cn.bean.AnalysisCBean
import com.ponko.cn.module.interflow.holder.SubCaseViewHolder
import com.xm.lib.common.util.ViewUtil

class SubCaseAdapter(val data: ArrayList<AnalysisCBean.ActivitiesBean>) : RecyclerView.Adapter<SubCaseViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SubCaseViewHolder {
        val view = ViewUtil.viewById(p0.context, R.layout.item_interflow_case, p0)
        return SubCaseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (data.isEmpty()) {
            0
        } else {
            data.size
        }
    }

    override fun onBindViewHolder(p0: SubCaseViewHolder, p1: Int) {
        p0.bindData(data[p1], p1)
    }
}
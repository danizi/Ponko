package com.ponko.cn.module.interflow.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ponko.cn.R
import com.ponko.cn.bean.ActivityCBean
import com.ponko.cn.module.interflow.holder.SubActivityHolder
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.util.ViewUtil

//class SubActivityAdapter(val data: ArrayList<ActivityCBean>) : RecyclerView.Adapter<SubActivityHolder>() {
//    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SubActivityHolder {
//        val view = ViewUtil.viewById(p0.context, R.layout.item_interflow_activity, p0)
//        return SubActivityHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return if (data.isEmpty()) {
//            0
//        } else {
//            data.size
//        }
//    }
//
//    override fun onBindViewHolder(p0: SubActivityHolder, p1: Int) {
//        p0.bindData(data[p1], p1)
//    }
//}

class SubActivityAdapter:BaseRvAdapter()
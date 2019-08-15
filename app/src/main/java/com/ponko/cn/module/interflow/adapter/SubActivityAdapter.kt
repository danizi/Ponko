package com.ponko.cn.module.interflow.adapter

import com.xm.lib.common.base.rv.v1.BaseRvAdapter

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

class SubActivityAdapter: BaseRvAdapter()
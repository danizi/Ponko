package com.ponko.cn.module.free.v1.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ponko.cn.R
import com.ponko.cn.module.free.v1.holder.CourseViewHolder
import com.xm.lib.common.util.ViewUtil

class CourseAdapter(val data: List<*>) : RecyclerView.Adapter<CourseViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CourseViewHolder {
        val view = ViewUtil.viewById(p0.context, R.layout.item_free_course)
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: CourseViewHolder, p1: Int) {
        p0.bindData(data[p1]!!, p1)
    }
}
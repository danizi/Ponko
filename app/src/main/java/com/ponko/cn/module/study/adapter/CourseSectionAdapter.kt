package com.ponko.cn.module.study.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ponko.cn.R
import com.ponko.cn.module.study.holder.CourseSectionViewHolder
import com.xm.lib.common.util.ViewUtil


class CourseSectionAdapter(val data: List<Any>) : RecyclerView.Adapter<CourseSectionViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CourseSectionViewHolder{
        val view = ViewUtil.viewById(p0.context, R.layout.item_study_course_section)
        return CourseSectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (data.isEmpty()) {
            0
        } else {
            data.size
        }
    }

    override fun onBindViewHolder(p0: CourseSectionViewHolder, p1: Int) {
        p0.bind(data[p1], p1)
    }
}
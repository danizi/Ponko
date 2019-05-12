package com.ponko.cn.module.study.holder

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.MainActivity
import com.ponko.cn.R
import com.ponko.cn.bean.CaseBean
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.common.util.ViewUtil
import kotlinx.android.synthetic.main.item_study_sub_case.view.*


class CaseViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val tvAdTitle: TextView, val tvMore: TextView, val rv: RecyclerView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tvAdTitle = rootView.findViewById<View>(R.id.tv_ad_title) as TextView
                val tvMore = rootView.findViewById<View>(R.id.tv_more) as TextView
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return ViewHolder(tvAdTitle, tvMore, rv)
            }
        }
    }

    private var v: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (v == null) {
            v = ViewHolder.create(itemView)
        }
        val caseBean = d as CaseBean
        val context = itemView.context
        v?.tvAdTitle?.text = "外贸案例"
        v?.tvMore?.setOnClickListener {
            BKLog.d("跳转到交流页面")
            MainActivity.bottomMenu.select(2)
        }
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        v?.rv?.layoutManager = linearLayoutManager
        val adapter = SubAdapter(caseBean.cases?.list)
        v?.rv?.adapter = adapter
    }

    class SubAdapter(val data: List<Any>?) : RecyclerView.Adapter<SubCaseViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SubCaseViewHolder {
            val view = ViewUtil.viewById(p0.context, R.layout.item_study_sub_case)
            return SubCaseViewHolder(view)
        }

        override fun getItemCount(): Int {
            return if (data?.isEmpty()!!) {
                0
            } else {
                data.size
            }
        }

        override fun onBindViewHolder(p0: SubCaseViewHolder, p1: Int) {
            p0.bindData(data!![p1], p1)
        }
    }

    class SubCaseViewHolder(view: View?) : BaseViewHolder(view!!) {
        private class ViewHolder private constructor(val groupView: LinearLayout, val dateTextView: TextView, val contentTextView: TextView, val contentFromTextView: TextView, val divider: View) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val groupView = rootView.findViewById<View>(R.id.groupView) as LinearLayout
                    val dateTextView = rootView.findViewById<View>(R.id.dateTextView) as TextView
                    val contentTextView = rootView.findViewById<View>(R.id.contentTextView) as TextView
                    val contentFromTextView = rootView.findViewById<View>(R.id.contentFromTextView) as TextView
                    val divider = rootView.findViewById(R.id.divider) as View
                    return ViewHolder(groupView, dateTextView, contentTextView, contentFromTextView, divider)
                }
            }
        }

        private var v: ViewHolder? = null
        @SuppressLint("SetTextI18n")
        override fun bindData(d: Any, position: Int) {
            if (v == null) {
                v = ViewHolder.create(itemView)
            }
            val listBean = d as MainCBean.CasesBean.ListBean
            val context = itemView.context
            itemView.groupView.setBackgroundColor(Color.parseColor(listBean.color))
            v?.dateTextView?.text = TimeUtil.unixStr("yyyy/MM/dd HH:mm:ss", listBean.createTime)
            v?.contentTextView?.text = listBean.brief
            itemView.contentFromTextView.text = "——" + listBean.author
            itemView.setOnClickListener {
                IntoTargetUtil.target(context, listBean.targetType, "/analysis/detail?id=" + listBean.id)
            }
        }
    }
}
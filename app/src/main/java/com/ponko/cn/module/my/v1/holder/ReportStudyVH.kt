package com.ponko.cn.module.my.v1.holder

import android.annotation.SuppressLint
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ReportStudyBean
import com.ponko.cn.bean.StudyReportCBean
import com.ponko.cn.module.my.option.HistoryActivity
import com.ponko.cn.module.study.v1.StudyCourseDetailActivity
import com.ponko.cn.module.study.v1.constract.SearchContract
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder

/**
 * 学习报告 - 最近 ViewHolder
 */
class ReportStudyVH(view: View) : BaseViewHolder(view) {
    private var reportStudyUI: ReportStudyUI? = null

    override fun bindData(d: Any, position: Int) {
        if (reportStudyUI == null) {
            reportStudyUI = ReportStudyUI.create(itemView)
        }
        val reportStudyBean = d as ReportStudyBean
        reportStudyUI?.tvTitle?.text = reportStudyBean.title
        reportStudyUI?.tvMore?.visibility = View.VISIBLE
        reportStudyUI?.tvMore?.text = reportStudyBean.subtitle
        reportStudyUI?.tvMore?.setOnClickListener {
            ActivityUtil.startActivity(itemView.context, Intent(itemView.context, HistoryActivity::class.java))
        }

        //设置适配器
        val adapter = object : BaseRvAdapter() {}
        adapter.data?.addAll(reportStudyBean.list!!)
        adapter.addItemViewDelegate(0, StudyHistoryVH::class.java, StudyReportCBean.ListBeanXX.BasicInfo::class.java, R.layout.item_report_study)
        reportStudyUI?.rv?.adapter = adapter
        reportStudyUI?.rv?.layoutManager = LinearLayoutManager(itemView.context)
    }


    /**
     * 学习历史ViewHolder
     */
    open class StudyHistoryVH(view: View) : BaseViewHolder(view) {

        private var studyHistoryUI: StudyHistoryUI? = null

        @SuppressLint("SetTextI18n")
        override fun bindData(d: Any, position: Int) {
            if (studyHistoryUI == null) {
                studyHistoryUI = StudyHistoryUI.create(itemView)
            }
            val basicInfo = d as StudyReportCBean.ListBeanXX.BasicInfo
            Glide.with(itemView.context, basicInfo.avatar, studyHistoryUI?.ivCover, 0)
            studyHistoryUI?.tvTitle?.text = basicInfo.sectionName
            if (basicInfo.isCompleted) {
                studyHistoryUI?.tvRecord?.text = "100%"
            } else {
                studyHistoryUI?.tvRecord?.text = (basicInfo.position * 100 / basicInfo.durationForSecond).toInt().toString() + "%"
            }
            itemView.setOnClickListener {
                StudyCourseDetailActivity.start(itemView.context, basicInfo.courseId, "", 0, 0)
            }
            studyHistoryUI?.tvRecord

        }

        /**
         * StudyHistory界面
         */
        private class StudyHistoryUI private constructor(val ivCover: ImageView, val tvTitle: TextView, val tvDate: TextView, val tvRecord: TextView, val ivRight: ImageView) {
            companion object {

                fun create(rootView: View): StudyHistoryUI {
                    val ivCover = rootView.findViewById<View>(R.id.iv_cover) as ImageView
                    val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                    val tvDate = rootView.findViewById<View>(R.id.tv_date) as TextView
                    val tvRecord = rootView.findViewById<View>(R.id.tv_record) as TextView
                    val ivRight = rootView.findViewById<View>(R.id.iv_right) as ImageView
                    return StudyHistoryUI(ivCover, tvTitle, tvDate, tvRecord, ivRight)
                }
            }
        }


    }

    /**
     *  ReportCommon界面
     */
    private class ReportStudyUI private constructor(val clTitle: ConstraintLayout, val tvTitle: TextView, val tvMore: TextView, val rv: RecyclerView) {
        companion object {

            fun create(rootView: View): ReportStudyUI {
                val clTitle = rootView.findViewById<View>(R.id.cl_title) as ConstraintLayout
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val tvMore = rootView.findViewById<View>(R.id.tv_more) as TextView
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return ReportStudyUI(clTitle, tvTitle, tvMore, rv)
            }
        }
    }
}
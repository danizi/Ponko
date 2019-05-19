package com.ponko.cn.module.free.holder

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.TrialBean
import com.ponko.cn.module.free.FreeMoreAct
import com.ponko.cn.module.free.adapter.CourseAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ViewUtil

/**
 * 免费课程相关
 */
class TrialViewHolder(view: View?) : BaseViewHolder(view!!) {

    private class ViewHolder private constructor(val tvTitle: TextView, val tvMore: TextView, val llDes: LinearLayout, val llBook: LinearLayout, val ivBook: ImageView, val tvBook: TextView, val llListen: LinearLayout, val ivListen: ImageView, val tvListen: TextView, val rv: RecyclerView, val llMore: LinearLayout, val tvBottomMore: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val tvMore = rootView.findViewById<View>(R.id.tv_more) as TextView
                val llDes = rootView.findViewById<View>(R.id.ll_des) as LinearLayout
                val llBook = rootView.findViewById<View>(R.id.ll_book) as LinearLayout
                val ivBook = rootView.findViewById<View>(R.id.iv_book) as ImageView
                val tvBook = rootView.findViewById<View>(R.id.tv_book) as TextView
                val llListen = rootView.findViewById<View>(R.id.ll_listen) as LinearLayout
                val ivListen = rootView.findViewById<View>(R.id.iv_listen) as ImageView
                val tvListen = rootView.findViewById<View>(R.id.tv_listen) as TextView
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                val llMore = rootView.findViewById<View>(R.id.ll_more) as LinearLayout
                val tvBottomMore = rootView.findViewById<View>(R.id.tv_bottom_more) as TextView
                return ViewHolder(tvTitle, tvMore, llDes, llBook, ivBook, tvBook, llListen, ivListen, tvListen, rv, llMore, tvBottomMore)
            }
        }
    }

    private var v: ViewHolder? = null
    override fun bindData(d: Any, position: Int) {
        if (v == null) {
            v = ViewHolder.create(itemView)
        }
        val trialBean = d as TrialBean
        val context = itemView.context
        v?.tvTitle?.text = trialBean.trial?.title
        v?.tvBook?.text = trialBean.trial?.summary
        v?.tvListen?.text = trialBean.trial?.summary2
        v?.llMore?.setOnClickListener {
            BKLog.d("点击更多")
            FreeMoreAct.start(context, trialBean.trial?.title!!, trialBean.trial.id!!)
        }
        v?.tvMore?.setOnClickListener {
            BKLog.d("点击更多")
            FreeMoreAct.start(context, trialBean.trial?.title!!, trialBean.trial.id!!)
        }
        v?.rv?.layoutManager = LinearLayoutManager(context)
        v?.rv?.isFocusableInTouchMode = false
        v?.rv?.requestFocus()
        v?.rv?.addItemDecoration(MyItemDecoration.divider(context!!, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
        val adapter = CourseAdapter(trialBean.trial?.list!!)
        v?.rv?.adapter = adapter
    }
}
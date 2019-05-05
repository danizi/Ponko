package com.ponko.cn.module.free.holder

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.CoursesCBean
import com.ponko.cn.bean.TrialBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.util.ViewUtil


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

        v?.rv?.layoutManager = LinearLayoutManager(context)
        v?.rv?.isFocusableInTouchMode = false
        v?.rv?.requestFocus()
        v?.rv?.addItemDecoration(MyItemDecoration.divider(context!!, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
        val adapter = CourseAdapter(trialBean.trial?.list!!)
        v?.rv?.adapter = adapter
    }

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

    class CourseViewHolder(view: View?) : BaseViewHolder(view!!) {

        private class ViewHolder private constructor(val clVideo: ConstraintLayout, val ivAvatar: ImageView, val ivPlayPause: ImageView, val tvTitle: TextView, val clDetail: LinearLayout, val tvDes: TextView) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val clVideo = rootView.findViewById<View>(R.id.cl_video) as ConstraintLayout
                    val ivAvatar = rootView.findViewById<View>(R.id.iv_avatar) as ImageView
                    val ivPlayPause = rootView.findViewById<View>(R.id.iv_play_pause) as ImageView
                    val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                    val clDetail = rootView.findViewById<View>(R.id.cl_detail) as LinearLayout
                    val tvDes = rootView.findViewById<View>(R.id.tv_des) as TextView
                    return ViewHolder(clVideo, ivAvatar, ivPlayPause, tvTitle, clDetail, tvDes)
                }
            }
        }


        private var v: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (v == null) {
                v = ViewHolder.create(itemView)
            }
            val context = itemView.context
            val listBean = d as CoursesCBean.TrialBean.ListBean
            v?.tvTitle?.text = listBean.title
            v?.tvDes?.text = listBean.summary
            v?.clDetail?.setOnClickListener {}
            Glide.with(context, listBean.avatar, v?.ivAvatar)
        }
    }
}
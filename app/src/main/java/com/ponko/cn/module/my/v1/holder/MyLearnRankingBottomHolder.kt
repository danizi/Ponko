package com.ponko.cn.module.my.holder

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.LearnRankingBottomBean
import com.ponko.cn.bean.LearnRankingCBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder
import de.hdodenhof.circleimageview.CircleImageView

class MyLearnRankingBottomHolder(view: View) : BaseViewHolder(view) {
    private class ViewHolder private constructor(val flRank: FrameLayout, val ivRanking: ImageView, val tvRanking: TextView, val ivHead: CircleImageView, val tvName: TextView, val tvStudyTime: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val flRank = rootView.findViewById<View>(R.id.fl_rank) as FrameLayout
                val ivRanking = rootView.findViewById<View>(R.id.iv_ranking) as ImageView
                val tvRanking = rootView.findViewById<View>(R.id.tv_ranking) as TextView
                val ivHead = rootView.findViewById<View>(R.id.iv_head) as CircleImageView
                val tvName = rootView.findViewById<View>(R.id.tv_name) as TextView
                val tvStudyTime = rootView.findViewById<View>(R.id.tv_study_time) as TextView
                return ViewHolder(flRank, ivRanking, tvRanking, ivHead, tvName, tvStudyTime)
            }
        }
    }
    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }

        val context = itemView.context
        val rankingBean = (d as LearnRankingBottomBean).oneself

        Glide.with(context, rankingBean?.headPicture, viewHolder?.ivHead)
        viewHolder?.tvRanking?.text = position.toString()
        viewHolder?.tvStudyTime?.text = rankingBean?.duration.toString()
        viewHolder?.tvName?.text = rankingBean?.nickname
    }
}
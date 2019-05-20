package com.ponko.cn.module.my.holder

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.xm.lib.common.base.rv.BaseViewHolder
import android.support.v7.widget.RecyclerView
import com.ponko.cn.R
import com.ponko.cn.bean.LearnRankingGeneralBean
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import android.widget.FrameLayout
import android.widget.ImageView
import com.ponko.cn.bean.LearnRankingCBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.util.NumUtil
import com.xm.lib.common.util.TimeUtil


class MyLearnRankingGeneralHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val rv: RecyclerView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return ViewHolder(rv)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val learnRankingGeneralBean = d as LearnRankingGeneralBean
        val context = itemView.context

        val adapter = object : BaseRvAdapter() {}
        adapter.data?.addAll(learnRankingGeneralBean.ranking)
        adapter.addItemViewDelegate(0, ItemViewHolder::class.java, Any::class.java, R.layout.item_my_learn_ranking_bttom)
        viewHolder?.rv?.layoutManager = LinearLayoutManager(context)
        viewHolder?.rv?.adapter = adapter
    }

    open class ItemViewHolder(view: View) : BaseViewHolder(view) {

        private var viewHolder: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }

            val context = itemView.context
            val rankingBean = d as LearnRankingCBean.RankingBean
            Glide.with(context, rankingBean.headPicture, viewHolder?.ivHead)
            if (position > 2) {
                viewHolder?.ivRanking?.visibility = View.GONE
            }
            when (position) {
                0 -> {
                    viewHolder?.tvRanking?.visibility = View.GONE
                    viewHolder?.ivRanking?.visibility = View.VISIBLE
                    viewHolder?.ivRanking?.setImageResource(R.mipmap.ranking_01)
                }
                1 -> {
                    viewHolder?.tvRanking?.visibility = View.GONE
                    viewHolder?.ivRanking?.visibility = View.VISIBLE
                    viewHolder?.ivRanking?.setImageResource(R.mipmap.ranking_02)
                }
                2 -> {
                    viewHolder?.tvRanking?.visibility = View.GONE
                    viewHolder?.ivRanking?.visibility = View.VISIBLE
                    viewHolder?.ivRanking?.setImageResource(R.mipmap.ranking_03)
                }
            }
            viewHolder?.tvRanking?.text = position.toString()
            viewHolder?.tvStudyTime?.text = NumUtil.getDecimalPoint(rankingBean.duration.toFloat() / 60f / 60f) + "小时"
            viewHolder?.tvName?.text = rankingBean.nickname
        }

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

    }
}
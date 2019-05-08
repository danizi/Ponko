package com.ponko.cn.module.my.holder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.IntegralRankGenenalBean
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import android.support.constraint.ConstraintLayout
import com.ponko.cn.bean.RankingV2
import com.ponko.cn.utils.Glide


class MyRankGenenalViewHolder(view: View) : BaseViewHolder(view) {

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
        val context = itemView.context
        val integralRankGenenalBean = d as IntegralRankGenenalBean
        val adapter = object : BaseRvAdapter() {}
        adapter.addItemViewDelegate(0, MyRankGenenalItemVH::class.java, Any::class.java, R.layout.item_my_store_integral_rank_genenal_1)
        adapter.data?.addAll(integralRankGenenalBean.ranking)
        viewHolder?.rv?.adapter = adapter
        viewHolder?.rv?.layoutManager = LinearLayoutManager(context)
    }

    private class MyRankGenenalItemVH(view: View) : BaseViewHolder(view) {

        private class ViewHolder private constructor(val constraintLayout3: ConstraintLayout, val tvRank: TextView, val ivHead: CircleImageView, val tvMe: TextView, val tvPayType: TextView, val tvIntegral: TextView) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val constraintLayout3 = rootView.findViewById<View>(R.id.constraintLayout3) as ConstraintLayout
                    val tvRank = rootView.findViewById<View>(R.id.tv_rank) as TextView
                    val ivHead = rootView.findViewById<View>(R.id.iv_head) as CircleImageView
                    val tvMe = rootView.findViewById<View>(R.id.tv_me) as TextView
                    val tvPayType = rootView.findViewById<View>(R.id.tv_pay_type) as TextView
                    val tvIntegral = rootView.findViewById<View>(R.id.tv_integral) as TextView
                    return ViewHolder(constraintLayout3, tvRank, ivHead, tvMe, tvPayType, tvIntegral)
                }
            }
        }

        private var viewHolder: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }
            val allBean = d as RankingV2.AllBean
            val context = itemView.context
            Glide.with(context, allBean.headPicture, viewHolder?.ivHead)
            viewHolder?.tvIntegral?.text = allBean.scores.toString()
            viewHolder?.tvMe?.text = allBean.nickname
            viewHolder?.tvPayType?.text = allBean.productName
            viewHolder?.tvRank?.text = (position + 3).toString()
        }
    }

}
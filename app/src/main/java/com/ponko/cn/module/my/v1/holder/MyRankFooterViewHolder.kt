package com.ponko.cn.module.my.holder

import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.IntegralRankFooterBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.v1.BaseViewHolder
import de.hdodenhof.circleimageview.CircleImageView

class MyRankFooterViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val line: View, val constraintLayout3: ConstraintLayout, val tvRank: TextView, val ivHead: CircleImageView, val tvMe: TextView, val tvPayType: TextView, val tvIntegral: TextView) {
        companion object {
            fun create(rootView: View): ViewHolder {
                val line = rootView.findViewById<View>(R.id.line) as View
                val constraintLayout3 = rootView.findViewById<View>(R.id.constraintLayout3) as ConstraintLayout
                val tvRank = rootView.findViewById<View>(R.id.tv_rank) as TextView
                val ivHead = rootView.findViewById<View>(R.id.iv_head) as CircleImageView
                val tvMe = rootView.findViewById<View>(R.id.tv_me) as TextView
                val tvPayType = rootView.findViewById<View>(R.id.tv_pay_type) as TextView
                val tvIntegral = rootView.findViewById<View>(R.id.tv_integral) as TextView
                return ViewHolder(line, constraintLayout3, tvRank, ivHead, tvMe, tvPayType, tvIntegral)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val context = itemView.context
        val integralRankFooterBean = d as IntegralRankFooterBean
        Glide.with(context, integralRankFooterBean.mine?.head_picture, viewHolder?.ivHead,Constants.LOAD_IMAGE_DELAY)
        viewHolder?.tvIntegral?.text = integralRankFooterBean.mine?.scores.toString()
        viewHolder?.tvMe?.text = integralRankFooterBean.mine?.nickname
        viewHolder?.tvPayType?.text = integralRankFooterBean.mine?.productName
        viewHolder?.tvRank?.text = integralRankFooterBean.mine?.ranking.toString()
    }

}
package com.ponko.cn.module.my.holder

import android.view.View
import com.ponko.cn.R.id.view
import com.xm.lib.common.base.rv.BaseViewHolder
import com.ponko.cn.R.id.tv_integral
import android.widget.TextView
import com.ponko.cn.R.id.tv_pay_type
import com.ponko.cn.R.id.tv_me
import com.ponko.cn.R.id.iv_head
import de.hdodenhof.circleimageview.CircleImageView
import com.ponko.cn.R.id.textView7
import com.ponko.cn.R.id.line
import com.ponko.cn.R.id.tv_3
import com.ponko.cn.R.id.tv_2
import com.ponko.cn.R.id.tv_1
import com.ponko.cn.R.id.constraintLayout3
import android.support.constraint.ConstraintLayout
import com.ponko.cn.R.id.tv_3_num
import com.ponko.cn.R.id.tv_3_type
import com.ponko.cn.R.id.tv_3_name
import com.ponko.cn.R.id.tv_2_num
import com.ponko.cn.R.id.tv_2_type
import com.ponko.cn.R.id.tv_2_name
import com.ponko.cn.R.id.tv_1_num
import com.ponko.cn.R.id.tv_1_type
import com.ponko.cn.R.id.tv_1_name
import com.ponko.cn.R.id.constraintLayout2
import com.ponko.cn.R.id.iv_head_3
import com.ponko.cn.R.id.iv_number_three
import android.support.v7.widget.AppCompatImageView
import com.ponko.cn.R
import com.ponko.cn.R.id.cl_number_three
import com.ponko.cn.R.id.iv_head_2
import com.ponko.cn.R.id.iv_number_two
import com.ponko.cn.R.id.cl_number_two
import com.ponko.cn.R.id.iv_head_1
import com.ponko.cn.R.id.iv_number_one
import com.ponko.cn.R.id.cl_number_one
import com.ponko.cn.R.id.guideline_center
import com.ponko.cn.bean.IntegralRankHeadBean
import com.ponko.cn.utils.Glide


class MyRankHeadViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val clNumberOne: ConstraintLayout, val ivNumberOne: AppCompatImageView, val ivHead1: CircleImageView, val clNumberTwo: ConstraintLayout, val ivNumberTwo: AppCompatImageView, val ivHead2: CircleImageView, val clNumberThree: ConstraintLayout, val ivNumberThree: AppCompatImageView, val ivHead3: CircleImageView, val constraintLayout2: ConstraintLayout, val tv1Name: TextView, val tv1Type: TextView, val tv1Num: TextView, val tv2Name: TextView, val tv2Type: TextView, val tv2Num: TextView, val tv3Name: TextView, val tv3Type: TextView, val tv3Num: TextView, val constraintLayout3: ConstraintLayout, val tv1: TextView, val tv2: TextView, val tv3: TextView, val line: View, val textView7: TextView, val ivHead: CircleImageView, val tvMe: TextView, val tvPayType: TextView, val tvIntegral: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val clNumberOne = rootView.findViewById<View>(R.id.cl_number_one) as ConstraintLayout
                val ivNumberOne = rootView.findViewById<View>(R.id.iv_number_one) as AppCompatImageView
                val ivHead1 = rootView.findViewById<View>(R.id.iv_head_1) as CircleImageView
                val clNumberTwo = rootView.findViewById<View>(R.id.cl_number_two) as ConstraintLayout
                val ivNumberTwo = rootView.findViewById<View>(R.id.iv_number_two) as AppCompatImageView
                val ivHead2 = rootView.findViewById<View>(R.id.iv_head_2) as CircleImageView
                val clNumberThree = rootView.findViewById<View>(R.id.cl_number_three) as ConstraintLayout
                val ivNumberThree = rootView.findViewById<View>(R.id.iv_number_three) as AppCompatImageView
                val ivHead3 = rootView.findViewById<View>(R.id.iv_head_3) as CircleImageView
                val constraintLayout2 = rootView.findViewById<View>(R.id.constraintLayout2) as ConstraintLayout
                val tv1Name = rootView.findViewById<View>(R.id.tv_1_name) as TextView
                val tv1Type = rootView.findViewById<View>(R.id.tv_1_type) as TextView
                val tv1Num = rootView.findViewById<View>(R.id.tv_1_num) as TextView
                val tv2Name = rootView.findViewById<View>(R.id.tv_2_name) as TextView
                val tv2Type = rootView.findViewById<View>(R.id.tv_2_type) as TextView
                val tv2Num = rootView.findViewById<View>(R.id.tv_2_num) as TextView
                val tv3Name = rootView.findViewById<View>(R.id.tv_3_name) as TextView
                val tv3Type = rootView.findViewById<View>(R.id.tv_3_type) as TextView
                val tv3Num = rootView.findViewById<View>(R.id.tv_3_num) as TextView
                val constraintLayout3 = rootView.findViewById<View>(R.id.constraintLayout3) as ConstraintLayout
                val tv1 = rootView.findViewById<View>(R.id.tv_1) as TextView
                val tv2 = rootView.findViewById<View>(R.id.tv_2) as TextView
                val tv3 = rootView.findViewById<View>(R.id.tv_3) as TextView
                val line = rootView.findViewById(R.id.line) as View
                val textView7 = rootView.findViewById<View>(R.id.textView7) as TextView
                val ivHead = rootView.findViewById<View>(R.id.iv_head) as CircleImageView
                val tvMe = rootView.findViewById<View>(R.id.tv_me) as TextView
                val tvPayType = rootView.findViewById<View>(R.id.tv_pay_type) as TextView
                val tvIntegral = rootView.findViewById<View>(R.id.tv_integral) as TextView
                return ViewHolder(clNumberOne, ivNumberOne, ivHead1, clNumberTwo, ivNumberTwo, ivHead2, clNumberThree, ivNumberThree, ivHead3, constraintLayout2, tv1Name, tv1Type, tv1Num, tv2Name, tv2Type, tv2Num, tv3Name, tv3Type, tv3Num, constraintLayout3, tv1, tv2, tv3, line, textView7, ivHead, tvMe, tvPayType, tvIntegral)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val context = itemView.context
        val integralRankHeadBean = d as IntegralRankHeadBean
        Glide.with(context, integralRankHeadBean.ranking[0].headPicture, viewHolder?.ivHead1)
        viewHolder?.tv1Name?.text = integralRankHeadBean.ranking[0].nickname
        viewHolder?.tv1Type?.text = integralRankHeadBean.ranking[0].productName
        viewHolder?.tv1Num?.text = integralRankHeadBean.ranking[0].scores.toString()

        Glide.with(context, integralRankHeadBean.ranking[1].headPicture, viewHolder?.ivHead2)
        viewHolder?.tv2Name?.text = integralRankHeadBean.ranking[1].nickname
        viewHolder?.tv2Type?.text = integralRankHeadBean.ranking[1].productName
        viewHolder?.tv2Num?.text = integralRankHeadBean.ranking[1].scores.toString()

        Glide.with(context, integralRankHeadBean.ranking[2].headPicture, viewHolder?.ivHead3)
        viewHolder?.tv3Name?.text = integralRankHeadBean.ranking[2].nickname
        viewHolder?.tv3Type?.text = integralRankHeadBean.ranking[2].productName
        viewHolder?.tv3Num?.text = integralRankHeadBean.ranking[2].scores.toString()
    }
}
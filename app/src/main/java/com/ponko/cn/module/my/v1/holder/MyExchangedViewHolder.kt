package com.ponko.cn.module.my.holder

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ponko.cn.R
import com.ponko.cn.bean.ExchangedHistoriesCBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.util.TimeUtil


class MyExchangedViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val divider: View, val tvState: TextView, val tvStateValue: TextView, val constraintLayout: ConstraintLayout, val ivCover: AppCompatImageView, val tv1: TextView, val tv2: TextView, val tvDate: TextView, val tvExpressNumber: TextView) {
        companion object {
            fun create(rootView: View): ViewHolder {
                val divider = rootView.findViewById(R.id.divider) as View
                val tvState = rootView.findViewById<View>(R.id.tv_state) as TextView
                val tvStateValue = rootView.findViewById<View>(R.id.tv_state_value) as TextView
                val constraintLayout = rootView.findViewById<View>(R.id.constraintLayout) as ConstraintLayout
                val ivCover = rootView.findViewById<View>(R.id.iv_cover) as AppCompatImageView
                val tv1 = rootView.findViewById<View>(R.id.tv_1) as TextView
                val tv2 = rootView.findViewById<View>(R.id.tv_2) as TextView
                val tvDate = rootView.findViewById<View>(R.id.tv_date) as TextView
                val tvExpressNumber = rootView.findViewById<View>(R.id.tv_express_number) as TextView
                return ViewHolder(divider, tvState, tvStateValue, constraintLayout, ivCover, tv1, tv2, tvDate, tvExpressNumber)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    @SuppressLint("SetTextI18n")
    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val context = itemView.context
        val exchangedHistoriesCBean = d as ExchangedHistoriesCBean
        Glide.with(context, exchangedHistoriesCBean.image, viewHolder?.ivCover)
        /*分为三个状态
         * 待发货
         * 已发货
         * 兑换成功
         */
        var state = ""
        var stateColor = ""
        var expressNumber = ""
        when (exchangedHistoriesCBean.type) {
            1 -> {
                state = if (!exchangedHistoriesCBean.isSend) {
                    viewHolder?.tvExpressNumber?.visibility = View.GONE
                    stateColor = "#FF85C072"
                    "待发货"
                } else {
                    viewHolder?.tvExpressNumber?.visibility = View.VISIBLE
                    expressNumber = "快递单号:${exchangedHistoriesCBean.express}${exchangedHistoriesCBean.postid}【点击复制】"
                    stateColor = "#FFFF3542"
                    viewHolder?.tvExpressNumber?.setOnClickListener {
                        val myClipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val myClip: ClipData
                        val text = exchangedHistoriesCBean.postid
                        myClip = ClipData.newPlainText("text", text)
                        myClipboard.primaryClip = myClip
                        Toast.makeText(context, "已复制:$text", Toast.LENGTH_SHORT).show()
                    }
                    "已发货"
                }
            }
            else -> {
                state = "兑换成功"
                viewHolder?.tvExpressNumber?.setOnClickListener {
                    val myClipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val myClip: ClipData
                    val text = exchangedHistoriesCBean.tel
                    myClip = ClipData.newPlainText("text", text)
                    myClipboard.primaryClip = myClip
                    Toast.makeText(context, "已复制:$text", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewHolder?.tvExpressNumber?.text = expressNumber
        viewHolder?.tvStateValue?.text = state
        viewHolder?.tvStateValue?.setTextColor(Color.parseColor(stateColor))
        Glide.with(context, exchangedHistoriesCBean.image, viewHolder?.ivCover)
        viewHolder?.tv1?.text = exchangedHistoriesCBean.name
        viewHolder?.tv2?.text = exchangedHistoriesCBean.scores.toString() + "积分"
        viewHolder?.tvDate?.text = "兑换时间:${TimeUtil.unixStr("yyyy-MM-dd HH:mm", exchangedHistoriesCBean.createTime)}"
    }
}
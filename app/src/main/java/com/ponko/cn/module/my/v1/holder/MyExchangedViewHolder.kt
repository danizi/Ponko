package com.ponko.cn.module.my.holder

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ponko.cn.R
import com.ponko.cn.bean.ExchangedHistoriesCBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.v1.BaseViewHolder
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
        setExpressTipAndClick(context, exchangedHistoriesCBean)
        setStatusTipAndColor(exchangedHistoriesCBean)
        Glide.with(context, exchangedHistoriesCBean.image, viewHolder?.ivCover)
        viewHolder?.tv1?.text = exchangedHistoriesCBean.name
        viewHolder?.tv2?.text = exchangedHistoriesCBean.scores.toString() + "积分"
        viewHolder?.tvDate?.text = "兑换时间:${TimeUtil.unixStr("yyyy-MM-dd HH : mm", exchangedHistoriesCBean.createTime)}"
    }

    /**
     * 当前商品订单
     */
    private fun setExpressTipAndClick(context: Context, exchangedHistoriesCBean: ExchangedHistoriesCBean) {
        var state = ""
        if (!TextUtils.isEmpty(exchangedHistoriesCBean.express)) {
            state = "快递单号:${exchangedHistoriesCBean.express}${exchangedHistoriesCBean.postid}【点击复制】"
            viewHolder?.tvExpressNumber?.visibility = View.VISIBLE
            viewHolder?.tvExpressNumber?.text = state
            viewHolder?.tvExpressNumber?.setOnClickListener {
                clip(exchangedHistoriesCBean.express, context)
            }
        } else if (!TextUtils.isEmpty(exchangedHistoriesCBean.postid)) {
            state = "兑换号码:${exchangedHistoriesCBean.postid}【点击复制】"
            viewHolder?.tvExpressNumber?.visibility = View.VISIBLE
            viewHolder?.tvExpressNumber?.text = state
            viewHolder?.tvExpressNumber?.setOnClickListener {
                clip(exchangedHistoriesCBean.postid, context)
            }
        } else {
            viewHolder?.tvExpressNumber?.visibility = View.GONE
        }
    }

    /**
     * 设置发货状态文字颜色 1 兑换实物物品 2 兑换虚拟物品
     */
    private fun setStatusTipAndColor(exchangedHistoriesCBean: ExchangedHistoriesCBean) {
        var state = ""
        var stateColor = ""
        if (exchangedHistoriesCBean.isSend) {
            state = if (exchangedHistoriesCBean.type == 1) {
                "已发货"
            } else {
                "兑换成功"
            }
            stateColor = "#FF85C072"
        } else {
            state = "未发货"
            stateColor = "#FFFF3542"
        }
        viewHolder?.tvStateValue?.text = state
        viewHolder?.tvStateValue?.setTextColor(Color.parseColor(stateColor))
    }

    /**
     * 复制到剪切板上
     */
    private fun clip(clipStr: String, context: Context) {
        val myClipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip: ClipData = ClipData.newPlainText("text", clipStr)
        myClipboard.primaryClip = myClip
        Toast.makeText(context, "已复制:$clipStr", Toast.LENGTH_SHORT).show()
    }

}
package com.ponko.cn.module.my.holder

import android.annotation.SuppressLint
import android.view.View
import com.xm.lib.common.base.rv.BaseViewHolder
import com.ponko.cn.R.id.tv_obtain_integral_type
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ExchangedHistoriesCBean
import com.ponko.cn.bean.StoreObtainLogBean
import com.xm.lib.common.util.TimeUtil


class MyIntegralRecordViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val tvObtainIntegralType: TextView, val tvSignTime: TextView, val tvIntegralNum: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tvObtainIntegralType = rootView.findViewById<View>(R.id.tv_obtain_integral_type) as TextView
                val tvSignTime = rootView.findViewById<View>(R.id.tv_sign_time) as TextView
                val tvIntegralNum = rootView.findViewById<View>(R.id.tv_integral_num) as TextView
                return ViewHolder(tvObtainIntegralType, tvSignTime, tvIntegralNum)
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
        val obtainLogBean = d as StoreObtainLogBean.ListBean
        viewHolder?.tvObtainIntegralType?.text = obtainLogBean.type
        viewHolder?.tvSignTime?.text = TimeUtil.unixStr("yyyy/MM/dd HH:mm:ss", obtainLogBean.createTime)
        viewHolder?.tvIntegralNum?.text = "${obtainLogBean.scores}积分"
    }
}
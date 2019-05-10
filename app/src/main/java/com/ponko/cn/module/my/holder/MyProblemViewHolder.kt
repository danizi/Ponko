package com.ponko.cn.module.my.holder

import android.view.View
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.WebAct
import com.ponko.cn.bean.HelpCBean
import com.xm.lib.common.base.rv.BaseViewHolder

class MyProblemViewHolder(view: View) : BaseViewHolder(view) {

    private var tv: TextView? = null

    override fun bindData(d: Any, position: Int) {
        tv = itemView.findViewById(R.id.helpTextView)
        val helpCBean = d as HelpCBean
        val context = itemView.context
        tv?.text = helpCBean.title
        itemView.setOnClickListener {
            WebAct.start(context, "url", "/help/detail?id=${helpCBean.id}", helpCBean.title)
        }
    }
}
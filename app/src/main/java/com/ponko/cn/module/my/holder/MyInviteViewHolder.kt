package com.ponko.cn.module.my.holder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.InviteFriendsBean
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog


class MyInviteViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val ivLogo: ImageView, val tvTitle: TextView, val llInvite: LinearLayout, val tvInviteNum: TextView, val llIntegral: LinearLayout, val tvIntegralNum: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val ivLogo = rootView.findViewById<View>(R.id.iv_logo) as ImageView
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val llInvite = rootView.findViewById<View>(R.id.ll_invite) as LinearLayout
                val tvInviteNum = rootView.findViewById<View>(R.id.tv_invite_num) as TextView
                val llIntegral = rootView.findViewById<View>(R.id.ll_integral) as LinearLayout
                val tvIntegralNum = rootView.findViewById<View>(R.id.tv_integral_num) as TextView
                return ViewHolder(ivLogo, tvTitle, llInvite, tvInviteNum, llIntegral, tvIntegralNum)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val productsBean = d as InviteFriendsBean.ProductsBean
        val context = itemView.context

        Glide.with(context, productsBean.logo_url, viewHolder?.ivLogo)
        viewHolder?.tvTitle?.text = productsBean.title
        viewHolder?.tvInviteNum?.text = productsBean.success.toString()
        viewHolder?.tvIntegralNum?.text = productsBean.score.toString()
        itemView.setOnClickListener {
            BKLog.d("点击了${productsBean.title}")
        }
    }

}
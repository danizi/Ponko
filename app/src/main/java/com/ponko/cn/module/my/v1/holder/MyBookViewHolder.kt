package com.ponko.cn.module.my.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.StoreProfileCMoreBean
import com.ponko.cn.bean.StoreTaskBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.web.WebAct
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.v1.BaseViewHolder
import retrofit2.Call
import retrofit2.Response


class MyBookViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val ivShade: ImageView, val ivBook: ImageView, val tvCourseName: TextView, val tvIntegralNum: TextView, val tvExchanged: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val ivShade = rootView.findViewById<View>(R.id.iv_shade) as ImageView
                val ivBook = rootView.findViewById<View>(R.id.iv_book) as ImageView
                val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                val tvIntegralNum = rootView.findViewById<View>(R.id.tv_integral_num) as TextView
                val tvExchanged = rootView.findViewById<View>(R.id.tv_exchanged) as TextView
                return ViewHolder(ivShade, ivBook, tvCourseName, tvIntegralNum, tvExchanged)
            }
        }
    }


    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val storesBean = d as StoreProfileCMoreBean.StoresBean
        val context = itemView.context
        if (storesBean.mask) {
            //viewHolder?.ivShade?.setImageResource(R.mipmap.store_has_change)
            Glide.with(context, storesBean.maskPicture, viewHolder?.ivShade, 1)
            viewHolder?.ivShade?.visibility=View.VISIBLE
        }
        Glide.with(context, storesBean.picture, viewHolder?.ivBook, 1)
        viewHolder?.tvCourseName?.text = storesBean.name
        viewHolder?.tvIntegralNum?.text = storesBean.scores.toString() + "积分"
        viewHolder?.tvExchanged?.text = "已兑课程" + storesBean.expend.toString() + "件"
        itemView.setOnClickListener {
            PonkoApp.myApi?.tasks()?.enqueue(object : HttpCallBack<StoreTaskBean>() {
                override fun onSuccess(call: Call<StoreTaskBean>?, response: Response<StoreTaskBean>?) {
                    WebAct.startExChange(context, "exchange", storesBean.url, storesBean.name, storesBean.id, needScore = storesBean.scores.toString(), aggregateScore = response?.body()?.scores.toString(), total = storesBean.total, isVirtualProduct = false)
                }
            })
        }
    }
}
package com.ponko.cn.module.my.holder

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.bean.Main2CBean
import com.ponko.cn.bean.RemindCBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.study.v2.StudyContract2
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import com.xm.lib.common.base.rv.v1.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil
import retrofit2.Call
import retrofit2.Response


class MyRemindHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val tvTitle: TextView, val tvTime: TextView, val tvDes: TextView, val llDetail: ConstraintLayout, val textView6: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                val tvDes = rootView.findViewById<View>(R.id.tv_des) as TextView
                val llDetail = rootView.findViewById<View>(R.id.ll_detail) as ConstraintLayout
                val textView6 = rootView.findViewById<View>(R.id.textView6) as TextView
                return ViewHolder(tvTitle, tvTime, tvDes, llDetail, textView6)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {}

    override fun bindData(adapter: BaseRvAdapter, d: Any, position: Int) {
        super.bindData(adapter, d, position)
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val remindCBean = d as RemindCBean
        val context = itemView.context
        if (remindCBean.type.equals("SIMPLE", true)) {
            //不可跳转的
            viewHolder?.llDetail?.visibility = View.GONE
        } else {
            if (remindCBean.unread) {
                //可跳转的
                viewHolder?.llDetail?.visibility = View.VISIBLE
                itemView.setOnClickListener {
                    IntoTargetUtil.target(context, remindCBean.type, remindCBean.url)
                    readRemind(context, adapter, position, remindCBean.id, remindCBean.title)
                }
            }
        }
        viewHolder?.tvTitle?.text = remindCBean.title
        viewHolder?.tvDes?.text = remindCBean.description
        viewHolder?.tvTime?.text = TimeUtil.unixStr("yyyy/MM/dd HH:mm:ss", remindCBean.createTime)
    }

    private fun readRemind(context: Context, adapter: BaseRvAdapter?, pos: Int, id: String?, title: String?) {
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(title)) return
        PonkoApp.myApi?.readRemind(id!!)?.enqueue(object : HttpCallBack<GeneralBean>() {
            override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                BKLog.d("已读取信息$id $title")
                (adapter?.data!![pos] as RemindCBean).unread = false
                adapter.notifyItemChanged(pos)

                //再次请求一下接口
                StudyContract2.M().requestStudyApi(object : HttpCallBack<Main2CBean>() {
                    override fun onSuccess(call: Call<Main2CBean>?, response: Response<Main2CBean>?) {
                        val msg = response?.body()?.msg_count!!
                        val intent = Intent(Constants.ACTION_SHOW_MSG_TIP)
                        if (msg > 0) {
                            BKLog.d("有提醒消息，提醒图标晃动")
                            intent.putExtra("msg", msg)
                            context.sendBroadcast(intent)
                        } else {
                            context.sendBroadcast(intent)
                        }
                    }
                })
            }
        })
    }
}
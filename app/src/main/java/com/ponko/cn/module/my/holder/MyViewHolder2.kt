package com.ponko.cn.module.my.holder

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.MyBean
import com.ponko.cn.module.my.option.*
import com.ponko.cn.module.my.option.store.IntegralExchangedAct
import com.ponko.cn.module.my.option.store.StoreAct
import com.ponko.cn.utils.ActivityUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog


class MyViewHolder2(view: View) : BaseViewHolder(view) {

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
        val myBeans = d as MyBean
        val context = itemView.context

        val adapter = Adapter()
        adapter.data?.addAll(myBeans.listBeans)
        adapter.addItemViewDelegate(0, com.ponko.cn.module.my.holder.ViewHolder::class.java, Any::class.java, R.layout.item_my)
        viewHolder?.rv?.adapter = adapter
        viewHolder?.rv?.layoutManager = GridLayoutManager(context, 4)
    }


}

class Adapter : BaseRvAdapter()

class ViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val iv: ImageView, val tv: TextView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val iv = rootView.findViewById<View>(R.id.iv) as ImageView
                val tv = rootView.findViewById<View>(R.id.tv) as TextView
                return ViewHolder(iv, tv)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val myListBean = d as MyBean.MyListBean
        val context = itemView.context
        viewHolder?.iv?.setImageResource(myListBean.icon)
        viewHolder?.tv?.text = myListBean.des
        itemView.setOnClickListener {
            when (myListBean.des) {
                "积分商城" -> {
                    ActivityUtil.startActivity(context,  Intent(context, StoreAct::class.java))
                }
                "缓存" -> {
                    ActivityUtil.startActivity(context,  Intent(context,CacheAct::class.java))
                }
                "收藏" -> {
                    ActivityUtil.startActivity(context,  Intent(context,ColoctAct::class.java))
                }
                "历史" -> {
                    ActivityUtil.startActivity(context,  Intent(context,HistoryActivity::class.java))
                }
                "提醒" -> {
                    ActivityUtil.startActivity(context,  Intent(context,RemindAct::class.java))
                }
                "学习排行" -> {
                    ActivityUtil.startActivity(context,  Intent(context,LearnRankingAct::class.java))
                }
                "BK码兑换" -> {
                    ActivityUtil.startActivity(context,  Intent(context,ExchangeAct::class.java))
                }
                "已兑课程" -> {
                    ActivityUtil.startActivity(context,  Intent(context, IntegralExchangedAct::class.java))
                }
                "咨询" -> {
                    //intent = Intent(context,StoreAct::class.java)
//                    val rx = RxPermissions(context)
//                    rx.request(Manifest.permission.CALL_PHONE)
//                            .subscribe { agree ->
//                                if (agree) {
//                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + BaseData.instance.data?.public_phone))
//                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                                    activity.startActivity(intent)
//                                }
//                            }
                }
                "常见问题" -> {
                    ActivityUtil.startActivity(context, Intent(context,ProblemAct::class.java))
                }
            }
            BKLog.d("点击${myListBean.des}")

        }
    }
}
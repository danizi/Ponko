package com.ponko.cn.module.my.holder

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.CoursesListCBean
import com.ponko.cn.bean.MyBean
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
        viewHolder?.iv?.setImageResource(myListBean.icon)
        viewHolder?.tv?.text = myListBean.des
        itemView.setOnClickListener {
            when(myListBean.des){
                "积分商城"->{ }
                "缓存"->{}
                "收藏"->{}
                "历史"->{}
                "提醒"->{}
                "学习排行"->{}
                "BK码兑换"->{}
                "已兑课程"->{}
                "咨询"->{}
                "常见问题"->{}
            }
            BKLog.d("点击${myListBean.des}")
        }
    }
}
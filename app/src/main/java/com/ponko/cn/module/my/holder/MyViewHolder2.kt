package com.ponko.cn.module.my.holder

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.MyBean
import com.ponko.cn.bean.StoreTaskBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.my.option.*
import com.ponko.cn.module.my.option.store.IntegralExchangedAct
import com.ponko.cn.module.my.option.store.StoreAct
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.AnimUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.task.DownTask
import retrofit2.Call
import retrofit2.Response


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
        viewHolder?.rv?.isFocusableInTouchMode = false
        viewHolder?.rv?.requestFocus()
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

        isStoreIconShake(myListBean.des)
        itemView.setOnClickListener {
            when (myListBean.des) {
                "积分商城" -> {
                    stopShakeAnim(viewHolder?.iv)
                    ActivityUtil.startActivity(context, Intent(context, StoreAct::class.java))
                }
                "缓存" -> {
                    ActivityUtil.startActivity(context, Intent(context, CacheAct::class.java))
                }
                "收藏" -> {
                    ActivityUtil.startActivity(context, Intent(context, ColoctAct::class.java))
                }
                "历史" -> {
                    ActivityUtil.startActivity(context, Intent(context, HistoryActivity::class.java))
                }
                "提醒" -> {
                    ActivityUtil.startActivity(context, Intent(context, RemindAct::class.java))
                }
                "学习排行" -> {
                    ActivityUtil.startActivity(context, Intent(context, LearnRankingAct::class.java))
                }
                "BK码兑换" -> {
                    ActivityUtil.startActivity(context, Intent(context, ExchangeAct::class.java))
                }
                "已兑课程" -> {
                    ActivityUtil.startActivity(context, Intent(context, IntegralExchangedAct::class.java))
                }
                "咨询" -> {
                    RxPermissions(context as AppCompatActivity)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe { aBoolean ->
                                if (aBoolean!!) {
                                    //当所有权限都允许之后，返回true
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PonkoApp.mainCBean?.public_phone))
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(intent)
                                } else {
                                    //只要有一个权限禁止，返回false，
                                    //下一次申请只申请没通过申请的权限
                                    Log.i("permissions", "btn_more_sametime：$aBoolean")
                                }
                            }
                }
                "常见问题" -> {
                    ActivityUtil.startActivity(context, Intent(context, ProblemAct::class.java))
                }
            }
            BKLog.d("点击${myListBean.des}")

        }
    }

    /**
     * 未签到，积分晃动提醒
     */
    private fun isStoreIconShake(des: String) {
        if (des == "积分商城") {
            PonkoApp.myApi?.tasks()?.enqueue(object : HttpCallBack<StoreTaskBean>() {
                override fun onSuccess(call: Call<StoreTaskBean>?, response: Response<StoreTaskBean>?) {
                    val storeTaskBean = response?.body()
                    PonkoApp.signInfo = storeTaskBean
                    if (storeTaskBean?.isCompleted != true) {
                        //未签到状态
                        BKLog.d("未签到状态,商城图片晃动")
                        AnimUtil.shakeAnim(viewHolder?.iv)
                    }
                }
            })
        }
    }

    /**
     * 停止动画
     */
    private fun stopShakeAnim(view: View?) {
        AnimUtil.cancel(view)
    }
}
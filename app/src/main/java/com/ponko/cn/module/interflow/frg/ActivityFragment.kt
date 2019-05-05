package com.ponko.cn.module.interflow.frg

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.ActivityCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.RefreshViewHolder
import com.ponko.cn.utils.Glide
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.xm.lib.common.base.BaseFragment
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.util.ViewUtil
import retrofit2.Call
import retrofit2.Response

class ActivityFragment : BaseFragment() {
    private var viewHolder: RefreshViewHolder? = null
    private var page = 1

    override fun getLayoutId(): Int {
        return R.layout.frg_free_activity
    }

    override fun findViews(view: View) {
        if (viewHolder == null) {
            viewHolder = RefreshViewHolder.create(view)
        }
    }

    override fun initDisplay() {
        viewHolder?.addItemDecoration(context, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder)
    }

    override fun iniEvent() {
        viewHolder?.srl?.setOnRefreshListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout?) {
                requestCaseApi("load")
            }

            override fun onRefresh(refreshLayout: RefreshLayout?) {
                requestCaseApi("refresh")
            }
        })
    }

    fun requestCaseApi(action: String) {
        when (action) {
            "load" -> {
                PonkoApp.interflowApi?.getParties(page++, 30)?.enqueue(object : HttpCallBack<ArrayList<ActivityCBean>>() {
                    override fun onSuccess(call: Call<ArrayList<ActivityCBean>>?, response: Response<ArrayList<ActivityCBean>>?) {
                        requestCaseApiSuccess(response?.body(), action)
                    }
                })
            }
            "refresh" -> {
                PonkoApp.interflowApi?.getParties(1, 30)?.enqueue(object : HttpCallBack<ArrayList<ActivityCBean>>() {
                    override fun onSuccess(call: Call<ArrayList<ActivityCBean>>?, response: Response<ArrayList<ActivityCBean>>?) {
                        requestCaseApiSuccess(response?.body(), action)
                    }
                })
            }
        }
    }

    private var adapter: SubActivityAdapter? = null

    fun requestCaseApiSuccess(act: ArrayList<ActivityCBean>?, action: String) {
        when (action) {
            "load" -> {
                adapter?.data?.addAll(act!!)
                val positionStart = adapter?.data?.size
                val itemCount = act?.size
                adapter?.notifyItemRangeChanged(positionStart!!, itemCount!!)
                viewHolder?.srl?.finishLoadMore()
            }
            "refresh" -> {
                //选项卡个数
                adapter = SubActivityAdapter(act!!)
                viewHolder?.rv?.adapter = adapter
                viewHolder?.srl?.finishRefresh()
            }
        }
    }

    override fun iniData() {
        viewHolder?.srl?.autoRefresh()
    }

    private class SubActivityHolder(view: View?) : BaseViewHolder(view!!) {
        override fun bindData(d: Any, position: Int) {
            Glide.with(itemView.context, (d as ActivityCBean).image, itemView.findViewById(R.id.iv_activity))
        }
    }

    private class SubActivityAdapter(val data: ArrayList<ActivityCBean>) : RecyclerView.Adapter<SubActivityHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SubActivityHolder {
            val view = ViewUtil.viewById(p0.context, R.layout.item_interflow_activity, p0)
            return SubActivityHolder(view)
        }

        override fun getItemCount(): Int {
            return if (data.isEmpty()) {
                0
            } else {
                data.size
            }
        }

        override fun onBindViewHolder(p0: SubActivityHolder, p1: Int) {
            p0.bindData(data[p1], p1)
        }
    }
}
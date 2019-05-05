package com.ponko.cn.module.interflow.frg

import android.support.v7.widget.DividerItemDecoration
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.ActivityCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshViewHolder
import com.ponko.cn.module.interflow.adapter.SubActivityAdapter
import com.xm.lib.common.base.BaseFragment
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
        viewHolder?.srl?.setOnLoadMoreListener {
            requestCaseApi("load")
        }
        viewHolder?.srl?.setOnRefreshListener {
            requestCaseApi("refresh")
        }

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
                page = 1
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




}
package com.ponko.cn.module.interflow.frg

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.AnalysisCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.RefreshViewHolder
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.xm.lib.common.base.BaseFragment
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.util.ViewUtil
import retrofit2.Call
import retrofit2.Response

class SubCaseFragment : BaseFragment() {
    private var viewHolder: RefreshViewHolder? = null
    private var adapter: SubCaseAdapter? = null
    private var page = 1

    companion object {
        fun create(): SubCaseFragment {
            return SubCaseFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_interflow_sub_case
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
        viewHolder?.isCanLoad(viewHolder?.rv, viewHolder?.srl)
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
                PonkoApp.interflowApi?.getAnalysis(page++, 30)?.enqueue(object : HttpCallBack<AnalysisCBean>() {
                    override fun onSuccess(call: Call<AnalysisCBean>?, response: Response<AnalysisCBean>?) {
                        requestCaseApiSuccess(response?.body(), action)
                    }
                })
            }
            "refresh" -> {
                PonkoApp.interflowApi?.getAnalysis(1, 30)?.enqueue(object : HttpCallBack<AnalysisCBean>() {
                    override fun onSuccess(call: Call<AnalysisCBean>?, response: Response<AnalysisCBean>?) {
                        requestCaseApiSuccess(response?.body(), action)
                    }
                })
            }
        }
    }


    fun requestCaseApiSuccess(case: AnalysisCBean?, action: String) {
        when (action) {
            "load" -> {
                adapter?.data?.addAll(case?.activities!!)
                val positionStart = adapter?.data?.size
                val itemCount = case?.activities?.size
                adapter?.notifyItemRangeChanged(positionStart!!, itemCount!!)
                viewHolder?.srl?.finishLoadMore()
            }
            "refresh" -> {
                //选项卡个数
                adapter = SubCaseAdapter(case?.activities!!)
                viewHolder?.rv?.adapter = adapter
                viewHolder?.srl?.finishRefresh()
            }
        }
    }

    override fun iniData() {
        viewHolder?.srl?.autoRefresh()
    }

    private class SubCaseViewHolder(view: View?) : BaseViewHolder(view!!) {
        override fun bindData(d: Any, position: Int) {

        }
    }

    private class SubCaseAdapter(val data: ArrayList<AnalysisCBean.ActivitiesBean>) : RecyclerView.Adapter<SubCaseViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SubCaseViewHolder {
            val view = ViewUtil.viewById(p0.context, R.layout.item_interflow_case, p0)
            return SubCaseViewHolder(view)
        }

        override fun getItemCount(): Int {
            return if (data.isEmpty()) {
                0
            } else {
                data.size
            }
        }

        override fun onBindViewHolder(p0: SubCaseViewHolder, p1: Int) {
            p0.bindData(data[p1], p1)
        }
    }
}
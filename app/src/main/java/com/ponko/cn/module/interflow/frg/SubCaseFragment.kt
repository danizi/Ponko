package com.ponko.cn.module.interflow.frg

import android.os.Bundle
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.AnalysisCBean
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.interflow.adapter.SubCaseAdapter
import com.ponko.cn.module.interflow.holder.SubCaseViewHolder
import com.xm.lib.common.base.rv.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response
import java.util.*

class SubCaseFragment : RefreshLoadFrg<Any, ArrayList<AnalysisCBean.ActivitiesBean>>() {

    private var typeId: String = ""
    private var requestSize: Int = 30

    companion object {
        fun create(typeId: String? = ""): SubCaseFragment {
            val frg = SubCaseFragment()
            val b = Bundle()
            b.putString("typeId", typeId)
            frg.arguments = b
            return frg
        }
    }

    override fun initDisplay() {
        addItemDecoration = false
        super.initDisplay()
    }

    override fun iniData() {
        typeId = arguments?.getString("typeId")!!
        super.iniData()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(SubCaseViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_interflow_case))
    }

    override fun requestMoreApi() {
        PonkoApp.interflowApi?.getAnalysis(typeId, ++page, requestSize)?.enqueue(object : HttpCallBack<AnalysisCBean>() {
            override fun onSuccess(call: Call<AnalysisCBean>?, response: Response<AnalysisCBean>?) {
                requestMoreSuccess(response?.body()?.activities)
            }
        })
    }

    override fun requestRefreshApi() {
        PonkoApp.interflowApi?.getAnalysis(typeId, 1, requestSize)?.enqueue(object : HttpCallBack<AnalysisCBean>() {
            override fun onSuccess(call: Call<AnalysisCBean>?, response: Response<AnalysisCBean>?) {
                requestRefreshSuccess(response?.body()?.activities)
            }
        })
    }

    override fun multiTypeData(body: ArrayList<AnalysisCBean.ActivitiesBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return SubCaseAdapter()
    }

    override fun presenter(): Any {
        return Any()
    }
}
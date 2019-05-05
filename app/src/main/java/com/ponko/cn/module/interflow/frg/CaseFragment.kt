package com.ponko.cn.module.interflow.frg

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.AnalysisCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.interflow.adapter.InterflowCaseAdapter
import com.xm.lib.common.base.BaseFragment
import retrofit2.Call
import retrofit2.Response

class CaseFragment : BaseFragment() {
    private var viewHolder: ViewHolder? = null

    override fun getLayoutId(): Int {
        return R.layout.frg_interflow_case
    }

    override fun findViews(view: View) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(view)
        }
    }

    override fun initDisplay() {

    }

    override fun iniEvent() {

    }

    override fun iniData() {
        //请求数据
        PonkoApp.interflowApi?.getAnalysis(1, 0)?.enqueue(object : HttpCallBack<AnalysisCBean>() {
            override fun onSuccess(call: Call<AnalysisCBean>?, response: Response<AnalysisCBean>?) {
                //选项卡个数
                val analysisCBean = response?.body()
                val frgs = ArrayList<Fragment>()
                val titls = ArrayList<String>()

                for (type in analysisCBean?.types!!) {
                    viewHolder?.tl?.addTab(viewHolder?.tl?.newTab()?.setText("")!!)
                    titls.add(type.name)
                    frgs.add(SubCaseFragment.create())
                }

                viewHolder?.vp?.adapter = InterflowCaseAdapter(childFragmentManager, frgs, titls)
                viewHolder?.tl?.setupWithViewPager(viewHolder?.vp)
            }
        })
    }


    private class ViewHolder private constructor(val tl: TabLayout, val vp: ViewPager) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tl = rootView.findViewById<View>(R.id.tl) as TabLayout
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                return ViewHolder(tl, vp)
            }
        }
    }
}
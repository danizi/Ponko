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
import com.xm.lib.component.XmStateView
import retrofit2.Call
import retrofit2.Response

/**
 * 案例
 */
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
        viewHolder?.viewState?.showLoading("正在加载中...")
        requestApi()
    }

    private fun requestApi() {
        PonkoApp.interflowApi?.getAnalysis(1, 0)?.enqueue(object : HttpCallBack<AnalysisCBean>() {
            override fun onSuccess(call: Call<AnalysisCBean>?, response: Response<AnalysisCBean>?) {
                //选项卡个数
                val analysisCBean = response?.body()
                val frgs = ArrayList<Fragment>()
                val titls = ArrayList<String>()
                titls.add("首页")
                frgs.add(SubCaseFragment.create())
                for (type in analysisCBean?.types!!) {
                    viewHolder?.tl?.addTab(viewHolder?.tl?.newTab()?.setText("")!!)
                    titls.add(type.name)
                    frgs.add(SubCaseFragment.create(type.id))
                }

                viewHolder?.vp?.adapter = InterflowCaseAdapter(childFragmentManager, frgs, titls)
                viewHolder?.tl?.setupWithViewPager(viewHolder?.vp)
                viewHolder?.viewState?.hide()
            }

            override fun onFailure(call: Call<AnalysisCBean>?, msg: String?) {
                super.onFailure(call, msg)
                //请求失败
                viewHolder?.viewState?.showError("请求数据失败请重试...", View.OnClickListener {
                    requestApi()
                })
            }
        })
    }


    private class ViewHolder private constructor(val tl: TabLayout, val vp: ViewPager, val viewState: XmStateView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tl = rootView.findViewById<View>(R.id.tl) as TabLayout
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                val viewState = rootView.findViewById<View>(R.id.view_state) as XmStateView

                return ViewHolder(tl, vp, viewState)
            }
        }
    }
}
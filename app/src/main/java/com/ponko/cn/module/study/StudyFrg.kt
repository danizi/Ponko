package com.ponko.cn.module.study

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.*
import com.ponko.cn.module.study.adapter.StudyAdapter
import com.ponko.cn.module.study.constract.StudyContract
import com.ponko.cn.module.study.holder.AdViewHolder
import com.ponko.cn.module.study.holder.BannerViewHolder
import com.ponko.cn.module.study.holder.CaseViewHolder
import com.ponko.cn.module.study.holder.CourseTypeViewHolder
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.base.rv.decoration.MyItemDecoration


class StudyFrg : MvpFragment<StudyContract.Present>(), StudyContract.View {

    private var srl: SmartRefreshLayout? = null
    private var rv: RecyclerView? = null
    private var adapter: StudyAdapter = StudyAdapter()

    override fun presenter(): StudyContract.Present {
        return StudyContract.Present(context, view = this)
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_nav_study
    }

    override fun findViews(view: View) {
        srl = view.findViewById(R.id.srl) as SmartRefreshLayout
        rv = view.findViewById(R.id.rv) as RecyclerView
    }

    override fun initDisplay() {
        srl?.autoRefresh(1000)
        srl?.isEnableRefresh = true
        srl?.isEnableLoadMore = false
        rv?.layoutManager = LinearLayoutManager(context)
        rv?.addItemDecoration(MyItemDecoration.divider(context!!, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder))  //https://www.jianshu.com/p/86aaaa49ed3e
    }

    override fun iniEvent() {
        srl?.setOnRefreshListener {
            //请求加载数据
            p?.requestStudyApi()
        }
    }

    override fun iniData() {
    }

    override fun requestStudyApiSuccess(body: MainCBean?) {
        //绑定ViewHolder
        adapter.addItemViewDelegate(0, BannerViewHolder::class.java, BannerBean::class.java, R.layout.item_study_banner)
        adapter.addItemViewDelegate(1, AdViewHolder::class.java, AdBean2::class.java, R.layout.item_study_ad)
        adapter.addItemViewDelegate(2, CourseTypeViewHolder::class.java, CourseTypeBean::class.java, R.layout.item_study_course_type)
        adapter.addItemViewDelegate(3, CaseViewHolder::class.java, CaseBean::class.java, R.layout.item_study_case)
        adapter.data = multiTypeData(body)
        //设置适配器
        rv?.adapter = adapter
        srl?.finishRefresh()
    }

    private fun multiTypeData(body: MainCBean?): List<Any> {
        val multiData = ArrayList<Any>()
        if (body?.tabbar != null) {
            //横幅 PS:横幅有可能为null
            multiData.add(BannerBean(body.scrolls, body.tabbar))
        }
        if (body?.ad != null && body.ad.isNotEmpty()) {
            //广告
            multiData.add(AdBean2(body.ad))
        }
        if (body?.types != null && body.types.isNotEmpty()) {
            //课程类别
            multiData.add(CourseTypeBean(body.types[0]))
            multiData.add(CourseTypeBean(body.types[1]))
        }
        if (body?.cases != null && body.cases.list.isNotEmpty()) {
            //交流案例
            multiData.add(CaseBean(body.cases))
        }
        return multiData
    }
}
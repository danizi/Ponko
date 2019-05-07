package com.ponko.cn.module.study

import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.*
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.study.adapter.StudyAdapter
import com.ponko.cn.module.study.constract.StudyContract
import com.ponko.cn.module.study.holder.AdViewHolder
import com.ponko.cn.module.study.holder.BannerViewHolder
import com.ponko.cn.module.study.holder.CaseViewHolder
import com.ponko.cn.module.study.holder.CourseTypeViewHolder
import com.xm.lib.common.base.rv.BaseRvAdapter

class StudyFrg : RefreshLoadFrg<StudyContract.Present, MainCBean>(), StudyContract.View {

    override fun initDisplay() {
        super.initDisplay()
        disableLoad = true
        addSearchBar(View.OnClickListener { }, View.OnClickListener { }, View.OnClickListener { })
    }

    override fun presenter(): StudyContract.Present {
        return StudyContract.Present(context, view = this)
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1, 2, 3),
                arrayOf(BannerViewHolder::class.java, AdViewHolder::class.java, CourseTypeViewHolder::class.java, CaseViewHolder::class.java),
                arrayOf(BannerBean::class.java, AdBean2::class.java, CourseTypeBean::class.java, CaseBean::class.java),
                arrayOf(R.layout.item_study_banner, R.layout.item_study_ad, R.layout.item_study_course_type, R.layout.item_study_case)
        )
    }

    override fun requestMoreApi() {
    }

    override fun requestRefreshApi() {
        p?.requestStudyApi()
    }

    override fun adapter(): BaseRvAdapter? {
        return StudyAdapter()
    }

    override fun requestStudyApiSuccess(body: MainCBean?) {
        requestRefreshSuccess(body)
    }

    override fun multiTypeData(body: MainCBean?): List<Any> {
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
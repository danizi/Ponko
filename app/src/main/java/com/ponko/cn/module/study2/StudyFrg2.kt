package com.ponko.cn.module.study2

import android.content.Intent
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.*
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.my.option.HistoryActivity
import com.ponko.cn.module.my.option.ProblemAct
import com.ponko.cn.module.study.SearchActivity
import com.ponko.cn.module.study.adapter.StudyAdapter
import com.ponko.cn.module.study.constract.StudyContract
import com.ponko.cn.module.study.holder.AdViewHolder
import com.ponko.cn.module.study.holder.BannerViewHolder
import com.ponko.cn.module.study.holder.CaseViewHolder
import com.ponko.cn.module.study.holder.CourseTypeViewHolder
import com.ponko.cn.module.study2.holder.BannerSubViewHolder
import com.ponko.cn.module.study2.holder.PayViewHolder
import com.ponko.cn.module.study2.holder.UnPayViewHolder
import com.ponko.cn.utils.ActivityUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.log.BKLog

/**
 * 新版学习页面
 */
class StudyFrg2 : RefreshLoadFrg<StudyContract2.Present, MainCBean>(), StudyContract2.V {

    override fun initDisplay() {
        super.initDisplay()
        disableLoad = true
        addSearchBar(View.OnClickListener {
            BKLog.d("点击搜索框")
            ActivityUtil.startActivity(context, Intent(context, SearchActivity::class.java))
        }, View.OnClickListener {
            BKLog.d("点击学习历史")
            ActivityUtil.startActivity(context, Intent(context, HistoryActivity::class.java))
        }, View.OnClickListener {
            BKLog.d("点击帮助")
            ActivityUtil.startActivity(context, Intent(context, ProblemAct::class.java))
        })
    }

    override fun presenter(): StudyContract2.Present {
        return StudyContract2.Present(context, view = this)
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(
                        0,
                        1,
                        2,
                        3,
                        4),
                arrayOf(
                        BannerViewHolder::class.java,
                        BannerSubViewHolder::class.java,
                        AdViewHolder::class.java,
                        PayViewHolder::class.java,
                        UnPayViewHolder::class.java),

                arrayOf(
                        BannerBean::class.java,
                        StudyHomeBannerSubBean::class.java,
                        AdBean2::class.java,
                        StudyHomePayBean::class.java,
                        StudyHomeUnPayBean::class.java),

                arrayOf(
                        R.layout.item_study_banner,
                        R.layout.item_study_sub_banner,
                        R.layout.item_study_ad,
                        R.layout.item_study_pay,
                        R.layout.item_study_un_pay)
        )
    }

    override fun requestMoreApi() {}

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
            body.tabbar.clear()
            multiData.add(BannerBean(body.scrolls, body.tabbar))
        }

        //横幅
        multiData.add(StudyHomeBannerSubBean())

        if (body?.ad != null && body.ad.isNotEmpty()) {
            //广告
            for (ad in body.ad) {
                multiData.add(AdBean2(ad))
            }
        }

        //已订购课程
        multiData.add(StudyHomePayBean())

        //未订购课程
        multiData.add(StudyHomeUnPayBean())

        return multiData
    }
}
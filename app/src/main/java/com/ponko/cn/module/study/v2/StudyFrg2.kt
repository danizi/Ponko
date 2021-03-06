package com.ponko.cn.module.study.v2

//import com.ponko.cn.MainActivity.Companion.bottomMenu
import android.content.Intent
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.*
import com.ponko.cn.constant.Constants
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.my.option.HistoryActivity
import com.ponko.cn.module.my.option.ProblemAct
import com.ponko.cn.module.study.v1.SearchActivity
import com.ponko.cn.module.study.v1.adapter.StudyAdapter
import com.ponko.cn.module.study.v1.holder.AdViewHolder
import com.ponko.cn.module.study.v2.holder.BannerSubViewHolder
import com.ponko.cn.module.study.v2.holder.BannerV2ViewHolder
import com.ponko.cn.module.study.v2.holder.PayViewHolder
import com.ponko.cn.module.study.v2.holder.UnPayViewHolder
import com.ponko.cn.utils.ActivityUtil
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import q.rorbin.badgeview.QBadgeView

/**
 * 新版学习页面
 */
class StudyFrg2 : RefreshLoadFrg<StudyContract2.Present, Main2CBean>(), StudyContract2.V {

    private var qbadgeView: QBadgeView? = null

    override fun iniData() {
        super.iniData()
        p?.registerTipReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        p?.unRegisterTipReceiver()
    }

    override fun showMsgTip(i: Int) {
        val intent = Intent(Constants.ACTION_BOTTOM_TIP_SHOW)
        intent.putExtra("count",i)
        context?.sendBroadcast(intent)

//        val count = i
//        val view = bottomMenu.getTabAt(3)?.customView
//        if (count > 0) {
//            if (qbadgeView == null) {
//                qbadgeView = QBadgeView(context)
//            }
//            qbadgeView?.visibility = View.VISIBLE
//            qbadgeView?.bindTarget(view)?.badgeNumber = count
//            qbadgeView?.setGravityOffset(6f, 0f, true)
//            return
//        }
//
//        qbadgeView?.visibility = View.GONE
//        BKLog.d("我的消息数量:$count")
    }

    override fun hideMsgTip() {
        val intent = Intent(Constants.ACTION_BOTTOM_TIP_HIDE)
        context?.sendBroadcast(intent)

//        //去掉消息提醒
//        qbadgeView?.visibility=View.GONE
//        qbadgeView?.bindTarget(view)?.badgeNumber = -1
    }

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
                        BannerV2ViewHolder::class.java,
                        BannerSubViewHolder::class.java,
                        AdViewHolder::class.java,
                        PayViewHolder::class.java,
                        UnPayViewHolder::class.java),

                arrayOf(
                        BannerV2Bean::class.java,
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

    override fun requestStudyApiSuccess(body: Main2CBean?) {
        requestRefreshSuccess(body)
    }

    override fun requestStudyApiFailure() {
        requestRefreshFailure()
    }

    override fun multiTypeData(body: Main2CBean?): List<Any> {
        val multiData = ArrayList<Any>()

        //顶部横幅
        if (!body?.banner_top?.isEmpty()!!) {
            multiData.add(BannerV2Bean(body.banner_top!!))
        }

        //子横幅
        if (!body.banner_mini.isEmpty()) {
            multiData.add(StudyHomeBannerSubBean(body.banner_mini))
        }

        //广告
        if (!body.banner_middle.isEmpty()) {
            for (ad in body.banner_middle) {
                val adBean = AdBean()
                adBean.image = ad.avatar
                adBean.title = ad.title
                adBean.id = ad.id
                adBean.type = ad.link_type
                adBean.link = ad.link_value
                multiData.add(AdBean2(adBean))
            }
        }

        //已订购课程
        if (!body.products_purchased.isEmpty()) {
            multiData.add(StudyHomePayBean(body.products_purchased))
        }

        //未订购课程
        if (!body.products_all.isEmpty()) {
            multiData.add(StudyHomeUnPayBean(body.products_all))
        }

        return multiData
    }
}
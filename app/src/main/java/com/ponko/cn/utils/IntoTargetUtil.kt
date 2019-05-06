package com.ponko.cn.utils

import android.annotation.SuppressLint
import android.content.Context
import com.ponko.cn.WebAct
import com.xm.lib.common.log.BKLog

@SuppressLint("StaticFieldLeak")
/**
 * 跳转到指定目标窗口
 */
object IntoTargetUtil {
    private val TAG = "IntoTargetUtil"
    /**
     * @param context 上下文对象
     * @param linkType 跳转的类型
     * @param linkValue 跳转携带的值
     */
    fun target(context: Context?, linkType: String?, linkValue: String?) {
        when {
            linkType.equals("profile_edit", true) -> {
                profileEdit(linkValue)
            }
            linkType.equals("invite", true) -> {
                invite(linkValue)
            }
            linkType.equals("msg", true) -> {
                msg(linkValue)
            }
            linkType.equals("payment", true) or linkType.equals("pay", true) -> {
                pay(context,linkValue)
            }
            linkType.equals("url", true) or linkType.equals("detail", true) -> {
                url(context,linkValue)
            }
            linkType.equals("course_type", true) -> {
                courseType(linkValue)
            }
            linkType.equals("course_detail", true) or linkType.equals("course", true) -> {
                courseDetail(linkValue)
            }
            linkType.equals("store", true) -> {
                store(linkValue)
            }
            linkType.equals("store_score_task", true) -> {
                storeScoreTask(linkValue)
            }
            linkType.equals("store_score_list", true) -> {
                storeScoreList(linkValue)
            }
            linkType.equals("store_score_exchange_list", true) -> {
                storeScoreExchangeList(linkValue)
            }
            linkType.equals("store_score_ranking", true) -> {
                storeScoreRanking(linkValue)
            }
            linkType.equals("store_product_detail", true) -> {
                storeProductDetail(linkValue)
            }
            linkType.equals("trial", true) -> {
                trial(linkValue)
            }
            linkType.equals("case", true) -> {
                case(linkValue)
            }
            linkType.equals("wechat_bind", true) -> {
                wechatBind(linkValue)
            }
        }
    }

    private fun wechatBind(linkValue: String?) {
        BKLog.d(TAG, "微信绑定操作 linkValue:$linkValue")
    }

    private fun case(linkValue: String?) {
        BKLog.d(TAG, "交流页面 linkValue:$linkValue")
    }

    private fun trial(linkValue: String?) {
        BKLog.d(TAG, "试听页面即免费页面 linkValue:$linkValue")
    }

    private fun storeProductDetail(linkValue: String?) {
        BKLog.d(TAG, "积分产品详情页 linkValue:$linkValue")
    }

    private fun storeScoreRanking(linkValue: String?) {
        BKLog.d(TAG, "积分排行榜 linkValue:$linkValue")
    }

    private fun storeScoreExchangeList(linkValue: String?) {
        BKLog.d(TAG, "积分阅换记录 linkValue:$linkValue")
    }

    private fun storeScoreList(linkValue: String?) {
        BKLog.d(TAG, "积分获取记录 linkValue:$linkValue")
    }

    private fun storeScoreTask(linkValue: String?) {
        BKLog.d(TAG, "积分任务 linkValue:$linkValue")
    }

    private fun store(linkValue: String?) {
        BKLog.d(TAG, "积分商城 linkValue:$linkValue")
    }

    private fun courseDetail(linkValue: String?) {
        BKLog.d(TAG, "课程详情 linkValue:$linkValue")
    }

    private fun courseType(linkValue: String?) {
        BKLog.d(TAG, "课程分类 linkValue:$linkValue")
    }

    private fun pay(context: Context?,linkValue: String?) {
        BKLog.d(TAG, "支付网页 linkValue:$linkValue")
        WebAct.start(context,"pay",linkValue )
    }

    private fun url(context: Context?,linkValue: String?) {
        BKLog.d(TAG, "普通网页 linkValue:$linkValue")
        WebAct.start(context,"pay",linkValue )
    }

    private fun msg(linkValue: String?) {
        BKLog.d(TAG, "提醒 linkValue:$linkValue")
    }

    private fun invite(linkValue: String?) {
        BKLog.d(TAG, "邀请 linkValue:$linkValue")
    }

    private fun profileEdit(linkValue: String?) {
        BKLog.d(TAG, "个人信息编辑 linkValue:$linkValue")
    }
}
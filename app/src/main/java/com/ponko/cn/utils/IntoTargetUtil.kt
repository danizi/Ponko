package com.ponko.cn.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.ponko.cn.MainActivity
import com.ponko.cn.WebAct
import com.ponko.cn.module.login.LoginAccountAct
import com.ponko.cn.module.login.LoginStartAct
import com.ponko.cn.module.my.option.InviteFriendActivity
import com.ponko.cn.module.my.option.RemindAct
import com.ponko.cn.module.my.option.acount.AccountAct
import com.ponko.cn.module.my.option.acount.PersonalActivity
import com.ponko.cn.module.my.option.store.*
import com.ponko.cn.module.study.StudyCourseDetailActivity
import com.xm.lib.common.log.BKLog

@SuppressLint("StaticFieldLeak")
/**
 * 跳转到指定目标窗口
 */
object IntoTargetUtil {
    private val TAG = "IntoTargetUtil"
    private var context: Context? = null
    /**
     * @param context 上下文对象
     * @param linkType 跳转的类型
     * @param linkValue 跳转携带的值
     */
    fun target(context: Context?, linkType: String?, linkValue: String?) {
        this.context = context
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
                pay(context, linkValue)
            }
            linkType.equals("url", true) or linkType.equals("detail", true) -> {
                url(context, linkValue)
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
            linkType.equals("head", true) -> {
                headClick()
            }
        }
    }

    /**
     * 点击头像
     */
    private fun headClick() {
        when (CacheUtil.getUserType()) {
            CacheUtil.USERTYPE_TOURIST -> {
                BKLog.d("游客模式 - 点击头像 - 进入登录页面")
//                val intent = Intent(context, LoginAccountAct::class.java)
                val intent = Intent(context, LoginStartAct::class.java)
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                ActivityUtil.startActivity(context, intent)
            }
            CacheUtil.USERTYPE_WXLOGIN -> {
                BKLog.d("微信模式 - 点击头像 - 进入个人信息页面")
                ActivityUtil.startActivity(context, Intent(context, AccountAct::class.java))
            }
            CacheUtil.USERTYPE_LOGIN -> {
                BKLog.d("登录模式 - 点击头像 - (未綁定先綁定綁定成功) - 进入个人信息页面")
                ActivityUtil.startActivity(context, Intent(context, AccountAct::class.java))
            }
        }
    }

    private fun wechatBind(linkValue: String?) {
        BKLog.d(TAG, "微信绑定操作 linkValue:$linkValue")
        //发送一个绑定广播
    }

    private fun case(linkValue: String?) {
        BKLog.d(TAG, "交流页面 linkValue:$linkValue")
        MainActivity.bottomMenu.select(2)
    }

    private fun trial(linkValue: String?) {
        BKLog.d(TAG, "试听页面即免费页面 linkValue:$linkValue")
        MainActivity.bottomMenu.select(1)
    }

    private fun storeProductDetail(linkValue: String?) {
        BKLog.d(TAG, "积分产品详情页 linkValue:$linkValue")
        WebAct.startExChange(context, "", linkValue, "", linkValue, "", "", 0)
    }

    private fun storeScoreRanking(linkValue: String?) {
        BKLog.d(TAG, "积分排行榜 linkValue:$linkValue")
        ActivityUtil.startActivity(context, Intent(context, IntegralRankingActivity::class.java))
    }

    private fun storeScoreExchangeList(linkValue: String?) {
        BKLog.d(TAG, "积分阅换记录 linkValue:$linkValue")
        ActivityUtil.startActivity(context, Intent(context, IntegralExchangedAct::class.java))
    }

    private fun storeScoreList(linkValue: String?) {
        BKLog.d(TAG, "积分获取记录 linkValue:$linkValue")
        ActivityUtil.startActivity(context, Intent(context, IntegralRecordActivity::class.java))
    }

    private fun storeScoreTask(linkValue: String?) {
        BKLog.d(TAG, "积分任务 linkValue:$linkValue")
        ActivityUtil.startActivity(context, Intent(context, IntegralTaskActivity::class.java))
    }

    private fun store(linkValue: String?) {
        BKLog.d(TAG, "积分商城 linkValue:$linkValue")
        ActivityUtil.startActivity(context, Intent(context, StoreAct::class.java))
    }

    private fun courseDetail(linkValue: String?) {
        BKLog.d(TAG, "课程详情 linkValue:$linkValue")
        StudyCourseDetailActivity.start(context, linkValue, "", 0L, 0L)
    }

    private fun courseType(linkValue: String?) {
        BKLog.d(TAG, "课程分类 linkValue:$linkValue")
    }

    private fun pay(context: Context?, linkValue: String?) {
        BKLog.d(TAG, "支付网页 linkValue:$linkValue")
        WebAct.start(context, "pay", linkValue)
    }

    private fun url(context: Context?, linkValue: String?) {
        BKLog.d(TAG, "普通网页 linkValue:$linkValue")
        WebAct.start(context, "url", linkValue)
    }

    private fun msg(linkValue: String?) {
        BKLog.d(TAG, "提醒 linkValue:$linkValue")
        ActivityUtil.startActivity(context, Intent(context, RemindAct::class.java))
    }

    private fun invite(linkValue: String?) {
        BKLog.d(TAG, "邀请 linkValue:$linkValue")
        ActivityUtil.startActivity(context, Intent(context, InviteFriendActivity::class.java))
    }

    private fun profileEdit(linkValue: String?) {
        BKLog.d(TAG, "个人信息编辑 linkValue:$linkValue")
        ActivityUtil.startActivity(context, Intent(context, PersonalActivity::class.java))
    }
}
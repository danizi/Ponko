package com.ponko.cn.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.ponko.cn.MainActivity
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.constant.Constants
import com.ponko.cn.module.login.LoginStartAct
import com.ponko.cn.module.my.option.InviteFriendShareActivity
import com.ponko.cn.module.my.option.acount.AccountAct
import com.ponko.cn.module.my.option.acount.PersonalActivity
import com.ponko.cn.module.my.option.store.*
import com.ponko.cn.module.my.v1.option.PosterActivity
import com.ponko.cn.module.my.v1.option.ReportActivity
import com.ponko.cn.module.my.v1.option.store.IntegralExchangedClassAct
import com.ponko.cn.module.my.v2.RemindActV2
import com.ponko.cn.module.study.v1.CourseTypeLinearActivity
import com.ponko.cn.module.study.v1.StudyCourseDetailActivity
import com.ponko.cn.module.study.v2.CourseTypeLinearActivityV2
import com.ponko.cn.module.study.v2.MediaAct
import com.ponko.cn.module.web.WebAct
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.OnEnterListener
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
import org.json.JSONObject

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
        if (context == null) {
            ToastUtil.show("上下文为null，跳转失败。")
            return
        }
        if (TextUtils.isEmpty(linkType)) {
            ToastUtil.show("linkType为null，跳转失败。")
            return
        }

        this.context = context?.applicationContext
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
            linkType.equals("free", true) -> {
                free(linkValue)
            }
            linkType.equals("home", true) -> {
                home(linkValue)
            }
            linkType.equals("wechat_bind", true) -> {
                wechatBind(linkValue)
            }
            linkType.equals("head", true) -> {
                headClick()
            }
            linkType.equals("STUDY_REPORT_MAIN", true) -> {
                reportMain()
            }
            linkType.equals("STUDY_REPORT_SHARE", true) -> {
                reportShare()
            }
            linkType.equals("STUDY_HISTORY_LIST", true) -> {
                reportHistory()
            }
            //
            linkType.equals("logout", true) -> {
                DialogUtil.show(context, "", "是否退出登录？", true, object : OnEnterListener {
                    override fun onEnter(dlg: AlertDialog) {
                        ActivityUtil.clearTheStackStartActivity(context, Intent(context, LoginStartAct::class.java))
                    }
                }, null)
                BKLog.d("logout - value:$linkValue")
            }

            linkType.equals("exit", true) -> {
                BKLog.d("exit - value:$linkValue")
                //CacheUtil.clearUserInfo()
                //System.exit(0)
                val act = PonkoApp.activityManager.getTopActivity()
                val intent = Intent(act, LoginStartAct::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                ActivityUtil.startActivity(act, intent)
            }

            linkType.equals("player", true) -> {
                BKLog.d("player - value:$linkValue")
                val intent = Intent(context, MediaAct::class.java)
                val jsonObject = JSONObject(linkValue)
                val url = jsonObject.getString("url")
                val title = jsonObject.getString("title")
                val auto_play = jsonObject.getBoolean("auto_play")
                val position = jsonObject.getInt("position")
                intent.putExtra("url", url)
                intent.putExtra("title", title)
                intent.putExtra("auto_play", auto_play)
                intent.putExtra("position", position)
                ActivityUtil.startActivity(context, intent)
            }

            linkType.equals("mp_call", true) -> {
                BKLog.d("mp_call - value:$linkValue")
                val share = WxShare(context as Activity)
                share.init(ShareConfig.Builder().appid(Constants.APP_ID).build())
                val jsonObject = JSONObject(linkValue)
                val username = jsonObject.getString("username")
                val path = jsonObject.getString("path")
                val type = jsonObject.getInt("type")
                share.miniProgram(username, path, type)
            }

            linkType.equals("camera_album", true) -> {
                BKLog.d("camera_album - value:$linkValue")
                (context as Activity).runOnUiThread {
                    CameraUtil2.showSelectDlg(context)
                    //在context对应的窗口中调用 CameraUtil2.onActivityResult(...)方法
                }
            }
        }
    }

    /**
     * 学习报告 - 学习历史界面
     */
    private fun reportHistory() {
        ActivityUtil.startActivity(context, Intent(context, IntegralExchangedClassAct::class.java))
    }

    /**
     * 学习报告 - 分享学习报告
     */
    private fun reportShare() {
        ActivityUtil.startActivity(context, Intent(context, PosterActivity::class.java))
    }

    /**
     * 学习报告 - 主界面
     */
    private fun reportMain() {
        ActivityUtil.startActivity(context, Intent(context, ReportActivity::class.java))
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

    private fun home(linkValue: String?) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("index", 0)
        ActivityUtil.startActivity(context, intent)
        val intentMune = Intent(Constants.ACTION_BOTTOM_MENU)
        intent.putExtra("index", 2)
        context?.sendBroadcast(intentMune)
    }

    private fun case(linkValue: String?) {
        BKLog.d(TAG, "交流页面 linkValue:$linkValue")
        //MainActivity.bottomMenu.select(2)
        val intent = Intent(Constants.ACTION_BOTTOM_MENU)
        intent.putExtra("index", 2)
        context?.sendBroadcast(intent)
    }

    private fun free(linkValue: String?) {
        BKLog.d(TAG, "免费页面 linkValue:$linkValue")
        //MainActivity.bottomMenu.select(1)
        val intent = Intent(Constants.ACTION_BOTTOM_MENU)
        intent.putExtra("index", 1)
        context?.sendBroadcast(intent)
    }

    private fun trial(linkValue: String?) {
        BKLog.d(TAG, "试听页面即免费页面 linkValue:$linkValue")
        //MainActivity.bottomMenu.select(1)
        val intent = Intent(Constants.ACTION_BOTTOM_MENU)
        intent.putExtra("index", 1)
        context?.sendBroadcast(intent)
    }

    private fun storeProductDetail(linkValue: String?) {
        BKLog.d(TAG, "积分产品详情页 linkValue:$linkValue")
        WebAct.startExChange(context, "exchange", linkValue, "兑换课程", linkValue, "0", "1", 1) //ps:
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
        when (PonkoApp.getAppUIVersion(context!!)) {
            Constants.UI_VERSION_1 -> {
                CourseTypeLinearActivity.start(context, "课程", linkValue)
            }
            Constants.UI_VERSION_2 -> {
                CourseTypeLinearActivityV2.start(context, "课程", linkValue)
            }
        }
    }

    private fun pay(context: Context?, linkValue: String?) {
        BKLog.d(TAG, "支付网页 linkValue:$linkValue")
        WebAct.start(context, "pay", linkValue, "", Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private fun url(context: Context?, linkValue: String?) {
        BKLog.d(TAG, "普通网页 linkValue:$linkValue")
        if (linkValue?.startsWith("http")!! || linkValue.startsWith("https")) {
            WebAct.start(context, "url", linkValue, "", Intent.FLAG_ACTIVITY_NEW_TASK)
        } else {
            WebAct.start(context, "url", "${Constants.BASE_API}$linkValue", "", Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun msg(linkValue: String?) {
        BKLog.d(TAG, "提醒 linkValue:$linkValue")
        ActivityUtil.startActivity(context, Intent(context, RemindActV2::class.java))
    }

    private fun invite(linkValue: String?) {
        BKLog.d(TAG, "邀请 linkValue:$linkValue")
        //ActivityUtil.startActivity(context, Intent(context, InviteFriendActivity::class.java))
        InviteFriendShareActivity.start(context!!, linkValue)

    }

    private fun profileEdit(linkValue: String?) {
        BKLog.d(TAG, "个人信息编辑 linkValue:$linkValue")
        ActivityUtil.startActivity(context, Intent(context, PersonalActivity::class.java))
    }
}
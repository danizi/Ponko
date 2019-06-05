package com.ponko.cn.utils

import android.text.TextUtils
import com.ponko.cn.app.PonkoApp
import com.tencent.bugly.proguard.s
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.SPUtil

object CacheUtil {
    private const val SP_FILE_NAME = "Cache.sp"
    const val USERTYPE_TOURIST = "Tourist"
    const val USERTYPE_LOGIN = "Login"
    const val USERTYPE_WXLOGIN = "WXLogin"

    /**
     * 保存用户token
     */
    fun putToken(token: String?) {
        BKLog.d("put token:$token")
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "token", token)
    }

    fun getToken(): String? {
        val token = SPUtil.get(PonkoApp.app, SP_FILE_NAME, "token", "token").toString()
        BKLog.d("get token:$token")
        return token
    }

    /**
     * 获取用户类型
     */
    fun getUserType(): String {
        return SPUtil.get(PonkoApp.app, SP_FILE_NAME, "UserType", "").toString()
    }


    /**
     * 用户-游客
     */
    fun putUserTypeTourist() {
        BKLog.d("put TypeTourist")
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "UserType", "Tourist")
    }

    /**
     * 用户-登录
     */
    fun putUserTypeLogin() {
        BKLog.d("put TypeLogin")
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "UserType", "Login")
    }

    /**
     * 用户微信-绑定
     */
    fun putUserTypeWx() {
        BKLog.d("put WXLogin")
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "UserType", "WXLogin")
    }

    /**
     * 用户是否是登录
     */
    @Deprecated("")
    fun isUserTypeLogin(): Boolean {
        val type = SPUtil.get(PonkoApp.app, SP_FILE_NAME, "UserType", "").toString()
        BKLog.d("UserType : $type")
        if (type == "Tourist") {
            return false
        }
        return true
    }


    /**
     * 保利威视视频配置信息
     */
    fun putPolyvConfig(configJsonStr: String) {
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "PolyvConfig", configJsonStr)
    }

    fun getPolycConfig(): String {
        return SPUtil.get(PonkoApp.app, SP_FILE_NAME, "PolyvConfig", "PolyvConfig").toString()
    }

    /**
     * 用户搜索关键字
     */
    fun putSearchRecord(record: String) {
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "record", record)
    }

    fun getSearchRecord(): String {
        return SPUtil.get(PonkoApp.app, SP_FILE_NAME, "record", "").toString()
    }

    fun clearUserInfo() {
        putToken("")
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "UserType", "")
    }

    /**
     * 引导页面
     */
    fun putJoinGuide(s: String) {
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "JoinGuide", s)
    }

    fun getJoinGuide(): String {
        return SPUtil.get(PonkoApp.app, SP_FILE_NAME, "JoinGuide", "") as String
    }

    /**
     * 视频分辨率、下载（ 1 标准  2 高清 3超高清） 选择
     */
    fun putMediaType(s: String) {
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "MediaType", s)
    }

    fun getMediaType(): String {
        return SPUtil.get(PonkoApp.app, SP_FILE_NAME, "MediaType", "3") as String
    }

    /**
     * 日志开关 0打开 1关闭
     */
    fun putLog(s: String) {
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "Log", s)
    }

    fun getLog(): String {
        return SPUtil.get(PonkoApp.app, SP_FILE_NAME, "Log", "1") as String
    }

    /**
     * UI 测试标志位 1关闭 2打开
     */
    fun putUI(s: String) {
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "UI", s)
    }

    fun getUI(): String {
        return SPUtil.get(PonkoApp.app, SP_FILE_NAME, "studyUI", "1") as String
    }

    /**
     * study 首页标志位 1旧版 2新版
     */
    fun putStudyUI(s: String) {
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "studyUI", s)
    }

    fun getStudyUI(): String {
        return SPUtil.get(PonkoApp.app, SP_FILE_NAME, "studyUI", "2") as String
    }

    /**
     * 跳转测试 详情请查看IntoTargetUtil
     * 微信绑定操作
     * 交流页面
     * 免费页面
     * 积分产品详情页面
     * 积分排行榜
     * 积分阅换记录
     * 积分获取记录
     * 积分任务
     * 积分商城
     * 课程详情
     * 支付网页
     * 普通网页
     * 提醒
     * 邀请
     * 个人信息编辑页面
     */
    fun putJump(s: String) {
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "Jump", s)
    }

    fun getJump(): String {
        return SPUtil.get(PonkoApp.app, SP_FILE_NAME, "Jump", "2") as String
    }


}
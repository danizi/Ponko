package com.ponko.cn.utils

import com.ponko.cn.app.PonkoApp
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.SPUtil

object CacheUtil {
    private const val SP_FILE_NAME = "Cache.sp"

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
     * 用户是否是登录
     */
    fun isUserTypeLogin(): Boolean {
        val type = SPUtil.get(PonkoApp.app, SP_FILE_NAME, "UserType", "UserType").toString()
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
}
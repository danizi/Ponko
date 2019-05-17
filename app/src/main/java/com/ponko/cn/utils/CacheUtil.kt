package com.ponko.cn.utils

import com.ponko.cn.app.PonkoApp
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.SPUtil

object CacheUtil {
    private const val SP_FILE_NAME = "Cache.sp"

    fun putToken(token: String?) {
        BKLog.d("put token:$token")
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "token", token)
    }

    fun getToken(): String? {
        val token = SPUtil.get(PonkoApp.app, SP_FILE_NAME, "token", "token").toString()
        BKLog.d("get token:$token")
        return token
    }

    fun putUserTypeTourist() {
        BKLog.d("put TypeTourist")
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "UserType", "Tourist")
    }

    fun putUserTypeLogin() {
        BKLog.d("put TypeLogin")
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "UserType", "Login")
    }

    fun isUserTypeLogin(): Boolean {
        val type = SPUtil.get(PonkoApp.app, SP_FILE_NAME, "UserType", "UserType").toString()
        BKLog.d("UserType : $type")
        if (type == "Tourist") {
            return false
        }
        return true
    }

    fun putPolyvConfig(configJsonStr: String) {
        SPUtil.put(PonkoApp.app, SP_FILE_NAME, "PolyvConfig", configJsonStr)
    }

    fun getPolycConfig(): String {
        return SPUtil.get(PonkoApp.app, SP_FILE_NAME, "PolyvConfig", "PolyvConfig").toString()
    }
}
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

}
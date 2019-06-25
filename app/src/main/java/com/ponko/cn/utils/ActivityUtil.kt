package com.ponko.cn.utils

import android.content.Context
import android.content.Intent

object ActivityUtil {

    /**
     * 启动窗口，为了统一跳转动画，最好使用该方法
     */
    fun startActivity(context: Context?, intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }

    /**
     * 启动窗口清空之前的栈,一般是跳转到登录页面使用该方法
     */
    fun clearTheStackStartActivity(context: Context?, intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        CacheUtil.clearUserInfo()
        startActivity(context?.applicationContext, intent)
    }

}
package com.ponko.cn.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

object ActivityUtil {


    fun startActivity(context: Context?, intent: Intent, enterAnim: Int, exitAnim: Int) {
        startActivity(context, intent)
    }

    /**
     * 启动窗口，为了统一跳转动画，最好使用该方法
     */
    fun startActivity(context: Context?, intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)   // Applliaction context 需要添加这句代码
        context?.startActivity(intent)
        if (context is Activity) {
            (context as Activity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    /**
     * 启动窗口清空之前的栈,一般是跳转到登录页面使用该方法
     */
    fun clearTheStackStartActivity(context: Context?, intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        CacheUtil.clearUserInfo()
        startActivity(context?.applicationContext, intent)
        if (context is Activity) {
            (context as Activity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

}
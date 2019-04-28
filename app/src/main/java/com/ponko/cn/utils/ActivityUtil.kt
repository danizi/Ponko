package com.ponko.cn.utils

import android.content.Context
import android.content.Intent

object ActivityUtil {
    fun startActivity(context: Context?, intent: Intent) {
        context?.startActivity(intent)
    }
}
package com.ponko.cn.module.interflow.constract

import android.content.Context

class InterflowConstract {
    interface View {}
    class Model {}
    class Present(private val context: Context?, private val view: View?) {}
}
package com.ponko.cn.module.my.constract

import android.content.Context

class MyConstract {
    interface View {}
    class Model {}
    class Present(private val context: Context?, private val view: View?) {}
}
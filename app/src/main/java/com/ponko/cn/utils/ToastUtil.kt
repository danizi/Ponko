package com.ponko.cn.utils

import android.widget.Toast
import com.ponko.cn.app.PonkoApp

object ToastUtil {

    fun show(msg: String?) {
        Toast.makeText(PonkoApp.app, msg, Toast.LENGTH_SHORT).show()
    }
}
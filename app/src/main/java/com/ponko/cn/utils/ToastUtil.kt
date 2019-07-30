package com.ponko.cn.utils

import android.os.Looper
import android.widget.Toast
import com.ponko.cn.app.PonkoApp

object ToastUtil {

    fun show(msg: String?) {
        //先检查当前线程
        if (Looper.getMainLooper() == Looper.myLooper()) {
            Toast.makeText(PonkoApp.app, msg, Toast.LENGTH_SHORT).show()
        }else{
            Looper.prepare()
            Toast.makeText(PonkoApp.app, msg, Toast.LENGTH_SHORT).show()
            Looper.loop()
        }
    }
}
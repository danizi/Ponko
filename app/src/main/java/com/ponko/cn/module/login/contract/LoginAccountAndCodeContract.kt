package com.ponko.cn.module.login.contract

import android.app.Activity
import android.content.Context
import com.ponko.cn.module.login.LoginFindPwdAct

class LoginAccountAndCodeContract {
    interface V {}

    class M {}

    class P(val context: Context, v: V?) {
        private val m = M()

        fun clickClose() {
            (context as Activity).finish()
        }

        fun clickFindPwd() {
            LoginFindPwdAct.startFindPwd(context)
        }

    }
}
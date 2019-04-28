package com.ponko.cn.module.login.contract

import android.content.Context
import android.content.Intent
import com.ponko.cn.module.login.LoginAccountAct
import com.ponko.cn.module.login.LoginRegisterAct
import com.ponko.cn.module.login.LoginWxAct
import com.ponko.cn.utils.ActivityUtil

class LoginStartContract {
    interface View {
        fun showTouristDlg()
    }

    class Model {
        fun requestTourist() {

        }
    }

    class Present(private val context: Context, private val v: View) {

        fun joinMainPageByTourist() {
            v.showTouristDlg()
        }

        fun clickWxLogin() {
            ActivityUtil.startActivity(context, Intent(context, LoginWxAct::class.java))
        }

        fun clickAccountLogin() {
            ActivityUtil.startActivity(context, Intent(context, LoginAccountAct::class.java))
        }

        fun clickRegister() {
            ActivityUtil.startActivity(context, Intent(context, LoginRegisterAct::class.java))
        }

    }
}
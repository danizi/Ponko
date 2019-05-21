package com.ponko.cn.module.login

import android.os.Bundle
import com.ponko.cn.R
import com.ponko.cn.module.common.PonkoBaseAct

class LoginResetPhoneAct : PonkoBaseAct<Any>() {
    override fun presenter(): Any {
        return Any()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_reset_phone
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_reset_phone)
    }
}

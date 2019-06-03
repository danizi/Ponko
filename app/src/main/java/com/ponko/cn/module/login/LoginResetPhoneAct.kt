package com.ponko.cn.module.login

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatImageButton
import android.view.View
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.module.common.PonkoBaseAct


class LoginResetPhoneAct : PonkoBaseAct<Any>() {

    private var ui: ViewHolder? = null
    override fun findViews() {
        super.findViews()
        if (ui == null) {
            ui = ViewHolder.create(this)
        }
    }

    override fun iniEvent() {
        super.iniEvent()
        ui?.registerBackButton?.setOnClickListener {
            finish()
        }
        ui?.btnEnter?.setOnClickListener {
            LoginUpdateAct.startFromResetPhone(this,"")
        }
    }

    override fun presenter(): Any {
        return Any()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_reset_phone
    }

    private class ViewHolder private constructor(val registerBackButton: AppCompatImageButton, val tvPhone: TextView, val btnEnter: AppCompatButton) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val registerBackButton = rootView.findViewById<View>(R.id.registerBackButton) as AppCompatImageButton
                val tvPhone = rootView.findViewById<View>(R.id.tv_phone) as TextView
                val btnEnter = rootView.findViewById<View>(R.id.btn_enter) as AppCompatButton
                return ViewHolder(registerBackButton, tvPhone, btnEnter)
            }
        }
    }

}

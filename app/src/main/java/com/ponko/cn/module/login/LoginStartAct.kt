package com.ponko.cn.module.login

import android.support.constraint.ConstraintLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.module.login.contract.LoginStartContract
import com.xm.lib.common.base.mvp.MvpActivity

class LoginStartAct : MvpActivity<LoginStartContract.Present>(), LoginStartContract.View {

    override fun showTouristDlg() {
        //弹出对话框
        p?.joinMainPageByTourist()
    }

    private var clTourist: ConstraintLayout? = null
    private var clWxLogin: ConstraintLayout? = null
    private var tvAccountLogin: TextView? = null
    private var tvRegister: TextView? = null

    override fun presenter(): LoginStartContract.Present {
        return LoginStartContract.Present(context = this, v = this)
    }

    override fun setContentViewBefore() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_start
    }

    override fun findViews() {
        clTourist = findViewById(R.id.cl_tourist)
        clWxLogin = findViewById(R.id.cl_wx_login)
        tvAccountLogin = findViewById(R.id.tv_account_login)
        tvRegister = findViewById(R.id.tv_register)
    }

    override fun initDisplay() {

    }

    override fun iniData() {

    }

    override fun iniEvent() {
        clTourist?.setOnClickListener {
            showTouristDlg()
        }
        clWxLogin?.setOnClickListener {
            p?.clickWxLogin()
        }
        tvAccountLogin?.setOnClickListener {
            p?.clickAccountLogin()
        }
        tvRegister?.setOnClickListener {
            p?.clickRegister()
        }
    }
}

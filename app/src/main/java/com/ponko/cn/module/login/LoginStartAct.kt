package com.ponko.cn.module.login

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.module.login.contract.LoginStartContract
import com.ponko.cn.utils.DialogUtil
import com.xm.lib.common.base.mvp.MvpActivity
import com.xm.lib.component.OnCancelListener
import com.xm.lib.component.OnEnterListener

class LoginStartAct : MvpActivity<LoginStartContract.Present>(), LoginStartContract.View {

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

    override fun showTouristDlg() {
        //弹出对话框
        DialogUtil.show(this, "", "是否游客登录", true, object : OnEnterListener {
            override fun onEnter(dlg: AlertDialog) {
                p?.joinMainPageByTourist()
            }
        }, object : OnCancelListener {
            override fun onCancel(dlg: AlertDialog) {
                dlg.dismiss()
            }
        })
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p?.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        p?.onDestroy()
    }
}

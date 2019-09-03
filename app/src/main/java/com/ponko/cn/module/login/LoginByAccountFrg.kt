package com.ponko.cn.module.login

import android.support.constraint.ConstraintLayout
import android.support.constraint.Guideline
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.ponko.cn.R
import com.ponko.cn.module.login.contract.LoginByAccountContract
import com.ponko.cn.utils.PolyvKeyBoardUtils
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.log.BKLog


/**
 * 通过账号登录
 */
class LoginByAccountFrg : MvpFragment<LoginByAccountContract.P>(), LoginByAccountContract.V {

    private var ui: UI? = null

    override fun presenter(): LoginByAccountContract.P {
        return LoginByAccountContract.P(context!!, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_login_by_account
    }

    override fun findViews(view: View) {
        if (ui == null) {
            ui = UI.create(view)
        }
    }

    override fun initDisplay() {

    }

    override fun iniEvent() {
        ui?.etAccount?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ui?.btnEnter?.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        ui?.etPwd?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                BKLog.d("afterTextChanged:" + s.toString())
                if (s.toString().isNotEmpty()) {
                    ui?.btnEnter?.isEnabled = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        ui?.btnEnter?.setOnClickListener {
            BKLog.d("点击登录")
            p?.clickEnter(ui?.etAccount?.text.toString(), ui?.etPwd?.text.toString())
            PolyvKeyBoardUtils.closeKeybord(ui?.etAccount, context)
        }
    }

    override fun iniData() {

    }

    private class UI private constructor(val constraintLayout2: ConstraintLayout, val etAccount: EditText, val etPwd: EditText, val btnEnter: Button, val guideline3: Guideline, val guideline6: Guideline) {
        companion object {

            fun create(rootView: View): UI {
                val constraintLayout2 = rootView.findViewById<View>(R.id.constraintLayout2) as ConstraintLayout
                val etAccount = rootView.findViewById<View>(R.id.et_account) as EditText
                val etPwd = rootView.findViewById<View>(R.id.et_pwd) as EditText
                val btnEnter = rootView.findViewById<View>(R.id.btn_enter) as Button
                val guideline3 = rootView.findViewById<View>(R.id.guideline3) as Guideline
                val guideline6 = rootView.findViewById<View>(R.id.guideline6) as Guideline
                return UI(constraintLayout2, etAccount, etPwd, btnEnter, guideline3, guideline6)
            }
        }
    }

}
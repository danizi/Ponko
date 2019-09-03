package com.ponko.cn.module.login

import android.support.constraint.ConstraintLayout
import android.support.constraint.Guideline
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.module.login.contract.LoginByCodeContract
import com.ponko.cn.utils.PolyvKeyBoardUtils
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.log.BKLog


/**
 * 通过短信登录
 */
class LoginByCodeFrg : MvpFragment<LoginByCodeContract.P>(), LoginByCodeContract.V {

    private var ui: UI? = null

    override fun presenter(): LoginByCodeContract.P {
        return LoginByCodeContract.P(context!!, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_login_by_code
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
        ui?.etCode?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
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
            p?.clickEnter(ui?.etAccount?.text.toString(), ui?.etCode?.text.toString())
            PolyvKeyBoardUtils.closeKeybord(ui?.etAccount, context)
        }

        ui?.btnSendCode?.setOnClickListener {
            p?.clickSendCode(ui?.etAccount?.text.toString())
            PolyvKeyBoardUtils.closeKeybord(ui?.etAccount, context)
        }
    }

    override fun iniData() {
    }

    private class UI private constructor(val constraintLayout2: ConstraintLayout, val etAccount: EditText, val etCode: EditText, val btnEnter: Button, val btnSendCode: TextView, val guideline3: Guideline, val guideline6: Guideline, val guideline8: Guideline) {
        companion object {

            fun create(rootView: View): UI {
                val constraintLayout2 = rootView.findViewById<View>(R.id.constraintLayout2) as ConstraintLayout
                val etAccount = rootView.findViewById<View>(R.id.et_account) as EditText
                val etCode = rootView.findViewById<View>(R.id.et_code) as EditText
                val btnEnter = rootView.findViewById<View>(R.id.btn_enter) as Button
                val btnSendCode = rootView.findViewById<View>(R.id.btn_send_code) as TextView
                val guideline3 = rootView.findViewById<View>(R.id.guideline3) as Guideline
                val guideline6 = rootView.findViewById<View>(R.id.guideline6) as Guideline
                val guideline8 = rootView.findViewById<View>(R.id.guideline8) as Guideline
                return UI(constraintLayout2, etAccount, etCode, btnEnter, btnSendCode, guideline3, guideline6, guideline8)
            }
        }
    }

}
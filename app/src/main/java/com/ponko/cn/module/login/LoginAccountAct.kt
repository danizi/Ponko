package com.ponko.cn.module.login

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.module.login.contract.LoginAccountContract
import com.ponko.cn.utils.PolyvKeyBoardUtils
import com.xm.lib.common.base.mvp.MvpActivity
import com.xm.lib.common.log.BKLog


@Deprecated("")
class LoginAccountAct : MvpActivity<LoginAccountContract.Present>(), LoginAccountContract.View {
    companion object {
        private const val TAG = "LoginAccountAct"
    }

    private var flClose: FrameLayout? = null
    private var tvFindPwd: TextView? = null
    private var etAccount: EditText? = null
    private var etPwd: EditText? = null
    private var btnEnter: Button? = null

    override fun presenter(): LoginAccountContract.Present {
        return LoginAccountContract.Present(context = this, view = this)
    }

    override fun setContentViewBefore() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_account
    }

    override fun findViews() {
        flClose = findViewById(R.id.fl_close)
        tvFindPwd = findViewById(R.id.tv_find_pwd)
        etAccount = findViewById(R.id.et_account)
        etPwd = findViewById(R.id.et_pwd)
        btnEnter = findViewById(R.id.btn_enter)
    }

    override fun initDisplay() {

    }

    override fun iniData() {

    }

    override fun iniEvent() {
        flClose?.setOnClickListener {
            p?.clickClose()
        }
        tvFindPwd?.setOnClickListener {
            p?.clickFindPwd()
        }
        etAccount?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnEnter?.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        etPwd?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                BKLog.d(TAG, "afterTextChanged:" + s.toString())
                if (s.toString().isNotEmpty()) {
                    btnEnter?.isEnabled = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        tvFindPwd?.setOnClickListener {
            BKLog.d("点击找回密码")
            p?.clickFindPwd()
        }

        btnEnter?.setOnClickListener {
            BKLog.d("点击登录")
            p?.clickEnter(etAccount?.text.toString(), etPwd?.text.toString())
            PolyvKeyBoardUtils.closeKeybord(etAccount, this)
        }
    }
}

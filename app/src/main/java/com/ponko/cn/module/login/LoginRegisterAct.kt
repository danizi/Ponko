package com.ponko.cn.module.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ponko.cn.R
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.PolyvKeyBoardUtils
import com.xm.lib.common.log.BKLog
import retrofit2.Call
import retrofit2.Response

class LoginRegisterAct : AppCompatActivity() {

    private class ViewHolder private constructor(val constraintLayout: ConstraintLayout, val flClose: FrameLayout, val tvWxSuccess: TextView, val constraintLayout2: ConstraintLayout, val etAccount: EditText, val etPwd: EditText, val btnEnter: Button, val textView3: TextView, val tvUserAgreement: TextView) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val constraintLayout = rootView.findViewById<View>(R.id.constraintLayout) as ConstraintLayout
                val flClose = rootView.findViewById<View>(R.id.fl_close) as FrameLayout
                val tvWxSuccess = rootView.findViewById<View>(R.id.tv_wx_success) as TextView
                val constraintLayout2 = rootView.findViewById<View>(R.id.constraintLayout2) as ConstraintLayout
                val etAccount = rootView.findViewById<View>(R.id.et_account) as EditText
                val etPwd = rootView.findViewById<View>(R.id.et_pwd) as EditText
                val btnEnter = rootView.findViewById<View>(R.id.btn_enter) as Button
                val textView3 = rootView.findViewById<View>(R.id.textView3) as TextView
                val tvUserAgreement = rootView.findViewById<View>(R.id.tv_user_agreement) as TextView

                return ViewHolder(constraintLayout, flClose, tvWxSuccess, constraintLayout2, etAccount, etPwd, btnEnter, textView3, tvUserAgreement)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }

        viewHolder?.flClose?.setOnClickListener { finish() }
        viewHolder?.etAccount?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewHolder?.btnEnter?.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        viewHolder?.etPwd?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    viewHolder?.btnEnter?.isEnabled = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        viewHolder?.btnEnter?.setOnClickListener {
            val phone = viewHolder?.etAccount?.text.toString()
            val pwd = viewHolder?.etPwd?.text.toString()
            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, "输入项不能为空", Toast.LENGTH_SHORT).show()
            } else {
                BKLog.d("點擊注冊 phone:$phone pwd:$pwd 首先請求短信")
                DialogUtil.showProcess(this)
                PonkoApp.loginApi?.registerSms(phone)?.enqueue(object : HttpCallBack<GeneralBean>() {
                    override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                        LogSmsAct.startFromRegister(this@LoginRegisterAct, phone, pwd)
                        DialogUtil.hideProcess()
                    }

                    override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                        super.onFailure(call, msg)
                        DialogUtil.hideProcess()
                    }
                })
            }
            PolyvKeyBoardUtils.closeKeybord( viewHolder?.etAccount, this)
        }
    }
}

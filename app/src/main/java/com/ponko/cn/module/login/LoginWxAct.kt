package com.ponko.cn.module.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import android.widget.TextView
import android.widget.EditText
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.ponko.cn.MainActivity
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.DialogUtil
import retrofit2.Call
import retrofit2.Response


/**
 * 用户未注册 微信登录 进入此窗口
 */
class LoginWxAct : AppCompatActivity() {

    private class ViewHolder private constructor(val constraintLayout: ConstraintLayout, val flClose: FrameLayout, val tvFindPwd: TextView, val tvWxSuccess: TextView, val tvDes: TextView, val constraintLayout2: ConstraintLayout, val etAccount: EditText, val etPwd: EditText, val btnEnter: Button, val textView3: TextView, val tvUserAgreement: TextView) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val constraintLayout = rootView.findViewById<View>(R.id.constraintLayout) as ConstraintLayout
                val flClose = rootView.findViewById<View>(R.id.fl_close) as FrameLayout
                val tvFindPwd = rootView.findViewById<View>(R.id.tv_find_pwd) as TextView
                val tvWxSuccess = rootView.findViewById<View>(R.id.tv_wx_success) as TextView
                val tvDes = rootView.findViewById<View>(R.id.tv_des) as TextView
                val constraintLayout2 = rootView.findViewById<View>(R.id.constraintLayout2) as ConstraintLayout
                val etAccount = rootView.findViewById<View>(R.id.et_account) as EditText
                val etPwd = rootView.findViewById<View>(R.id.et_pwd) as EditText
                val btnEnter = rootView.findViewById<View>(R.id.btn_enter) as Button
                val textView3 = rootView.findViewById<View>(R.id.textView3) as TextView
                val tvUserAgreement = rootView.findViewById<View>(R.id.tv_user_agreement) as TextView
                return ViewHolder(constraintLayout, flClose, tvFindPwd, tvWxSuccess, tvDes, constraintLayout2, etAccount, etPwd, btnEnter, textView3, tvUserAgreement)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_wx)

        val code = intent.getStringExtra("code")

        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
        viewHolder?.flClose?.setOnClickListener {
            finish()
        }
        viewHolder?.tvFindPwd?.setOnClickListener {
            ActivityUtil.startActivity(this, Intent(this, LoginFindPwdAct::class.java))
        }
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
            DialogUtil.showProcess(this)
            PonkoApp.loginApi?.wxbind(viewHolder?.etAccount?.text.toString(), viewHolder?.etPwd?.text.toString(), code,  "wechat")?.enqueue(object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    val token = response?.body()?.token
                    CacheUtil.putToken(token)
                    val intent = Intent(this@LoginWxAct, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    ActivityUtil.startActivity(this@LoginWxAct, intent)
                    DialogUtil.hideProcess()
                }

                override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    DialogUtil.hideProcess()
                }
            })
        }
    }
}

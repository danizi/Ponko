package com.ponko.cn.module.login

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ponko.cn.R
import com.ponko.cn.utils.ActivityUtil
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.text.SpannableString
import android.text.TextUtils
import android.widget.*
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.DialogUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.OnEnterListener
import retrofit2.Call
import retrofit2.Response


class LoginUpdateAct : AppCompatActivity() {
    companion object {

        private const val TYPE = "type"
        private const val CODE = "code"
        private const val FINDPWD = "FindPwd"
        private const val RESETPHONE = "Resetphone"

        fun startFromFindPwd(context: Context, code: String?) {
            val intent = Intent(context, LoginUpdateAct::class.java)
            intent.putExtra(TYPE, FINDPWD)
            intent.putExtra(CODE, code)
            ActivityUtil.startActivity(context, intent)
        }

        fun startFromResetphone(context: Context, code: String) {
            val intent = Intent(context, LoginUpdateAct::class.java)
            intent.putExtra(TYPE, RESETPHONE)
            intent.putExtra(CODE, code)
            ActivityUtil.startActivity(context, intent)
        }
    }

    private class ViewHolder private constructor(val constraintLayout: ConstraintLayout, val flClose: FrameLayout, val tvWxSuccess: TextView, val constraintLayout2: ConstraintLayout, val etAccount: EditText, val etPwd: EditText, val btnEnter: Button) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val constraintLayout = rootView.findViewById<View>(R.id.constraintLayout) as ConstraintLayout
                val flClose = rootView.findViewById<View>(R.id.fl_close) as FrameLayout
                val tvWxSuccess = rootView.findViewById<View>(R.id.tv_wx_success) as TextView
                val constraintLayout2 = rootView.findViewById<View>(R.id.constraintLayout2) as ConstraintLayout
                val etAccount = rootView.findViewById<View>(R.id.et_account) as EditText
                val etPwd = rootView.findViewById<View>(R.id.et_pwd) as EditText
                val btnEnter = rootView.findViewById<View>(R.id.btn_enter) as Button

                return ViewHolder(constraintLayout, flClose, tvWxSuccess, constraintLayout2, etAccount, etPwd, btnEnter)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_update)
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
        val from = intent.getStringExtra(TYPE)
        val code = intent.getStringExtra(CODE)
        viewHolder?.flClose?.setOnClickListener {
            finish()
        }
        viewHolder?.tvWxSuccess?.text = when (from) {
            FINDPWD -> {
                viewHolder?.etAccount?.hint = SpannableString("请输入新密码")
                "重置密码"
            }
            else -> {
                viewHolder?.etAccount?.hint = SpannableString("请输入新号码")
                "重置号码"
            }
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
        viewHolder?.etAccount?.text
        viewHolder?.btnEnter?.setOnClickListener {
            when (from) {
                FINDPWD -> {
                    val newPwd = viewHolder?.etAccount?.text.toString()
                    BKLog.d("code:$code")
                    if (!TextUtils.isEmpty(newPwd)) {
                        PonkoApp.loginApi?.resetPassword(newPwd, code)?.enqueue(object : HttpCallBack<GeneralBean>() {
                            override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                                DialogUtil.show(this@LoginUpdateAct, "提示", "修改成功，请重新登录", false, object : OnEnterListener {
                                    override fun onEnter(dlg: AlertDialog) {
                                        ActivityUtil.startActivity(this@LoginUpdateAct, Intent(this@LoginUpdateAct, LoginAccountAct::class.java))
                                        dlg.dismiss()
                                    }
                                }, null)
                            }
                        })
                    } else {
                        Toast.makeText(this@LoginUpdateAct, "新密码不能为空", Toast.LENGTH_SHORT).show()
                    }
                }
                RESETPHONE -> {

                }
            }
        }
    }
}

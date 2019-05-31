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
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.utils.DialogUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.OnEnterListener
import retrofit2.Call
import retrofit2.Response


class LoginUpdateAct : PonkoBaseAct<Any>() {


    companion object {

        private const val TYPE = "type"
        private const val CODE = "code"
        private const val TOKEN = "token"
        private const val FINDPWD = "FindPwd"
        private const val RESETPHONE = "Resetphone"

        /**
         * 找回密码
         * @param context 上下文对象
         * @param code 短信验证码 - 没有使用
         * @param token 短信校验返回的token
         */
        fun startFromFindPwd(context: Context, code: String?, token: String) {
            val intent = Intent(context, LoginUpdateAct::class.java)
            intent.putExtra(TYPE, FINDPWD)
            intent.putExtra(CODE, code)
            intent.putExtra(TOKEN, token)
            ActivityUtil.startActivity(context, intent)
        }

        /**
         * 重置手机号码
         * @param context 上下文对象
         * @param code 短信验证码
         */
        fun startFromResetPhone(context: Context, code: String) {
            val intent = Intent(context, LoginUpdateAct::class.java)
            intent.putExtra(TYPE, RESETPHONE)
            intent.putExtra(CODE, code)
            ActivityUtil.startActivity(context, intent)
        }
    }

    /**
     * 窗口UI ViewHolder
     */
    private var viewHolder: ViewHolder? = null
    /**
     * 那个窗口跳转过来标识
     */
    private var from: String? = null
    /**
     * 短信验证码
     */
    private var code: String? = null
    /**
     * 短信验证接口返回token
     */
    private var token: String? = null

    override fun presenter(): Any {
        return Any()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra(TYPE)
        code = intent.getStringExtra(CODE)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_update
    }

    override fun findViews() {
        super.findViews()
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
    }

    override fun initDisplay() {
        super.initDisplay()
        viewHolder?.tvWxSuccess?.text = when (from) {
            FINDPWD  -> {
                viewHolder?.etAccount?.hint = SpannableString("请输入新密码")
                "重置密码"
            }
            RESETPHONE->{
                viewHolder?.etAccount?.hint = SpannableString("请输入新号码")
                "请输入新号码"
            }
            else -> {
                viewHolder?.etAccount?.hint = SpannableString("请输入新密码")
                "重置密码"
            }
        }
    }

    override fun iniEvent() {
        super.iniEvent()
        viewHolder?.flClose?.setOnClickListener {
            finish()
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
        viewHolder?.btnEnter?.setOnClickListener {
            when (from) {
                FINDPWD -> {
                    findPwd()
                }
                RESETPHONE -> {
                    resetPhone()
                }
            }

        }
    }

    private fun findPwd() {
        val newPwd = viewHolder?.etAccount?.text.toString()
        token = intent.getStringExtra(TOKEN)
        BKLog.d("token:$token")
        if (!TextUtils.isEmpty(newPwd)) {
            DialogUtil.showProcess(this)
            PonkoApp.loginApi?.resetPassword(newPwd, token!!)?.enqueue(object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    DialogUtil.show(this@LoginUpdateAct, "提示", "修改成功，请重新登录", false, object : OnEnterListener {
                        override fun onEnter(dlg: AlertDialog) {
                            //ActivityUtil.startActivity(this@LoginUpdateAct, Intent(this@LoginUpdateAct, LoginAccountAct::class.java))
                            ActivityUtil.clearTheStackStartActivity(this@LoginUpdateAct, Intent(this@LoginUpdateAct, LoginAccountAct::class.java))
                            dlg.dismiss()
                        }
                    }, null)
                    DialogUtil.hideProcess()
                }

                override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    DialogUtil.hideProcess()
                }
            })
        } else {
            Toast.makeText(this@LoginUpdateAct, "新密码不能为空", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetPhone() {

    }

    /**
     * 窗口UI
     */
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
}

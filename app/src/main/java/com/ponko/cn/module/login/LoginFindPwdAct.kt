package com.ponko.cn.module.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.PonkoBaseAct
import com.xm.lib.common.base.BaseActivity
import retrofit2.Call
import retrofit2.Response


/**
 * 找回密码和修改密码重置密码 - 都是改密码
 */
class LoginFindPwdAct : PonkoBaseAct<Any>() {
    companion object {
        var type = ""
        var TYPE_UPDATEPWD = "UpdatePwd"
        var TYPE_FINDPWD = "FindPwd"
        var TYPE_RESETPWD = "ResetPwd"
        /**
         * 修改密码
         */
        fun startUpdatePwd(context: Context?) {
            type = TYPE_UPDATEPWD
            context?.startActivity(Intent(context, LoginFindPwdAct::class.java))
        }

        /**
         * 找回密码
         */
        fun startFindPwd(context: Context?) {
            type = TYPE_FINDPWD
            context?.startActivity(Intent(context, LoginFindPwdAct::class.java))
        }

        /**
         * 重置密码
         */
        fun startResetPwd(context: Context?) {
            type = TYPE_RESETPWD
            context?.startActivity(Intent(context, LoginFindPwdAct::class.java))
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun presenter(): Any {
        return Any()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_find_pwd
    }

    override fun findViews() {
        super.findViews()
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
    }

    override fun initDisplay() {
        super.initDisplay()
        viewHolder?.tvWxSuccess?.text = when (type) {
            TYPE_UPDATEPWD -> "修改密码"
            TYPE_FINDPWD -> "找回密码"
            TYPE_RESETPWD -> "重置密码"
            else -> {
                ""
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
            //下一步，請求發送短信接口成功，跳轉到短信頁面
            PonkoApp.loginApi?.forgetPasswordSms(viewHolder?.etAccount?.text.toString())?.enqueue(object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    //下一步，請求發送短信接口成功，跳轉到短信頁面
                    LogSmsAct.startFromFindPwd(this@LoginFindPwdAct, viewHolder?.etAccount?.text.toString())
                }
            })
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login_find_pwd)
//        if (viewHolder == null) {
//            viewHolder = ViewHolder.create(this)
//        }
//        viewHolder?.flClose?.setOnClickListener {
//            finish()
//        }
//        viewHolder?.etAccount?.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                viewHolder?.btnEnter?.isEnabled = s.toString().isNotEmpty()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })
//        viewHolder?.btnEnter?.setOnClickListener {
//            PonkoApp.loginApi?.forgetPasswordSms(viewHolder?.etAccount?.text.toString())?.enqueue(object : HttpCallBack<GeneralBean>() {
//                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
//                    //下一步，請求發送短信接口成功，跳轉到短信頁面
//                    LogSmsAct.startFromFindPwd(this@LoginFindPwdAct, viewHolder?.etAccount?.text.toString())
//                }
//            })
//        }
//    }

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

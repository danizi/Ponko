package com.ponko.cn.module.login

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ponko.cn.R
import com.ponko.cn.utils.ActivityUtil
import android.support.constraint.ConstraintLayout
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.FrameLayout
import android.widget.Toast
import com.github.gongw.VerifyCodeView
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.login.contract.LoginAccountContract
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.common.log.BKLog
import retrofit2.Call
import retrofit2.Response

class LogSmsAct : AppCompatActivity() {

    companion object {
        private const val TYPE = "type"
        private const val VALUE_FINDPWD = "startFromFindPwd"
        private const val VALUE_REGISTER = "startFromRegister"
        private const val VALUE_RESETPHONE = "startFromResetPhone"
        private const val PHONE = "phone"
        private const val PWD = "pwd"

        fun startFromFindPwd(context: Context, phone: String?) {
            val intent = Intent(context, LogSmsAct::class.java)
            intent.putExtra(TYPE, VALUE_FINDPWD)
            intent.putExtra(PHONE, phone)
            ActivityUtil.startActivity(context, intent)
        }

        fun startFromRegister(context: Context, phone: String?, pwd: String?) {
            val intent = Intent(context, LogSmsAct::class.java)
            intent.putExtra(TYPE, VALUE_REGISTER)
            intent.putExtra(PHONE, phone)
            intent.putExtra(PWD, pwd)
            ActivityUtil.startActivity(context, intent)
        }

        fun startFromResetPhone(context: Context) {
            val intent = Intent(context, LogSmsAct::class.java)
            intent.putExtra(TYPE, VALUE_RESETPHONE)
            ActivityUtil.startActivity(context, intent)
        }
    }

    private class ViewHolder private constructor(val constraintLayout: ConstraintLayout, val flClose: FrameLayout, val tvWxSuccess: TextView, val clTimer: ConstraintLayout, val tvDes: TextView, val tvTimer: TextView, val constraintLayout2: ConstraintLayout, val viewCode: VerifyCodeView, val btnEnter: Button) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val constraintLayout = rootView.findViewById<View>(R.id.constraintLayout) as ConstraintLayout
                val flClose = rootView.findViewById<View>(R.id.fl_close) as FrameLayout
                val tvWxSuccess = rootView.findViewById<View>(R.id.tv_wx_success) as TextView
                val clTimer = rootView.findViewById<View>(R.id.cl_timer) as ConstraintLayout
                val tvDes = rootView.findViewById<View>(R.id.tv_des) as TextView
                val tvTimer = rootView.findViewById<View>(R.id.tv_timer) as TextView
                val constraintLayout2 = rootView.findViewById<View>(R.id.constraintLayout2) as ConstraintLayout
                val viewCode = rootView.findViewById<View>(R.id.view_code) as VerifyCodeView
                val btnEnter = rootView.findViewById<View>(R.id.btn_enter) as Button

                return ViewHolder(constraintLayout, flClose, tvWxSuccess, clTimer, tvDes, tvTimer, constraintLayout2, viewCode, btnEnter)
            }
        }
    }


    private var viewHolder: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_sms)

        // 判断是哪一个窗口跳转
        val from = intent.getStringExtra(TYPE)

        // findViews
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }

        // initEvent
        viewHolder?.flClose?.setOnClickListener {
            finish()
        }
        viewHolder?.btnEnter?.setOnClickListener {
            when (from) {
                VALUE_FINDPWD -> {
                    BKLog.d("找回密码处理")

                    //短信验证，成功跳转到更新窗口LoginUpdateAct
                    val code = viewHolder?.viewCode?.vcText!!
                    val phone = intent.getStringExtra(PHONE)
                    if (TextUtils.isEmpty(code)) {
                        Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show()
                    } else {
                        //检查验证码是否合法
                        PonkoApp.loginApi?.checkForgetCode(phone, code)?.enqueue(object : HttpCallBack<GeneralBean>() {
                            override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                                PonkoApp.retrofitClient?.headers?.put("x-tradestudy-access-token", response?.body()?.token!!)
                                LoginUpdateAct.startFromFindPwd(this@LogSmsAct, code)
                            }
                        })
                    }
                }
                VALUE_REGISTER -> {
                    BKLog.d("注册账号处理")
                    val code = viewHolder?.viewCode?.vcText!!
                    val token = CacheUtil.getToken()!! //游客token
                    val phone = intent.getStringExtra(PHONE)
                    val pwd = intent.getStringExtra(PWD)
                    BKLog.d("phone: $phone  pwd: $pwd  code: $code token:$token")
                    if (TextUtils.isEmpty(code)) {
                        Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show()
                    } else {
                        PonkoApp.loginApi?.register(phone, pwd, code, token)?.enqueue(object : HttpCallBack<GeneralBean>() {
                            override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                                BKLog.d("找回密码验证码合法")
                                //请求登录接口进行登录
                                LoginAccountContract.Present(this@LogSmsAct, null).clickEnter(phone, pwd)
                            }
                        })
                    }
                }
                VALUE_RESETPHONE -> {
                    BKLog.d("重置号码处理")
                    val code = viewHolder?.viewCode?.vcText!!
                    if (TextUtils.isEmpty(code)) {
                        Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show()
                    } else {
                        PonkoApp.loginApi?.checkResetPhoneCode(code)?.enqueue(object : HttpCallBack<GeneralBean>() {
                            override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {

                                LoginUpdateAct.startFromResetphone(this@LogSmsAct, code)
                            }
                        })

                    }
                }
            }
        }
    }
}

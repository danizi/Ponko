package com.ponko.cn.module.login

import android.content.Context
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
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.login.contract.LoginWxContract
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.DialogUtil
import com.xm.lib.common.log.BKLog
import retrofit2.Call
import retrofit2.Response


/**
 * 用户未注册 微信登录 进入此窗口
 *
 * 用户注册未绑定
 *               1 填写账号密码
 *               2 请求接口绑定微信，返回错误则是未注册，需要发送注册验证码，然后再请求绑定接口
 *
 * 用户注册未绑定
 *               1 填写账号密码
 *               2 请求接口绑定微信判断是否注册过，没有注册就发送短信验证码，然后再请求绑定接口
 */
class LoginWxAct : PonkoBaseAct<LoginWxContract.Present>(), LoginWxContract.V {

    companion object {
        fun start(context: Context, code: String?, unionId: String?) {
            val intent = Intent(context, LoginWxAct::class.java)
            intent.putExtra("code", code)
            intent.putExtra("unionId", code)
            ActivityUtil.startActivity(context, intent)
        }
    }

    /**
     * 窗口ui
     */
    private var ui: ViewHolder? = null

    override fun presenter(): LoginWxContract.Present {
        return LoginWxContract.Present(context = this, v = this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_wx
    }

    override fun findViews() {
        super.findViews()
        if (ui == null) {
            ui = ViewHolder.create(this)
        }
    }

    override fun iniData() {
        super.iniData()
        p?.getIntentExtra(intent)
    }

    override fun iniEvent() {
        super.iniEvent()
        ui?.flClose?.setOnClickListener {
            finish()
        }
        ui?.tvFindPwd?.setOnClickListener {
            p?.clickFindPwd()
        }
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
            val params = HashMap<String, String>()
            params["phone"] = ui?.etAccount?.text.toString()
            params["pwd"] = ui?.etPwd?.text.toString()
            p?.clickEnter(params)
        }
    }

    override fun showLoading() {
        DialogUtil.showProcess(this)
    }

    override fun hideLoading() {
        DialogUtil.hideProcess()
    }

    override fun showDialog(s: String) {
        DialogUtil.show(this, "提示", s, true, null, null)
    }

    /**
     * 窗口UI
     */
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
}

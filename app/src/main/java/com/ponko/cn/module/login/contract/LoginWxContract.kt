package com.ponko.cn.module.login.contract

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.ponko.cn.MainActivity
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.login.LoginFindPwdAct
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.log.BKLog
import retrofit2.Call
import retrofit2.Response

/**
 * 首页-微信登录绑定契约类 MVP模式
 */
class LoginWxContract {

    /**
     * 视图层
     */
    interface V {
        /**
         * 显示等待加载框
         */
        fun showLoading()

        /**
         * 隐藏等待加载框
         */
        fun hideLoading()

        /**
         * 错误提示弹框
         */
        fun showDialog(s: String)
    }

    /**
     * 数据层
     */
    class M {
        /**
         * 微信授权返回的唯一码
         */
        var code = ""
        /**
         * 微信唯一标识
         */
        var unionId = ""

        /**
         * 请求微信绑定接口 PS：这个也可以判断用户是否注册过 请求返回错误就是未注册
         * @param phone 手机号码
         * @param pwd 账号密码
         * @param unionId 微信unionId
         * @param smsCode 短信 未注册用户到绑定需要该字段
         */
        fun requestWechatBindApi(phone: String?, pwd: String?, unionId: String?, /*smsCode: String? = "",*/ callback: HttpCallBack<GeneralBean>) {
            PonkoApp.loginApi?.wxbind(phone, pwd, "wechat", unionId/*, smsCode*/)?.enqueue(callback)
        }

        /**
         * 登录接口
         */
        fun requestLoginApi(phone: String?, pwd: String?, callback: HttpCallBack<GeneralBean>) {
            PonkoApp.loginApi?.login(phone, pwd, "")?.enqueue(callback)
        }
    }

    /**
     * 控制层
     */
    class Present(private val context: Context, private val v: V) {
        private val m = M()

        /**
         * 点击绑定
         */
        fun clickEnter(parameters: HashMap<String, String>) {
            BKLog.d("点击确认绑定")
            val phone = parameters["phone"]
            val pwd = parameters["pwd"]
            //检查参数
            if (TextUtils.isEmpty(phone)) {
                v.showDialog("输入手机号码为空")
                return
            }
            if (TextUtils.isEmpty(pwd)) {
                v.showDialog("输入密码为空")
                return
            }
            //显示加载框
            v.showLoading()
            //请求绑定接口
            m.requestWechatBindApi(phone, pwd, m.unionId, /*""*/ object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    //隐藏加载框
                    v.hideLoading()
                    //请求登录接口进入主界面
                    m.requestLoginApi(phone, pwd, object : HttpCallBack<GeneralBean>() {
                        override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                            BKLog.d("登录成功")
                            val token = response?.body()?.token
                            CacheUtil.putToken(token)
                            CacheUtil.putUserTypeWx()
                            val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            ActivityUtil.startActivity(context, intent)
                            DialogUtil.hideProcess()
                        }

                        override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                            super.onFailure(call, msg)
                            BKLog.d("绑定成功-请求登录接口失败")
                        }
                    })
                }

                override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    //请求加载框
                    v.hideLoading()
                    //判断该用户是未注册用户，发送注册短信码，填写完成再请求绑定接口
                    ToastUtil.show("你尚未注册，请先注册。")
                }
            })
        }

        /**
         * 获取其他窗口传递的信息
         */
        fun getIntentExtra(intent: Intent?) {
            try {
                m.code = intent?.getStringExtra("code")!!
                m.unionId = intent.getStringExtra("unionId")!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun clickFindPwd() {
            LoginFindPwdAct.startFindPwd(context)
            BKLog.d("点击找回密码")
        }
    }
}
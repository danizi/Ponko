package com.ponko.cn.module.my.option.acount

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.APP_ID
import com.ponko.cn.app.PonkoApp.Companion.loginApi
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.login.LoginFindPwdAct
import com.ponko.cn.module.login.LoginResetPhoneAct
import com.ponko.cn.module.login.LoginStartAct
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.broadcast.BroadcastManager
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class AccountAct : PonkoBaseAct<Any>() {


    companion object {
        const val ACTION_BIND = "broadcast.action.bind"
        const val ACTION_UN_BIND = "broadcast.action.unbind"
    }

    /**
     * 窗口ui
     */
    private var viewHolder: ViewHolder? = null
    private var broadcastManager: BroadcastManager? = null
    private var wxReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                ACTION_BIND -> {
                    val wxShare = WxShare(this@AccountAct)
                    wxShare.init(ShareConfig.Builder().appid(APP_ID).build())
                    wxShare.oauth()
                }
                ACTION_UN_BIND -> {
                    loginApi?.wechatUnBint()?.enqueue(object : HttpCallBack<ResponseBody>() {
                        override fun onSuccess(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                            ToastUtil.show("微信解绑成功")
                            CacheUtil.putUserTypeLogin() //从绑定用户 -> 登录用户
                        }
                    })
                }
            }
        }
    }
    private var wxOauthReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "COMMAND_SENDAUTH") {
                val code = intent.getStringExtra("code")
                ToastUtil.show("code:$code")
                val params = HashMap<String, String>()
                params["token"] = code
                params["type"] = "wechat"
                loginApi?.wechatBind(params)?.enqueue(object : HttpCallBack<GeneralBean>() {
                    override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                        ToastUtil.show("微信绑定成功")
                        CacheUtil.putUserTypeWx()
                    }
                })
            }
        }
    }

    override fun presenter(): Any {
        return Any()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broadcastManager = BroadcastManager.create(this)
        broadcastManager?.registerReceiver(ACTION_BIND, wxReceiver)
        broadcastManager?.registerReceiver(ACTION_UN_BIND, wxReceiver)
        broadcastManager?.registerReceiver("COMMAND_SENDAUTH", wxOauthReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcastManager?.unRegisterReceiver(wxReceiver)
        broadcastManager?.unRegisterReceiver(wxOauthReceiver)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_account
    }

    override fun findViews() {
        super.findViews()
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
    }

    override fun initDisplay() {
        super.initDisplay()
        //设置顶部栏
        BarUtil.addBar1(this, viewHolder?.toolbar, "账号安全")
    }

    override fun iniEvent() {
        super.iniEvent()
        viewHolder?.btnExit?.setOnClickListener {
            ActivityUtil.clearTheStackStartActivity(this@AccountAct, Intent(this, LoginStartAct::class.java))
        }
    }

    override fun iniData() {
        super.iniData()
        //账号
        val accountAdapter = object : BaseRvAdapter() {}
        accountAdapter.addItemViewDelegate(0, ItemViewHolder::class.java, ItemBean::class.java, R.layout.item_account_my)
        accountAdapter.data?.add(ItemBean("个人信息"))
        accountAdapter.data?.add(ItemBean("收件信息"))
        accountAdapter.data?.add(ItemBean("微信绑定", true))
        viewHolder?.rvAccount?.adapter = accountAdapter
        viewHolder?.rvAccount?.layoutManager = LinearLayoutManager(this)

        //安全
        val secureAdapter = object : BaseRvAdapter() {}
        secureAdapter.addItemViewDelegate(0, ItemViewHolder::class.java, ItemBean::class.java, R.layout.item_account_my)
        secureAdapter.data?.add(ItemBean("修改手机"))
        secureAdapter.data?.add(ItemBean("修改密码"))
        viewHolder?.rvSecure?.adapter = secureAdapter
        viewHolder?.rvSecure?.layoutManager = LinearLayoutManager(this)
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_account)
//        if (viewHolder == null) {
//            viewHolder = ViewHolder.create(this)
//        }
//
//        //设置顶部栏
//        BarUtil.addBar1(this, viewHolder?.toolbar, "账号安全")
//
//        //账号
//        val accountAdapter = object : BaseRvAdapter() {}
//        accountAdapter.addItemViewDelegate(0, ItemViewHolder::class.java, ItemBean::class.java, R.layout.item_account_my)
//        accountAdapter.data?.add(ItemBean("个人信息"))
//        accountAdapter.data?.add(ItemBean("收件信息"))
//        accountAdapter.data?.add(ItemBean("微信绑定", true))
//        viewHolder?.rvAccount?.adapter = accountAdapter
//        viewHolder?.rvAccount?.layoutManager = LinearLayoutManager(this)
//
//        //安全
//        val secureAdapter = object : BaseRvAdapter() {}
//        secureAdapter.addItemViewDelegate(0, ItemViewHolder::class.java, ItemBean::class.java, R.layout.item_account_my)
//        secureAdapter.data?.add(ItemBean("修改手机"))
//        secureAdapter.data?.add(ItemBean("修改密码"))
//        viewHolder?.rvSecure?.adapter = secureAdapter
//        viewHolder?.rvSecure?.layoutManager = LinearLayoutManager(this)
//
//        viewHolder?.btnExit?.setOnClickListener {
//            ActivityUtil.clearTheStackStartActivity(this@AccountAct, Intent(this, LoginStartAct::class.java))
//        }
//    }

    /**
     * 窗体UI ViewHolder
     */
    private class ViewHolder private constructor(val toolbar: android.support.v7.widget.Toolbar, val tvAccount: TextView, val rvAccount: RecyclerView, val tvSecure: TextView, val rvSecure: RecyclerView, val btnExit: Button) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val toolbar = rootView.findViewById<View>(R.id.toolbar) as android.support.v7.widget.Toolbar
                val tvAccount = rootView.findViewById<View>(R.id.tv_account) as TextView
                val rvAccount = rootView.findViewById<View>(R.id.rv_account) as RecyclerView
                val tvSecure = rootView.findViewById<View>(R.id.tv_secure) as TextView
                val rvSecure = rootView.findViewById<View>(R.id.rv_secure) as RecyclerView
                val btnExit = rootView.findViewById<View>(R.id.btn_exit) as Button
                return ViewHolder(toolbar, tvAccount, rvAccount, tvSecure, rvSecure, btnExit)
            }
        }
    }

    /**
     * 功能选项ViewHolder
     */
    open class ItemViewHolder(view: View) : BaseViewHolder(view) {

        private class ViewHolder private constructor(val tv: TextView, val ivArrow: ImageView, val sc: Switch, val divider: View) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val tv = rootView.findViewById<View>(R.id.tv) as TextView
                    val ivArrow = rootView.findViewById<View>(R.id.iv_arrow) as ImageView
                    val sc = rootView.findViewById<View>(R.id.sc) as Switch
                    val divider = rootView.findViewById(R.id.divider) as View
                    return ViewHolder(tv, ivArrow, sc, divider)
                }
            }
        }

        private var viewHolder: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }
            val context = itemView.context
            val itemBean = d as ItemBean
            viewHolder?.tv?.text = itemBean.content

            //是否是是带开关的
            if (itemBean.isSwitch == true) {
                viewHolder?.sc?.isChecked = PonkoApp.mainCBean?.account?.isIs_bind_wechat == true

                //发起微信鉴权
                WxShare(context as Activity).oauth()

                viewHolder?.sc?.visibility = View.VISIBLE
                viewHolder?.sc?.setOnCheckedChangeListener { buttonView, isChecked ->
                    //BKLog.d("微信绑定状态 : $isChecked")
                    isChecked(context, isChecked)
                }
                viewHolder?.ivArrow?.visibility = View.INVISIBLE
            }
            itemView.setOnClickListener {
                when (itemBean.content) {
                    "个人信息" -> {
                        BKLog.d("点击个人信息")
                        ActivityUtil.startActivity(context, Intent(context, PersonalActivity::class.java))
                    }
                    "收件信息" -> {
                        BKLog.d("点击收件信息")
                        ActivityUtil.startActivity(context, Intent(context, AddressActivity::class.java))
                    }
                    "微信绑定" -> {
                        BKLog.d("点击微信绑定")
                        viewHolder?.sc?.isChecked = !viewHolder?.sc?.isChecked!!
                        isChecked(context, viewHolder?.sc?.isChecked)
                    }
                    "修改手机" -> {
                        BKLog.d("点击修改手机")
                        ActivityUtil.startActivity(context, Intent(context, LoginResetPhoneAct::class.java))
                    }
                    "修改密码" -> {
                        BKLog.d("点击修改密码")
                        LoginFindPwdAct.startUpdatePwd(context)
                        //ActivityUtil.startActivity(context, Intent(context, LoginFindPwdAct::class.java))
                    }
                }
            }
        }

        private fun isChecked(context: Context, isChecked: Boolean?) {
            if (isChecked!!) {
                BKLog.d("绑定微信")
                context.sendBroadcast(Intent(ACTION_BIND))
            } else {
                BKLog.d("解绑微信")
                context.sendBroadcast(Intent(ACTION_UN_BIND))
            }
        }
    }

    private class ItemBean(var content: String, var isSwitch: Boolean? = false)
}

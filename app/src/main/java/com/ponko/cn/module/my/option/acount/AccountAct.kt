package com.ponko.cn.module.my.option.acount

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.ponko.cn.R
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog


class AccountAct : AppCompatActivity() {


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


    private var viewHolder: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
        BarUtil.addBar1(this, viewHolder?.toolbar, "账号安全")

        val accountAdapter = object : BaseRvAdapter() {}
        accountAdapter.addItemViewDelegate(0, ItemViewHolder::class.java, ItemBean::class.java, R.layout.item_account_my)
        accountAdapter.data?.add(ItemBean("个人信息"))
        accountAdapter.data?.add(ItemBean("收件信息"))
        accountAdapter.data?.add(ItemBean("微信绑定", true))
        viewHolder?.rvAccount?.adapter = accountAdapter
        viewHolder?.rvAccount?.layoutManager = LinearLayoutManager(this)

        val secureAdapter = object : BaseRvAdapter() {}
        secureAdapter.addItemViewDelegate(0, ItemViewHolder::class.java, ItemBean::class.java, R.layout.item_account_my)
        secureAdapter.data?.add(ItemBean("修改手机"))
        secureAdapter.data?.add(ItemBean("修改密码"))
        viewHolder?.rvSecure?.adapter = secureAdapter
        viewHolder?.rvSecure?.layoutManager = LinearLayoutManager(this)

    }

    private class ItemBean(var content: String, var isSwitch: Boolean? = false)

    private class ItemViewHolder(view: View) : BaseViewHolder(view) {

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
            if (itemBean.isSwitch == true) {
                viewHolder?.sc?.visibility = View.VISIBLE
                viewHolder?.sc?.setOnCheckedChangeListener { buttonView, isChecked ->
                    BKLog.d("微信绑定状态 : $isChecked")
                    if (isChecked) {
                        BKLog.d("绑定微信")
                    } else {
                        BKLog.d("解绑微信")
                    }
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
                    }
                    "修改手机" -> {
                        BKLog.d("点击修改手机")
                    }
                    "修改密码" -> {
                        BKLog.d("点击修改密码")
                    }
                }
            }
        }
    }
}

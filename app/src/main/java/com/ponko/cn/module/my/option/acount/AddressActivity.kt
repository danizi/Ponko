package com.ponko.cn.module.my.option.acount

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.ponko.cn.R
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import android.widget.EditText
import de.hdodenhof.circleimageview.CircleImageView
import android.widget.TextView
import android.widget.Toast
import com.github.gongw.VerifyCodeView
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.AddressBean
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.log.BKLog
import retrofit2.Call
import retrofit2.Response


class AddressActivity : AppCompatActivity() {

    private var viewHolder: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }

        //设置顶部状态栏
        BarUtil.addBar1(this, viewHolder?.toolbar, "收件地址", "保存", View.OnClickListener {
            val phone = viewHolder?.rv?.layoutManager?.getChildAt(0)?.findViewById<EditText>(R.id.et)?.text.toString()
            val name = viewHolder?.rv?.layoutManager?.getChildAt(1)?.findViewById<EditText>(R.id.et)?.text.toString()
            val address = viewHolder?.rv?.layoutManager?.getChildAt(2)?.findViewById<EditText>(R.id.et)?.text.toString()
            if (isCanSava(phone, name, address)) {
                saveAddress(phone, name, address)
            }
        })

        //设置收件信息
        getAddress()
    }

    /**
     * 检查输入信息
     */
    private fun isCanSava(phone: String, name: String, address: String): Boolean {
        BKLog.d("保存地址信息 phone:$phone neme$name address$address")
        var canSava = true
        if (TextUtils.isEmpty(phone)) {
            canSava = false
            Toast.makeText(this@AddressActivity, "手机号码为空", Toast.LENGTH_SHORT).show()
        }
        if (TextUtils.isEmpty(name)) {
            canSava = false
            Toast.makeText(this@AddressActivity, "姓名为空", Toast.LENGTH_SHORT).show()
        }
        if (TextUtils.isEmpty(address)) {
            canSava = false
            Toast.makeText(this@AddressActivity, "收件地址为空", Toast.LENGTH_SHORT).show()
        }
        return canSava
    }

    /**
     * 保存信息
     */
    private fun saveAddress(phone: String, name: String, address: String) {
        val params = HashMap<String, String>()
        params["tel"] = phone
        params["recipient"] = name
        params["address"] = address
        PonkoApp.myApi?.saveAddress(params)?.enqueue(object : HttpCallBack<GeneralBean>() {
            override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                Toast.makeText(this@AddressActivity, "保存收货地址成功", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * 获取收件信息
     */
    private fun getAddress() {
        PonkoApp.myApi?.getAddress()?.enqueue(object : HttpCallBack<AddressBean>() {
            override fun onSuccess(call: Call<AddressBean>?, response: Response<AddressBean>?) {
                val addressBean = response?.body()
                val adapter = object : BaseRvAdapter() {}
                adapter.data?.add(ItemBean("手机", "请输入你的手机号码", addressBean?.tel))
                adapter.data?.add(ItemBean("姓名", "请输入你的真实姓名", addressBean?.recipient))
                adapter.data?.add(ItemBean("地址", "请输入你的详细地址", addressBean?.address))
                adapter.addItemViewDelegate(0, ItemViewHolder::class.java, ItemBean::class.java, R.layout.item_account_my_edit)
                viewHolder?.rv?.adapter = adapter
                viewHolder?.rv?.layoutManager = LinearLayoutManager(this@AddressActivity)
            }
        })
    }

    /**
     * 窗口的ViewHolder
     */
    private class ViewHolder private constructor(val toolbar: Toolbar, val rv: RecyclerView) {
        companion object {
            fun create(rootView: AppCompatActivity): ViewHolder {
                val toolbar = rootView.findViewById<View>(R.id.toolbar) as Toolbar
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return ViewHolder(toolbar, rv)
            }
        }
    }

    /**
     * 实体bean
     */
    private class ItemBean(var content: String, var hint: String, var et: String? = "")

    /**
     * 地址填写ViewHolder
     */
    private class ItemViewHolder(view: View) : BaseViewHolder(view) {

        private class ViewHolder private constructor(val tv: TextView, val ivArrow: CircleImageView, val et: EditText, val divider: View) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val tv = rootView.findViewById<View>(R.id.tv) as TextView
                    val ivArrow = rootView.findViewById<View>(R.id.iv_arrow) as CircleImageView
                    val et = rootView.findViewById<View>(R.id.et) as EditText
                    val divider = rootView.findViewById(R.id.divider) as View
                    return ViewHolder(tv, ivArrow, et, divider)
                }
            }
        }

        private var viewHolder: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }
            val itemBean = d as ItemBean
            viewHolder?.tv?.text = itemBean.content
            viewHolder?.et?.hint = SpannableString(itemBean.hint)
            viewHolder?.et?.setText(itemBean.et)
        }
    }
}

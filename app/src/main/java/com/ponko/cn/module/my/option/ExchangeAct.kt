package com.ponko.cn.module.my.option

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.my.option.store.IntegralExchangedAct
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.log.BKLog
import retrofit2.Call
import retrofit2.Response


class ExchangeAct : AppCompatActivity() {

    private var viewHolder: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)

        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }

        BarUtil.addBar1(this, viewHolder?.toolbar, "BK码兑换")
        viewHolder?.exchangeButton?.setOnClickListener {
            val code = viewHolder?.exchangeCodeEditText.toString()
            PonkoApp.myApi?.exchangeCode(code)?.enqueue(object : HttpCallBack<GeneralBean>() {
                override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                    val alertDialog = AlertDialog.Builder(this@ExchangeAct)
                    alertDialog.setMessage("恭喜，课程兑换成功。")
                    alertDialog.setNegativeButton("开始学习") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                        BKLog.d("兑换成功，跳转到已兑课程") //PS:跳转到免费页面还是已兑课程
                        ActivityUtil.startActivity(this@ExchangeAct, Intent(this@ExchangeAct, IntegralExchangedAct::class.java))
                        sendBroadcast(Intent("com.moudle.free.startRefresh"))  //PS:广播通知免费界面刷新
                    }.create().show()
                }

                override fun onFailure(call: Call<GeneralBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    when (msg) {
                        "RepeatExchange" ->
                            showToast("不可以重复兑换")
                        "CDKeyNotFound" ->
                            showToast("兑换码不存在")
                        "CouponNotFound" ->
                            showToast("兑换码不存在")
                        else -> {
                            showToast(msg)
                        }
                    }
                }
            })
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private class ViewHolder private constructor(val toolbar: Toolbar, val exchangeCodeEditText: EditText, val exchangeButton: AppCompatButton) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val toolbar = rootView.findViewById<View>(R.id.toolbar) as Toolbar
                val exchangeCodeEditText = rootView.findViewById<View>(R.id.exchangeCodeEditText) as EditText
                val exchangeButton = rootView.findViewById<View>(R.id.exchangeButton) as AppCompatButton
                return ViewHolder(toolbar, exchangeCodeEditText, exchangeButton)
            }
        }
    }

}

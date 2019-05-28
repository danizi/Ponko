package com.ponko.cn.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.my.option.store.IntegralExchangedAct
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.OnCancelListener
import com.xm.lib.component.OnEnterListener
import com.xm.lib.component.Type
import com.xm.lib.component.XmIOSDialog
import retrofit2.Call
import retrofit2.Response

object DialogUtil {
    @SuppressLint("StaticFieldLeak")
    private var dlg: AlertDialog? = null

    /**
     * 显示苹果风格弹框
     */
    fun show(context: Context, title: String, msg: String, isCancelable: Boolean, enterListener: OnEnterListener?, cancelListener: OnCancelListener?) {
        try {
            if (dlg != null && dlg?.isShowing!!) {
                dlg?.dismiss()
            }
            dlg = newXmIOSDialog(context, title, msg, isCancelable, enterListener, cancelListener)
            dlg?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hide() {
        try {
            if (dlg != null && dlg?.isShowing!!) {
                dlg?.dismiss()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun newXmIOSDialog(context: Context, title: String, msg: String, isCancelable: Boolean, enterListener: OnEnterListener?, cancelListener: OnCancelListener?): AlertDialog {
        return XmIOSDialog(context)
                .setType(Type.GENERAL)
                .setMsg(msg)
                .setTitle(title)
                .setCancelable(isCancelable)
                .setSize(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnEnterListener(enterListener)
                .setOnCancelListener(cancelListener)
                .build()
    }

    /**
     * 显示积分弹框
     */
    fun showExchange(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_et_exchange, null, false)
        AlertDialog.Builder(context!!)
                .setTitle("兑换")
                .setView(view)
                .setPositiveButton("确认") { dialog, which ->
                    //获取用户输入
                    val etExchange: EditText = view.findViewById(R.id.et_exchange)
                    val code = etExchange.text.toString()
                    BKLog.d("用户输入兑换码:$code")
                    PonkoApp.myApi?.exchangeCode(code)?.enqueue(object : HttpCallBack<GeneralBean>() {
                        override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                            val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setMessage("恭喜，课程兑换成功。")
                            alertDialog.setNegativeButton("开始学习") { dialog, _ ->
                                dialog.dismiss()
                                BKLog.d("兑换成功，跳转到已兑课程") //PS:跳转到免费页面还是已兑课程
                                ActivityUtil.startActivity(context, Intent(context, IntegralExchangedAct::class.java))
                                //sendBroadcast(Intent("com.moudle.free.startRefresh"))  //PS:广播通知免费界面刷新
                            }.create().show()
                        }
                    })

                }.show()
    }

    /**
     * 显示进度
     */
    private var progressDialog: ProgressDialog? = null

    /**
     * 显示进度条
     */
    fun showProcess(context: Context) {
        try {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context)
                progressDialog?.setMessage("请稍等，正在处理中...")
                progressDialog?.setCanceledOnTouchOutside(true)
            }
            if (progressDialog?.isShowing == true) {
                progressDialog?.dismiss()
            }
            progressDialog?.show()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    /**
     * 隐藏进度条
     */
    fun hideProcess() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
        progressDialog = null
    }
}
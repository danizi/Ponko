package com.ponko.cn.utils

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AlertDialog
import com.xm.lib.component.OnCancelListener
import com.xm.lib.component.OnEnterListener
import com.xm.lib.component.Type
import com.xm.lib.component.XmIOSDialog

object DialogUtil {
    @SuppressLint("StaticFieldLeak")
    private var dlg: AlertDialog? = null

    fun show(context: Context,  title: String, msg: String,isCancelable: Boolean, enterListener: OnEnterListener?, cancelListener: OnCancelListener?) {
        if (dlg == null) {
            dlg = XmIOSDialog(context)
                    .setType(Type.GENERAL)
                    .setMsg(msg)
                    .setTitle(title)
                    .setCancelable(isCancelable)
                    .setSize(600, 400)
                    .setOnEnterListener(enterListener)
                    .setOnCancelListener(cancelListener)
                    .build()
            dlg?.show()
        } else {
            dlg?.dismiss()
            dlg = XmIOSDialog(context)
                    .setType(Type.GENERAL)
                    .setMsg(msg)
                    .setTitle(title)
                    .setCancelable(isCancelable)
                    .setSize(600, 400)
                    .setOnEnterListener(enterListener)
                    .setOnCancelListener(cancelListener)
                    .build()
            dlg?.show()
        }
    }
}
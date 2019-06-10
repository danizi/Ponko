package com.ponko.cn.wxapi

import android.content.Intent
import android.os.Bundle
import android.provider.UserDictionary.Words.APP_ID
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xm.lib.common.log.BKLog
import com.xm.lib.pay.wx.WxPay


class WXPayEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
    companion object {
        private const val TAG = "WXPayEntryActivity"
    }

    private var api: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //注册API
        api = WXAPIFactory.createWXAPI(this, APP_ID, false)
        api?.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {

    }

    /**
     * https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5
     */
    override fun onResp(baseResp: BaseResp) {
        if (baseResp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when {
                baseResp.errCode == 0 -> {
                    BKLog.d(TAG, "支付成功")
                    sendBroadcast(Intent(WxPay.ACTION_PAY_SUCCESS))
                    //Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show()
                }
                baseResp.errCode == -1 -> {
                    BKLog.d(TAG, "支付失败")
                    sendBroadcast(Intent(WxPay.ACTION_PAY_FAILURE))
                    //Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show()
                }
                baseResp.errCode == -2 -> {
                    BKLog.d(TAG, "用户取消支付")
                    sendBroadcast(Intent(WxPay.ACTION_PAY_CANCEL))
                    //Toast.makeText(this, "用户取消支付", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        }
    }
}

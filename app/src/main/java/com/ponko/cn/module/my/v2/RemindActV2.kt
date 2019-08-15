package com.ponko.cn.module.my.v2

import android.content.Intent
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.RemindCBeanV2
import com.ponko.cn.constant.Constants
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.v2.constract.RemindContractV2
import com.ponko.cn.module.my.v2.holder.MyRemindHolderV2
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.v1.BaseRvAdapter

class RemindActV2  : RefreshLoadAct<RemindContractV2.Present, RemindCBeanV2>(), RemindContractV2.V {


    override fun presenter(): RemindContractV2.Present {
        return RemindContractV2.Present(context = this, v = this)
    }

    override fun initDisplay() {
//        addBar2("消息提醒", "删除所有", View.OnClickListener {
//            p?.clearAll()
//        })
        super.initDisplay()
    }

    override fun requestCleanRemindApiSuccess() {
        ToastUtil.show("删除成功")
        sendBroadcast(Intent(Constants.ACTION_HIDE_MSG_TIP))
        requestRefreshApi()
    }

    override fun requestCleanRemindApiFailure() {
        ToastUtil.show("删除失败")
    }

    override fun remindListApiSuccess(body: RemindCBeanV2?) {
        if(body?.messages?.isEmpty()!!){
            addBar2("消息提醒", "", View.OnClickListener {})
        }else{
            addBar2("消息提醒", "删除所有", View.OnClickListener {
                p?.clearAll()
            })
        }
        requestRefreshSuccess(body)
    }

    override fun remindListApiFailure() {
        requestRefreshFailure()
    }

    override fun remindListMoreApiSuccess(body: RemindCBeanV2?) {
        requestMoreSuccess(body)
    }

    override fun remindListMoreApiFailure() {
        requestMoreFailure()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(MyRemindHolderV2::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_my_msg_remind)
        )
    }

    override fun requestMoreApi() {
        p?.remindListMoreApi()
    }

    override fun requestRefreshApi() {
        p?.remindListApi()
    }

    override fun multiTypeData(body: RemindCBeanV2?): List<Any> {
        return body?.messages!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }
}

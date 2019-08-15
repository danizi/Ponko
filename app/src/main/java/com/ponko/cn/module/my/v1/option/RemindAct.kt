package com.ponko.cn.module.my.option

import android.content.Intent
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.RemindCBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.constract.RemindContract
import com.ponko.cn.module.my.holder.MyRemindHolder
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.v1.BaseRvAdapter

@Deprecated("使用RemindActV2")
class RemindAct : RefreshLoadAct<RemindContract.Present, ArrayList<RemindCBean>>(), RemindContract.V {


    override fun presenter(): RemindContract.Present {
        return RemindContract.Present(context = this, v = this)
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

    override fun remindListApiSuccess(body: ArrayList<RemindCBean>?) {
        if(body?.isEmpty()!!){
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

    override fun remindListMoreApiSuccess(body: ArrayList<RemindCBean>?) {
        requestMoreSuccess(body)
    }

    override fun remindListMoreApiFailure() {
        requestMoreFailure()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(MyRemindHolder::class.java),
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

    override fun multiTypeData(body: ArrayList<RemindCBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }
}

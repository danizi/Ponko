package com.ponko.cn.module.my.v2

import android.content.Context
import android.content.Intent
import android.view.View
import com.ponko.cn.constant.Constants
import com.ponko.cn.module.my.option.InviteFriendRecordActivity
import com.ponko.cn.module.web.WebAct
import com.ponko.cn.module.web.WebContract
import com.xm.lib.common.log.BKLog

class InviteFriendV2Activity : WebAct() {
    companion object {
        fun start(context: Context?, link_type: String?, link_value: String?, title: String? = "", lunMode: Int? = Intent.FLAG_ACTIVITY_SINGLE_TOP) {
            WebAct.startFromInvite(context, "url", Constants.URL_INVITE, "邀请好友")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun presenter(): WebContract.Present {
        return super.presenter()
    }

    override fun getLayoutId(): Int {
        return super.getLayoutId()
    }

    override fun findViews() {
        super.findViews()
    }

    override fun iniEvent() {
        super.iniEvent()
    }

    override fun iniData() {
        super.iniData()
    }

    override fun initDisplay() {
        //super.initDisplay()
        mainUI?.addBar2(this, "邀请好友", "邀请记录", View.OnClickListener {
            BKLog.d("点击邀请记录")
            InviteFriendRecordActivity.start(this)
        })
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_invite_friend_v2)
//    }

}

package com.ponko.cn.module.my.option

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.InviteFriendsBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.holder.MyInviteViewHolder
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import retrofit2.Call
import retrofit2.Response
import android.widget.TextView


class InviteFriendActivity : RefreshLoadAct<Any, InviteFriendsBean>() {

    private var inviteFriendViewHolder: InviteFriendViewHolder? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_invite_friend
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(MyInviteViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_my_open_invite)
        )
    }

    override fun findViews() {
        super.findViews()
        if (inviteFriendViewHolder == null) {
            inviteFriendViewHolder = InviteFriendViewHolder.create(this)
        }
    }

    override fun initDisplay() {
        addItemDecoration = false
        super.initDisplay()
        addBar2("邀请好友", "邀请记录", View.OnClickListener {
            BKLog.d("点击邀请记录")
            InviteFriendRecordActivity.start(this)
        })
        viewHolder?.clContent?.setBackgroundColor(resources?.getColor(R.color.divider)!!)
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        PonkoApp.myApi?.invites()?.enqueue(object : HttpCallBack<InviteFriendsBean>() {
            override fun onSuccess(call: Call<InviteFriendsBean>?, response: Response<InviteFriendsBean>?) {
                requestRefreshSuccess(response?.body())
                //设置底部提示语
                displayBottomTip(response?.body())
            }
        })
    }

    private fun displayBottomTip(inviteFriendsBean: InviteFriendsBean?) {
        inviteFriendViewHolder?.tvTitle?.text = inviteFriendsBean?.getRule_title()
        inviteFriendViewHolder?.tvRuleContent?.text = inviteFriendsBean?.getRule_content()
        inviteFriendViewHolder?.tvDetail?.setOnClickListener {
            if (inviteFriendViewHolder?.tvRuleContent?.visibility == View.VISIBLE) {
                BKLog.d("隐藏详情")
                inviteFriendViewHolder?.tvRuleContent?.visibility = View.GONE
            } else {
                BKLog.d("显示详情")
                inviteFriendViewHolder?.tvRuleContent?.visibility = View.VISIBLE
            }
        }
    }

    override fun multiTypeData(body: InviteFriendsBean?): List<Any> {
        return body?.getProducts()!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

    private class InviteFriendViewHolder private constructor(val tvTitle: TextView, val tvDetail: TextView, val tvRuleContent: TextView) {
        companion object {

            fun create(act: AppCompatActivity): InviteFriendViewHolder {
                val tvTitle = act.findViewById<View>(R.id.tv_title) as TextView
                val tvDetail = act.findViewById<View>(R.id.tv_detail) as TextView
                val tvRuleContent = act.findViewById<View>(R.id.tv_rule_content) as TextView
                return InviteFriendViewHolder(tvTitle, tvDetail, tvRuleContent)
            }
        }
    }

}

package com.ponko.cn.module.my.option

import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp.Companion.myApi
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.InviteRecordBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import com.xm.lib.common.base.rv.v1.BaseViewHolder
import com.xm.lib.common.util.TimeUtil
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Response


class InviteFriendRecordActivity : RefreshLoadAct<Any, MutableList<InviteRecordBean>>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, InviteFriendRecordActivity::class.java))
        }
    }

    override fun initDisplay() {
        addItemDecoration = false
        super.initDisplay()
        viewHolder?.rv?.addItemDecoration(MyItemDecoration.divider(this, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))  //https://www.jianshu.com/p/86aaaa49ed3e
        BarUtil.addBar1(this, viewHolder?.toolbar, "邀请记录")
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(InviteFriendRecordViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_invite_record)
        )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        myApi?.shareInviteRecords()?.enqueue(object : HttpCallBack<MutableList<InviteRecordBean>>() {
            override fun onSuccess(call: Call<MutableList<InviteRecordBean>>?, response: Response<MutableList<InviteRecordBean>>?) {
                requestRefreshSuccess(response?.body())
            }

            override fun onFailure(call: Call<MutableList<InviteRecordBean>>?, msg: String?) {
                super.onFailure(call, msg)
                requestRefreshFailure()
            }
        })
    }

    override fun multiTypeData(body: MutableList<InviteRecordBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

    /**
     * 邀请记录
     */
    open class InviteFriendRecordViewHolder(view: View) : BaseViewHolder(view) {

        private class ViewHolder private constructor(val ivAvatar: CircleImageView, val tvNick: TextView, val tvData: TextView, val tvScore: TextView) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val ivAvatar = rootView.findViewById<View>(R.id.iv_avatar) as CircleImageView
                    val tvNick = rootView.findViewById<View>(R.id.tv_nick) as TextView
                    val tvData = rootView.findViewById<View>(R.id.tv_data) as TextView
                    val tvScore = rootView.findViewById<View>(R.id.tv_score) as TextView
                    return ViewHolder(ivAvatar, tvNick, tvData, tvScore)
                }
            }
        }

        private var ui: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = ViewHolder.create(itemView)
            }
            val inviteRecordBean = d as InviteRecordBean
            val context = itemView.context

            Glide.with(context, inviteRecordBean.avatar, ui?.ivAvatar)
            ui?.tvData?.text = TimeUtil.unixStr("yyyy/MM/dd HH:mm:ss", inviteRecordBean.createTime)
            ui?.tvNick?.text = inviteRecordBean.nickname
            ui?.tvScore?.text = inviteRecordBean.scores.toString()
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_invite_friend_record)
//    }
}

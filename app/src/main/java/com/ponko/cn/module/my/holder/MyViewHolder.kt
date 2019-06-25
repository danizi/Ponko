package com.ponko.cn.module.my.holder

import android.annotation.SuppressLint
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.ProfileCBean
import com.ponko.cn.constant.Constants.LOAD_IMAGE_DELAY
import com.ponko.cn.module.my.option.HistoryActivity
import com.ponko.cn.module.my.option.InviteFriendActivity
import com.ponko.cn.module.my.option.OpenRollActivity
import com.ponko.cn.module.my.option.acount.AccountAct
import com.ponko.cn.module.my.option.store.IntegralTaskActivity
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil.USERTYPE_LOGIN
import com.ponko.cn.utils.CacheUtil.USERTYPE_TOURIST
import com.ponko.cn.utils.CacheUtil.USERTYPE_WXLOGIN
import com.ponko.cn.utils.CacheUtil.getUserType
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import de.hdodenhof.circleimageview.CircleImageView


class MyViewHolder(view: View) : BaseViewHolder(view) {

    private class ViewHolder private constructor(val btnWxUnbind: Button, val clUserInfo: ConstraintLayout, val tvName: TextView, val ivVipNoOrYes: ImageView, val tvVipDes: TextView, val ivCircleHead: CircleImageView, val imageView4: ImageView, val clOther: ConstraintLayout, val llCourse: LinearLayout, val tvCourseNumber: TextView, val tvCourseDes: TextView, val llTime: LinearLayout, val tvTimeNumber: TextView, val tvTimeDes: TextView, val llIntegral: LinearLayout, val tvIntegralNumber: TextView, val tvIntegralDes: TextView, val clOpenInvite: ConstraintLayout, val clOpenRoll: ConstraintLayout, val llOpen: LinearLayout, val clInvite: ConstraintLayout, val llInvite: LinearLayout) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val btnWxUnbind = rootView.findViewById<View>(R.id.btn_wx_unbind) as Button
                val clUserInfo = rootView.findViewById<View>(R.id.cl_user_info) as ConstraintLayout
                val tvName = rootView.findViewById<View>(R.id.tv_name) as TextView
                val ivVipNoOrYes = rootView.findViewById<View>(R.id.iv_vip_no_or_yes) as ImageView
                val tvVipDes = rootView.findViewById<View>(R.id.tv_vip_des) as TextView
                val ivCircleHead = rootView.findViewById<View>(R.id.iv_circle_head) as CircleImageView
                val imageView4 = rootView.findViewById<View>(R.id.imageView4) as ImageView
                val clOther = rootView.findViewById<View>(R.id.cl_other) as ConstraintLayout
                val llCourse = rootView.findViewById<View>(R.id.ll_course) as LinearLayout
                val tvCourseNumber = rootView.findViewById<View>(R.id.tv_course_number) as TextView
                val tvCourseDes = rootView.findViewById<View>(R.id.tv_course_des) as TextView
                val llTime = rootView.findViewById<View>(R.id.ll_time) as LinearLayout
                val tvTimeNumber = rootView.findViewById<View>(R.id.tv_time_number) as TextView
                val tvTimeDes = rootView.findViewById<View>(R.id.tv_time_des) as TextView
                val llIntegral = rootView.findViewById<View>(R.id.ll_integral) as LinearLayout
                val tvIntegralNumber = rootView.findViewById<View>(R.id.tv_integral_number) as TextView
                val tvIntegralDes = rootView.findViewById<View>(R.id.tv_integral_des) as TextView
                val clOpenInvite = rootView.findViewById<View>(R.id.cl_open_invite) as ConstraintLayout
                val clOpenRoll = rootView.findViewById<View>(R.id.cl_open_roll) as ConstraintLayout
                val llOpen = rootView.findViewById<View>(R.id.ll_open) as LinearLayout
                val clInvite = rootView.findViewById<View>(R.id.cl_invite) as ConstraintLayout
                val llInvite = rootView.findViewById<View>(R.id.ll_invite) as LinearLayout
                return ViewHolder(btnWxUnbind, clUserInfo, tvName, ivVipNoOrYes, tvVipDes, ivCircleHead, imageView4, clOther, llCourse, tvCourseNumber, tvCourseDes, llTime, tvTimeNumber, tvTimeDes, llIntegral, tvIntegralNumber, tvIntegralDes, clOpenInvite, clOpenRoll, llOpen, clInvite, llInvite)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    @SuppressLint("SetTextI18n")
    override fun bindData(d: Any, position: Int) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }
        val profileCBean = d as ProfileCBean
        val context = itemView.context
        Glide.with(context, profileCBean.account.avatar, viewHolder?.ivCircleHead, LOAD_IMAGE_DELAY)  //头像
        viewHolder?.tvCourseNumber?.text = "" + profileCBean.account.study_count   //学习课程数量
        viewHolder?.tvTimeNumber?.text = "" + profileCBean.account.study_duration / 60  //学习时长
        viewHolder?.tvIntegralNumber?.text = "" + profileCBean.account.integration //当前积分

        // 用户是否订购
        val (name, vipDes) = infoPair(profileCBean)
        viewHolder?.tvName?.text = name
        viewHolder?.tvVipDes?.text = vipDes

        // 判断是否微信绑定
        val userType = userType(profileCBean)

        // 监听
        viewHolder?.btnWxUnbind?.setOnClickListener {
            BKLog.d("微信解除绑定")
            ActivityUtil.startActivity(context, Intent(context, AccountAct::class.java))
        }
        viewHolder?.ivCircleHead?.setOnClickListener {
            BKLog.d("点击头像")
            IntoTargetUtil.target(context, "head", "")
        }
        viewHolder?.clOpenRoll?.setOnClickListener {
            BKLog.d("点击开通学籍")
            ActivityUtil.startActivity(context, Intent(context, OpenRollActivity::class.java))
        }
        viewHolder?.clInvite?.setOnClickListener {
            BKLog.d("点击邀请好友")
            ActivityUtil.startActivity(context, Intent(context, InviteFriendActivity::class.java))
        }
        viewHolder?.llCourse?.setOnClickListener {
            BKLog.d("点击学习课程")
            ActivityUtil.startActivity(context, Intent(context, HistoryActivity::class.java))
        }
        viewHolder?.llTime?.setOnClickListener {
            BKLog.d("点击学习记录")
            ActivityUtil.startActivity(context, Intent(context, HistoryActivity::class.java))
        }
        viewHolder?.llIntegral?.setOnClickListener {
            BKLog.d("点击获取积分")
            ActivityUtil.startActivity(context, Intent(context, IntegralTaskActivity::class.java))
        }
    }

    @Deprecated("")
    private fun userType(profileCBean: ProfileCBean): String {
        var userType = "tourist"

        if (profileCBean.account.isIs_bind_wechat) {
            viewHolder?.btnWxUnbind?.visibility = View.GONE
//            viewHolder?.tvName?.text = profileCBean.account.nickname
            userType = "wxBind"
        } else {
//            viewHolder?.tvName?.text = profileCBean.account.realName
            userType = "login"
        }
        return userType
    }

    private fun infoPair(profileCBean: ProfileCBean): Pair<String, String> {
        var name = ""
        var vipDes = ""
        when (getUserType()) {
            USERTYPE_LOGIN -> {
                if (profileCBean.account.expiredTime > 0) {
                    vipDes = "已加入帮课大学"
                    viewHolder?.ivVipNoOrYes?.setImageResource(R.mipmap.my_info_vip)
                } else {
                    vipDes = "未入学"
                    viewHolder?.ivVipNoOrYes?.setImageResource(R.mipmap.my_info_no_vip)
                }
                if (!TextUtils.isEmpty(profileCBean.account.nickname)) {
                    name = profileCBean.account.nickname
                } else {
                    if (!TextUtils.isEmpty(profileCBean.account.realName)) {
                        name = profileCBean.account.realName
                    }
                }
            }
            USERTYPE_TOURIST -> {
                // 游客模式
                name = "同学，请登录"
                vipDes = "登录后可以多终端同步您的所有信息"
            }
            USERTYPE_WXLOGIN -> {
                if (profileCBean.account.expiredTime > 0) {
                    vipDes = "已加入帮课大学"
                    viewHolder?.ivVipNoOrYes?.setImageResource(R.mipmap.my_info_vip)
                } else {
                    vipDes = "未入学"
                    viewHolder?.ivVipNoOrYes?.setImageResource(R.mipmap.my_info_no_vip)
                }
                name = profileCBean.account.nickname
                if (TextUtils.isEmpty(name)) {
                    name = profileCBean.account.realName
                }
            }
        }

        if (TextUtils.isEmpty(name)) {
            name = "帮课大学学员"
        }
        return Pair(name, vipDes)
    }
}
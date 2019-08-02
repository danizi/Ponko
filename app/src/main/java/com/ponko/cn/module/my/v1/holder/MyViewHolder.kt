package com.ponko.cn.module.my.holder

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.support.constraint.ConstraintLayout
import android.support.constraint.Guideline
import android.support.v7.widget.AppCompatImageButton
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.request.target.ImageViewTarget
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.ProfileCBean
import com.ponko.cn.constant.Constants.LOAD_IMAGE_DELAY
import com.ponko.cn.constant.Constants.UI_VERSION_1
import com.ponko.cn.constant.Constants.URL_INVITE
import com.ponko.cn.constant.Constants.URL_OPEN_ROLL
import com.ponko.cn.module.my.option.HistoryActivity
import com.ponko.cn.module.my.option.InviteFriendActivity
import com.ponko.cn.module.my.option.OpenRollActivity
import com.ponko.cn.module.my.option.acount.AccountAct
import com.ponko.cn.module.my.option.store.IntegralTaskActivity
import com.ponko.cn.module.my.v1.option.ReportActivity
import com.ponko.cn.module.my.v2.InviteFriendV2Activity
import com.ponko.cn.module.my.v2.OpenRollV2Activity
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil.USERTYPE_LOGIN
import com.ponko.cn.utils.CacheUtil.USERTYPE_TOURIST
import com.ponko.cn.utils.CacheUtil.USERTYPE_WXLOGIN
import com.ponko.cn.utils.CacheUtil.getUserType
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception


class MyViewHolder(view: View) : BaseViewHolder(view) {

    private var viewHolder: MyViewHolder.ViewHolder? = null

    private var ui: UI? = null

    @SuppressLint("SetTextI18n")
    override fun bindData(d: Any, position: Int) {
        //加载item_my_top2布局
        new(d, LOAD_IMAGE_DELAY)

        //加载item_my_top布局
        //old(d, LOAD_IMAGE_DELAY)
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

    private fun new(d: Any, LOAD_IMAGE_DELAY: Long) {
        if (ui == null) {
            ui = UI.create(itemView)
        }
        val profileCBean = d as ProfileCBean
        val context = itemView.context



        Glide.with(context, profileCBean.account.avatar, ui?.ivCircleHead, LOAD_IMAGE_DELAY)  //头像
        // 用户是否订购
        val (name, vipDes) = infoPair(profileCBean)
        ui?.tvName?.text = name
        ui?.tvVipDes?.text = vipDes

        // 判断是否微信绑定
        val userType = userType(profileCBean)

        // 监听
        ui?.btnWxUnbind?.setOnClickListener {
            BKLog.d("微信解除绑定")
            ActivityUtil.startActivity(context, Intent(context, AccountAct::class.java))
        }
        ui?.ivCircleHead?.setOnClickListener {
            BKLog.d("点击头像")
            IntoTargetUtil.target(context, "head", "")
        }
        ui?.clOpenRoll?.setOnClickListener {
            BKLog.d("点击开通学籍")
            if (PonkoApp.getAppUIVersion(context) == UI_VERSION_1) {
                ActivityUtil.startActivity(context, Intent(context, OpenRollActivity::class.java))
            } else {
                OpenRollV2Activity.start(context, "url", URL_OPEN_ROLL, "开通学籍")
            }
        }
        ui?.clInvite?.setOnClickListener {
            BKLog.d("点击邀请好友")
            if (PonkoApp.getAppUIVersion(context) == UI_VERSION_1) {
                ActivityUtil.startActivity(context, Intent(context, InviteFriendActivity::class.java))
            } else {
                InviteFriendV2Activity.start(context, "url", URL_INVITE, "邀请好友")
            }
        }

        //学习时长
        ui?.tvStudyTodayH?.text = (profileCBean.study_today.toInt() / 3600).toString()
        ui?.tvStudyTodayM?.text = (profileCBean.study_today.toInt() / 60 % 60).toString()
        ui?.tvStudyTotalH?.text = (profileCBean.study_all.toInt() / 3600).toString()
        ui?.tvStudyTotalM?.text = (profileCBean.study_all.toInt() / 60 % 60).toString()
        ui?.ivScholarship?.setOnClickListener {
            if (profileCBean.study_alert != null) {
                //弹出一个广告框
                setCover(profileCBean, context, profileCBean.study_alert.picture)
            } else {
                BKLog.e("获取积分奖学金弹框信息失败!!!")
            }
        }
        ui?.clPoster?.setOnClickListener {
            ActivityUtil.startActivity(context, Intent(context, ReportActivity::class.java))
        }

    }

    private fun old(d: Any, LOAD_IMAGE_DELAY: Long) {
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
            if (PonkoApp.getAppUIVersion(context) == UI_VERSION_1) {
                ActivityUtil.startActivity(context, Intent(context, OpenRollActivity::class.java))
            } else {
                OpenRollV2Activity.start(context, "url", URL_OPEN_ROLL, "开通学籍")
            }
        }
        viewHolder?.clInvite?.setOnClickListener {
            BKLog.d("点击邀请好友")
            if (PonkoApp.getAppUIVersion(context) == UI_VERSION_1) {
                ActivityUtil.startActivity(context, Intent(context, InviteFriendActivity::class.java))
            } else {
                InviteFriendV2Activity.start(context, "url", URL_INVITE, "邀请好友")
            }
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

    private fun setCover(profileCBean: ProfileCBean?, context: Context?, picture: String?) {
        val scale = profileCBean?.study_alert?.width?.toFloat()!! / profileCBean.study_alert?.height!!
        val context = itemView.context
        val dia = Dialog(context, R.style.edit_AlertDialog_style)
        dia.setContentView(R.layout.dig_cus)
        val imageView = dia.findViewById(R.id.iv_cover) as ImageView
        imageView.setOnClickListener {
            if (!profileCBean.study_alert.link_type.equals("NONE", true)) {
                IntoTargetUtil.target(context, profileCBean.study_alert.link_type, profileCBean.study_alert.link_value)
            }
            dia.dismiss()
        }
        com.bumptech.glide.Glide.with(context)
                .load(picture)
                .asBitmap() // 制Glide返回一个Bitmap对象
                .into(Transformation(scale, context as Activity, imageView, dia))
        dia.show()
    }

    private class Transformation(private val scale: Float, private val activity: Activity, private val target: ImageView?, private val dia: Dialog?) : ImageViewTarget<Bitmap>(target) {

        override fun setResource(resource: Bitmap) {
            try {
                //获取原图的宽高
                val width = resource.width
                val height = resource.height
                val maxWidth = Math.min(ScreenUtil.getNormalWH(activity)[0] - (2 * (ScreenUtil.getNormalWH(activity)[0] * 0.2f)).toInt(), width)
                val maxHeight = (maxWidth / scale).toInt()

                dia?.setCanceledOnTouchOutside(true) // Sets whether this dialog is
                val w = dia?.window
                val lp = w?.attributes
                lp?.width = maxWidth
                lp?.height = maxHeight
                lp?.x = 0
                lp?.y = 0
                dia?.onWindowAttributesChanged(lp)
                target?.setImageBitmap(resource)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun s(resource: Bitmap) {
            view.setImageBitmap(resource)

            //获取原图的宽高
            val width = resource.width
            val height = resource.height

            //获取imageView的宽
            val imageViewWidth = target?.width

            //计算缩放比例
            val sy = (imageViewWidth!! * 0.1).toFloat() / (width * 0.1).toFloat()

            //计算图片等比例放大后的高
            val imageViewHeight = (height * sy).toInt()
            val params = target?.layoutParams
            params?.height = imageViewHeight
            target?.layoutParams = params

        }

        fun sy(resource: Bitmap): Float {
            val width = resource.width
            val height = resource.height
            val imageViewWidth = target?.width
            return (imageViewWidth!! * 0.1).toFloat() / (width * 0.1).toFloat()
        }

        fun getNewBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
            // 获得图片的宽高.
            val width = bitmap.width
            val height = bitmap.height
            // 计算缩放比例.
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            // 取得想要缩放的matrix参数.
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            // 得到新的图片.
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        }

    }

    /**
     * 旧版界面
     */
    class ViewHolder private constructor(val btnWxUnbind: Button, val clUserInfo: ConstraintLayout, val tvName: TextView, val ivVipNoOrYes: ImageView, val tvVipDes: TextView, val ivCircleHead: CircleImageView, val imageView4: ImageView, val clOther: ConstraintLayout, val llCourse: LinearLayout, val tvCourseNumber: TextView, val tvCourseDes: TextView, val llTime: LinearLayout, val tvTimeNumber: TextView, val tvTimeDes: TextView, val llIntegral: LinearLayout, val tvIntegralNumber: TextView, val tvIntegralDes: TextView, val clOpenInvite: ConstraintLayout, val clOpenRoll: ConstraintLayout, val llOpen: LinearLayout, val clInvite: ConstraintLayout, val llInvite: LinearLayout) {
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

    /**
     * 新版界面
     */
    private class UI private constructor(val btnWxUnbind: Button, val clUserInfo: ConstraintLayout, val clPoster: ConstraintLayout, val tvName: TextView, val ivVipNoOrYes: ImageView, val tvVipDes: TextView, val ivCircleHead: CircleImageView, val imageView4: ImageView, val llOther: LinearLayout, val divider1: View, val textView: TextView, val ivArrowRight: ImageView, val ivScholarship: AppCompatImageButton, val clStudyToday: ConstraintLayout, val tvStudyTodayH: TextView, val tvStudyTodayM: TextView, val clStudyTotal: ConstraintLayout, val tvStudyTotalH: TextView, val tvStudyTotalM: TextView, val glCenter: Guideline, val divider2: View, val clOpenInvite: ConstraintLayout, val clOpenRoll: ConstraintLayout, val llOpen: LinearLayout, val clInvite: ConstraintLayout, val llInvite: LinearLayout, val divider: View) {
        companion object {

            fun create(rootView: View): UI {
                val btnWxUnbind = rootView.findViewById<View>(R.id.btn_wx_unbind) as Button
                val clUserInfo = rootView.findViewById<View>(R.id.cl_user_info) as ConstraintLayout
                val clPoster = rootView.findViewById<View>(R.id.cl_poster) as ConstraintLayout
                val tvName = rootView.findViewById<View>(R.id.tv_name) as TextView
                val ivVipNoOrYes = rootView.findViewById<View>(R.id.iv_vip_no_or_yes) as ImageView
                val tvVipDes = rootView.findViewById<View>(R.id.tv_vip_des) as TextView
                val ivCircleHead = rootView.findViewById<View>(R.id.iv_circle_head) as CircleImageView
                val imageView4 = rootView.findViewById<View>(R.id.imageView4) as ImageView
                val llOther = rootView.findViewById<View>(R.id.ll_other) as LinearLayout
                val divider1 = rootView.findViewById(R.id.divider1) as View
                val textView = rootView.findViewById<View>(R.id.textView) as TextView
                val ivArrowRight = rootView.findViewById<View>(R.id.iv_arrow_right) as ImageView
                val ivScholarship = rootView.findViewById<View>(R.id.iv_scholarship) as AppCompatImageButton
                val clStudyToday = rootView.findViewById<View>(R.id.cl_study_today) as ConstraintLayout
                val tvStudyTodayH = rootView.findViewById<View>(R.id.tv_study_today_h) as TextView
                val tvStudyTodayM = rootView.findViewById<View>(R.id.tv_study_today_m) as TextView
                val clStudyTotal = rootView.findViewById<View>(R.id.cl_study_total) as ConstraintLayout
                val tvStudyTotalH = rootView.findViewById<View>(R.id.tv_study_total_h) as TextView
                val tvStudyTotalM = rootView.findViewById<View>(R.id.tv_study_total_m) as TextView
                val glCenter = rootView.findViewById<View>(R.id.gl_center) as Guideline
                val divider2 = rootView.findViewById(R.id.divider2) as View
                val clOpenInvite = rootView.findViewById<View>(R.id.cl_open_invite) as ConstraintLayout
                val clOpenRoll = rootView.findViewById<View>(R.id.cl_open_roll) as ConstraintLayout
                val llOpen = rootView.findViewById<View>(R.id.ll_open) as LinearLayout
                val clInvite = rootView.findViewById<View>(R.id.cl_invite) as ConstraintLayout
                val llInvite = rootView.findViewById<View>(R.id.ll_invite) as LinearLayout
                val divider = rootView.findViewById(R.id.divider) as View
                return UI(btnWxUnbind, clUserInfo, clPoster, tvName, ivVipNoOrYes, tvVipDes, ivCircleHead, imageView4, llOther, divider1, textView, ivArrowRight, ivScholarship, clStudyToday, tvStudyTodayH, tvStudyTodayM, clStudyTotal, tvStudyTotalH, tvStudyTotalM, glCenter, divider2, clOpenInvite, clOpenRoll, llOpen, clInvite, llInvite, divider)
            }
        }
    }
}


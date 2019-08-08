package com.ponko.cn.module.my.v1.option

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ponko.cn.R
import com.ponko.cn.bean.ReportShareCBean
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.my.v1.constract.PosterContract
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.CircleImageView


/**
 * 学习战报页面
 */
class PosterActivity : PonkoBaseAct<PosterContract.P>(), PosterContract.V {


    /**
     * 界面控件
     */
    private var ui: UI? = null

    override fun presenter(): PosterContract.P {
        return PosterContract.P(this, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_poster
    }

    override fun findViews() {
        super.findViews()
        if (ui == null) {
            ui = UI.create(this)
        }
    }

    override fun initDisplay() {
        super.initDisplay()
        addBar2(this, ui?.toolbar, "我的学习战报")
    }

    override fun setTitle(title: String?) {
        if (!TextUtils.isEmpty(title)){
            addBar2(this, ui?.toolbar, title!!)
        }
    }

    override fun iniData() {
        super.iniData()
        //请求页面接口数据
        p?.requestReportShareApi()
        //获取当前时间-年月日星期几
        p?.getCurrentDate()
    }

    override fun iniEvent() {
        super.iniEvent()
        ui?.srl?.setOnRefreshListener {
            p?.refresh()
        }
        ui?.btnSharePoster?.setOnClickListener {
            p?.clickShareBtn()
        }

        ui?.clPoster?.setOnLongClickListener {
            p?.savePoster()
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun setCurrentDate(year: String, mmdd: String, whatDay: String) {
        ui?.tvYear?.text = year
        ui?.tvMmdd?.text = mmdd
        ui?.tvWhatDay?.text = whatDay
    }

    override fun setPosterBk(bg: String?) {
        Glide.with(this, bg, ui?.clPoster)
    }

    override fun setPosterChickenWord(title: String?) {
        ui?.tvChicken?.text = title
    }

    override fun setPersonInfo(data: ReportShareCBean.DataBean?) {
        Glide.with(this, data?.avatar, ui?.ivCover, 0)
        ui?.tvName?.text = data?.nickname
        ui?.tvReport?.text = data?.subtitle
        if (data?.count?.size == 3) {

            ui?.tvTodayStudy?.text = data.count!![0].key
            ui?.tvTodayStudyNum?.text = getValue(data.count!![0].value)
            ui?.tvTodayStudyUnit?.text = data.count!![0].unit

            ui?.tvContinuousStudy?.text = data.count!![1].key
            ui?.tvContinuousStudyNum?.text = getValue(data.count!![1].value)
            ui?.tvContinuousStudyUnit?.text = data.count!![1].unit

            ui?.tvTotalStudy?.text = data.count!![2].key
            ui?.tvTotalStudyNum?.text = getValue(data.count!![2].value)
            ui?.tvTvTotalStudyUnit?.text = data.count!![2].unit

        } else {
            ToastUtil.show("set count failure, count is not 3 size!!!")
        }

        val startIndex = data?.footer?.indexOf("<red>")
        val endIndex = data?.footer?.lastIndexOf("</red>")
        //"今日学习力打败了<red>86%</red>的同学！"
        ui?.tv1?.text = data?.footer?.substring(0, startIndex!!)
        ui?.tv2?.text = data?.footer?.substring(startIndex!! + 5, endIndex!!)
        ui?.tv3?.text = data?.footer?.substring(endIndex!! + 6, data.footer.length)
    }

    private fun getValue(num: Double): String {
        return if (num * 10 % 10 > 0) {
            num.toString()
        } else {
            num.toInt().toString()
        }
    }

    override fun setQRInfo(qr: ReportShareCBean.QrBean?) {
        ui?.tvDes1?.text = qr?.subtitle
        ui?.tvCourseName?.text = qr?.title
        ui?.tvDes2?.text = qr?.desc

        Glide.with(this, qr?.qr, ui?.ivQr)
        ui?.tvQrDes?.text = qr?.qr_desc
    }

    override fun setRecordInfo(count: ReportShareCBean.CountBeanX?) {

    }

    override fun setShareBtn(btn: String?) {
        ui?.btnSharePoster?.text = btn
    }

    override fun getShareBmp(): Bitmap {
        return ViewUtil.getViewBitmap2(ui?.clPoster!!,375.toFloat()/667)
    }

    override fun requestReportShareApiSuccess(body: ReportShareCBean?) {
        ui?.srl?.isRefreshing = false
    }

    override fun requestReportShareApiFailure(msg: String?) {
        ui?.srl?.isRefreshing = false
    }

    /**
     * 标题栏
     */
    private fun addBar2(context: Context?, toolbar: Toolbar?, title: String) {
        val barView = ViewUtil.viewById(context, R.layout.bar_2, null)
        toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar?.visibility = View.VISIBLE
        val flBack: FrameLayout? = barView?.findViewById(R.id.fl_back) as FrameLayout
        val tvTitle: TextView? = barView.findViewById(R.id.tv_title) as TextView
        val tvBarRight: TextView? = barView.findViewById(R.id.tv_bar_right) as TextView
        flBack?.setOnClickListener {
            (context as Activity).finish()
        }
        tvTitle?.text = title
        tvBarRight?.visibility = View.GONE
    }

    /**
     * Activity 界面
     */
    private class UI private constructor(val toolbar: Toolbar, val srl: SwipeRefreshLayout, val nsv: NestedScrollView, val clPoster: ConstraintLayout, val ivLogo: ImageView, val constraintLayout4: ConstraintLayout, val tvYear: TextView, val view7: View, val tvMmdd: TextView, val tvWhatDay: TextView, val tvChicken: TextView, val cardStudy: CardView, val ivCover: CircleImageView, val tvName: TextView, val tvReport: TextView, val linearLayout7: LinearLayout, val tvTodayStudy: TextView, val tvTodayStudyNum: TextView, val tvTodayStudyUnit: TextView, val tvContinuousStudy: TextView, val tvContinuousStudyNum: TextView, val tvContinuousStudyUnit: TextView, val tvTotalStudy: TextView, val tvTotalStudyNum: TextView, val tvTvTotalStudyUnit: TextView, val divider: View, val tv1: TextView, val tv2: TextView, val tv3: TextView, val cardQr: CardView, val tvDes1: TextView, val tvCourseName: TextView, val tvDes2: TextView, val linearLayout8: LinearLayout, val ivQr: ImageView, val tvQrDes: TextView, val cardRecord: CardView, val clRecord: ConstraintLayout, val llTitle: LinearLayout, val tvRecordDes: TextView, val ivIntegral: ImageView, val llFriendNum: LinearLayout, val tvFriendNum: TextView, val tvIntegralNum: TextView, val btnSharePoster: Button) {
        companion object {

            fun create(rootView: Activity): UI {
                val toolbar = rootView.findViewById<View>(R.id.toolbar) as Toolbar
                val srl = rootView.findViewById<View>(R.id.srl) as SwipeRefreshLayout
                val nsv = rootView.findViewById<View>(R.id.nsv) as NestedScrollView
                val clPoster = rootView.findViewById<View>(R.id.cl_poster) as ConstraintLayout
                val ivLogo = rootView.findViewById<View>(R.id.iv_logo) as ImageView
                val constraintLayout4 = rootView.findViewById<View>(R.id.constraintLayout4) as ConstraintLayout
                val tvYear = rootView.findViewById<View>(R.id.tv_year) as TextView
                val view7 = rootView.findViewById(R.id.view7) as View
                val tvMmdd = rootView.findViewById<View>(R.id.tv_mmdd) as TextView
                val tvWhatDay = rootView.findViewById<View>(R.id.tv_what_day) as TextView
                val tvChicken = rootView.findViewById<View>(R.id.tv_chicken) as TextView
                val cardStudy = rootView.findViewById<View>(R.id.card_study) as CardView
                val ivCover = rootView.findViewById<View>(R.id.iv_cover) as CircleImageView
                val tvName = rootView.findViewById<View>(R.id.tv_name) as TextView
                val tvReport = rootView.findViewById<View>(R.id.tv_report) as TextView
                val linearLayout7 = rootView.findViewById<View>(R.id.linearLayout7) as LinearLayout
                val tvTodayStudy = rootView.findViewById<View>(R.id.tv_today_study) as TextView
                val tvTodayStudyNum = rootView.findViewById<View>(R.id.tv_today_study_num) as TextView
                val tvTodayStudyUnit = rootView.findViewById<View>(R.id.tv_today_study_unit) as TextView
                val tvContinuousStudy = rootView.findViewById<View>(R.id.tv_continuous_study) as TextView
                val tvContinuousStudyNum = rootView.findViewById<View>(R.id.tv_continuous_study_num) as TextView
                val tvContinuousStudyUnit = rootView.findViewById<View>(R.id.tv_continuous_study_unit) as TextView
                val tvTotalStudy = rootView.findViewById<View>(R.id.tv_total_study) as TextView
                val tvTotalStudyNum = rootView.findViewById<View>(R.id.tv_total_study_num) as TextView
                val tvTvTotalStudyUnit = rootView.findViewById<View>(R.id.tv_tv_total_study_unit) as TextView
                val divider = rootView.findViewById(R.id.divider) as View
                val tv1 = rootView.findViewById<View>(R.id.tv_1) as TextView
                val tv2 = rootView.findViewById<View>(R.id.tv_2) as TextView
                val tv3 = rootView.findViewById<View>(R.id.tv_3) as TextView
                val cardQr = rootView.findViewById<View>(R.id.card_qr) as CardView
                val tvDes1 = rootView.findViewById<View>(R.id.tv_des_1) as TextView
                val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                val tvDes2 = rootView.findViewById<View>(R.id.tv_des_2) as TextView
                val linearLayout8 = rootView.findViewById<View>(R.id.linearLayout8) as LinearLayout
                val ivQr = rootView.findViewById<View>(R.id.iv_qr) as ImageView
                val tvQrDes = rootView.findViewById<View>(R.id.tv_qr_des) as TextView
                val cardRecord = rootView.findViewById<View>(R.id.card_record) as CardView
                val clRecord = rootView.findViewById<View>(R.id.cl_record) as ConstraintLayout
                val llTitle = rootView.findViewById<View>(R.id.ll_title) as LinearLayout
                val tvRecordDes = rootView.findViewById<View>(R.id.tv_record_des) as TextView
                val ivIntegral = rootView.findViewById<View>(R.id.iv_integral) as ImageView
                val llFriendNum = rootView.findViewById<View>(R.id.ll_friend_num) as LinearLayout
                val tvFriendNum = rootView.findViewById<View>(R.id.tv_friend_num) as TextView
                val tvIntegralNum = rootView.findViewById<View>(R.id.tv_integral_num) as TextView
                val btnSharePoster = rootView.findViewById<View>(R.id.btn_share_poster) as Button
                return UI(toolbar, srl, nsv, clPoster, ivLogo, constraintLayout4, tvYear, view7, tvMmdd, tvWhatDay, tvChicken, cardStudy, ivCover, tvName, tvReport, linearLayout7, tvTodayStudy, tvTodayStudyNum, tvTodayStudyUnit, tvContinuousStudy, tvContinuousStudyNum, tvContinuousStudyUnit, tvTotalStudy, tvTotalStudyNum, tvTvTotalStudyUnit, divider, tv1, tv2, tv3, cardQr, tvDes1, tvCourseName, tvDes2, linearLayout8, ivQr, tvQrDes, cardRecord, clRecord, llTitle, tvRecordDes, ivIntegral, llFriendNum, tvFriendNum, tvIntegralNum, btnSharePoster)
            }
        }
    }


}

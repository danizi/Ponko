package com.ponko.cn.module.common.share

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.TransformationUtil
import com.xm.lib.common.base.mvp.MvpActivity


/**
 * 分享好友
 */
class ShareAct : MvpActivity<ShareContract.P>(), ShareContract.V {


    companion object {
        fun start(ctx: Context) {
            val intent = Intent(ctx, ShareAct::class.java)
            ctx.startActivity(intent)
        }
    }

    private var toolbar: Toolbar? = null
    private var nsv: NestedScrollView? = null
    private var clCover: ConstraintLayout? = null
    private var ivCover: ImageView? = null
    private var ivQr: ImageView? = null
    private var llDes: LinearLayout? = null
    private var tv1: TextView? = null
    private var tv21pre: TextView? = null
    private var tv22price: TextView? = null
    private var tv23end: TextView? = null
    private var tv3: TextView? = null
    private var llShareType: LinearLayout? = null
    private var llFriend: LinearLayout? = null
    private var llFriendCircle: LinearLayout? = null
    private var llMiniProgram: LinearLayout? = null

    override fun presenter(): ShareContract.P {
        return ShareContract.P(this, this)
    }

    override fun setContentViewBefore() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_share
    }

    override fun findViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        nsv = findViewById<View>(R.id.nsv) as NestedScrollView
        clCover = findViewById<View>(R.id.cl_cover) as ConstraintLayout
        ivCover = findViewById<View>(R.id.iv_cover) as ImageView
        ivQr = findViewById<View>(R.id.iv_qr) as ImageView
        llDes = findViewById<View>(R.id.ll_des) as LinearLayout
        tv1 = findViewById<View>(R.id.tv_1) as TextView
        tv21pre = findViewById<View>(R.id.tv_2_1pre) as TextView
        tv22price = findViewById<View>(R.id.tv_2_2price) as TextView
        tv23end = findViewById<View>(R.id.tv_2_3end) as TextView
        tv3 = findViewById<View>(R.id.tv_3) as TextView
        llShareType = findViewById<View>(R.id.ll_share_type) as LinearLayout
        llFriend = findViewById<View>(R.id.ll_friend) as LinearLayout
        llFriendCircle = findViewById<View>(R.id.ll_friend_circle) as LinearLayout
        llMiniProgram = findViewById<View>(R.id.ll_mini_program) as LinearLayout

    }

    override fun initDisplay() {
        BarUtil.addBar2(this, toolbar, "分享好友", "", View.OnClickListener { })
    }

    override fun iniData() {
        p?.requestShareApi(intent.getStringExtra("type"), intent.getStringExtra("id"))
    }

    override fun iniEvent() {
        clCover?.setOnLongClickListener {
            p?.longClickSaveCover(clCover)
            false
        }
        llFriend?.setOnClickListener {
            p?.shareTo(clCover, 0)
        }
        llFriendCircle?.setOnClickListener {
            p?.shareTo(clCover, 1)
        }
        llMiniProgram?.setOnClickListener {
            p?.shareTo(clCover, 2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        p?.clear()
    }

    override fun setShareBg(bg: String?) {
        com.bumptech.glide.Glide.with(this)
                .load(bg)
                .asBitmap() // 制Glide返回一个Bitmap对象
                .into(TransformationUtil(ivCover!!))
    }

    override fun setSharerInformation(qr: String?, title1: String?, title2: String?, title3: String?) {
        //设置封面
        Glide.with(this, qr, ivQr)
        tv1?.text = title1
        val startIndex = title2?.indexOf("<red>")
        val endIndex = title2?.lastIndexOf("</red>")
        //"送你一门<red>¥499</red>的课程"
        val pre = title2?.substring(0, startIndex!!)
        val price = title2?.substring(startIndex!! + 5, endIndex!!)
        val end = title2?.substring(endIndex!! + 6, title2.length)
        tv21pre?.text = pre
        tv22price?.text = price
        tv23end?.text = end
        tv3?.text = title3
    }
}

package com.ponko.cn.module.common.share

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.ponko.cn.R
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.base.mvp.MvpActivity

/**
 * 分享好友
 */
class ShareAct : MvpActivity<ShareContract.P>(), ShareContract.V {

    private var toolbar: Toolbar? = null
    private var clCover: ConstraintLayout? = null
    private var ivCover: ImageView? = null
    private var ivQr: ImageView? = null
    private var llDes: LinearLayout? = null
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
        clCover = findViewById<View>(R.id.cl_cover) as ConstraintLayout
        ivCover = findViewById<View>(R.id.iv_cover) as ImageView
        ivQr = findViewById<View>(R.id.iv_qr) as ImageView
        llDes = findViewById<View>(R.id.ll_des) as LinearLayout
        llShareType = findViewById<View>(R.id.ll_share_type) as LinearLayout
        llFriend = findViewById<View>(R.id.ll_friend) as LinearLayout
        llFriendCircle = findViewById<View>(R.id.ll_friend_circle) as LinearLayout
        llMiniProgram = findViewById<View>(R.id.ll_mini_program) as LinearLayout
    }

    override fun initDisplay() {
        BarUtil.addBar2(this, toolbar, "分享好友", "", View.OnClickListener { })
    }

    override fun iniData() {
        p?.requestShareApi()
    }

    override fun iniEvent() {
        clCover?.setOnLongClickListener {
            p?.longClickSaveCover(clCover)
            false
        }
        llFriend?.setOnClickListener {
            p?.shareTo(clCover,0)
        }
        llFriendCircle?.setOnClickListener {
            p?.shareTo(clCover,1)
        }
        llMiniProgram?.setOnClickListener {
            p?.shareTo(clCover,2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        p?.clear()
    }
}

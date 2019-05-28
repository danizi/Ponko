package com.ponko.cn.module.my.option

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp.Companion.APP_ID
import com.ponko.cn.app.PonkoApp.Companion.myApi
import com.ponko.cn.bean.Invite
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.mvp.MvpActivity
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.common.util.ViewUtil.getViewBitmap2
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
import retrofit2.Call
import retrofit2.Response


class InviteFriendShareActivity : MvpActivity<Any>() {

    companion object {
        var productId = ""
        fun start(context: Context, id: String?) {
            productId = id!!
            context.startActivity(Intent(context, InviteFriendShareActivity::class.java))
        }
    }

    private var ui: ViewHolder? = null

    override fun presenter(): Any {
        return Any()
    }

    override fun setContentViewBefore() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_invite_friend_share
    }

    override fun findViews() {
        if (ui == null) {
            ui = ViewHolder.create(this)
        }
    }

    override fun initDisplay() {
        ui?.srl?.isRefreshing = true
        BarUtil.addBar1(this, ui?.toolbar, "邀请好友")
    }

    override fun iniData() {

        myApi?.shareInvites(productId)?.enqueue(object : HttpCallBack<Invite>() {
            override fun onSuccess(call: Call<Invite>?, response: Response<Invite>?) {
                val invite = response?.body()
                ui?.vp?.pageMargin = 80
                for (template in invite?.template!!) {
                    template.nickname = invite.nickname
                    template.qr_url = invite.qr_url
                    template.course_name = invite.course_name
                }
                ui?.vp?.adapter = Adapter(this@InviteFriendShareActivity, invite.template!!)
                ui?.srl?.isRefreshing = false
            }

            override fun onFailure(call: Call<Invite>?, msg: String?) {
                super.onFailure(call, msg)
                ui?.srl?.isRefreshing = false
            }
        })
    }

    override fun iniEvent() {
        ui?.srl?.setOnRefreshListener {
            iniData()
        }
        var index = 0
        ui?.vp?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                index = p0
            }
        })
        ui?.btnShare?.setOnClickListener {
            //findViewById<ImageView>(R.id.iv_test).setImageBitmap(getViewBitmap2(ui?.vp?.getChildAt(index)!!))
            val wxShare = WxShare(this)
            wxShare.init(ShareConfig.Builder().appid(APP_ID).build())
            wxShare.shareImage(getViewBitmap2(ui?.vp?.getChildAt(index)!!))
        }
    }

    /**
     * ViewPager适配器
     */
    private class Adapter(val context: Context, val data: List<Invite.Template>) : PagerAdapter() {
        override fun isViewFromObject(p0: View, p1: Any): Boolean {
            return p0 == p1
        }

        override fun getCount(): Int {
            return if (data.isEmpty()) {
                0
            } else {
                data.size
            }
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = ViewUtil.viewById(context, R.layout.item_invite_friend_share_img, null)
            val ivBk = view?.findViewById<ImageView>(R.id.iv_bk)
            val ivQr = view?.findViewById<ImageView>(R.id.iv_qr)
            val tvUserName = view?.findViewById<TextView>(R.id.tv_user_name)
            val tvCourseName = view?.findViewById<TextView>(R.id.tv_course_name)
            Glide.with(context, data[position].bg_url, ivBk)
            Glide.with(context, data[position].qr_url, ivQr)
            tvUserName?.text = data[position].nickname
            tvCourseName?.text = data[position].course_name
            container.addView(view)
            return view!!
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            //super.destroyItem(container, position, `object`)
            container.removeView(`object` as View?)
        }
    }

    /**
     * 窗口UI
     */
    private class ViewHolder private constructor(val toolbar: Toolbar, val vp: ViewPager, val btnShare: Button, val srl: SwipeRefreshLayout) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val toolbar = rootView.findViewById<View>(R.id.toolbar) as Toolbar
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                val btnShare = rootView.findViewById<View>(R.id.btn_share) as Button
                val srl = rootView.findViewById<View>(R.id.srl) as SwipeRefreshLayout
                return ViewHolder(toolbar, vp, btnShare, srl)
            }
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_invite_friend_share)
//    }
}
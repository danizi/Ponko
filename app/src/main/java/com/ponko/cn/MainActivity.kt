package com.ponko.cn

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.adApi
import com.ponko.cn.bean.AdCBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.constant.Constants.UI_VERSION_1
import com.ponko.cn.constant.Constants.UI_VERSION_2
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.free.v1.FreeFrg
import com.ponko.cn.module.free.v2.FreeFrgV2
import com.ponko.cn.module.interflow.frg.InterflowFrg
import com.ponko.cn.module.media.AttachmentGesture
import com.ponko.cn.module.my.v1.MyFrg
import com.ponko.cn.module.study.v1.StudyFrg
import com.ponko.cn.module.study.v2.StudyFrg2
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.IntoTargetUtil
import com.ponko.cn.utils.ToastUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xm.lib.common.base.BaseActivity
import com.xm.lib.common.http.NetworkUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.BottomMenu
import com.xm.lib.component.OnItemClickListener
import com.xm.lib.component.XmAdView
import com.xm.lib.media.broadcast.BroadcastManager
import q.rorbin.badgeview.QBadgeView
import retrofit2.Call
import retrofit2.Response
import java.lang.ref.WeakReference

/**
 * 首页窗口
 */
class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
//        @Deprecated("")
//        lateinit var bottomMenu: BottomMenu
    }

    private var qbadgeView: QBadgeView? = null
    private var view: View? = null
    private var broadcastManager: BroadcastManager? = null
    private var bottomMenuReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when {
                intent?.action == Constants.ACTION_BOTTOM_MENU -> {
                    bottomPos = intent.getIntExtra("index", 0)
                    bar.select(bottomPos)
                }
                intent?.action == Constants.ACTION_BOTTOM_TIP_SHOW -> {
                    showMsgTip(intent.getIntExtra("count", 0))
                }
                intent?.action == Constants.ACTION_BOTTOM_TIP_HIDE -> {
                    hideMsgTip()
                }
            }
        }
    }

    private fun showMsgTip(count: Int) {
        view = bar.getTabAt(3)?.customView
        if (count > 0) {
            if (qbadgeView == null) {
                qbadgeView = QBadgeView(this)
            }
            qbadgeView?.visibility = View.VISIBLE
            qbadgeView?.bindTarget(view)?.badgeNumber = count
            qbadgeView?.setGravityOffset(6f, 0f, true)
            return
        }
        qbadgeView?.visibility = View.GONE
        BKLog.d("我的消息数量:$count")
    }

    private fun hideMsgTip() {
        view = bar.getTabAt(3)?.customView
        //去掉消息提醒
        qbadgeView?.visibility = View.GONE
        qbadgeView?.bindTarget(view)?.badgeNumber = -1
    }

    /**
     * 当前底部菜单所在位置
     */
    private var bottomPos = 0
    lateinit var bar: BottomMenu

    override fun setContentViewBefore() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (broadcastManager == null) {
            broadcastManager = BroadcastManager.create(this)
        }
        broadcastManager?.registerReceiver(Constants.ACTION_BOTTOM_MENU, bottomMenuReceiver)
        broadcastManager?.registerReceiver(Constants.ACTION_BOTTOM_TIP_SHOW, bottomMenuReceiver)
        broadcastManager?.registerReceiver(Constants.ACTION_BOTTOM_TIP_HIDE, bottomMenuReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcastManager?.unRegisterReceiver(bottomMenuReceiver)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun findViews() {
        bar = findViewById(R.id.bottom_menu)
//        bar = bottomMenu
    }

    private fun getStudyFragment(): Fragment {
//        val uiVersion = PonkoApp.getAppUIVersion(this)
//        return when (uiVersion) {
//            "1" -> {
//                StudyFrg()
//            }
//            "2" -> {
//                StudyFrg2()
//            }
//            else -> {
//                StudyFrg2()
//            }
//        }
        val type = PonkoApp.getAppUIVersion(this)
        return when (type) {
            UI_VERSION_1 -> {
                StudyFrg()
            }
            UI_VERSION_2 -> {
                StudyFrg2()
            }
            else -> {
                StudyFrg2()
            }
        }
    }

    private fun getFreeFragment(): Fragment {
        val type = PonkoApp.getAppUIVersion(this)
        return when (type) {
            UI_VERSION_1 -> {
                FreeFrg()
            }
            UI_VERSION_2 -> {
                FreeFrgV2()
            }
            else -> {
                FreeFrgV2()
            }
        }
    }

    override fun initDisplay() {
        BKLog.d("应用销毁保存数据接受的数据：")
        //根据标志位选择使用哪个学习页面Fragment
        bar.select(bottomPos)
                .setContainer(R.id.container)
                .setTitleColor(R.color.grey, R.color.red)
                .setItemLayoutId(R.layout.item_bottom_menu)
                .addItem(getStudyFragment(), "学习", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
                .addItem(getFreeFragment(), "免费", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
                .addItem(InterflowFrg(), "交流", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
                .addItem(MyFrg(), "我的", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
                .setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(view: BottomMenu, pos: Int) {
                        bottomPos = pos
                        BKLog.d(TAG, "pos:$pos")
                    }
                })
                .build()

//        if (NetworkUtil.is3GNet(this)) {
//            DialogUtil.show(this, "提示", "当前使用是手机流量", true, null, null)
//        }
        if (!NetworkUtil.isNetworkConnected(this)) {
            ToastUtil.show("当前处于断网状态...")
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("CheckResult")
    override fun iniData() {
        PonkoApp.retrofitClient?.headers?.put("x-tradestudy-access-token", CacheUtil.getToken()!!)
        requestAdApi()
        val weakReference = WeakReference(this)
        val activity = weakReference.get()!!
        RxPermissions(activity).request(
                Manifest.permission.WRITE_SECURE_SETTINGS,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
                ?.subscribe { granted ->
                    if (granted!!) {
                        BKLog.d(AttachmentGesture.TAG, "All requested permissions are granted")
                    } else {

                        BKLog.d(AttachmentGesture.TAG, "At least one permission is denied")
                    }
                }
    }

    override fun iniEvent() {

    }

    /**
     * 请求广告接口
     */
    private fun requestAdApi() {
        adApi?.home()?.enqueue(object : HttpCallBack<AdCBean>() {
            private var xmAdView: XmAdView? = null
            private var body: AdCBean? = null
            override fun onSuccess(call: Call<AdCBean>?, response: Response<AdCBean>?) {
                //ps:如果没有广告数据后台会返回一个空数据,因为限定了转化实体类是AdCBean，所以会出现类型转化错误
                try {
                    body = response?.body()
                    if (!TextUtils.isEmpty(body?.picture)) {
                        show(body?.id, body?.picture, body?.type, body?.target)
                    }
                } catch (e: Exception) {
                    BKLog.e("未请求到广告数据")
                    //show()
                }
            }

            private fun show(adId: String?, adUrl: String?, linkType: String?, linkValue: String?) {
                xmAdView = XmAdView.Builder()
                        .context(this@MainActivity)
                        .activity(this@MainActivity)
                        .build()
                xmAdView?.setAdRate(1.57f, 24)
                xmAdView?.setCover(adUrl)
                //xmAdView?.show()
                xmAdView?.setOnAdListener(View.OnClickListener {
                    requestAdFeedbackApi(adId, 1)
                    IntoTargetUtil.target(this@MainActivity, linkType, linkValue)
                })
                xmAdView?.setOnCloseListener(View.OnClickListener {
                    requestAdFeedbackApi(adId, 2)
                })
            }

            /**
             * 请求广告点击反馈接口
             */
            private fun requestAdFeedbackApi(id: String?, type: Int) {
                adApi?.feedback(id, type)?.enqueue(object : HttpCallBack<Any>() {
                    override fun onSuccess(call: Call<Any>?, response: Response<Any>?) {
                        when (type) {
                            1 -> {
                                BKLog.d("点击广告,广告反馈成功")
                            }
                            2 -> {
                                BKLog.d("关闭广告,广告反馈成功")
                            }
                        }
                    }

                    override fun onFailure(call: Call<Any>?, msg: String?) {
                        super.onFailure(call, msg)
                        when (type) {
                            1 -> {
                                BKLog.e("点击广告,广告反馈失败")
                            }
                            2 -> {
                                BKLog.e("关闭广告,广告反馈失败")
                            }
                        }
                    }
                })
                if (xmAdView?.isShowing == true) {
                    xmAdView?.dismiss()
                }
            }
        })
    }

    /**
     * 点击两次退出应用
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        for (fragment in this.supportFragmentManager.fragments) {
            if (fragment is FreeFrgV2 && fragment.isVisible && keyCode == KeyEvent.KEYCODE_BACK) {
                val freeFrgV2 = (fragment as FreeFrgV2)
                if (freeFrgV2.isLandscepe()) {
                    freeFrgV2.keyBack()
                    return false
                }
            }
        }
        return back(keyCode, event)
    }

    /**
     * 窗口销毁之前保存状态
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("pos", bottomPos)
        BKLog.d("应用销毁保存的数据 -> $bottomPos")
    }

    /**
     * 应用重启取保存数据恢复状态
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        bottomPos = savedInstanceState?.getInt("pos")!!
        BKLog.d("应用重启拿到的保存的数据 -> $bottomPos")
        bar.select(bottomPos)
    }

}


package com.ponko.cn

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.adApi
import com.ponko.cn.bean.AdCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.free.FreeFrg
import com.ponko.cn.module.interflow.frg.InterflowFrg
import com.ponko.cn.module.media.AttachmentGesture
import com.ponko.cn.module.my.MyFrg
import com.ponko.cn.module.study.StudyFrg
import com.ponko.cn.module.study.constract.StudyContract
import com.ponko.cn.module.study2.StudyFrg2
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.CacheUtil.getStudyUI
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.IntoTargetUtil
import com.ponko.cn.utils.ToastUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.bugly.beta.Beta
import com.xm.lib.common.base.BaseActivity
import com.xm.lib.common.http.NetworkUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.BrightnessUtil
import com.xm.lib.component.BottomMenu
import com.xm.lib.component.OnItemClickListener
import com.xm.lib.component.XmAdView
import retrofit2.Call
import retrofit2.Response

/**
 * 首页窗口
 */
class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
        lateinit var bottomMenu: BottomMenu
    }

    /**
     * 当前底部菜单所在位置
     */
    private var bottomPos = 0

    override fun setContentViewBefore() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun findViews() {
        bottomMenu = findViewById(R.id.bottom_menu)
    }

    override fun initDisplay() {
        BKLog.d("应用销毁保存数据接受的数据：")
        //根据标志位选择使用哪个学习页面Fragment
        val studyFragment = when (getStudyUI()) {
            "1" -> {
                StudyFrg()
            }
            "2" -> {
                StudyFrg2()
            }
            else -> {
                StudyFrg2()
            }
        }

        bottomMenu.select(bottomPos)
                .setContainer(R.id.container)
                .setTitleColor(R.color.grey, R.color.red)
                .setItemLayoutId(R.layout.item_bottom_menu)
                .addItem(studyFragment, "学习", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
                .addItem(FreeFrg(), "免费", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
                .addItem(InterflowFrg(), "交流", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
                .addItem(MyFrg(), "我的", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
                .setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(view: BottomMenu, pos: Int) {
                        bottomPos = pos
                        BKLog.d(TAG, "pos:$pos")
                    }
                })
                .build()

        if (NetworkUtil.is3GNet(this)) {
            DialogUtil.show(this, "提示", "当前使用是手机流量", true, null, null)
        }
        if (!NetworkUtil.isNetworkConnected(this)) {
            ToastUtil.show("当前处于断网状态...")
        }
    }

    @SuppressLint("CheckResult")
    override fun iniData() {
        PonkoApp.retrofitClient?.headers?.put("x-tradestudy-access-token", CacheUtil.getToken()!!)
        requestAdApi()
        RxPermissions(this).request(
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
                xmAdView?.setAdRate(1.57f, 20)
                xmAdView?.setCover(adUrl)
                xmAdView?.show()
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
        bottomMenu.select(bottomPos)
    }

}

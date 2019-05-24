package com.ponko.cn

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.adApi
import com.ponko.cn.bean.AdCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.free.FreeFrg
import com.ponko.cn.module.interflow.frg.InterflowFrg
import com.ponko.cn.module.my.MyFrg
import com.ponko.cn.module.study.StudyFrg
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.common.base.BaseActivity
import com.xm.lib.common.log.BKLog
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
        bottomMenu.select(bottomPos)
                .setContainer(R.id.container)
                .setTitleColor(R.color.grey, R.color.red)
                .setItemLayoutId(R.layout.item_bottom_menu)
                .addItem(StudyFrg(), "学习", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
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
    }

    override fun iniData() {
        PonkoApp.retrofitClient?.headers?.put("x-tradestudy-access-token", CacheUtil.getToken()!!)
        requestAdApi()
    }

    override fun iniEvent() {

    }

    /**
     * 请求广告接口
     */
    private fun requestAdApi() {
        adApi?.home()?.enqueue(object : HttpCallBack<AdCBean>() {
            private var xmAdView: XmAdView? = null
            override fun onSuccess(call: Call<AdCBean>?, response: Response<AdCBean>?) {
                //ps:如果没有广告数据后台会返回一个空数据,因为限定了转化实体类是AdCBean，所以会出现类型转化错误
                try {
                    val body = response?.body()
                    if (!TextUtils.isEmpty(body?.picture)) {
                        xmAdView = XmAdView.Builder()
                                .context(this@MainActivity)
                                .activity(this@MainActivity)
                                .build()
                        xmAdView?.setCover(body?.picture)
                        xmAdView?.show()
                        xmAdView?.setOnAdListener(View.OnClickListener {
                            requestAdFeedbackApi(body?.id?.toInt()!!, 1)
                            xmAdView?.dismiss()
                        })
                        xmAdView?.setOnCloseListener(View.OnClickListener {
                            requestAdFeedbackApi(response?.body()?.id?.toInt()!!, 2)
                            xmAdView?.dismiss()
                        })

                    }
                } catch (e: Exception) {
                    BKLog.e("未请求到广告数据")
                    //show()
                }
            }

            private fun show() {
                xmAdView = XmAdView.Builder()
                        .context(this@MainActivity)
                        .activity(this@MainActivity)
                        .build()
                xmAdView?.setAdRate(1.57f, 43)
                xmAdView?.setCover("http://mmbiz.qpic.cn/mmbiz/PwIlO51l7wuFyoFwAXfqPNETWCibjNACIt6ydN7vw8LeIwT7IjyG3eeribmK4rhibecvNKiaT2qeJRIWXLuKYPiaqtQ/0")
                xmAdView?.show()
                xmAdView?.setOnAdListener(View.OnClickListener {
                    xmAdView?.dismiss()
                })
                xmAdView?.setOnCloseListener(View.OnClickListener {
                    xmAdView?.dismiss()
                })
            }

            /**
             * 请求广告点击反馈接口
             */
            private fun requestAdFeedbackApi(id: Int, type: Int) {
                adApi?.feedback(id.toString(), type)?.enqueue(object : HttpCallBack<Any>() {
                    override fun onSuccess(call: Call<Any>?, response: Response<Any>?) {}
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


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (KeyEvent.KEYCODE_BACK == keyCode) {
//            finish()
//        }
//        return super.onKeyDown(keyCode, event)
        return back(keyCode, event)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("pos", bottomPos)
        BKLog.d("应用销毁保存的数据 -> $bottomPos")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        bottomPos = savedInstanceState?.getInt("pos")!!
        BKLog.d("应用重启拿到的保存的数据 -> $bottomPos")
        bottomMenu.select(bottomPos)
    }

}

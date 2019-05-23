package com.ponko.cn

import android.annotation.SuppressLint
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.UI_DEBUG
import com.ponko.cn.bean.AdCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.BaseActivity
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimerHelper
import retrofit2.Call
import retrofit2.Response

/**
 * 启动页
 */
class StartAct : BaseActivity() {
    companion object {
        const val TAG = "StartAct"
    }

    private var timerHelper = TimerHelper()
    private var adCBean: AdCBean? = null
    private var clAd: ConstraintLayout? = null
    private var flAdOver: FrameLayout? = null
    private var tvAdOver: TextView? = null
    private var ivAd: ImageView? = null

    override fun setContentViewBefore() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_start
    }

    override fun findViews() {
        clAd = findViewById(R.id.cl_ad)
        flAdOver = findViewById(R.id.fl_ad_over)
        tvAdOver = findViewById(R.id.tv_ad_over)
        ivAd = findViewById(R.id.iv_ad)
    }

    override fun initDisplay() {
        clAd?.visibility = View.GONE
        flAdOver?.isClickable = false
        ivAd?.isClickable = false
    }

    override fun iniData() {
        //请求广告接口
        requestAd()
    }

    private fun requestAd() {
        PonkoApp.adApi?.screen()?.enqueue(object : HttpCallBack<AdCBean>() {
            override fun onSuccess(call: Call<AdCBean>?, response: Response<AdCBean>?) {
                try {
                    //后台会有返回null，response?.body()解析会报错，所以加了一个try
                    BKLog.d(TAG, "requestAd onSuccess : ${response?.body().toString()}")
                    requestAdSuccess(response?.body())
                } catch (e: Exception) {
                    joinMainActivity(1000)
                }
            }

            override fun onFailure(call: Call<AdCBean>?, msg: String?) {
                super.onFailure(call, msg)
                BKLog.d(TAG, "onFailure : $msg")
                flAdOver?.visibility = View.GONE
                joinMainActivity(1000)
            }
        })
    }

    /**
     * 反馈广告点击
     * @param ad Id 广告唯一标识
     * @param action 1进入广告 2关闭广告
     */
    private fun requestAdFeedback(adId: String?, action: Int) {
        BKLog.d(TAG, "requestAdFeedback adId;$adId action:$action")
        PonkoApp.adApi?.feedback(adId, action)?.enqueue(object : HttpCallBack<Any>() {
            override fun onSuccess(call: Call<Any>?, response: Response<Any>?) {
                BKLog.d(TAG, "requestAdFeedback onSuccess")
            }
        })
    }

    /**
     * 请求广告接口成功
     */
    @SuppressLint("SetTextI18n")
    private fun requestAdSuccess(body: AdCBean? = AdCBean()) {
        adCBean = body

        //没有返回广告实体测试
        if (UI_DEBUG) {
            body?.id = "http://img2.imgtn.bdimg.com/it/u=1395765958,3377106680&fm=26&gp=0.jpg"
            body?.countdown = 5
            body?.picture = "http://img2.imgtn.bdimg.com/it/u=1395765958,3377106680&fm=26&gp=0.jpg"
            body?.type = ""
            body?.target = ""
        }

        //设置广告可见
        clAd?.visibility = View.VISIBLE



        flAdOver?.isClickable = true
        ivAd?.isClickable = true
        Glide.with(this, body?.picture, ivAd) //加载图片

        //加载一个计时器
        tvAdOver?.text = "${body?.countdown} | 跳过"
        timerHelper.start(object : TimerHelper.OnPeriodListener {
            private var countdown = body?.countdown
            override fun onPeriod() {
                countdown = countdown!! - 1
                tvAdOver?.text = "$countdown | 跳过"
                BKLog.d(TAG, "倒计时:$countdown")
                //计时器通过进入主界面
                if (countdown == 0) {
                    joinMainActivity()
                }
            }

        }, body?.countdown!! * 200.toLong())
    }

    override fun iniEvent() {
        flAdOver?.setOnClickListener {
            joinMainActivity()
            requestAdFeedback(adCBean?.id, 2)
        }
        clAd?.setOnClickListener {
            joinAdDetailsActivity()
            requestAdFeedback(adCBean?.id, 1)
        }
    }

    /**
     * 进入主界面
     */
    private fun joinMainActivity(s: Long? = 0) {
        Thread.sleep(s!!)
        finish()
        timerHelper.stop()
        ActivityUtil.startActivity(this, Intent(this, MainActivity::class.java))
    }

    /**
     * 进入广告页面
     */
    private fun joinAdDetailsActivity() {
        finish()
        IntoTargetUtil.target(this, adCBean?.type, adCBean?.target)
    }
}

package com.ponko.cn.module.my.holder

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.MyTaskSignBean
import com.ponko.cn.bean.StoreTaskBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.DialogUtil
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.http.NetBean
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.Xm7DaySignView
import retrofit2.Call
import retrofit2.Response


/**
 * 签到顶部签到信息展示ViewHolder
 */
class MyTaskSignViewHolder(view: View) : BaseViewHolder(view) {

    private var ui: ViewHolder? = null
    private var context: Context? = null
    private var myTaskSignBean: MyTaskSignBean? = null
    override fun bindData(d: Any, position: Int) {
        if (ui == null) {
            ui = ViewHolder.create(itemView)
        }
        context = itemView.context
        myTaskSignBean = d as MyTaskSignBean

        //初始化显示积分数字滚动控件
        ui?.tvNumber?.setCharacterList(TickerUtils.getDefaultNumberList())
        ui?.tvNumber?.textColor = R.color.colorPrimary
        ui?.tvNumber?.textSize = 100f
        ui?.tvNumber?.animationDuration = 1000
        ui?.tvNumber?.typeface = Typeface.DEFAULT
        //tickerView?.animationInterpolator = OvershootInterpolator()
        ui?.tvNumber?.gravity = Gravity.BOTTOM
        ui?.tvNumber?.setText("0")

        //判断是否签到了
        if (myTaskSignBean?.completed!!) {
            // ps:未了确保积分数正常还是重新请求了积分 - 不再使用 myTaskSignBean.day myTaskSignBean.scores
            showSignContent()
        } else {
            showSignBtn()
        }
        //显示签到天数
        ui?.view7daySign?.setSelect(myTaskSignBean?.days!!, false)
    }

    /**
     * 显示签到按钮
     */
    private fun showSignBtn() {
        //隐藏积分数和签到天数
        hideSignContent()
        ui?.ivSign?.visibility = View.VISIBLE
        ui?.ivSign?.setOnClickListener {
            BKLog.d("点击签到")
            PonkoApp.myApi?.signin()?.enqueue(object : HttpCallBack<NetBean>() {
                override fun onSuccess(call: Call<NetBean>?, response: Response<NetBean>?) {
                    BKLog.d("签到成功")
                    showSignContent() //签到积分数显示
                    hideSignBtn()     //点击圆按钮隐藏
                    context?.sendBroadcast(Intent(Constants.ACTION_SIGN_SUCCESS)) //签到成功发送广播，让我的页面和积分商城页面刷新
                }

                override fun onFailure(call: Call<NetBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    DialogUtil.show(context!!, "提示", "签到失败", true, null, null)
                }
            })
        }
    }

    /**
     * 隐藏签到按钮
     */
    private fun hideSignBtn() {
        ui?.ivSign?.visibility = View.GONE
    }

    /**
     * 显示积分数和签到天数
     */
    private fun showSignContent() {
        //显示签到信息
        ui?.clSign?.visibility = View.VISIBLE
        //隐藏签到按钮
        hideSignBtn()
        //初始化显示积分数字滚动控件
        ui?.tvNumber?.setCharacterList(TickerUtils.getDefaultNumberList())
        ui?.tvNumber?.textColor = R.color.colorPrimary
        ui?.tvNumber?.textSize = 100f
        ui?.tvNumber?.animationDuration = 1000
        ui?.tvNumber?.typeface = Typeface.DEFAULT
        //tickerView?.animationInterpolator = OvershootInterpolator()
        ui?.tvNumber?.gravity = Gravity.BOTTOM
        ui?.tvNumber?.setText("0")

        PonkoApp.myApi?.tasks()?.enqueue(object : HttpCallBack<StoreTaskBean>() {
            override fun onSuccess(call: Call<StoreTaskBean>?, response: Response<StoreTaskBean>?) {
                val body = response?.body()
                //设置签到天数
                ui?.tvDay?.text = body?.days.toString()
                //设置积分
                ui?.tvNumber?.setText(body?.scores.toString(), true)
                //签到控件进度
                ui?.view7daySign?.setSelect(body?.days!!, body.isCompleted)
            }
        })
    }

    /**
     * 隐藏积分数和签到天数
     */
    private fun hideSignContent() {
        ui?.clSign?.visibility = View.GONE
    }

    private class ViewHolder private constructor(val clSign: ConstraintLayout, val llSignDay: LinearLayout, val tvDay: TextView, val tvNumber: TickerView, val ivSign: ImageView, val view7daySign: Xm7DaySignView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val clSign = rootView.findViewById<View>(R.id.cl_sign) as ConstraintLayout
                val llSignDay = rootView.findViewById<View>(R.id.ll_sign_day) as LinearLayout
                val tvDay = rootView.findViewById<View>(R.id.tv_day) as TextView
                val tvNumber = rootView.findViewById<View>(R.id.tv_number) as TickerView
                val ivSign = rootView.findViewById<View>(R.id.iv_sign) as ImageView
                val view7daySign = rootView.findViewById<View>(R.id.view_7day_sign) as Xm7DaySignView
                return ViewHolder(clSign, llSignDay, tvDay, tvNumber, ivSign, view7daySign)
            }
        }
    }
}
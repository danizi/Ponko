package com.ponko.cn.module.my.holder

import android.graphics.Typeface
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.MyTaskSignBean
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
    override fun bindData(d: Any, position: Int) {
        if (ui == null) {
            ui = ViewHolder.create(itemView)
        }
        val context = itemView.context
        val myTaskSignBean = d as MyTaskSignBean

        //判断是否签到了
        if (!PonkoApp.signInfo?.isCompleted!!) {
            ui?.clSign?.visibility = View.GONE
            ui?.ivSign?.visibility = View.VISIBLE
            ui?.ivSign?.setOnClickListener {
                BKLog.d("点击签到")
                PonkoApp.myApi?.signin()?.enqueue(object : HttpCallBack<NetBean>() {
                    override fun onSuccess(call: Call<NetBean>?, response: Response<NetBean>?) {
                        BKLog.d("签到成功")
                        ui?.clSign?.visibility = View.VISIBLE
                        ui?.ivSign?.visibility = View.GONE
                        //去掉动画
                    }

                    override fun onFailure(call: Call<NetBean>?, msg: String?) {
                        super.onFailure(call, msg)
                        DialogUtil.show(context, "提示", "签到失败", true, null, null)
                    }
                })
            }
        } else {
            ui?.clSign?.visibility = View.VISIBLE
            ui?.ivSign?.visibility = View.GONE
            //初始化签到
            initDisplayTickerView()
            ui?.tvNumber?.setText(PonkoApp.signInfo?.scores.toString(), true)
        }

    }

    /**
     * 初始化数字滚动组件
     */
    private fun initDisplayTickerView() {
        ui?.tvNumber?.setCharacterList(TickerUtils.getDefaultNumberList())
        ui?.tvNumber?.textColor = R.color.colorPrimary
        ui?.tvNumber?.textSize = 100f
        ui?.tvNumber?.animationDuration = 1000
        ui?.tvNumber?.typeface = Typeface.DEFAULT
        //tickerView?.animationInterpolator = OvershootInterpolator()
        ui?.tvNumber?.gravity = Gravity.BOTTOM
        ui?.tvNumber?.setText("0")
    }

    /**
     * UI
     */
    private class ViewHolder private constructor(val clSign: ConstraintLayout, val tvSignDay: TextView, val tvNumber: TickerView, val ivSign: ImageView, val view7daySign: Xm7DaySignView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val clSign = rootView.findViewById<View>(R.id.cl_sign) as ConstraintLayout
                val tvSignDay = rootView.findViewById<View>(R.id.tv_sign_day) as TextView
                val tvNumber = rootView.findViewById<View>(R.id.tv_number) as TickerView
                val ivSign = rootView.findViewById<View>(R.id.iv_sign) as ImageView
                val view7daySign = rootView.findViewById<View>(R.id.view_7day_sign) as Xm7DaySignView
                return ViewHolder(clSign, tvSignDay, tvNumber, ivSign, view7daySign)
            }
        }
    }

}
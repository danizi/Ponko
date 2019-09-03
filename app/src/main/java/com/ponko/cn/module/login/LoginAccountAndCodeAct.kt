package com.ponko.cn.module.login

import android.app.Activity
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.constraint.Guideline
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.login.contract.LoginAccountAndCodeContract
import com.xm.lib.common.base.adapter.BaseVpAdapter

/**
 * 账号 & 短信方式登录
 */
class LoginAccountAndCodeAct : PonkoBaseAct<LoginAccountAndCodeContract.P>(), LoginAccountAndCodeContract.V {

    private var ui: UI? = null

    override fun presenter(): LoginAccountAndCodeContract.P {
        return LoginAccountAndCodeContract.P(this, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_account_and_code
    }

    override fun findViews() {
        super.findViews()
        if (ui == null) {
            ui = UI.create(this)
        }
    }

    override fun iniEvent() {
        super.iniEvent()

        ui?.flClose?.setOnClickListener {
            p?.clickClose()
        }

        ui?.tvFindPwd?.setOnClickListener {
            p?.clickFindPwd()
        }
    }

    override fun iniData() {
        super.iniData()
        ui?.vp?.adapter = BaseVpAdapter.Builder()
                .addTab("账号登录", LoginByAccountFrg())
                .addTab("短信登录", LoginByCodeFrg())
                .setFragmentManager(supportFragmentManager).builder()
        ui?.tl?.setupWithViewPager(ui?.vp, false)
        ui?.tl?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                for (i in 0..1) {
                    val tab = ui?.tl?.getTabAt(i)
                    val itemView = tab?.view as ViewGroup
                    val tv = itemView.getChildAt(1) as TextView
                    tv.textSize = 29f// 这里设置文字大小是无效的
                    tv.gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
                    itemView.setPadding(20, 0, 20, 0)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ui?.tl?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }

    private class UI private constructor(val constraintLayout: ConstraintLayout, val flClose: FrameLayout, val tvFindPwd: TextView, val tl: TabLayout, val vp: ViewPager, val guideline3: Guideline, val guideline6: Guideline) {
        companion object {

            fun create(rootView: Activity): UI {
                val constraintLayout = rootView.findViewById<View>(R.id.constraintLayout) as ConstraintLayout
                val flClose = rootView.findViewById<View>(R.id.fl_close) as FrameLayout
                val tvFindPwd = rootView.findViewById<View>(R.id.tv_find_pwd) as TextView
                val tl = rootView.findViewById<View>(R.id.tl) as TabLayout
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                val guideline3 = rootView.findViewById<View>(R.id.guideline3) as Guideline
                val guideline6 = rootView.findViewById<View>(R.id.guideline6) as Guideline
                return UI(constraintLayout, flClose, tvFindPwd, tl, vp, guideline3, guideline6)
            }
        }
    }
}

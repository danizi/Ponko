package com.ponko.cn

import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.common.base.BaseActivity
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.StatusBarUtil

class GuideAct : BaseActivity() {

    companion object {
        const val TAG = "GuideAct"
    }

    private var vp: ViewPager? = null
    private val array = arrayOf(
            R.mipmap.guide_01,
            R.mipmap.guide_02,
            R.mipmap.guide_03,
            R.mipmap.guide_04,
            R.mipmap.guide_05,
            R.mipmap.guide_06
    )

    override fun setContentViewBefore() {
        // 判断用户是否第一次启动app
        if (!TextUtils.isEmpty(CacheUtil.getJoinGuide())) {
            joinStartActivity()
            finish()
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_guide
    }


    override fun findViews() {
        vp = findViewById(R.id.vp)
    }

    override fun initDisplay() {
        vp?.isClickable = false
        // 小米手机设置状态栏白色，显示的字体也是白色所以需要加上如下代码进行设置，才会字显示黑色
        StatusBarUtil.StatusBarLightMode(this)
    }

    override fun iniData() {
        vp?.adapter = object : PagerAdapter() {
            override fun isViewFromObject(p0: View, p1: Any): Boolean {
                return p0 == p1
            }

            override fun getCount(): Int {
                return array.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val iv = ImageView(this@GuideAct)
                iv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                iv.setImageResource(array[position])
                iv.scaleType = ImageView.ScaleType.FIT_XY
                if (iv.parent != null) {
                    (iv.parent as ViewGroup).removeView(iv)
                }
                if (position == array.size - 1) {
                    iv.setOnClickListener {
                        //進入启动界面
                        joinStartActivity()
                        BKLog.d(TAG, "進入启动界面")
                    }
                }
                container.addView(iv)
                return iv
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                //super.destroyItem(container, position, `object`)
                container.removeView(`object` as View)
            }
        }
    }

    override fun iniEvent() {
    }

    private fun joinStartActivity() {
        ActivityUtil.startActivity(this, Intent(this, StartAct::class.java))
        CacheUtil.putJoinGuide("true")
        finish()
    }
}

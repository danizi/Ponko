package com.ponko.cn.module.guide

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ponko.cn.R
import com.xm.lib.common.base.BaseActivity

class GuideAct : BaseActivity() {

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

    }

    override fun getLayouId(): Int {
        return R.layout.activity_guide
    }


    override fun findViews() {
        vp = findViewById(R.id.vp)
    }

    override fun initDisplay() {
        vp?.isClickable = false
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
                return iv
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                //super.destroyItem(container, position, `object`)
                container.removeView(`object` as View)
            }
        }
    }

    override fun iniEvent() {
        vp?.setOnClickListener {
            //進入主界面
        }
        vp?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                vp?.isClickable = p0 == (array.size - 1)
            }
        })
    }
}

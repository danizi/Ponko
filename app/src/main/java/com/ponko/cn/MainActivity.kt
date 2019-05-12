package com.ponko.cn

import android.view.KeyEvent
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.constant.Constant.TOKEN
import com.ponko.cn.module.free.FreeFrg
import com.ponko.cn.module.interflow.frg.InterflowFrg
import com.ponko.cn.module.my.MyFrg
import com.ponko.cn.module.study.StudyFrg
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.common.base.BaseActivity
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.BottomMenu
import com.xm.lib.component.OnItemClickListener


/**
 *
 */
class MainActivity : BaseActivity() {
    companion object {
        private val TAG = "MainActivity"
        lateinit var bottomMenu: BottomMenu
    }



    override fun setContentViewBefore() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun findViews() {
        bottomMenu = findViewById(R.id.bottom_menu)
    }

    override fun initDisplay() {
        bottomMenu.select(0)
                .setContainer(R.id.container)
                .setTitleColor(R.color.grey, R.color.red)
                .setItemLayoutId(R.layout.item_bottom_menu)
                .addItem(StudyFrg(), "学习", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
                .addItem(FreeFrg(), "免费", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
                .addItem(InterflowFrg(), "交流", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
                .addItem(MyFrg(), "我的", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
                .setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(view: BottomMenu, pos: Int) {
                        BKLog.d(TAG, "pos:$pos")
                    }
                })
                .build()
    }

    override fun iniData() {
        //2019-05-12 10:57:37.142 14041-14041/com.ponko.cn D/XmLib: token:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IllRSmtrSVJZcitDMkl5aVZ0TTg5bXJVbTZIODhsOGw1ZEtzcXBpaHV2VzFoZHQ5V1ZJV1l1cWlISnp3YjJTQnRlbVJuUmZ5UHRHcXVsWTlrb3VqY21BPT0iLCJwaG9uZSI6IjE1MDc0NzcwNzA4IiwiaWQiOiI2NTc4M2IxNWQ0NzcxMWU4OGI0NDAyNDJhYzEzMDAwMyIsInRva2VuIjoiYzc5M2NlZTkxOTAyNGJlNjljMjNjY2E3MTkzYmRmNjQifQ.MdoLdsQvqIeGn2TswSPtMC5pImp_cXaMi26-Ev6Rjig
        //ogHau0f97BAJNmuu2tpqaolHKYTk
        TOKEN = CacheUtil.getToken()!!
        PonkoApp.retrofitClient?.headers?.put("x-tradestudy-access-token", TOKEN)
        BKLog.d("token:$TOKEN")
    }

    override fun iniEvent() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}

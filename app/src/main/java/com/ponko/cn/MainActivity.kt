package com.ponko.cn

import android.view.KeyEvent
import com.ponko.cn.module.free.FreeFrg
import com.ponko.cn.module.interflow.frg.InterflowFrg
import com.ponko.cn.module.my.MyFrg
import com.ponko.cn.module.study.StudyFrg
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
    }

    private lateinit var bottomMenu: BottomMenu

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

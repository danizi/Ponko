package com.ponko.cn.module.common

import android.os.Bundle
import android.view.View
import com.ponko.cn.utils.AnimUtil
import com.xm.lib.common.base.mvp.MvpActivity
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.Utils
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper

abstract class PonkoBaseAct<P> : MvpActivity<P>(), SwipeBackActivityBase {

    /**
     * 滑动退出
     */
    private var mHelper: SwipeBackActivityHelper? = null

    override fun setContentViewBefore() {
        mHelper = SwipeBackActivityHelper(this)
        mHelper?.onActivityCreate()
    }

    override fun findViews() {}

    override fun initDisplay() {}

    override fun iniData() {}

    override fun iniEvent() {}


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper?.onPostCreate()
    }

    override fun <T : View> findViewById(id: Int): T? {
        val v = super.findViewById<View>(id)
        return if (v == null && mHelper != null) mHelper?.findViewById(id) as T else v as T
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mHelper?.swipeBackLayout!!
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout.setEnableGesture(enable)
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        swipeBackLayout.scrollToFinishActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        mHelper = null
        AnimUtil.cancelAll()
    }
}
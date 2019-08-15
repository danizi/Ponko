package com.ponko.cn.module.my.option

import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp.Companion.UI_DEBUG
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.constract.SettingContract
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.base.rv.v1.BaseRvAdapter

/**
 * 应用相关设置
 */
class SettingActivity : RefreshLoadAct<SettingContract.Present, List<String>>() {

    override fun initDisplay() {
        disableLoad = true
        disableRefresh = true
        addItemDecoration = false
        super.initDisplay()
        BarUtil.addBar1(this, viewHolder?.toolbar, "设置", "", null)
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(SettingContract.V.SettingItemViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_setting)
        )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        val data = ArrayList<String>()
        data.add("视频分辨率")
        if (UI_DEBUG) {
            data.add("学习页面新旧版开关")
            data.add("日志开关")
            data.add("跳转测试开关")
        }
        requestRefreshSuccess(data)
    }

    override fun multiTypeData(body: List<String>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): SettingContract.Present {
        return SettingContract.Present()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_setting)
//    }
}

package com.ponko.cn.module.my.option

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ponko.cn.R
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.module.common.RefreshLoadAct
import com.xm.lib.common.base.rv.BaseRvAdapter

/**
 * 应用相关设置
 */
class SettingActivity : RefreshLoadAct<Any, List<String>>() {

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(),
                arrayOf(),
                arrayOf(),
                arrayOf()
        )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun multiTypeData(body: List<String>?): List<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_setting)
//    }
}

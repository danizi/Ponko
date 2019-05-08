package com.ponko.cn.module.my.option

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ponko.cn.R
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.module.common.RefreshLoadFrg
import com.xm.lib.common.base.rv.BaseRvAdapter

class RemindAct : RefreshLoadFrg<Any,Any>() {
    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestMoreApi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestRefreshApi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun multiTypeData(body: Any?): List<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun adapter(): BaseRvAdapter? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun presenter(): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_remind)
//    }
}

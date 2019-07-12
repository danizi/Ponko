package com.ponko.cn.module.my.v1.option.store

import android.support.v7.widget.DividerItemDecoration
import com.ponko.cn.R
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.InternalCourseCBean
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.my.v1.constract.IntegralExchangedClassContract
import com.ponko.cn.module.my.v1.holder.ExchangedClassVH
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.decoration.MyDividerItemDecoration

/**
 * 已兑课程
 */
class IntegralExchangedClassAct : RefreshLoadAct<IntegralExchangedClassContract.P, MutableList<InternalCourseCBean>>(), IntegralExchangedClassContract.V {

    override fun initDisplay() {
        disableRefresh = true
        disableLoad = true
        addItemDecoration = false
        viewHolder?.rv?.addItemDecoration(MyDividerItemDecoration.divider(this, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
        super.initDisplay()
        addBar1("已兑课程")
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(ExchangedClassVH::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_course_introduction))
    }

    override fun requestMoreApi() {

    }

    override fun requestRefreshApi() {
        p?.requestExchangedCourseApi()
    }

    override fun requestExchangedCourseApiSuccess(body: MutableList<InternalCourseCBean>?) {
        requestRefreshSuccess(body)
    }

    override fun requestExchangedCourseApiFailure() {
        requestRefreshFailure()
    }

    override fun multiTypeData(body: MutableList<InternalCourseCBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): IntegralExchangedClassContract.P {
        return IntegralExchangedClassContract.P(this, this)
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_integral_exchanged_class)
//    }
}

package com.ponko.cn.module.common.pay

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import com.ponko.cn.R
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.base.mvp.MvpActivity
import com.xm.lib.common.base.rv.decoration.MyDividerItemDecoration
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2

/**
 * 支付页面
 */
class PayAct : MvpActivity<PayContract.P>(), PayContract.V {

    private var toolbar: Toolbar? = null
    private var rvAdapter: BaseRvAdapterV2? = null
    private var srl: SwipeRefreshLayout? = null
    private var rv: RecyclerView? = null
    private var btnPay: Button? = null
    private var stateView: com.xm.lib.component.XmStateView? = null

    override fun presenter(): PayContract.P {
        return PayContract.P(this, this)
    }

    override fun setContentViewBefore() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_pay
    }

    override fun findViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        srl = findViewById<View>(R.id.srl) as SwipeRefreshLayout
        rv = findViewById<View>(R.id.rv) as RecyclerView
        btnPay = findViewById<View>(R.id.btn_pay) as Button
        stateView = findViewById<View>(R.id.view_state) as com.xm.lib.component.XmStateView
    }

    override fun initDisplay() {
        stateView?.visibility = View.GONE
        BarUtil.addBar2(this, toolbar, "安全支付", "", View.OnClickListener { })
        rv?.layoutManager = LinearLayoutManager(this)
        rv?.addItemDecoration(MyDividerItemDecoration.divider(this, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder))
    }

    override fun iniData() {
        p?.requestPayApi()
    }

    override fun iniEvent() {
        btnPay?.setOnClickListener {
            p?.clickPay()
        }
        srl?.setOnRefreshListener {
            p?.refresh()
            srl?.isRefreshing = false
        }
    }

    override fun setAdapter(datas: ArrayList<Any>) {
        rvAdapter = BaseRvAdapterV2.Builder()
                .addDataResouce(datas)
                .addHolderFactory(ItemPayLessonVH.Factory())
                .addHolderFactory(ItemPayRightsVH.Factory())
                .addHolderFactory(ItemPaymentVH.Factory(object : ItemPaymentVH.IPaymentListener {
                    override fun select(type: Int) {
                        p?.paymentSelect(type)
                    }
                }))
                .addDataResouce(datas)
                .build()
        rv?.adapter = rvAdapter
    }

    override fun requestPayApiSuccess(data: ArrayList<Any>) {
        //stateView?.showLoading("正在加载中...")
    }

    override fun requestPayApiFailure(msg: String?) {
        stateView?.showError(msg!!, View.OnClickListener {
            p?.requestPayApi()
        })
    }
}

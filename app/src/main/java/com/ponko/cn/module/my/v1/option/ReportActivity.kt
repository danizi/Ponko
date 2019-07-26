package com.ponko.cn.module.my.v1.option

import android.graphics.Color
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp.Companion.storeProfileBean
import com.ponko.cn.bean.StudyReportCBean
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.my.v1.constract.ReportContract
import com.ponko.cn.module.web.WebAct
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.decoration.MyDividerItemDecoration
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil


/**
 * 学习报告页面
 */
class ReportActivity : PonkoBaseAct<ReportContract.P>(), ReportContract.V {
    /**
     * 标题栏
     */
    private var toolbar: Toolbar? = null
    /**
     * 滑动组件
     */
    private var nsv: NestedScrollView? = null
    /**
     * 刷新组件
     */
    private var srl: SwipeRefreshLayout? = null
    /**
     * 内容组件
     */
    private var rv: RecyclerView? = null
    /**
     * 当前时间
     */
    private var tvDate: TextView? = null
    /**
     * 生成报告按钮
     */
    private var btnReport: Button? = null

    override fun presenter(): ReportContract.P {
        return ReportContract.P(this, this)
    }

    override fun setContentViewBefore() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_report
    }

    override fun findViews() {
        nsv = findViewById(R.id.nsv)
        toolbar = findViewById(R.id.toolbar)
        srl = findViewById(R.id.srl)
        rv = findViewById(R.id.rv)
        tvDate = findViewById(R.id.tv_date)
        btnReport = findViewById(R.id.btn_report)
    }

    override fun initDisplay() {
        //顶部栏颜色
        toolbar?.setBackgroundColor(Color.parseColor("#41434F"))

        //系统栏颜色
        com.jaeger.library.StatusBarUtil.setColor(this, Color.parseColor("#41434F"), 0)

    }

    override fun iniData() {
        p?.requestReportApi()
    }

    override fun iniEvent() {
        srl?.setOnRefreshListener {
            p?.refresh()
        }
        btnReport?.setOnClickListener {
            p?.intoPosterPage()
        }
    }

    override fun displayReportPage(body: StudyReportCBean?, rvAdapter: BaseRvAdapter?) {
        //设置标题栏
        if (!TextUtils.isEmpty(body?.title)) {
            BarUtil.addWhiteBar(this, toolbar, body?.title!!, "规则", View.OnClickListener {
                BKLog.d("点击规则")
                if (!TextUtils.isEmpty(body.rule)) {
                    WebAct.start(this, "url", body.rule)
                } else {
                    ToastUtil.show("跳转规则页面失败,请联系帮课大学。")
                }
            })
        }

        if (rvAdapter != null) {
            rv?.adapter = rvAdapter
            rv?.layoutManager = LinearLayoutManager(this)
            rv?.addItemDecoration(MyDividerItemDecoration.divider(this, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder))
        }

        //显示当前日期
        tvDate?.text = body?.subtitle
    }

    override fun refreshSuccess(rvAdapter: BaseRvAdapter?) {
        rvAdapter?.notifyDataSetChanged()
        srl?.isRefreshing = false
    }

    override fun refreshFailure() {
        srl?.isRefreshing = false
    }
}

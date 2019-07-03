package com.ponko.cn.module.my.option.store

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.module.web.WebAct
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.StoreProfileBean
import com.ponko.cn.bean.StoreProfileCMoreBean
import com.ponko.cn.bean.StoreTaskBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.constant.Constants.ACTION_SIGN_SUCCESS
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.my.holder.MyBookViewHolder
import com.ponko.cn.module.my.holder.MyCourseViewHolder
import com.ponko.cn.utils.*
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.component.CircleImageView
import com.xm.lib.component.XmStateView
import com.xm.lib.media.broadcast.BroadcastManager
import retrofit2.Call
import retrofit2.Response

/**
 * 积分商城首页
 */
class StoreAct : PonkoBaseAct<Any>() {
    /**
     * 窗口UI
     */
    private var viewHolder: ViewHolder? = null
    /**
     * 积分商城接口信息
     */
    private var storeProfileBean: StoreProfileBean? = null
    /**
     * 广播管理器
     */
    private var broadcastManager: BroadcastManager? = null
    /**
     * 刷新广播
     */
    private var refreshBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_SIGN_SUCCESS) {
                BKLog.d("用户签到成功，刷新积分商城页面")
                iniData()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broadcastManager = BroadcastManager.create(this)
        broadcastManager?.registerReceiver(ACTION_SIGN_SUCCESS, refreshBroadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcastManager?.unRegisterReceiver(refreshBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        iniData()
    }

    override fun presenter(): Any {
        return Any()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_store
    }

    override fun findViews() {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
    }

    override fun initDisplay() {
        //顶部栏
        BarUtil.addWhiteBar(this, viewHolder?.toolbar, "积分商城", "规则", View.OnClickListener {
            BKLog.d("点击规则")
            WebAct.start(this, "url", storeProfileBean?.score_rule)
        })
        //顶部栏颜色
        viewHolder?.toolbar?.setBackgroundColor(Color.parseColor("#FF41434E"))

        //系统栏颜色
        com.jaeger.library.StatusBarUtil.setColor(this, Color.parseColor("#FF41434E"), 0)

    }

    override fun iniData() {
        //请求积分任务接口，查询是否签到成功
        PonkoApp.myApi?.tasks()?.enqueue(object : HttpCallBack<StoreTaskBean>() {
            override fun onSuccess(call: Call<StoreTaskBean>?, response: Response<StoreTaskBean>?) {
                val storeTaskBean = response?.body()
                PonkoApp.signInfo = storeTaskBean

                //是否在赚积分显示提示图标和动画
                if (PonkoApp.signInfo?.isCompleted != true) {
                    viewHolder?.ivSign?.visibility = View.VISIBLE
                    AnimUtil.shakeAnim(viewHolder?.ivSign)
                } else {
                    viewHolder?.ivSign?.visibility = View.GONE
                    AnimUtil.cancel(viewHolder?.ivSign)
                }
            }
        })


        //请求首页接口
        DialogUtil.showProcess(this)
        PonkoApp.myApi?.home()?.enqueue(object : HttpCallBack<StoreProfileBean>() {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(call: Call<StoreProfileBean>?, response: Response<StoreProfileBean>?) {
                storeProfileBean = response?.body()
                Glide.with(this@StoreAct, storeProfileBean?.avatar, viewHolder?.ivHead, Constants.LOAD_IMAGE_DELAY)
                viewHolder?.tvNick?.text = storeProfileBean?.name
                viewHolder?.tvPayType?.text = storeProfileBean?.paid
                viewHolder?.tvIntegralNum?.text = "${storeProfileBean?.score}积分"

                if (frgs.isEmpty()) {
                    //frgs.clear()
                    val titls = ArrayList<String>()
                    var count = 1
                    for (list in storeProfileBean?.list?.iterator()!!) {
                        viewHolder?.tb?.addTab(viewHolder?.tb?.newTab()?.setText(list.name)!!)
                        frgs.add(ExchangeFrg.create(count++, list.name/*, viewHolder?.vp, viewHolder?.sv*/))
                        titls.add(list.name)
                    }

                    if (count < 6) {
                        viewHolder?.tb?.layoutParams?.width = ScreenUtil.dip2px(this@StoreAct, 80) * (count - 1)
                    } else {
                        viewHolder?.tb?.tabMode = TabLayout.MODE_SCROLLABLE
                    }
                    viewHolder?.vp?.adapter = Adapter(supportFragmentManager, frgs, titls)
                    viewHolder?.tb?.setupWithViewPager(viewHolder?.vp)
                }
                DialogUtil.hideProcess()
            }

            override fun onFailure(call: Call<StoreProfileBean>?, msg: String?) {
                super.onFailure(call, msg)
                DialogUtil.hideProcess()
                viewHolder?.viewState?.showError("请求数据错误，请检查您的网络", View.OnClickListener {})
            }
        })
    }

    var frgs = ArrayList<Fragment>()
    var indexPage = 0
    override fun iniEvent() {
        viewHolder?.vp?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                indexPage = p0
                //在刷新一次页面
                viewHolder?.vp?.requestLayout()

            }
        })
        viewHolder?.sv?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { p0, p1, p2, p3, p4 ->
            // ps:最好做一次滑动停止判断
            //if (isFinishing) {
            val view = viewHolder?.sv?.getChildAt(0)
            if (view?.height!! <= viewHolder?.sv?.scrollY!! + viewHolder?.sv?.height!!) {
                BKLog.d("滑动到底部")
                (frgs[indexPage] as ExchangeFrg).reqeustExchangeMoreApi(viewHolder?.vp)
            } else if (viewHolder?.sv?.scrollY == 0) {
                BKLog.d("滑动到顶部")
                (frgs[indexPage] as ExchangeFrg).reqeustExchangeRefreshApi(viewHolder?.vp)
            }
            //}
        })
        viewHolder?.ivHead?.setOnClickListener {
            BKLog.d("点击头像")
            IntoTargetUtil.target(this, "head", "")
        }
        viewHolder?.llIntegral?.setOnClickListener {
            BKLog.d("点击赚积分")
            ActivityUtil.startActivity(this, Intent(this, IntegralTaskActivity::class.java))
        }
        viewHolder?.llExchange?.setOnClickListener {
            BKLog.d("点击兑换记录")
            ActivityUtil.startActivity(this, Intent(this, IntegralExchangedAct::class.java))
        }
        viewHolder?.llRank?.setOnClickListener {
            BKLog.d("点击积分排行榜")
            ActivityUtil.startActivity(this, Intent(this, IntegralRankingActivity::class.java))
        }
        viewHolder?.llRecord?.setOnClickListener {
            BKLog.d("点击获取记录")
            ActivityUtil.startActivity(this, Intent(this, IntegralRecordActivity::class.java))
        }
    }

    /**
     * 积分商城UI
     */
    private class ViewHolder private constructor(val sv: NestedScrollView, val toolbar: Toolbar, val clInfo: ConstraintLayout, val container: ConstraintLayout, val ivHead: CircleImageView, val tvNick: TextView, val tvPayType: TextView, val tvIntegralNum: TextView, val llObtainLog: LinearLayout, val llRecord: LinearLayout, val clAction: ConstraintLayout, val llIntegral: LinearLayout, val ivIntegral: AppCompatImageView, val tvIntegral: TextView, val ivSign: ImageView, val llExchange: LinearLayout, val ivExchange: AppCompatImageView, val tvExchange: TextView, val llRank: LinearLayout, val ivRank: AppCompatImageView, val tvRank: TextView, val tb: TabLayout, val vp: ViewPager, val viewState: XmStateView) {
        companion object {

            fun create(act: AppCompatActivity): ViewHolder {
                val sv = act.findViewById<View>(R.id.sv) as NestedScrollView
                val toolbar = act.findViewById<View>(R.id.toolbar) as Toolbar
                val clInfo = act.findViewById<View>(R.id.cl_info) as ConstraintLayout
                val container = act.findViewById<View>(R.id.container) as ConstraintLayout
                val ivHead = act.findViewById<View>(R.id.iv_head) as CircleImageView
                val tvNick = act.findViewById<View>(R.id.tv_nick) as TextView
                val tvPayType = act.findViewById<View>(R.id.tv_pay_type) as TextView
                val tvIntegralNum = act.findViewById<View>(R.id.tv_integral_num) as TextView
                val llObtainLog = act.findViewById<View>(R.id.ll_obtain_log) as LinearLayout
                val llRecord = act.findViewById<View>(R.id.ll_record) as LinearLayout
                val clAction = act.findViewById<View>(R.id.cl_action) as ConstraintLayout
                val llIntegral = act.findViewById<View>(R.id.ll_integral) as LinearLayout
                val ivIntegral = act.findViewById<View>(R.id.iv_integral) as AppCompatImageView
                val tvIntegral = act.findViewById<View>(R.id.tv_integral) as TextView
                val ivSign = act.findViewById<View>(R.id.iv_sign) as ImageView
                val llExchange = act.findViewById<View>(R.id.ll_exchange) as LinearLayout
                val ivExchange = act.findViewById<View>(R.id.iv_exchange) as AppCompatImageView
                val tvExchange = act.findViewById<View>(R.id.tv_exchange) as TextView
                val llRank = act.findViewById<View>(R.id.ll_rank) as LinearLayout
                val ivRank = act.findViewById<View>(R.id.iv_rank) as AppCompatImageView
                val tvRank = act.findViewById<View>(R.id.tv_rank) as TextView
                val tb = act.findViewById<View>(R.id.tb) as TabLayout
                val vp = act.findViewById<View>(R.id.vp) as ViewPager
                val viewState = act.findViewById<View>(R.id.view_state) as XmStateView
                return ViewHolder(sv, toolbar, clInfo, container, ivHead, tvNick, tvPayType, tvIntegralNum, llObtainLog, llRecord, clAction, llIntegral, ivIntegral, tvIntegral, ivSign, llExchange, ivExchange, tvExchange, llRank, ivRank, tvRank, tb, vp, viewState)
            }
        }
    }

    /**
     * ViewPager Fragment契约类页面
     */
    class ExchangeContract {
        interface V {
            fun reqeustExchangeRefreshApiSuccess(body: ArrayList<StoreProfileCMoreBean>?)
            fun reqeustExchangeMoreApiSuccess(body: ArrayList<StoreProfileCMoreBean>?)
        }

        class M {
            /**
             * 请求积分兑换数据
             */
            fun reqeustExchangeRefreshApi(cid: String, callback: HttpCallBack<ArrayList<StoreProfileCMoreBean>>) {
                reqeustExchangeMoreApi(cid, 1, callback)

            }

            /**
             * 请求积分兑换更多数据
             */
            fun reqeustExchangeMoreApi(cid: String, page: Int, callback: HttpCallBack<ArrayList<StoreProfileCMoreBean>>) {
                PonkoApp.myApi?.homeMore(cid, page)?.enqueue(callback)
            }
        }

        class Present(private val v: V?) {
            /**
             * 上拉加载开关
             */
            private val isDebug = PonkoApp.UI_DEBUG
            private val m = M()

            fun reqeustExchangeRefreshApi(cid: String) {
                m.reqeustExchangeRefreshApi(cid, object : HttpCallBack<ArrayList<StoreProfileCMoreBean>>() {
                    override fun onSuccess(call: Call<ArrayList<StoreProfileCMoreBean>>?, response: Response<ArrayList<StoreProfileCMoreBean>>?) {
                        v?.reqeustExchangeRefreshApiSuccess(response?.body())
                    }
                })
            }

            fun reqeustExchangeMoreApi(cid: String, page: Int) {
                if (isDebug) {
                    val body = ArrayList<StoreProfileCMoreBean>()
                    val testBean = StoreProfileCMoreBean()
                    testBean.id = 2
                    testBean.name = "测试-课程"
                    testBean.layout = "bar"
                    testBean.sort = 2
                    testBean.stores = ArrayList<StoreProfileCMoreBean.StoresBean>()
                    for (i in 0..9) {
                        val storesBean = StoreProfileCMoreBean.StoresBean()
                        storesBean.id = "47975b0c6f4e11e9b5c00242ac130004"
                        storesBean.name = "测试-帮课大学特训营第7期-$i"
                        storesBean.type = "COMMODITY"
                        storesBean.scores = 400
                        storesBean.picture = "http://cdn.tradestudy.cn/upload/product/20190506/a78e5a61aec6a4beca0c30387a759b2c.jpg"
                        testBean.stores.add(storesBean)
                    }
                    body.add(testBean)
                    v?.reqeustExchangeMoreApiSuccess(body)
                } else {
                    m.reqeustExchangeMoreApi(cid, page, object : HttpCallBack<ArrayList<StoreProfileCMoreBean>>() {
                        override fun onSuccess(call: Call<ArrayList<StoreProfileCMoreBean>>?, response: Response<ArrayList<StoreProfileCMoreBean>>?) {
                            val data = response?.body()
                            if (data != null && !data.isEmpty()) {
                                v?.reqeustExchangeMoreApiSuccess(response.body())

                            }
                        }
                    })
                }
            }
        }
    }

    /**
     * ViewPager Fragment页面
     */
    @SuppressLint("ValidFragment")
    open class ExchangeFrg : MvpFragment<ExchangeContract.Present>(), ExchangeContract.V {

        companion object {
            /**
             * @param cid   课程id、 一般是1 2 3....
             * @param type  列表的类型 一种线性一种网格
             * @param vp    viewpager
             * @param sv
             */
            fun create(cid: Int, type: String/*, vp: ViewPager?, sv: NestedScrollView?*/): ExchangeFrg {
                val fragment = ExchangeFrg()
                val bundle = Bundle()
                bundle.putString("cid", cid.toString())
                bundle.putString("type", type) //暂时提供两种类型列表 书籍和课程
                fragment.arguments = bundle
                return fragment
            }
        }

        private var page: Int = 1
        private var rv: RecyclerView? = null
        private var type: String = "书籍"
        private var cid: String = ""
        private var adapter = object : BaseRvAdapter() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                BKLog.d("StoreAct", "onCreateViewHolder viewType:$viewType")
                return super.onCreateViewHolder(parent, viewType)
            }

            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                BKLog.d("StoreAct", "onBindViewHolder holder:$holder position:$position")
            }

        }

        override fun getLayoutId(): Int {
            return R.layout.frg_rv
        }

        override fun initDisplay() {

        }

        override fun findViews(view: View) {
            rv = view.findViewById(R.id.rv)
        }

        override fun iniEvent() {
            //RecycerView加载更多加这些配置，viewpager第二个页面就会刷不出数据
            rv?.isFocusableInTouchMode = false
//            rv?.isNestedScrollingEnabled = false
//            rv?.requestFocus()
            rv?.setHasFixedSize(true)
        }

        override fun iniData() {
            cid = arguments?.getString("cid")!!
            type = arguments?.getString("type")!!
            p?.reqeustExchangeRefreshApi(cid)
        }

        private var vp: ViewPager? = null
        fun reqeustExchangeRefreshApi(vp: ViewPager?) {
            p?.reqeustExchangeRefreshApi(cid)
            this.vp = vp
        }

        fun reqeustExchangeMoreApi(vp: ViewPager?) {
            p?.reqeustExchangeMoreApi(cid, ++page)
            this.vp = vp
        }

        override fun reqeustExchangeRefreshApiSuccess(body: ArrayList<StoreProfileCMoreBean>?) {
            adapter.data?.clear()
            adapter.data?.addAll(body!![0].stores!!)
            when (body!![0].layout) {
                "bar" -> {
                    adapter.addItemViewDelegate(0, MyCourseViewHolder::class.java, Any::class.java, R.layout.item_my_store_course)
                    val linearLayoutManager = LinearLayoutManager(context)
                    linearLayoutManager.isSmoothScrollbarEnabled = true
                    linearLayoutManager.isAutoMeasureEnabled = true
                    rv?.layoutManager = linearLayoutManager

                }
                else -> {
                    adapter.addItemViewDelegate(0, MyBookViewHolder::class.java, Any::class.java, R.layout.item_my_store_book)
                    val gridLayoutManager = GridLayoutManager(context, 2)
                    gridLayoutManager.isSmoothScrollbarEnabled = true
                    gridLayoutManager.isAutoMeasureEnabled = true
                    rv?.layoutManager = gridLayoutManager
                }
            }
            rv?.adapter = adapter
            this.vp?.requestLayout()
        }

        override fun reqeustExchangeMoreApiSuccess(body: ArrayList<StoreProfileCMoreBean>?) {
            val positionStart = adapter.data?.size
            val itemCount = body!![0].stores!!.size
            adapter.data?.addAll(body[0].stores!!)
            adapter.notifyItemRangeInserted(positionStart!!, itemCount)
            adapter.notifyItemChanged(positionStart, itemCount)
//            adapter.notifyDataSetChanged()
            this.vp?.requestLayout()
        }

        override fun presenter(): ExchangeContract.Present {
            return ExchangeContract.Present(this)
        }
    }

    /**
     * ViewPager适配器
     */
    open class Adapter(fm: FragmentManager, val frgs: ArrayList<Fragment>, val title: ArrayList<String>) : FragmentPagerAdapter(fm) {

        override fun getItem(p0: Int): Fragment {
            return frgs[p0]
        }

        override fun getCount(): Int {
            return frgs.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return title[position]
        }
    }
}

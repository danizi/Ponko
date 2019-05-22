package com.ponko.cn.module.my.option.store

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.WebAct
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.StoreProfileBean
import com.ponko.cn.bean.StoreProfileCMoreBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.my.holder.MyBookViewHolder
import com.ponko.cn.module.my.holder.MyCourseViewHolder
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.AnimUtil
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import de.hdodenhof.circleimageview.CircleImageView
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

        //
        if (PonkoApp.signInfo?.isCompleted != true) {
            viewHolder?.ivSign?.visibility = View.VISIBLE
            AnimUtil.shakeAnim(viewHolder?.ivSign)
        } else {
            viewHolder?.ivSign?.visibility = View.GONE
            AnimUtil.cancel(viewHolder?.ivSign)
        }
    }

    override fun iniData() {
        PonkoApp.myApi?.home()?.enqueue(object : HttpCallBack<StoreProfileBean>() {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(call: Call<StoreProfileBean>?, response: Response<StoreProfileBean>?) {
                storeProfileBean = response?.body()
                Glide.with(baseContext, storeProfileBean?.avatar, viewHolder?.ivHead)
                viewHolder?.tvNick?.text = storeProfileBean?.name
                viewHolder?.tvPayType?.text = storeProfileBean?.paid
                viewHolder?.tvIntegralNum?.text = "${storeProfileBean?.score}积分"
                val frgs = ArrayList<Fragment>()
                val titls = ArrayList<String>()
                var count = 1
                for (list in storeProfileBean?.list?.iterator()!!) {
                    viewHolder?.tb?.addTab(viewHolder?.tb?.newTab()?.setText(list.name)!!)
                    frgs.add(ExchangeFrg.create(count++, list.name, viewHolder?.vp))
                    titls.add(list.name)
                }
                viewHolder?.vp?.adapter = Adapter(supportFragmentManager, frgs, titls)
                viewHolder?.tb?.setupWithViewPager(viewHolder?.vp)
            }
        })
    }

    override fun iniEvent() {
        viewHolder?.vp?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {

            }
        })

        viewHolder?.llIntegral?.setOnClickListener {
            BKLog.d("点击赚积分")
            ActivityUtil.startActivity(this, Intent(this, IntegralTaskActivity::class.java))
        }
        viewHolder?.llExchange?.setOnClickListener {
            BKLog.d("点击兑换记录")
            ActivityUtil.startActivity(this, Intent(this, IntegralExchangedAct::class.java))
        }
        viewHolder?.llRank?.setOnClickListener {
            BKLog.d("点击积分排行版")
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
    private class ViewHolder private constructor(val toolbar: Toolbar, val clInfo: ConstraintLayout, val container: ConstraintLayout, val ivHead: CircleImageView, val tvNick: TextView, val tvPayType: TextView, val tvIntegralNum: TextView, val llObtainLog: LinearLayout, val llRecord: LinearLayout, val clAction: ConstraintLayout, val llIntegral: LinearLayout, val ivIntegral: AppCompatImageView, val tvIntegral: TextView, val ivSign: ImageView, val llExchange: LinearLayout, val ivExchange: AppCompatImageView, val tvExchange: TextView, val llRank: LinearLayout, val ivRank: AppCompatImageView, val tvRank: TextView, val tb: TabLayout, val vp: ViewPager) {
        companion object {

            fun create(act: AppCompatActivity): ViewHolder {
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
                return ViewHolder(toolbar, clInfo, container, ivHead, tvNick, tvPayType, tvIntegralNum, llObtainLog, llRecord, clAction, llIntegral, ivIntegral, tvIntegral, ivSign, llExchange, ivExchange, tvExchange, llRank, ivRank, tvRank, tb, vp)
            }
        }
    }

    /**
     * ViewPager Fragment页面
     */
    @SuppressLint("ValidFragment")
    open class ExchangeFrg : MvpFragment<Any>() {
        private var rv: RecyclerView? = null
        private var type: String = "书籍"
        private var cid: String = ""

        companion object {
            @SuppressLint("StaticFieldLeak")
            var viewPager: ViewPager? = null

            fun create(cid: Int, type: String, vp: ViewPager?): ExchangeFrg {
                val fragment = ExchangeFrg()
                val bundle = Bundle()
                bundle.putString("cid", cid.toString())
                bundle.putString("type", type) //暂时提供两种类型列表 书籍和课程
                fragment.arguments = bundle
                viewPager = vp
                return fragment
            }
        }

        override fun getLayoutId(): Int {
            return R.layout.frg_rv
        }

        override fun findViews(view: View) {
            rv = view.findViewById(R.id.rv)
        }

        override fun iniEvent() {
            rv?.isFocusableInTouchMode = false
            rv?.requestFocus()
        }

        override fun iniData() {
            cid = arguments?.getString("cid")!!
            type = arguments?.getString("type")!!
            when (type) {
                "书籍" -> {
                    PonkoApp.myApi?.homeMore(cid, 1)?.enqueue(object : HttpCallBack<ArrayList<StoreProfileCMoreBean>>() {
                        override fun onSuccess(call: Call<ArrayList<StoreProfileCMoreBean>>?, response: Response<ArrayList<StoreProfileCMoreBean>>?) {
                            adapter.data?.addAll(response?.body()!![0].stores!!)
                            adapter.addItemViewDelegate(0, MyBookViewHolder::class.java, Any::class.java, R.layout.item_my_store_book)
                            rv?.layoutManager = GridLayoutManager(context, 2)
                            rv?.adapter = adapter
                        }
                    })
                }
                "课程" -> {
                    PonkoApp.myApi?.homeMore(cid, 1)?.enqueue(object : HttpCallBack<ArrayList<StoreProfileCMoreBean>>() {
                        override fun onSuccess(call: Call<ArrayList<StoreProfileCMoreBean>>?, response: Response<ArrayList<StoreProfileCMoreBean>>?) {
                            adapter.data?.addAll(response?.body()!![0].stores!!)
                            adapter.addItemViewDelegate(0, MyCourseViewHolder::class.java, Any::class.java, R.layout.item_my_store_course)
                            rv?.layoutManager = LinearLayoutManager(context)
                            rv?.adapter = adapter
                        }
                    })
                }
            }
        }

        private var adapter = object : BaseRvAdapter() {}

        override fun initDisplay() {

        }

        override fun presenter(): Any {
            return Any()
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

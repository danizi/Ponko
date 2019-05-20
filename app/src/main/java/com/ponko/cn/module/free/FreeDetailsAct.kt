package com.ponko.cn.module.free

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp.Companion.freeApi
import com.ponko.cn.bean.DetailCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.study.holder.CourseSectionViewHolder
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.common.base.BaseActivity
import com.xm.lib.common.base.rv.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.media.base.XmVideoView


/**
 * 免费详情页面
 */
class FreeDetailsAct : BaseActivity() {


    companion object {
        /**
         * 进入免费详情页面
         * @param context 上下文对象
         * @param id      课程id
         */
        fun start(context: Context, id: String) {
            val intent = Intent(context, FreeDetailsAct::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }

    /**
     * 窗口ui相关控件
     */
    private var ui: ViewHolder? = null

    /**
     * 适配器
     */
    private var freeDetailFragmentPagerAdapter: FreeDetailFragmentPagerAdapter? = null

    override fun setContentViewBefore() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_free_details
    }

    override fun findViews() {
        if (ui == null) {
            ui = ViewHolder.create(this)
        }
    }

    override fun initDisplay() {

    }

    override fun iniData() {
        val id = intent.getStringExtra("id")
        freeApi?.freeDetail(id)?.enqueue(object : HttpCallBack<DetailCBean>() {
            override fun onSuccess(call: Call<DetailCBean>?, response: Response<DetailCBean>?) {
                //设置内容
                val body = response?.body()
                DetailTopBean(
                        body?.id,
                        body?.teacher,
                        body?.count,
                        body?.duration,
                        body?.course_id,
                        body?.share_title,
                        body?.share_description,
                        body?.share_url,
                        body?.pay_url,
                        body?.pay_btn,
                        body?.title
                )
                val frgs = ArrayList<Fragment>()
                WebFragment.detailContentBean = DetailContentBean(body?.summary_url, body?.chapters)
                ChaptersFragment.detailContentBean = DetailContentBean(body?.summary_url, body?.chapters)
                frgs.add(WebFragment())
                frgs.add(ChaptersFragment())
                val titles = ArrayList<String>()
                titles.add("老师介绍")
                titles.add("课程")
                ui?.vp?.adapter = FreeDetailFragmentPagerAdapter(frgs, titles, supportFragmentManager)
                ui?.tb?.setupWithViewPager(ui?.vp)
                ui?.vp?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                    }

                    override fun onPageSelected(position: Int) {
                        ui?.vp?.requestLayout()
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })
            }
        })
    }

    override fun iniEvent() {

    }

    class DetailTopBean(var id: String?, var teacher: String?, var count: Int? = 0, var duration: Int? = 0, var course_id: String?, var share_title: String?, var share_description: String?, var share_url: String?, var pay_url: String?, var pay_btn: String?, var title: String?)

    class DetailContentBean(var summary_url: String?, var chapters: List<DetailCBean.ChaptersBean>?)

    /**
     * 适配器
     */
    class FreeDetailFragmentPagerAdapter(private val frgs: List<Fragment>?, private val titles: List<String>?, private val fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(p0: Int): Fragment {
            return frgs!![p0]
        }

        override fun getCount(): Int {
            return frgs?.size!!
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles!![position]
        }
    }

    class WebFragment : Fragment() {
        companion object {
            var detailContentBean: DetailContentBean? = null
        }

        private var webView: WebView? = null
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val v = inflater.inflate(R.layout.fragment_free_detail_web, container, false)
            webView = v.findViewById(R.id.web)
            webView?.loadUrl(detailContentBean?.summary_url, heads())
            return v
        }

        private fun heads(): Map<String, String> {
            val heads = HashMap<String, String>()
            heads["x-tradestudy-client-version"] = "3.4.6"
            heads["x-tradestudy-client-device"] = "android_phone"
            heads["x-tradestudy-access-key-id"] = "c"
            heads["x-tradestudy-access-token"] = CacheUtil.getToken()!!
            return heads
        }
    }

    class ChaptersFragment : Fragment() {
        companion object {
            var detailContentBean: DetailContentBean? = null
        }

        private var rv: RecyclerView? = null

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val v = inflater.inflate(R.layout.fragment_free_detail_chapters, container, false)
            rv = v.findViewById(R.id.rv)
            val adapter = object : BaseRvAdapter() {}
            detailContentBean?.chapters
            adapter.data?.addAll(detailContentBean?.chapters!!)
            adapter.addItemViewDelegate(0, ChapterNameViewHolder::class.java, String::class.java, R.layout.item_free_catalogue_content)
            adapter.addItemViewDelegate(1, SectionsBeanViewHolder::class.java, DetailCBean.ChaptersBean::class.java, R.layout.item_free_catalogue_section)
            rv?.layoutManager = LinearLayoutManager(context)
            rv?.adapter = adapter
            return v
        }
    }

    class ChapterNameViewHolder(view: View) : BaseViewHolder(view) {
        override fun bindData(d: Any, position: Int) {

        }
    }

    class SectionsBeanViewHolder(view: View) : BaseViewHolder(view) {
        override fun bindData(d: Any, position: Int) {

        }
    }

    /**
     * 免费页面窗口ui ViewHolder
     */
    private class ViewHolder private constructor(val video: XmVideoView, val tvCourseTitle: TextView, val llDes: LinearLayout, val tvTeacher: TextView, val tvClass: TextView, val tvTime: TextView, val button2: Button, val llShareWx: LinearLayout, val llShareFriend: LinearLayout, val tb: TabLayout, val vp: ViewPager) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val video = rootView.findViewById<View>(R.id.video) as XmVideoView
                val tvCourseTitle = rootView.findViewById<View>(R.id.tv_course_title) as TextView
                val llDes = rootView.findViewById<View>(R.id.ll_des) as LinearLayout
                val tvTeacher = rootView.findViewById<View>(R.id.tv_teacher) as TextView
                val tvClass = rootView.findViewById<View>(R.id.tv_class) as TextView
                val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                val button2 = rootView.findViewById<View>(R.id.button2) as Button
                val llShareWx = rootView.findViewById<View>(R.id.ll_share_wx) as LinearLayout
                val llShareFriend = rootView.findViewById<View>(R.id.ll_share_friend) as LinearLayout
                val tb = rootView.findViewById<View>(R.id.tb) as TabLayout
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                return ViewHolder(video, tvCourseTitle, llDes, tvTeacher, tvClass, tvTime, button2, llShareWx, llShareFriend, tb, vp)
            }
        }
    }

}

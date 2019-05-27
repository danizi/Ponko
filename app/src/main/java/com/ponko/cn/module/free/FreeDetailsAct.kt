package com.ponko.cn.module.free

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.widget.*
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp.Companion.APP_ID
import com.ponko.cn.app.PonkoApp.Companion.freeApi
import com.ponko.cn.bean.DetailCBean
import com.ponko.cn.constant.Constant
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.media.AttachmentComplete
import com.ponko.cn.module.media.AttachmentGesture
import com.ponko.cn.module.media.AttachmentPre
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.IntoTargetUtil
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.xm.lib.common.base.BaseActivity
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.broadcast.BroadcastManager
import com.xm.lib.pay.wx.uikit.Constants
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
import retrofit2.Call
import retrofit2.Response


/**
 * 免费详情页面
 */
class FreeDetailsAct : BaseActivity() {

    companion object {
        private const val TAG = "FreeDetailsAct"
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
     * 附着播放器控制器
     */
    private var attachmentControl: AttachmentControl? = null
    /**
     * ViewPager适配器
     */
    //private var freeDetailFragmentPagerAdapter: FreeDetailFragmentPagerAdapter? = null
    /**
     * 详情顶部操作实体
     */
    private var detailTopBean: DetailTopBean? = null

    private var broadcastManager: BroadcastManager? = null
    private var clickFreeItemPlayReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Constant.ACTION_CLICK_FREE_PLAY_ITEM) {
                val vid = intent.getStringExtra("vid")
                val sectionName = intent.getStringExtra("sectionName")
                attachmentControl?.showLoading()
                attachmentControl?.start(vid,0,0)
                BKLog.d(TAG, "播放接受通知,播放$sectionName")
            }
        }
    }


    override fun setContentViewBefore() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (broadcastManager == null) {
            broadcastManager = BroadcastManager.create(this)
        }
        broadcastManager?.registerReceiver(Constant.ACTION_CLICK_FREE_PLAY_ITEM, clickFreeItemPlayReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcastManager?.unRegisterReceiver(clickFreeItemPlayReceiver)
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
        MediaUitl.initXmVideoView(ui?.video!!, this)
        attachmentControl = ui?.video?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl
        //initVideo(this, ui?.video!!)
    }

    override fun iniData() {
        val id = intent.getStringExtra("id")
        freeApi?.freeDetail(id)?.enqueue(object : HttpCallBack<DetailCBean>() {
            override fun onSuccess(call: Call<DetailCBean>?, response: Response<DetailCBean>?) {
                //设置内容
                val body = response?.body()

                //设置播放列表信息
                attachmentControl?.setMediaInfo(MediaUitl.buildPlayListByFree(body))

                //初始化播放器预览页面
                displayVideo(body)

                //设置内容顶部老师说明
                displayTop(body)

                //ViewPager设置 设置网页&课程
                displayContent(body)
            }
        })
    }

    fun displayVideo(body: DetailCBean?) {
        val attachmentPre = ui?.video?.getChildAt(0) as AttachmentPre
        attachmentPre.load(vid = body?.chapters!![0].sections[0].vid, preUrl = body.chapters!![0].sections[0]?.avatar!!)

//        MediaUitl.getM3u8Url(body?.chapters!![0].sections[0].vid, object : MediaUitl.OnPlayUrlListener {
//            override fun onFailure() {
//                this@FreeDetailsAct.runOnUiThread {
//                    Toast.makeText(this@FreeDetailsAct, "获取播放地址失败", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onSuccess(url: String, size: Int?) {
//                //然后请求视频地址
//                val attachmentPre = ui?.video?.getChildAt(0) as AttachmentPre
//                this@FreeDetailsAct.runOnUiThread {
//                    attachmentPre.load(url, body.chapters!![0].sections[0]?.avatar!!)
//                }
//            }
//        })
    }

    fun displayContent(body: DetailCBean?) {
        val frgs = ArrayList<Fragment>()
        WebFragment.detailContentBean = DetailContentBean(body?.summary_url, body?.chapters)
        ChaptersFragment.detailContentBean = DetailContentBean(body?.summary_url, body?.chapters)
        frgs.add(WebFragment())
        frgs.add(ChaptersFragment())
        val titles = ArrayList<String>()
        titles.add("介绍")
        titles.add("目录")
        ui?.vp?.adapter = FreeDetailFragmentPagerAdapter(frgs, titles, supportFragmentManager)
        ui?.tb?.setupWithViewPager(ui?.vp)
        ui?.vp?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                //滑动时再重新计算viewpager的大小
                ui?.vp?.requestLayout()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }


    @SuppressLint("SetTextI18n")
    fun displayTop(body: DetailCBean?) {
        detailTopBean = DetailTopBean(
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
        ui?.tvTeacher?.text = "${detailTopBean?.teacher}老师"
        ui?.tvClass?.text = "${detailTopBean?.count}节课"
        ui?.tvTime?.text = "${detailTopBean?.duration!!}分钟"
    }


    override fun iniEvent() {
        ui?.llShareFriend?.setOnClickListener {
            BKLog.d("点击微信朋友圈分享按钮")
            val wxShare = WxShare(this)
            wxShare.init(ShareConfig.Builder().appid(APP_ID).build())
            wxShare.shareWebPage(R.mipmap.ic_launcher, detailTopBean?.share_url!!, detailTopBean?.share_title!!, detailTopBean?.share_description!!, SendMessageToWX.Req.WXSceneSession)
        }
        ui?.llShareWx?.setOnClickListener {
            BKLog.d("点击微信分享按钮")
            val wxShare = WxShare(this)
            wxShare.init(ShareConfig.Builder().appid(APP_ID).build())
            wxShare.shareWebPage(R.mipmap.ic_launcher, detailTopBean?.share_url!!, detailTopBean?.share_title!!, detailTopBean?.share_description!!, SendMessageToWX.Req.WXSceneSession)
        }
        ui?.btnPay?.setOnClickListener {
            BKLog.d("点击支付按钮")
            IntoTargetUtil.target(this, "pay", detailTopBean?.pay_url)
        }
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

    /**
     * 网页fragment
     */
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

    /**
     * 章节fragment
     */
    class ChaptersFragment : Fragment() {
        companion object {
            var detailContentBean: DetailContentBean? = null
        }

        private var rv: RecyclerView? = null

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val v = inflater.inflate(R.layout.fragment_free_detail_chapters, container, false)
            rv = v.findViewById(R.id.rv)
            val adapter = object : BaseRvAdapter() {}
            for (chapter in detailContentBean?.chapters!!) {
                adapter.data?.add(chapter.chapterName!!)
                adapter.data?.addAll(chapter.sections)
            }
            adapter.addItemViewDelegate(0, ChapterNameViewHolder::class.java, String::class.java, R.layout.item_free_catalogue_content)
            adapter.addItemViewDelegate(1, SectionsBeanViewHolder::class.java, DetailCBean.ChaptersBean.SectionsBean::class.java, R.layout.item_free_catalogue_section)
            rv?.layoutManager = LinearLayoutManager(context)
            rv?.adapter = adapter
            rv?.addItemDecoration(MyItemDecoration.divider(context, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
            rv?.isFocusableInTouchMode = false
            rv?.requestFocus()

            return v
        }
    }

    /**
     * 章节标题ViewHolder
     */
    class ChapterNameViewHolder(view: View) : BaseViewHolder(view) {

        private var ui: ViewHolder? = null
        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = ViewHolder.create(itemView)
            }
            val context = itemView.context
            val title = d as String
            ui?.tv?.text = title
        }

        private class ViewHolder private constructor(val tv: TextView) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val tv = rootView.findViewById<View>(R.id.tv) as TextView
                    return ViewHolder(tv)
                }
            }
        }

    }

    /**
     * 课程内容ViewHolder
     */
    class SectionsBeanViewHolder(view: View) : BaseViewHolder(view) {
        private var ui: ViewHolder? = null
        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = ViewHolder.create(itemView)
            }
            val context = itemView.context
            val sectionsBean = d as DetailCBean.ChaptersBean.SectionsBean
            ui?.tvContent?.text = sectionsBean.sectionName
            if (sectionsBean.isFree) {
                ui?.ivLock?.visibility = View.GONE
            } else {
                ui?.ivLock?.visibility = View.VISIBLE
            }
            ui?.tvTime?.text = TimeUtil.hhmmss(sectionsBean.duration.toLong() * 1000)
            itemView.setOnClickListener {
                BKLog.d("点击播放${sectionsBean.sectionName}")
                val intent = Intent(Constant.ACTION_CLICK_FREE_PLAY_ITEM)
                intent.putExtra("vid", sectionsBean.vid)
                intent.putExtra("sectionName", sectionsBean.sectionName)
                context.sendBroadcast(intent)
            }
        }


        private class ViewHolder private constructor(val tvContent: TextView, val tvTime: TextView, val ivLock: ImageView) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val tvContent = rootView.findViewById<View>(R.id.tv_content) as TextView
                    val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                    val ivLock = rootView.findViewById<View>(R.id.iv_lock) as ImageView
                    return ViewHolder(tvContent, tvTime, ivLock)
                }
            }
        }
    }

    /**
     * 免费页面窗口ui ViewHolder
     */
    private class ViewHolder private constructor(val video: XmVideoView, val tvCourseTitle: TextView, val llDes: LinearLayout, val tvTeacher: TextView, val tvClass: TextView, val tvTime: TextView, val btnPay: Button, val llShareWx: LinearLayout, val llShareFriend: LinearLayout, val tb: TabLayout, val vp: ViewPager) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val video = rootView.findViewById<View>(R.id.video) as XmVideoView
                val tvCourseTitle = rootView.findViewById<View>(R.id.tv_course_title) as TextView
                val llDes = rootView.findViewById<View>(R.id.ll_des) as LinearLayout
                val tvTeacher = rootView.findViewById<View>(R.id.tv_teacher) as TextView
                val tvClass = rootView.findViewById<View>(R.id.tv_class) as TextView
                val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                val btnPay = rootView.findViewById<View>(R.id.btn_pay) as Button
                val llShareWx = rootView.findViewById<View>(R.id.ll_share_wx) as LinearLayout
                val llShareFriend = rootView.findViewById<View>(R.id.ll_share_friend) as LinearLayout
                val tb = rootView.findViewById<View>(R.id.tb) as TabLayout
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                return ViewHolder(video, tvCourseTitle, llDes, tvTeacher, tvClass, tvTime, btnPay, llShareWx, llShareFriend, tb, vp)
            }
        }
    }

}

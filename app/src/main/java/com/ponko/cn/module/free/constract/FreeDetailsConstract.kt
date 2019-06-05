package com.ponko.cn.module.free.constract

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.DetailCBean
import com.ponko.cn.bean.DetailContentBean
import com.ponko.cn.bean.DetailTopBean
import com.ponko.cn.bean.MediaBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.free.FreeDetailsAct
import com.ponko.cn.module.interflow.frg.SubCaseFragment
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.IntoTargetUtil
import com.ponko.cn.utils.ToastUtil
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.media.broadcast.BroadcastManager
import com.xm.lib.share.ShareConfig
import com.xm.lib.share.wx.WxShare
import retrofit2.Call
import retrofit2.Response

/**
 * 免费详情页面契约类 MVP模式
 */
class FreeDetailsConstract {
    /**
     * View层
     */
    interface V {
        /**
         * 显示播放加载
         */
        fun showPlayLoading()

        /**
         * 播放视频
         */
        fun play(vid: String?)

        /**
         * 设置列表
         */
        fun setMediaInfo(buildPlayListByFree: MediaBean)

        fun displayVideo(detailCBean: DetailCBean?)

        fun displayTop(detailTopBean: DetailTopBean?)

        fun displayContent(frgs: ArrayList<Fragment>, titles: ArrayList<String>)

        fun requestFreeDetailApiSuccess(detailCBean: DetailCBean?)

        fun requestFreeDetailApiFailure()

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
            private var adapter: BaseRvAdapter? = null
            override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                val v = inflater.inflate(R.layout.fragment_free_detail_chapters, container, false)
                rv = v.findViewById(R.id.rv)
                adapter = object : BaseRvAdapter() {}
                for (chapter in detailContentBean?.chapters!!) {
                    adapter?.data?.add(chapter.chapterName!!)
                    adapter?.data?.addAll(chapter.sections)
                }
                adapter?.addItemViewDelegate(0, ChapterNameViewHolder::class.java, String::class.java, R.layout.item_free_catalogue_content)
                adapter?.addItemViewDelegate(1, SectionsBeanViewHolder::class.java, DetailCBean.ChaptersBean.SectionsBean::class.java, R.layout.item_free_catalogue_section)
                val linearLayoutManager = LinearLayoutManager(context)
                linearLayoutManager.isSmoothScrollbarEnabled = true
                linearLayoutManager.isAutoMeasureEnabled = true
                rv?.layoutManager = linearLayoutManager
                rv?.adapter = adapter
                rv?.addItemDecoration(MyItemDecoration.divider(context, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
                rv?.isFocusableInTouchMode = false
                rv?.requestFocus()
                rv?.setHasFixedSize(true)
                rv?.isNestedScrollingEnabled = false

                return v
            }

            /**
             * 跟新第几个
             */
            fun notifyItem(position: Int) {
                val bean = adapter?.data!![position]
                if (bean is DetailCBean.ChaptersBean.SectionsBean) {
                    (adapter?.data!![position] as DetailCBean.ChaptersBean.SectionsBean).isSelect = true
                    adapter?.notifyItemChanged(position)
                }
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
                //是否显示锁图标
                isShowLock(sectionsBean)
                //是否选中
                isSelect(sectionsBean)
                //item点击监听
                initEvent(sectionsBean, position, context)
            }

            private fun initEvent(sectionsBean: DetailCBean.ChaptersBean.SectionsBean, position: Int, context: Context) {
                itemView.setOnClickListener {
                    val isB2BVip = (!PonkoApp.mainCBean?.types?.isEmpty()!! && PonkoApp.mainCBean?.types!![0].isIs_vip)
                    val isB2CVip = (!PonkoApp.mainCBean?.types?.isEmpty()!! && PonkoApp.mainCBean?.types!![1].isIs_vip)
                    if (sectionsBean.isFree || isB2BVip || isB2CVip) {
                        BKLog.d("点击播放${sectionsBean.sectionName}")
                        val intent = Intent(Constants.ACTION_CLICK_FREE_PLAY_ITEM)
                        intent.putExtra("vid", sectionsBean.vid)
                        intent.putExtra("sectionName", sectionsBean.sectionName)
                        intent.putExtra("free", sectionsBean.isFree)
                        intent.putExtra("position", position)
                        context.sendBroadcast(intent)
                    } else {
                        ToastUtil.show("请先购买课程")
                    }
                }
            }

            private fun isShowLock(sectionsBean: DetailCBean.ChaptersBean.SectionsBean) {
                val isB2BVip = (!PonkoApp.mainCBean?.types?.isEmpty()!! && PonkoApp.mainCBean?.types!![0].isIs_vip)
                val isB2CVip = (!PonkoApp.mainCBean?.types?.isEmpty()!! && PonkoApp.mainCBean?.types!![1].isIs_vip)
                if (sectionsBean.isFree || isB2BVip || isB2CVip) {
                    ui?.ivLock?.visibility = View.GONE
                } else {
                    ui?.ivLock?.visibility = View.VISIBLE
                }
            }

            private fun isSelect(sectionsBean: DetailCBean.ChaptersBean.SectionsBean) {
                if (sectionsBean.isSelect) {
                    ui?.tvContent?.isEnabled = true
                    ui?.tvTime?.isEnabled = true
                    ui?.tvContent?.text = sectionsBean.sectionName
                    ui?.tvTime?.text = TimeUtil.hhmmss(sectionsBean.duration.toLong() * 1000)
                } else {
                    ui?.tvContent?.isEnabled = false
                    ui?.tvTime?.isEnabled = false
                    ui?.tvContent?.text = sectionsBean.sectionName
                    ui?.tvTime?.text = TimeUtil.hhmmss(sectionsBean.duration.toLong() * 1000)
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
    }

    /**
     * 数据层
     */
    class M {
        /**
         * 免费课程id
         */
        var id = ""
        /**
         * 詳情頂部顯示數據bean
         */
        var detailTopBean: DetailTopBean? = null
        /**
         * 列表
         */
        var frgs: ArrayList<Fragment>? = null

        /**
         * 免费详情页面接口
         */
        fun requestFreeDetailApi(id: String, callback: HttpCallBack<DetailCBean>) {
            PonkoApp.freeApi?.freeDetail(id)?.enqueue(callback)
        }
    }

    /**
     * 控制层
     */
    class Present(val context: Context, val v: V?) {
        private val m = M()
        /**
         * 广播管理
         */
        private var broadcastManager: BroadcastManager? = null
        /**
         * 点击item广播接收器
         */
        private var clickFreeItemPlayReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Constants.ACTION_CLICK_FREE_PLAY_ITEM) {
                    val vid = intent.getStringExtra("vid")
                    val sectionName = intent.getStringExtra("sectionName")
                    val free = intent.getBooleanExtra("free", false)
                    val position = intent.getIntExtra("position", 0)
                    //显示播放加载
                    v?.showPlayLoading()
                    //点击刷新
                    clickPlayListItem(vid, 0, null, position)
                    //执行播放操作
                    v?.play(vid)
                    BKLog.d(FreeDetailsAct.TAG, "播放接受通知,播放$sectionName")
                }
            }
        }

        /**
         * 注册点击广播
         */
        fun registerClickFreeItemReceiver() {
            if (broadcastManager == null) {
                broadcastManager = BroadcastManager.create(context)
            }
            broadcastManager?.registerReceiver(Constants.ACTION_CLICK_FREE_PLAY_ITEM, clickFreeItemPlayReceiver)
        }

        /**
         * 注销点击广播
         */
        fun unRegisterClickFreeItemReceiver() {
            broadcastManager?.unRegisterReceiver(clickFreeItemPlayReceiver)
        }

        /**
         * 请求免费详情接口
         */
        fun requestFreeDetailApi() {
            m.requestFreeDetailApi(m.id, object : HttpCallBack<DetailCBean>() {
                override fun onSuccess(call: Call<DetailCBean>?, response: Response<DetailCBean>?) {
                    //设置内容
                    val body = response?.body()
                    val detailCBean = body

                    //设置播放列表信息
                    v?.setMediaInfo(MediaUitl.buildPlayListByFree(body))

                    //初始化播放器预览页面
                    v?.displayVideo(body)

                    //设置内容顶部老师说明
                    m.detailTopBean = DetailTopBean(
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
                    v?.displayTop(m.detailTopBean)

                    //ViewPager设置 设置网页&课程
                    val frgs = ArrayList<Fragment>()
                    FreeDetailsConstract.V.WebFragment.detailContentBean = DetailContentBean(detailCBean?.summary_url, detailCBean?.chapters)
                    FreeDetailsConstract.V.ChaptersFragment.detailContentBean = DetailContentBean(detailCBean?.summary_url, detailCBean?.chapters)
                    frgs.add(FreeDetailsConstract.V.WebFragment())
                    frgs.add(FreeDetailsConstract.V.ChaptersFragment())
                    m.frgs = frgs
                    val titles = ArrayList<String>()
                    titles.add("介绍")
                    titles.add("目录")
//                    ui?.vp?.adapter = FreeDetailFragmentPagerAdapter(frgs, titles, supportFragmentManager)
                    v?.displayContent(frgs, titles)
                    v?.requestFreeDetailApiSuccess(body)
                }

                override fun onFailure(call: Call<DetailCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.requestFreeDetailApiFailure()
                }
            })
        }

        /**
         * 点击分享给朋友圈
         */
        fun clickShareFriend() {
            BKLog.d("点击微信朋友圈分享按钮")
            val wxShare = WxShare(context as Activity)
            wxShare.init(ShareConfig.Builder().appid(PonkoApp.APP_ID).build())
            wxShare.shareWebPage(
                    R.mipmap.ic_launcher,
                    m.detailTopBean?.share_url!!,
                    m.detailTopBean?.share_title!!,
                    m.detailTopBean?.share_description!!,
                    SendMessageToWX.Req.WXSceneSession)
        }

        /**
         * 点击分享给朋友
         */
        fun clickShareWx() {
            BKLog.d("点击微信分享按钮")
            val wxShare = WxShare(context as Activity)
            wxShare.init(ShareConfig.Builder().appid(PonkoApp.APP_ID).build())
            wxShare.shareWebPage(
                    R.mipmap.ic_launcher,
                    m.detailTopBean?.share_url!!,
                    m.detailTopBean?.share_title!!,
                    m.detailTopBean?.share_description!!,
                    SendMessageToWX.Req.WXSceneSession)
        }

        /**
         * 点击支付
         */
        fun clickPay() {
            BKLog.d("点击支付按钮")
            IntoTargetUtil.target(
                    context,
                    "pay",
                    m.detailTopBean?.pay_url)
        }

        /**
         * 获取其他窗口传递过来的内容
         */
        fun getIntentExtras(intent: Intent?) {
            try {
                m.id = intent?.getStringExtra("id")!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * 点击了竖屏列表
         */
        fun clickPlayListItem(vid: String?, progress: Int?, view: View?, postion: Int) {
            (m.frgs!![1] as V.ChaptersFragment).notifyItem(postion)
        }
    }
}
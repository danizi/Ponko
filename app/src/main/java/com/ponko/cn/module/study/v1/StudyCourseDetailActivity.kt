package com.ponko.cn.module.study.v1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.bean.MediaBean
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.media.AttachmentPre
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.module.study.v1.constract.StudyCourseDetailContract
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.CacheUtil.getMediaBackground
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.media.attachment.OnPlayListItemClickListener
import com.xm.lib.media.broadcast.BroadcastManager

/**
 * 学习详情页面
 */
class StudyCourseDetailActivity : PonkoBaseAct<StudyCourseDetailContract.Present>(), StudyCourseDetailContract.V {


    companion object {
        const val TAG = "StudyCourseDetailActivity"
        const val TYPE_FROM_GENERAL = "from_general"
        const val TYPE_FROM_CACHE = "from_cache"
        const val TYPE_FROM_SEARCH = "from_search"

        /**
         * 点击专题中课程使用该方法
         * @param typeId   专题ID 即课程courseId
         * @param teachers 老师
         * @param num      课程总集数
         * @param duration 课程总时间 （单位：秒）
         */
        fun start(context: Context?, typeId: String?, teachers: String, num: Long?, duration: Long) {
            val intent = Intent(context, StudyCourseDetailActivity::class.java)
            intent.putExtra("type", TYPE_FROM_GENERAL)
            intent.putExtra("typeId", typeId)
            intent.putExtra("teachers", teachers)
            intent.putExtra("num", num)
            intent.putExtra("duration", duration)
            ActivityUtil.startActivity(context, intent)
        }

        /**
         * 缓存完成课程跳转使用该方法
         * @param typeId   专题ID 即课程courseId
         * @param teachers 老师
         * @param num      课程总集数
         * @param duration 课程总时间 （单位：秒）
         * @param vid      课程视频唯一标识
         */
        fun startFromCacheCourse(context: Context?, typeId: String?, teachers: String, num: Long?, duration: Long, vid: String) {
            val intent = Intent(context, StudyCourseDetailActivity::class.java)
            intent.putExtra("type", TYPE_FROM_CACHE)
            intent.putExtra("typeId", typeId)
            intent.putExtra("teachers", teachers)
            intent.putExtra("num", num)
            intent.putExtra("duration", duration)
            intent.putExtra("vid", vid)
            ActivityUtil.startActivity(context, intent)
        }

        /**
         * 搜索页面跳转使用该方法 todo 缺少老师信息,那就填空
         * @param typeId     专题ID 即课程courseId
         * @param teachers   老师
         * @param num         课程总集数
         * @param duration    课程总时间 （单位：秒）
         * @param sectionName 课程其中小节名称
         */
        fun startFromSearch(context: Context?, typeId: String?, teachers: String, num: Long?, duration: Long, sectionName: String) {
            val intent = Intent(context, StudyCourseDetailActivity::class.java)
            intent.putExtra("type", TYPE_FROM_SEARCH)
            intent.putExtra("typeId", typeId)
            intent.putExtra("teachers", teachers)
            intent.putExtra("num", num)
            intent.putExtra("duration", duration)
            intent.putExtra("sectionName", sectionName)
            ActivityUtil.startActivity(context, intent)
        }
    }

    private var coursesDetailCBean: CoursesDetailCBean? = null

    /**
     * 主窗口UI
     */
    private var viewHolder: StudyCourseDetailContract.V.ViewHolder? = null
    /**
     * 附着页面控制器
     */
    private var attachmentControl: AttachmentControl? = null

    /**
     * 广播接收器
     */
    private var broadcastManager: BroadcastManager? = null

    /**
     * 窗口创建
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p?.uploadVideoProgress(viewHolder?.video)
        AttachmentControl.isShowTop = true
    }

    /**
     * 窗口销毁
     */
    override fun onDestroy() {
        super.onDestroy()
        viewHolder?.video?.onDestroy()
        p?.closeUploadVideoProgress()
    }

    override fun onPause() {
        super.onPause()
        if (getMediaBackground() == "0") {
            viewHolder?.video?.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        viewHolder?.video?.onResume()
    }

    override fun presenter(): StudyCourseDetailContract.Present {
        return StudyCourseDetailContract.Present(context = this, v = this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_study_course_detail
    }

    override fun findViews() {
        if (viewHolder == null) {
            viewHolder = StudyCourseDetailContract.V.ViewHolder.create(this)
        }
    }

    override fun initDisplay() {
        super.initDisplay()
        //系统栏颜色
        com.jaeger.library.StatusBarUtil.setColor(this, this.resources.getColor(R.color.black), 0)
        //按比例设置播放器View
        val present = 1f / 1.8f
        val layoutParams = viewHolder?.video?.layoutParams
        layoutParams?.height = ((ScreenUtil.getNormalWH(this)[0]) * present).toInt()
        viewHolder?.video?.layoutParams = layoutParams
    }

    override fun iniEvent() {
        super.iniEvent()
        viewHolder?.ivColect?.setOnClickListener {
            p?.clickCollect()
        }
        viewHolder?.ivDown?.setOnClickListener {
            p?.clickDown()
        }
        viewHolder?.ivShare?.setOnClickListener {
            p?.clickShare()
        }
        viewHolder?.expandList?.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            attachmentControl?.index = p?.twoToOne(coursesDetailCBean, groupPosition, childPosition)!!
            p?.clickExpandListItem(parent, v, groupPosition, childPosition, id)!!
        }

        attachmentControl?.setOnPlayListItemClickListener(object : OnPlayListItemClickListener {
            override fun item(vid: String?, progress: Int?, view: View?, postion: Int) {
                p?.clickPlayListItem(vid, progress, view, postion)
                //暂停播放
                attachmentControl?.pause()
                attachmentControl?.showLoading()

                //更新下横屏列表item
                attachmentControl?.updateListItem(postion)

                //更新下竖屏列表item
                val (groupPosition, childPosition) = p?.oneToTwo(postion)!!
                p?.updateExtendableListItem(groupPosition, childPosition)
                BKLog.d("选中groupPosition:$groupPosition childPosition:$childPosition vid$vid")

                //设置标题
                setTitle(coursesDetailCBean?.chapters!![groupPosition].sections[childPosition].name)

                //播放视频
                attachmentControl?.start(vid!!, progress, postion)
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ScreenUtil.isLandscape(this)) {
                attachmentControl?.addPortraitView()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun iniData() {
        super.iniData()
        //获取跳转信息
        p?.getIntentExtra(intent)

        //初始化播放器，以及附着页面
        MediaUitl.initXmVideoView(viewHolder?.video, this@StudyCourseDetailActivity)
        attachmentControl = viewHolder?.video?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl

        //请求课程接口
        p?.requestCourseDetailApi()
    }

    override fun requestCourseDetailApiFailure() {
        viewHolder?.xmStateView?.showError("加载数据错误...", object : View.OnClickListener {
            override fun onClick(v: View?) {
                p?.requestCourseDetailApi()
            }
        })
    }

    override fun classNotFound() {
        viewHolder?.xmStateView?.showNoData("课程不存在...")
    }

    override fun hideStateView() {
        viewHolder?.xmStateView?.hide()
    }

    override fun setVideoControlConfigInfo(coursesDetailCBean: CoursesDetailCBean?) {
        //设置配置信息
        attachmentControl?.setConfigInfo(MediaBean.ConfigBean())
    }

    override fun setVideoControlShareInfo(coursesDetailCBean: CoursesDetailCBean?) {
        //设置分享信息，默认状态
        val shareUrl = coursesDetailCBean?.share_base_url!! + "/" + coursesDetailCBean.chapters!![0].sections[0].id
        val shareTitle = coursesDetailCBean.title
        val shareDes = coursesDetailCBean.features
        attachmentControl?.setShareInfo(MediaBean.ShareBean(shareUrl, shareTitle, shareDes))
    }

    override fun setVideoControlShareInfo(shareUrl: String, shareTitle: String?, shareDes: String?) {
        //设置分享信息,点击不同的list item 分享信息是不同的
        attachmentControl?.setShareInfo(MediaBean.ShareBean(shareUrl, shareTitle, shareDes))
        attachmentControl?.showLoading()
    }

    override fun setVideoControlPlayListInfo(coursesDetailCBean: CoursesDetailCBean?) {
        this.coursesDetailCBean = coursesDetailCBean
        attachmentControl?.setMediaInfo(MediaUitl.buildPlayListByStudy(coursesDetailCBean))
    }

    override fun setVideoPre(coursesDetailCBean: CoursesDetailCBean?) {
        val attachmentPre = viewHolder?.video?.getChildAt(0) as AttachmentPre
//        val avatar = coursesDetailCBean?.chapters!![0].sections[0].avatar
//        val vid = coursesDetailCBean.chapters!![0].sections[0].vid  //PS 游客模式vid获取不到
        var pos = 0
        var avatar = ""
        var vid = ""
        var flag = false
        for (chapters in coursesDetailCBean?.chapters!!) {
            if (flag) break
            for (section in chapters.sections) {
                if (section.isFree || coursesDetailCBean.isPossess) {
                    avatar = section.avatar
                    vid = section.vid
                    flag = true
                    pos = section.progress_duration * 1000
                    break
                }
            }
        }
        val isPay = if (flag) {
            true
        } else {
            coursesDetailCBean.isPossess
        }
        attachmentPre.load(vid = vid, preUrl = avatar, isPay = isPay, pos = pos)      //todo 窗口有销毁的可能
    }

    override fun displayVideoExtendableList(myExtendableListAdp: StudyCourseDetailContract.V.MyExtendableListViewAdapter?) {
        viewHolder?.expandList?.setAdapter(myExtendableListAdp)
        for (i in 0..(myExtendableListAdp?.child?.size!! - 1)) {
            viewHolder?.expandList?.expandGroup(i)
        }
    }

    override fun start(coursesDetailCBean: CoursesDetailCBean, vid: String, groupPosition: Int, childPosition: Int) {
        val duration = coursesDetailCBean.chapters[groupPosition].sections[childPosition].duration.toInt() * 1000
        val progress = coursesDetailCBean.chapters[groupPosition].sections[childPosition].progress_duration * 1000
        val pos = p?.twoToOne(coursesDetailCBean, groupPosition, childPosition)
        BKLog.d("二维转一维数组-> 下标pos:$pos")
        attachmentControl?.start(vid, progress, pos!!)
    }

    override fun setTitle(title: String?) {
        attachmentControl?.setTitle(title!!)
    }

    override fun showCollectIcon() {
        viewHolder?.ivColect?.setImageResource(R.mipmap.class_collection_p)
    }

    override fun hideCollectIcon() {
        viewHolder?.ivColect?.setImageResource(R.mipmap.class_collection_n)
    }
}

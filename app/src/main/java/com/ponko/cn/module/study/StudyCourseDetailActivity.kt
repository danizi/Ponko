package com.ponko.cn.module.study

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ponko.cn.R
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.bean.MediaBean
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.media.AttachmentPre
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.module.study.constract.StudyCourseDetailContract
import com.ponko.cn.utils.ActivityUtil
import com.xm.lib.common.log.BKLog
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
        /**
         * 点击专题中课程使用该方法
         * @param typeId   专题ID
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
         * @param typeId   专题ID
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
         * 搜索页面跳转使用该方法 todo 缺失专题信息啊 缺少老师信息
         */
        fun startFromSearch(context: Context?) {

        }
    }

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
    }

    /**
     * 窗口销毁
     */
    override fun onDestroy() {
        super.onDestroy()
        viewHolder?.video?.onDestroy()
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
        com.jaeger.library.StatusBarUtil.setColor(this, this.resources.getColor(R.color.black), 0) //系统栏颜色
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
            p?.clickExpandListItem(parent, v, groupPosition, childPosition, id)!!
        }

        attachmentControl?.setOnPlayListItemClickListener(object : OnPlayListItemClickListener {
            override fun item(view: View, postion: Int) {
                BKLog.d("横屏状态点击了播放列表:$postion")
                //更新一下
                attachmentControl?.refreshItem(postion)
            }
        })
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
        attachmentControl?.setMediaInfo(MediaUitl.buildPlayListByStudy(coursesDetailCBean))
    }

    override fun setVideoPre(coursesDetailCBean: CoursesDetailCBean?) {
        val attachmentPre = viewHolder?.video?.getChildAt(0) as AttachmentPre
        val vid = coursesDetailCBean?.chapters!![0].sections[0].vid  //PS 游客模式vid获取不到
        attachmentPre.load(vid = vid, preUrl = coursesDetailCBean.image)   //todo 窗口有销毁的可能
    }

    override fun displayVideoExtendableList(myExtendableListAdp: StudyCourseDetailContract.V.MyExtendableListViewAdapter?) {
        viewHolder?.expandList?.setAdapter(myExtendableListAdp)
        for (i in 0..(myExtendableListAdp?.child?.size!! - 1)) {
            viewHolder?.expandList?.expandGroup(i)
        }
    }

    override fun start(coursesDetailCBean: CoursesDetailCBean, vid: String, groupPosition: Int, childPosition: Int) {
        val progress = coursesDetailCBean.chapters[groupPosition].sections[childPosition].progress_duration * 1000
        var pos = 0
        for ((count, chapters) in coursesDetailCBean.chapters.withIndex()) {
            if (count < groupPosition) {
                pos += chapters.sections.size
            } else {
                break
            }
        }
        pos += childPosition
        BKLog.d("二维转一维数组-> 下标pos:$pos")
        attachmentControl?.start(vid, progress, pos)
    }

    private fun oneToTwo() {

    }

    private fun twoToOne() {

    }
}

package com.ponko.cn.module.study

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.media.AttachmentComplete
import com.ponko.cn.module.media.AttachmentGesture
import com.ponko.cn.module.media.AttachmentPre
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.utils.ActivityUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.base.XmVideoView
import retrofit2.Call
import retrofit2.Response

class StudyCourseDetailActivity : AppCompatActivity() {

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
    }

    private var viewHolder: ViewHolder? = null
    private var courseInfo: CoursesDetailCBean? = null
    private var vid = ""
    private var type = ""
    private var typeId = ""
    private var teachers = ""
    private var num = 0L
    private var duration = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_course_detail)
        findViews()
        initEvent()
        initData(this@StudyCourseDetailActivity, viewHolder?.video!!)
    }

    private fun initEvent() {
        viewHolder?.ivColect?.setOnClickListener {
            BKLog.d("点击收藏")
        }
        viewHolder?.ivDown?.setOnClickListener {
            BKLog.d("点击下载缓存页面")
            CacheActivity.start(this, typeId, teachers, num, duration)
        }
        viewHolder?.ivShare?.setOnClickListener {
            BKLog.d("点击分享")
        }
    }

    private fun findViews() {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
    }

    private fun initData(context: Context?, xmVideoView: XmVideoView) {

        //接受的信息，需要转发到缓存页面中。
        type = intent.getStringExtra("type")
        typeId = intent.getStringExtra("typeId")
        teachers = intent.getStringExtra("teachers")
        num = intent.getLongExtra("num", 0L)
        duration = intent.getLongExtra("duration", 0L)
        when (type) {
            TYPE_FROM_CACHE -> {
                vid = intent.getStringExtra("vid")
                BKLog.d(TAG, "点击了“已”缓存课程跳转过来")
            }
            TYPE_FROM_CACHE -> {
                BKLog.d(TAG, "点击了“未”缓存课程跳转过来")
            }
        }
        BKLog.d("专题id:$typeId 老师$teachers 集数$num 总时长${duration / 60f / 60f}小时")

        //绑定的页面
        val preUrl = ""
        val playUrl = ""
        val attachmentPre = AttachmentPre(context, preUrl)
        attachmentPre.url = playUrl
        val attachmentControl = AttachmentControl(context)
        val attachmentGesture = AttachmentGesture(context)
        val attachmentComplete = AttachmentComplete(context)
        xmVideoView.bindAttachmentView(attachmentPre)      //预览附着页面
        xmVideoView.bindAttachmentView(attachmentControl)  //控制器附着页面
        xmVideoView.bindAttachmentView(attachmentGesture)  //手势附着页面(调节亮度和音量)
        xmVideoView.bindAttachmentView(attachmentComplete) //播放完成附着页面
        //播放器回调观察者
        xmVideoView.addPlayerObserver(attachmentPre)
        xmVideoView.addPlayerObserver(attachmentControl)
        xmVideoView.addPlayerObserver(attachmentGesture)
        xmVideoView.addPlayerObserver(attachmentComplete)
        //手势观察者
        xmVideoView.addGestureObserver(attachmentPre)
        xmVideoView.addGestureObserver(attachmentControl)
        xmVideoView.addGestureObserver(attachmentGesture)
        xmVideoView.addGestureObserver(attachmentComplete)
        //各种状态（断网、音量、电话、插上耳机、电量...）观察者
        xmVideoView.addPhoneStateObserver(attachmentPre)
        xmVideoView.addPhoneStateObserver(attachmentControl)
        xmVideoView.addPhoneStateObserver(attachmentGesture)
        xmVideoView.addPhoneStateObserver(attachmentComplete)

        //请求专题信息接口
        PonkoApp.studyApi?.getCourseDetail(typeId)?.enqueue(object : HttpCallBack<CoursesDetailCBean>() {
            override fun onSuccess(call: Call<CoursesDetailCBean>?, response: Response<CoursesDetailCBean>?) {
                //专题下课程信息保存
                courseInfo = response?.body()

                //设置预览界面
                setPre(getVid())

                //设置控制界面，传递视频列表到横向控制界面
                setControl()

                //视频列表展示设置
            }

            fun setControl() {
                for (i in 0..(viewHolder?.video?.childCount!! - 1)) {
                    if (viewHolder?.video?.getChildAt(i) is AttachmentControl) {
                        (viewHolder?.video?.getChildAt(i) as AttachmentControl).courseDetail = courseInfo
                        break
                    }
                }
            }

            fun setPre(vid: String?) {
                MediaUitl.getM3u8Url(vid, object : MediaUitl.OnPlayUrlListener {
                    override fun onFailure() {
                        Toast.makeText(this@StudyCourseDetailActivity, "获取播放地址失败 - ", Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(url: String,size:Int?) {
                        //然后请求视频地址
                        val attachmentPre = viewHolder?.video?.getChildAt(0) as AttachmentPre
                        this@StudyCourseDetailActivity.runOnUiThread {
                            attachmentPre.load(url, courseInfo?.image!!)
                        }
                    }
                })
//                if (PonkoApp.courseDao?.exist(vid) == true) {
//                    val courses = PonkoApp.courseDao?.select(vid)
//                    if (courses.isNullOrEmpty() && courses?.size == 1 && !TextUtils.isEmpty(courses[0].column_down_path)) {
//                        val cacheM3u8 = courses[0].column_down_path
//                        BKLog.d(TAG, "播放缓存视频 : $cacheM3u8 ")
//                        //然后请求视频地址
//                        val attachmentPre = viewHolder?.video?.getChildAt(0) as AttachmentPre
//                        attachmentPre.load(cacheM3u8, courseInfo?.image!!)
//                    }
//                } else {
//                    MediaUitl.getUrlByVid(vid, PonkoApp.mainCBean?.polyv?.user_id!!, PonkoApp.mainCBean?.polyv?.secret_key!!, object : MediaUitl.OnVideoInfoListener {
//                        override fun onFailure() {
//                            BKLog.d(TAG, "通过vid${vid}获取m3u8地址失败")
//                            Toast.makeText(context, "请检查您的网络", Toast.LENGTH_SHORT).show()
//                        }
//
//                        override fun onSuccess(videoInfo: VideoInfoCBean) {
//                            BKLog.d(TAG, "通过vid${vid}获取m3u8地址成功${videoInfo.toString()}")
//                            val m3u8 = videoInfo.data[0].hls[3]
//                            BKLog.d(TAG, "播放网络视频 : $m3u8 ")
//
//                            //然后请求视频地址
//                            val attachmentPre = viewHolder?.video?.getChildAt(0) as AttachmentPre
//                            attachmentPre.load(m3u8, courseInfo?.image!!)
//                        }
//                    })
//                }
            }

            fun getVid(): String? {
                val playUrl = if (TextUtils.isEmpty(vid)) {
                    courseInfo?.chapters!![0].sections[0].vid  //PS 游客模式vid获取不到
                } else {
                    vid
                }
                BKLog.d("vid:$playUrl")
                return playUrl
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewHolder?.video!!.onDestroy()//销毁播放器
    }

    /**
     * 课程详情播放页面ViewHolder
     */
    private class ViewHolder private constructor(val video: XmVideoView, val constraintLayout5: ConstraintLayout, val ivShare: ImageView, val ivColect: ImageView, val ivDown: ImageView, val rv: RecyclerView) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val video = rootView.findViewById<View>(R.id.video) as XmVideoView
                val constraintLayout5 = rootView.findViewById<View>(R.id.constraintLayout5) as ConstraintLayout
                val ivShare = rootView.findViewById<View>(R.id.iv_share) as ImageView
                val ivColect = rootView.findViewById<View>(R.id.iv_colect) as ImageView
                val ivDown = rootView.findViewById<View>(R.id.iv_down) as ImageView
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return ViewHolder(video, constraintLayout5, ivShare, ivColect, ivDown, rv)
            }
        }
    }
}

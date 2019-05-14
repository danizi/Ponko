package com.ponko.cn.module.study

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.media.AttachmentComplete
import com.ponko.cn.module.media.AttachmentGesture
import com.ponko.cn.module.media.AttachmentPre
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.utils.ActivityUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.base.XmVideoView
import retrofit2.Call
import retrofit2.Response

class StudyCourseDetailActivity : AppCompatActivity() {

    companion object {
        /**
         * @param typeId 专题ID
         * @param teachers 老师
         * @param num 课程总集数
         * @param duration 课程总时间 （单位：秒）
         */
        fun start(context: Context?, typeId: String?, teachers: String, num: Long?, duration: Long) {
            val intent = Intent(context, StudyCourseDetailActivity::class.java)
            intent.putExtra("typeId", typeId)
            intent.putExtra("teachers", teachers)
            intent.putExtra("num", num)
            intent.putExtra("duration", duration)
            ActivityUtil.startActivity(context, intent)
        }
    }

    private var viewHolder: ViewHolder? = null
    private var courseInfo: CoursesDetailCBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_course_detail)

        //接受的信息，需要转发到缓存页面中。
        val typeId = intent.getStringExtra("typeId")
        val teachers = intent.getStringExtra("teachers")
        val num = intent.getLongExtra("num", 0L)
        val duration = intent.getLongExtra("duration", 0L)
        BKLog.d("专题id:$typeId 老师$teachers 集数$num 总时长${duration / 60f / 60f}小时")

        //findViews
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }

        //initEvent
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

        val preUrl = ""
        val playUrl = ""
        initData(this@StudyCourseDetailActivity, viewHolder?.video!!, preUrl, playUrl)

        //initData
        PonkoApp.studyApi?.getCourseDetail(typeId)?.enqueue(object : HttpCallBack<CoursesDetailCBean>() {
            override fun onSuccess(call: Call<CoursesDetailCBean>?, response: Response<CoursesDetailCBean>?) {
                courseInfo = response?.body()
                //预览页面
                val playUrl = courseInfo?.chapters!![0].sections[0].hls1
                BKLog.d("playUrl:$playUrl")
                val attachmentPre = viewHolder?.video?.getChildAt(0) as AttachmentPre
                attachmentPre.load(playUrl, courseInfo?.image!!)

                //查看控制界面
                for (i in 0..(viewHolder?.video?.childCount!! - 1)) {
                    if (viewHolder?.video?.getChildAt(i) is AttachmentControl) {
                        (viewHolder?.video?.getChildAt(i) as AttachmentControl).courseDetail = courseInfo
                        break
                    }
                }

                //视频列表展示设置
            }
        })
    }

    private fun initData(context: Context?, xmVideoView: XmVideoView, preUrl: String?, playUrl: String?) {
        //绑定的页面
        val attachmentPre = AttachmentPre(context, preUrl!!)
        attachmentPre.url = playUrl!!
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
    }

    override fun onDestroy() {
        super.onDestroy()
    }

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

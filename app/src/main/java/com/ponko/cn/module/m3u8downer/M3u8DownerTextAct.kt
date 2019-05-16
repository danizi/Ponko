package com.ponko.cn.module.m3u8downer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.m3u8DownManager
import com.ponko.cn.bean.VideoInfoCBean
import com.ponko.cn.db.bean.CourseDbBean
import com.ponko.cn.db.dao.CourseDao
import com.ponko.cn.module.m3u8downer.core.M3u8DownTask
import com.ponko.cn.module.m3u8downer.core.M3u8Utils
import com.ponko.cn.module.m3u8downer.core.OnDownListener
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.study.StudyCourseDetailActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.utils.FileUtil
import java.io.File

class M3u8DownerTextAct : AppCompatActivity() {

    companion object {
        private const val TAG = "M3u8DownerTextAct"
        private const val UPDATE_PROCESS = 1
        private const val UPDATE_COMPLETE = 2
        private const val UPDATE_STATE = 3
        private const val DOWN_STATE_START = "下载准备中...."
        private const val DOWN_STATE_COMPLETE = "下载完成"
        private const val DOWN_STATE_PROCESS = "下载中..."
        private const val DOWN_STATE_ERROR = "下载错误"
        private const val DOWN_STATE_PAUSE = "暂停"

        private var value_typeId = ""
        private var value_teachers = ""
        private var value_num = 0L
        private var value_duration = 0L

        fun start(context: Context, typeId: String, teachers: String, num: Long, duration: Long) {
            val intent = Intent(context, M3u8DownerTextAct::class.java)
            value_typeId = typeId
            value_teachers = teachers
            value_num = num
            value_duration = duration
            context.startActivity(intent)
        }
    }

    private var rv: RecyclerView? = null
    private var adapter: BaseRvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_m3u8_downer_text)
        //获取需要下载的课程
        rv = findViewById<RecyclerView>(R.id.rv)

        //设置rv
        //val dao = CourseDao(PonkoApp.dbHelp?.writableDatabase)
        val dao = PonkoApp.courseDao!!
        val datas = setRv(dao)

        //检查权限
        checkPermission()

        //开始下载
        down(datas)
    }

    private fun setRv(dao: CourseDao): ArrayList<CourseDbBean> {
        val datas = dao.selectAll()
        adapter = object : BaseRvAdapter() {}
        adapter?.data?.addAll(datas)
        adapter?.addItemViewDelegate(0, M3u8ViewHolder::class.java, CourseDbBean::class.java, R.layout.item_down_m3u8)
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(this)
        return datas
    }

    @SuppressLint("CheckResult")
    private fun checkPermission() {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { aBoolean ->
                    if (aBoolean!!) {
                        //当所有权限都允许之后，返回true
                        Toast.makeText(this, "文件授权成功", Toast.LENGTH_SHORT).show()

                    } else {
                        //只要有一个权限禁止，返回false，
                        //下一次申请只申请没通过申请的权限
                        BKLog.d("permissions", "btn_more_sametime：$aBoolean")
                    }
                }
    }


    fun down(datas: ArrayList<CourseDbBean>) {
        for (course in datas.iterator()) {

            val vid = course.column_vid
            MediaUitl.getUrlByVid(vid, PonkoApp.mainCBean?.polyv?.user_id!!, PonkoApp.mainCBean?.polyv?.secret_key!!, object : MediaUitl.OnVideoInfoListener {
                override fun onFailure() {
                    BKLog.d(TAG, "通过vid${vid}获取m3u8地址失败")
                    Toast.makeText(this@M3u8DownerTextAct, "请检查您的网络", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(videoInfo: VideoInfoCBean) {
                    BKLog.d(TAG, "通过vid${vid}获取m3u8地址成功${videoInfo.toString()}")
                    val m3u8 = videoInfo.data[0].hls[3]
                    //存入数据库
                    PonkoApp.courseDao?.update(vid)

                    val m3u8DownTask = M3u8DownTask.Builder()
                            .m3u8(course.column_m3u8_url)
                            .name(course.column_title)
                            .fileSize(course.column_total.toLong())
                            .build()
                    m3u8DownManager?.newTasker(m3u8DownTask)?.enqueue(null)
                }
            })

        }

        m3u8DownManager?.listener = object : OnDownListener {
            override fun onStart(url: String, m3u8Analysis: ArrayList<String>) {
                BKLog.d(TAG, "M3u8DownTasker 下载准备中....")
                val courseDbBean = CourseDbBean()
                courseDbBean.column_state = DOWN_STATE_START
                updateRv(url, courseDbBean, UPDATE_STATE)
            }

            override fun onComplete(url: String) {
                BKLog.d(TAG, "M3u8DownTasker $url 下载完成")
                val courseDbBean = CourseDbBean()
                courseDbBean.column_complete = 1
                courseDbBean.column_state = DOWN_STATE_COMPLETE
                updateRv(url, courseDbBean, UPDATE_COMPLETE)
                //updataUIComplete(url)

                val cacheM3u8 = PonkoApp.m3u8DownManager?.path + File.separator + PonkoApp.m3u8DownManager?.dir + File.separator + M3u8Utils.m3u8Unique(url) +File.separator+ M3u8Utils.m3u8FileName(url)
                PonkoApp.courseDao?.update(url,cacheM3u8,1)

            }

            override fun onProcess(url: String, progress: Int) {
                BKLog.d(TAG, "M3u8DownTasker $url 下载进度")
                val courseDbBean = CourseDbBean()
                courseDbBean.column_progress = progress
                courseDbBean.column_state = DOWN_STATE_PROCESS
                updateRv(url, courseDbBean, UPDATE_PROCESS)
                //updateUIProcess(url, progress.toLong())
            }

            override fun onError(url: String, msg: String) {
                BKLog.d(TAG, "M3u8DownTasker下载错误")
                val courseDbBean = CourseDbBean()
                courseDbBean.column_state = DOWN_STATE_ERROR
                updateRv(url, courseDbBean, UPDATE_STATE)
            }
        }
    }

    /**
     * 更新Rv界面
     */
    private fun updateRv(m3u8: String, value: CourseDbBean?, type: Int) {
        var progressIndex = -1
        for (i in 0..(adapter?.data?.size!! - 1)) {
            val courseDbBean = adapter?.data!![i] as CourseDbBean
            if (m3u8 == courseDbBean.column_m3u8_url) {
                progressIndex = i
                when (type) {
                    UPDATE_PROCESS -> {
                        courseDbBean.column_progress = value?.column_progress!!
                        courseDbBean.column_state = value.column_state
                    }
                    UPDATE_COMPLETE -> {
                        courseDbBean.column_complete = value?.column_complete!! //1
                        courseDbBean.column_state = value.column_state
                    }
                    UPDATE_STATE -> {
                        courseDbBean.column_state = value?.column_state!!
                    }
                }
                break
            }
        }
        this.runOnUiThread {
            adapter?.notifyItemChanged(progressIndex)
        }
    }

    /**
     * 下载任务页面
     */
    class M3u8ViewHolder(view: View) : BaseViewHolder(view) {
        private var viewHolder: ViewHolder? = null

        @SuppressLint("SetTextI18n")
        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }
            val courseDbBean = d as CourseDbBean
            val context = itemView.context

            //设置界面内容
            progress(courseDbBean)                                     //下载修改“进度提示字”和“进度条”
            viewHolder?.tvCourseName?.text = courseDbBean.column_title //任务名称
            when (courseDbBean.column_complete) {
                0 -> {
                    viewHolder?.tvState?.text = courseDbBean.column_state
                }
                1 -> {
                    viewHolder?.tvState?.text = DOWN_STATE_COMPLETE    //下载状态提示字
                }
            }

            //监听
            itemView.setOnClickListener {
                BKLog.d(TAG, "点击下载任务item")
                when (courseDbBean.column_complete) {
                    0 -> {
                        notDownComplete(courseDbBean)          //未完成点击处理
                    }
                    1 -> {
                        downComplete(context, courseDbBean)    //完成点击处理
                    }
                }
            }
        }

        private fun downComplete(context: Context, courseDbBean: CourseDbBean) {
            StudyCourseDetailActivity.startFromCacheCourse(context, value_typeId, value_teachers, value_num, value_duration, courseDbBean.column_vid)
        }

        private fun notDownComplete(courseDbBean: CourseDbBean) {
            if (m3u8DownManager?.isRun(courseDbBean.column_m3u8_url) == true) {
                m3u8DownManager?.pause(courseDbBean.column_m3u8_url)
                viewHolder?.tvState?.text = DOWN_STATE_PAUSE     //点击暂停后
            } else {
                m3u8DownManager?.resume(
                        M3u8DownTask.Builder()
                                .name(courseDbBean.column_title)
                                .m3u8(courseDbBean.column_m3u8_url)
                                .fileSize(courseDbBean.column_total.toLong())
                                .build())
                viewHolder?.tvState?.text = DOWN_STATE_PROCESS
            }
        }

        @SuppressLint("SetTextI18n")
        fun progress(courseDbBean: CourseDbBean) {
            viewHolder?.pb?.max = courseDbBean.column_total//进度条最大值设置
            val total = FileUtil.getSizeUnit(courseDbBean.column_total.toLong())
            val progress = FileUtil.getSizeUnit(courseDbBean.column_progress.toLong())
            if (courseDbBean.column_complete == 1) {
                //完成
                //viewHolder?.tvState?.text = "下载完成"
                viewHolder?.pb?.progress = courseDbBean.column_total
                viewHolder?.tvProcessTotal?.text = "$total | $total"
            } else {
                //未完成
                //viewHolder?.tvState?.text = "下载中"
                viewHolder?.pb?.progress = courseDbBean.column_progress
                viewHolder?.tvProcessTotal?.text = "$progress | $total"
            }
        }

        private class ViewHolder private constructor(val imageView5: ImageView, val tvCourseName: TextView, val pb: ProgressBar, val tvState: TextView, val tvProcessTotal: TextView) {
            companion object {
                fun create(rootView: View): ViewHolder {
                    val imageView5 = rootView.findViewById<View>(R.id.imageView5) as ImageView
                    val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                    val pb = rootView.findViewById<View>(R.id.pb) as ProgressBar
                    val tvState = rootView.findViewById<View>(R.id.tv_state) as TextView
                    val tvProcessTotal = rootView.findViewById<View>(R.id.tv_process_total) as TextView
                    return ViewHolder(imageView5, tvCourseName, pb, tvState, tvProcessTotal)
                }
            }
        }
    }
}

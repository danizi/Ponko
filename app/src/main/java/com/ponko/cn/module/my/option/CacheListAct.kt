package com.ponko.cn.module.my.option

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.m3u8DownManager
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.VideoInfoCBean
import com.ponko.cn.db.bean.CourseDbBean
import com.ponko.cn.module.common.RefreshLoadAct
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

/**
 * 缓存列表
 * 分两种情况处理：
 *               1 如果用户在专题
 */
class CacheListAct : RefreshLoadAct<Any, ArrayList<CourseDbBean>>() {
    companion object {
        const val TAG = "CacheListAct"
        private const val UPDATE_PROCESS = 1
        private const val UPDATE_COMPLETE = 2
        private const val UPDATE_STATE = 3
        private const val DOWN_STATE_START = "下载准备中...."
        private const val DOWN_STATE_COMPLETE = "下载完成"
        private const val DOWN_STATE_PROCESS = "下载中..."
        private const val DOWN_STATE_ERROR = "下载错误"
        private const val DOWN_STATE_PAUSE = "暂停"
    }

    override fun initDisplay() {
        disableLoad = true
        disableRefresh = true
        addItemDecoration = false
        super.initDisplay()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(M3u8ViewHolder::class.java),
                arrayOf(CourseDbBean::class.java),
                arrayOf(R.layout.item_down_m3u8)
        )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        //从数据库中获取
        val datas = PonkoApp.courseDao?.selectAll()
        requestRefreshSuccess(datas)
        //检查权限
        checkPermission()
        //开始下载
        //down(datas)
        //下载监听
        downListener()
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

    private fun down(datas:ArrayList<CourseDbBean>?){
        for (course in datas?.iterator()!!) {
            val m3u8DownTask = M3u8DownTask.Builder()
                    .vid(course.column_vid)
                    .m3u8(course.column_m3u8_url)
                    .name(course.column_title)
                    .fileSize(course.column_total.toLong())
                    .build()
            m3u8DownManager?.newTasker(m3u8DownTask)?.enqueue(null)
        }
    }

    private fun downListener() {
        m3u8DownManager?.listener = object : OnDownListener {
            override fun onStart(vid: String, url: String, m3u8Analysis: ArrayList<String>) {
                BKLog.d(TAG, "M3u8DownTasker 下载准备中....")
                val courseDbBean = CourseDbBean()
                courseDbBean.column_vid = vid
                courseDbBean.column_m3u8_url = url
                courseDbBean.column_state = DOWN_STATE_START
                updateRv(vid, url, courseDbBean, UPDATE_STATE)
            }

            override fun onComplete(vid: String, url: String) {
                BKLog.d(TAG, "M3u8DownTasker $url 下载完成")

                val courseDbBean = CourseDbBean()
                courseDbBean.column_vid = vid
                courseDbBean.column_m3u8_url = url
                courseDbBean.column_complete = 1
                courseDbBean.column_state = DOWN_STATE_COMPLETE
                updateRv(vid, url, courseDbBean, UPDATE_COMPLETE)
            }

            override fun onProcess(vid: String, url: String, progress: Int) {
                BKLog.d(TAG, "M3u8DownTasker $url 下载进度")

                val courseDbBean = CourseDbBean()
                courseDbBean.column_vid = vid
                courseDbBean.column_m3u8_url = url
                courseDbBean.column_progress = progress
                courseDbBean.column_state = DOWN_STATE_PROCESS
                updateRv(vid, url, courseDbBean, UPDATE_PROCESS)
            }

            override fun onError(vid: String, url: String, msg: String) {
                BKLog.d(TAG, "M3u8DownTasker下载错误")
                val courseDbBean = CourseDbBean()
                courseDbBean.column_vid = vid
                courseDbBean.column_m3u8_url = url
                courseDbBean.column_state = DOWN_STATE_ERROR
                updateRv(vid, url, courseDbBean, UPDATE_STATE)
            }
        }
    }

    /**
     * 更新Rv界面
     */
    private fun updateRv(vid: String,m3u8: String, value: CourseDbBean?, type: Int) {
        var progressIndex = -1
        for (i in 0..(adapter?.data?.size!! - 1)) {
            val courseDbBean = adapter?.data!![i] as CourseDbBean
//            if (m3u8 == courseDbBean.column_m3u8_url) {
            if (vid == courseDbBean.column_vid) {
                progressIndex = i
                when (type) {
                    UPDATE_PROCESS -> {
                        courseDbBean.column_progress = value?.column_progress!!
                        courseDbBean.column_state = value.column_state
                        courseDbBean.column_vid = vid
                        courseDbBean.column_m3u8_url = m3u8
                        //下载中状态更新到数据库当中
                        PonkoApp.courseDao?.downProgressUpdate(vid,value.column_progress)
                    }
                    UPDATE_COMPLETE -> {
                        courseDbBean.column_complete = value?.column_complete!! //1代表成功
                        courseDbBean.column_vid = vid
                        courseDbBean.column_m3u8_url = m3u8!!
                        courseDbBean.column_state = value.column_state

                        //下载完成状态更新到数据库中
                        val cacheM3u8 = PonkoApp.m3u8DownManager?.path + File.separator + PonkoApp.m3u8DownManager?.dir + File.separator + M3u8Utils.m3u8Unique(m3u8!!) + File.separator + M3u8Utils.m3u8FileName(m3u8)
                        PonkoApp.courseDao?.downCompleteUpdate(vid, cacheM3u8, m3u8, 1)
                    }
                    UPDATE_STATE -> {
                        courseDbBean.column_state = value?.column_state!!
                        courseDbBean.column_vid = vid
                        courseDbBean.column_m3u8_url = m3u8!!
                    }
                }
                break
            }
        }
        this.runOnUiThread {
            adapter?.notifyItemChanged(progressIndex)
        }
    }

    override fun multiTypeData(body: ArrayList<CourseDbBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

    /**
     * 下载任务页面ViewHolder
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
            val courseSpecialDbBeans = PonkoApp.courseSpecialDao?.select(courseDbBean.column_special_id)
            if (!courseSpecialDbBeans?.isEmpty()!!) {
                val value_typeId = courseSpecialDbBeans[0].special_id
                val value_teachers = courseSpecialDbBeans[0].teacher
                val value_num = courseSpecialDbBeans[0].num.toLong()
                val value_duration = courseSpecialDbBeans[0].duration.toLong()
                StudyCourseDetailActivity.startFromCacheCourse(context, value_typeId, value_teachers, value_num, value_duration, courseDbBean.column_vid)
            } else {
                BKLog.e(TAG, "请检查专题数据库")
            }
            //StudyCourseDetailActivity.startFromCacheCourse(context, value_typeId, value_teachers, value_num, value_duration, courseDbBean.column_vid)
        }

        private fun notDownComplete(courseDbBean: CourseDbBean) {
            if (m3u8DownManager?.isRun(courseDbBean.column_m3u8_url) == true) {
                m3u8DownManager?.pause(courseDbBean.column_m3u8_url)
                viewHolder?.tvState?.text = DOWN_STATE_PAUSE     //点击暂停后
            } else {
                m3u8DownManager?.resume(
                        M3u8DownTask.Builder()
                                .vid(courseDbBean.column_vid)
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

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_cache_list)
//    }
}

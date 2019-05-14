package com.ponko.cn.module.m3u8downer

import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.db.bean.CourseDbBean
import com.ponko.cn.db.dao.CourseDao
import com.ponko.cn.module.m3u8downer.M3u8Utils.analysis
import com.ponko.cn.module.m3u8downer.M3u8Utils.copyInputStream
import com.ponko.cn.module.m3u8downer.M3u8Utils.m3u8FileName
import com.ponko.cn.module.m3u8downer.M3u8Utils.writeLocal
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.DownManager
import com.xm.lib.downloader.enum_.DownErrorType
import com.xm.lib.downloader.event.DownObserver
import com.xm.lib.downloader.task.DownTask
import com.xm.lib.downloader.task.DownTasker
import com.xm.lib.downloader.utils.FileUtil
import okhttp3.*
import java.io.IOException

class M3u8DownerTextAct : AppCompatActivity() {

    private var rv: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_m3u8_downer_text)
        //获取需要下载的课程
        val dao = CourseDao(PonkoApp.dbHelp?.writableDatabase)
        val datas = dao.selectAll()
        rv = findViewById<RecyclerView>(R.id.rv)
        val adapter = object : BaseRvAdapter() {}
        adapter.data?.addAll(datas)
        adapter.addItemViewDelegate(0, M3u8ViewHolder::class.java, CourseDbBean::class.java, R.layout.item_down_m3u8)
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(this)

        // 请求网络
        val m3u8 = datas[0].column_m3u8_url
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url(m3u8).get().build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                BKLog.d("请求m3u8失败")
            }

            override fun onResponse(call: Call, response: Response) {
                val (stream1, stream2) = copyInputStream(response.body()?.byteStream())
                val (m3u8Key, m3u8Ts) = analysis(stream1)
                writeLocal(stream2, m3u8, "XmDown", m3u8Key, m3u8Ts)
                down("XmDown/${m3u8FileName(m3u8)}", m3u8Key, m3u8Ts)
            }
        })
    }

    private fun down(dir: String, m3u8Key: String, m3u8Ts: ArrayList<String>) {
        //开启下载的线程
        val downManager = DownManager.createDownManager(this@M3u8DownerTextAct)
        downManager.downConfig()?.dir = dir
        val task = DownTask()
        task.url = m3u8Key
        downManager.createDownTasker(task).enqueue()
        for (ts in m3u8Ts) {
            val task = DownTask()
            task.url = ts
            downManager.createDownTasker(task).enqueue()
        }
        //进度
        downManager.downObserverable()?.registerObserver(object : DownObserver {
            override fun onComplete(tasker: DownTasker, total: Long) {
                tasker.task.url
                BKLog.d(task.fileName + "total:$total thread:${Looper.getMainLooper()==Looper.getMainLooper()}")
            }

            override fun onError(tasker: DownTasker, typeError: DownErrorType, s: String) {

            }

            override fun onProcess(tasker: DownTasker, process: Long, total: Long, present: Float) {
                // BKLog.d(task.fileName+"process:$process")
            }

            override fun onPause(tasker: DownTasker) {

            }

            override fun onDelete(tasker: DownTasker) {

            }
        })
    }

    /**
     * 下载任务页面
     */
    class M3u8ViewHolder(view: View) : BaseViewHolder(view) {
        private var viewHolder: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }
            val courseDbBean = d as CourseDbBean
            val context = itemView.context
            viewHolder?.tvCourseName?.text = courseDbBean.column_title
            viewHolder?.pb?.max = 100
            if (courseDbBean.column_complete == 1) {
                //完成
                viewHolder?.pb?.progress = 100
                viewHolder?.tvProcessTotal?.text = "${FileUtil.getSizeUnit(courseDbBean.column_total.toLong())} | ${FileUtil.getSizeUnit(courseDbBean.column_total.toLong())}"
            } else {
                //未完成
                viewHolder?.pb?.progress = courseDbBean.column_progress
                viewHolder?.tvProcessTotal?.text = "${FileUtil.getSizeUnit(courseDbBean.column_progress.toLong())} | ${FileUtil.getSizeUnit(courseDbBean.column_total.toLong())}"
            }
            itemView.setOnClickListener {
                BKLog.d("点击下载任务item")
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

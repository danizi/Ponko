package com.ponko.cn.module.m3u8downer

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.m3u8DownManager
import com.ponko.cn.db.bean.CourseDbBean
import com.ponko.cn.db.dao.CourseDao
import com.ponko.cn.module.m3u8downer.core.M3u8DownManager
import com.ponko.cn.module.m3u8downer.core.M3u8DownTask
import com.ponko.cn.module.m3u8downer.core.M3u8Utils.analysis
import com.ponko.cn.module.m3u8downer.core.M3u8Utils.copyInputStream
import com.ponko.cn.module.m3u8downer.core.M3u8Utils.m3u8FileName
import com.ponko.cn.module.m3u8downer.core.M3u8Utils.writeLocal
import com.ponko.cn.module.m3u8downer.core.OnDownListener
import com.tbruyelle.rxpermissions2.RxPermissions
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
    private var adapter: BaseRvAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_m3u8_downer_text)
        //获取需要下载的课程
        val dao = CourseDao(PonkoApp.dbHelp?.writableDatabase)
        val datas = dao.selectAll()
        rv = findViewById<RecyclerView>(R.id.rv)
        adapter = object : BaseRvAdapter() {}
        adapter?.data?.addAll(datas)
        adapter?.addItemViewDelegate(0, M3u8ViewHolder::class.java, CourseDbBean::class.java, R.layout.item_down_m3u8)
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(this)

        rv?.adapter?.notifyItemChanged(1)

//        RxPermissions(this)
//                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe { aBoolean ->
//                    if (aBoolean!!) {
//                        //当所有权限都允许之后，返回true
//                        Toast.makeText(this,"文件授权成功",Toast.LENGTH_SHORT).show()
//
//                    } else {
//                        //只要有一个权限禁止，返回false，
//                        //下一次申请只申请没通过申请的权限
//                        Log.i("permissions", "btn_more_sametime：$aBoolean")
//                    }
//                }
        //val m3u8DownManager = M3u8DownManager(this)
        for (course in datas.iterator()) {
            val m3u8DownTask = M3u8DownTask.Builder()
                    .m3u8(course.column_m3u8_url)
                    .name(course.column_title)
                    .fileSize(course.column_total.toLong())
                    .build()
            m3u8DownManager?.newTasker(m3u8DownTask)?.enqueue(object : OnDownListener {
                override fun onStart(m3u8Analysis: ArrayList<String>) {
                    BKLog.d("M3u8DownTasker 开始下载")
                }


                override fun onComplete(url: String) {
                    BKLog.d("M3u8DownTasker $url 下载完成")
                    updataUIComplete(url)
                }

                override fun onProcess(url: String, progress: Int) {
                    BKLog.d("M3u8DownTasker $url 下载进度")
                    updateUIProcess(url, progress.toLong())
                }

                override fun onError(msg: String) {
                    BKLog.d("M3u8DownTasker下载错误")
                }
            })
        }
    }

    /**
     * 更新item进度
     */
    private fun updateUIProcess(m3u8: String, progress: Long) {
        var progressIndex = -1
        for (i in 0..(adapter?.data?.size!! - 1)) {
            val courseDbBean = adapter?.data!![i] as CourseDbBean
            if (m3u8 == courseDbBean.column_m3u8_url) {
                progressIndex = i
                courseDbBean.column_progress = progress.toInt()
                break
            }
        }
//        Looper.prepare();//增加部分
//        val handler = Handler()
//        val r = Runnable {  adapter?.notifyItemChanged(progressIndex) }
//        handler.post(r)
//        Looper.loop()
        this.runOnUiThread {
            adapter?.notifyItemChanged(progressIndex)
        }

    }

    private fun updataUIComplete(m3u8: String) {
        var progressIndex = -1
        for (i in 0..(adapter?.data?.size!! - 1)) {
            val courseDbBean = adapter?.data!![i] as CourseDbBean
            if (m3u8 == courseDbBean.column_m3u8_url) {
                progressIndex = i
                courseDbBean.column_complete = 1
                break
            }
        }

        this.runOnUiThread {
            adapter?.notifyItemChanged(progressIndex)
        }

    }

    private fun updateCompleteItem() {
        rv?.adapter
    }


    private fun test(datas: ArrayList<CourseDbBean>) {
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
        //key url
        val task = DownTask()
        task.url = m3u8Key
        downManager.createDownTasker(task).enqueue()

        //ts url
        for (ts in m3u8Ts) {
            val task = DownTask()
            task.url = ts
            downManager.createDownTasker(task).enqueue()
        }
        //进度
        downManager.downObserverable()?.registerObserver(object : DownObserver {
            override fun onComplete(tasker: DownTasker, total: Long) {
                BKLog.d(task.fileName + "total:$total thread:${Looper.getMainLooper() == Looper.getMainLooper()}")
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
            viewHolder?.pb?.max = courseDbBean.column_total
            if (courseDbBean.column_complete == 1) {
                //完成
                viewHolder?.pb?.progress = courseDbBean.column_total
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

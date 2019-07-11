package com.ponko.cn.module.my.option

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.db.bean.CourseDbBean
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.m3u8downer.core.M3u8Utils
import com.ponko.cn.module.my.constract.CacheListContract
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.DialogUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.OnEnterListener
import com.xm.lib.downloader.utils.FileUtil
import java.io.File
import java.util.concurrent.LinkedBlockingQueue


/**
 * 缓存列表 - 缓存的章节
 */
class CacheListAct : RefreshLoadAct<CacheListContract.Present, ArrayList<CourseDbBean>>(), CacheListContract.V {

    companion object {
        const val TAG = "CacheListAct"
        const val UPDATE_PROCESS = 1
        const val UPDATE_COMPLETE = 2
        const val UPDATE_STATE = 3
        const val UPDATE_QUEUE = 4
        const val UPDATE_ERROR = 5
        const val UPDATE_PAUSE = 6

        const val DOWN_STATE_START = "下载准备中...."
        const val DOWN_STATE_COMPLETE = "已完成"
        const val DOWN_STATE_PROCESS = "下载中..."
        const val DOWN_STATE_ERROR = "下载错误"
        const val DOWN_STATE_PAUSE = "已暂停"
        const val DOWN_STATE_READY = "队列中..."
        const val DOWN_STATE_CLICK_DONW = "点击下载"

        var isDelete = false
        var isSelectAll = false
        var selectItems = LinkedBlockingQueue<CourseDbBean>()

        var accept_special_id = ""
        var accept_title = ""
        var accept_teacher = ""
        var accept_num = 0
        var accept_duration = 0

        /**
         * 离线缓存列表跳转过来
         * @param context    上下文对象
         * @param special_id 课程专题id
         * @param title      专题名称
         * @param teacher    专题老师
         * @param num        专题课程数量
         * @param duration   专题总时长
         */
        fun start(context: Context?, special_id: String, title: String, teacher: String, num: Int, duration: Int) {
            val intent = Intent(context, CacheListAct::class.java)
            intent.putExtra("id", special_id)
            intent.putExtra("title", title)
            intent.putExtra("teacher", teacher)
            intent.putExtra("num", num)
            intent.putExtra("duration", duration)
            accept_special_id = special_id
            accept_title = title
            accept_teacher = teacher
            accept_num = num
            accept_duration = duration
            context?.startActivity(intent)
        }
    }

    /**
     * 窗口ui
     */
    private var ui: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p?.registerDownAllBroadCast()
    }

    override fun onDestroy() {
        super.onDestroy()
        p?.unRegisterDownAllBroadCast()
    }

    override fun onResume() {
        super.onResume()
        requestRefreshApi()
    }

    override fun presenter(): CacheListContract.Present {
        return CacheListContract.Present(context = this, v = this)
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1),
                arrayOf(CacheListContract.V.CacheListTopHolder::class.java, CacheListContract.V.M3u8ViewHolder::class.java),
                arrayOf(CacheListContract.V.CacheListTopBean::class.java, CourseDbBean::class.java),
                arrayOf(R.layout.activity_cache_list_top, R.layout.item_down_m3u8)
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cache_list
    }

    override fun findViews() {
        super.findViews()
        if (ui == null) {
            ui = ViewHolder.create(this)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initDisplay() {
        disableLoad = true
        disableRefresh = true
        addItemDecoration = false
        super.initDisplay()
        BarUtil.addBarIcon(this, viewHolder?.toolbar, "缓存", R.mipmap.delete, R.mipmap.cancel, View.OnClickListener {
            if (ui?.llBottom?.visibility == View.GONE) {
                ui?.llBottom?.visibility = View.VISIBLE
                isDelete = true
            } else {
                ui?.llBottom?.visibility = View.GONE
                isDelete = false
                updateSelectDownList(false)
            }
            adapter?.notifyDataSetChanged()
        })
        ui?.tvSpace?.text = "手机总空间${FileUtil.getSizeUnit(FileUtil.getTotalSpace())}/剩余空间${FileUtil.getSizeUnit(FileUtil.getUsableSpace())}"
    }

    override fun iniEvent() {
        super.iniEvent()
        ui?.btnAllSelect?.setOnClickListener {
            p?.clickAllBtn()
        }
        ui?.btnDelete?.setOnClickListener {
            p?.clickDeleteBtn()
        }
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        p?.requestRefreshApi()
    }

    override fun multiTypeData(body: ArrayList<CourseDbBean>?): List<Any> {
        val data = ArrayList<Any>()
        data.add(CacheListContract.V.CacheListTopBean(
                accept_special_id,
                accept_title,
                accept_teacher,
                accept_num,
                accept_duration
        ))
        data.addAll(body!!)
        return data
    }

    override fun adapter(): BaseRvAdapter? {
        return p?.getAdapter()
    }

    override fun updateSelectDownList(isSelect: Boolean) {
        for (data in adapter?.data!!) {
            if (data is CourseDbBean) {
                data.isSelect = isSelect
            }
        }
        adapter?.notifyDataSetChanged()
    }

    override fun updateDeleteDownList(courseDbBean: CourseDbBean) {
        var index = 0
        val it = adapter?.data?.iterator()
        while (it?.hasNext()!!) {
            val data = it.next()
            if (data is CourseDbBean) {
                if (courseDbBean == data) {
                    it.remove()
                    adapter?.notifyItemRemoved(index)
                    adapter?.notifyItemChanged(index)
                }
            }
            index++
        }
    }

    override fun showDeleteDialog(title: String, msg: String) {
        DialogUtil.show(this, title, msg, true, object : OnEnterListener {
            override fun onEnter(dlg: AlertDialog) {
                p?.enterDelete()
                dlg.dismiss()
            }
        }, null)
    }

    override fun requestCacheListSuccess(datas: ArrayList<CourseDbBean>?) {
        requestRefreshSuccess(datas)
    }

    override fun updateDownUI(vid: String?, m3u8: String?, value: CourseDbBean?, type: Int) {
        var progressIndex = -1
        var progressCourseDbBean: CourseDbBean? = null
        var stateType = ""
        var isRefrsh = false
        for (i in 1..(adapter?.data?.size!! - 1)) {
            val courseDbBean = adapter?.data!![i] as CourseDbBean

            if (vid == courseDbBean.column_vid || m3u8 == courseDbBean.column_m3u8_url) {
                progressCourseDbBean = courseDbBean
                progressCourseDbBean.column_vid = vid!!
                progressCourseDbBean.column_m3u8_url = m3u8!!
                progressIndex = i

                when (type) {
                    UPDATE_PROCESS -> {
                        stateType = "进度"
                        progressCourseDbBean.column_complete = 0
                        progressCourseDbBean.column_progress = value?.column_progress!!
                        progressCourseDbBean.column_state = CacheListAct.DOWN_STATE_PROCESS
                        //下载中状态更新到数据库当中
                        PonkoApp.courseDao?.downProgressUpdate(vid, value.column_progress)
                    }
                    UPDATE_COMPLETE -> {
                        stateType = "完成"
                        progressCourseDbBean.column_complete = 1  //0 代表未下载完成 1代表成功
                        progressCourseDbBean.column_state = CacheListAct.DOWN_STATE_COMPLETE
                        //下载完成状态更新到数据库中
                        val cacheM3u8 = PonkoApp.m3u8DownManager?.path + File.separator + PonkoApp.m3u8DownManager?.dir + File.separator + M3u8Utils.m3u8Unique(m3u8) + File.separator + M3u8Utils.m3u8FileName(m3u8)
                        PonkoApp.courseDao?.downCompleteUpdate(vid, cacheM3u8, m3u8, 1)
                    }
                    UPDATE_QUEUE -> {
                        stateType = "队列"
                        progressCourseDbBean.column_state = CacheListAct.DOWN_STATE_READY
                        PonkoApp.courseDao?.downQueueUpdate(vid)
                    }
                    UPDATE_ERROR -> {
                        stateType = "错误"
                        progressCourseDbBean.column_state = CacheListAct.DOWN_STATE_ERROR
                        PonkoApp.courseDao?.downErrorUpdate(vid)
                    }
                    UPDATE_STATE -> {
                        stateType = "状态"
                        progressCourseDbBean.column_state = CacheListAct.DOWN_STATE_START
                        PonkoApp.courseDao?.downStateUpdate(vid)
                    }
                    UPDATE_PAUSE -> {
                        stateType = "已暂停"
                        progressCourseDbBean.column_state = CacheListAct.DOWN_STATE_PAUSE
                    }
                    else -> {

                    }
                }
                isRefrsh = true
                break
            }
        }
        if (isRefrsh) {
            this.runOnUiThread {
                //打印日志
                val sb = StringBuilder()
                        .append("***********")
                        .append("【更新下标】：$progressIndex ")
                        .append("【更新类型】：$stateType ")
                        .append("【更新标题】：${progressCourseDbBean?.column_title}")
                BKLog.d(CacheListAct.TAG, sb.toString())
                //更新RecycleView
                adapter?.notifyItemChanged(progressIndex)
            }
        } else {
            BKLog.d("m3u8: $m3u8 队列中未存在")
            if (!TextUtils.isEmpty(vid) && !TextUtils.isEmpty(m3u8)) {
                //PonkoApp.m3u8DownManager?.pause(vid!!, m3u8!!)
            }
        }
    }

    /**
     * 窗口ui
     */
    private class ViewHolder private constructor(val toolbar: android.support.v7.widget.Toolbar, val llBottom: LinearLayout, val btnAllSelect: Button, val btnDelete: Button, val tvSpace: TextView) {
        companion object {

            @SuppressLint("NewApi")
            fun create(rootView: AppCompatActivity?): ViewHolder {
                val toolbar = rootView?.findViewById<View>(R.id.toolbar) as android.support.v7.widget.Toolbar
                val llBottom = rootView.findViewById<View>(R.id.ll_bottom) as LinearLayout
                val btnAllSelect = rootView.findViewById<View>(R.id.btn_all_select) as Button
                val btnDelete = rootView.findViewById<View>(R.id.btn_delete) as Button
                val tvSpace = rootView.findViewById<View>(R.id.tv_space) as TextView
                return ViewHolder(toolbar, llBottom, btnAllSelect, btnDelete, tvSpace)
            }
        }
    }

}

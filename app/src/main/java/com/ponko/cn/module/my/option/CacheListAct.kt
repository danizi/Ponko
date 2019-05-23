package com.ponko.cn.module.my.option

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.courseDao
import com.ponko.cn.app.PonkoApp.Companion.m3u8DownManager
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.db.bean.CourseDbBean
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.m3u8downer.core.M3u8DownTask
import com.ponko.cn.module.m3u8downer.core.M3u8Utils
import com.ponko.cn.module.m3u8downer.core.OnDownListener
import com.ponko.cn.module.study.StudyCacheActivity
import com.ponko.cn.module.study.StudyCourseDetailActivity
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.utils.FileUtil
import com.xm.lib.media.broadcast.BroadcastManager
import java.io.File


/**
 * 缓存列表 - 缓存的章节
 */
class CacheListAct : RefreshLoadAct<Any, ArrayList<CourseDbBean>>() {
    companion object {
        const val TAG = "CacheListAct"
        private const val UPDATE_PROCESS = 1
        private const val UPDATE_COMPLETE = 2
        private const val UPDATE_STATE = 3
        private const val UPDATE_QUEUE = 4
        private const val UPDATE_ERROR = 5

        private const val DOWN_STATE_START = "下载准备中...."
        private const val DOWN_STATE_COMPLETE = "下载完成"
        private const val DOWN_STATE_PROCESS = "下载中..."
        private const val DOWN_STATE_ERROR = "下载错误"
        private const val DOWN_STATE_PAUSE = "暂停"
        private const val DOWN_STATE_READY = "已加入下载队列中..."
        private const val DOWN_STATE_CLICK_DONW = "点击下载"

        /**
         * 下载全部任务广播
         */
        const val ACTION_DOWN_ALL = "broadcast.action.down.all"

        var isDelete = false
        var isSelectAll = false
        var selectItems = ArrayList<CourseDbBean>()

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
     * 底部按钮全选/删除
     */
    private var llbottom: LinearLayout? = null
    private var btnAll: Button? = null
    private var btnDelete: Button? = null

    /**
     * 下载所有广播
     */
    private var broadcastManager: BroadcastManager? = null
    private var downAllBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_DOWN_ALL) {
                val datas = PonkoApp.courseDao?.selectBySpecialId(accept_special_id)
                //任务全部加载队列中
                for (data in datas!!) {
                    data.column_state = DOWN_STATE_READY
                }
                down(datas)
                //刷新列表页面
                adapter?.notifyDataSetChanged()
                BKLog.d(TAG, "开始所有任务，刷新列表页面")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (broadcastManager == null) {
            broadcastManager = BroadcastManager.create(this)
        }
        broadcastManager?.registerReceiver(ACTION_DOWN_ALL, downAllBroadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcastManager?.unRegisterReceiver(downAllBroadcastReceiver)
        isDelete = false
        isSelectAll = false
        selectItems.clear()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cache_list
    }

    override fun findViews() {
        super.findViews()
        llbottom = findViewById(R.id.ll_bottom)
        btnAll = findViewById(R.id.btn_all_select)
        btnDelete = findViewById(R.id.btn_down)
    }

    override fun initDisplay() {
        disableLoad = true
        disableRefresh = true
        addItemDecoration = false
        super.initDisplay()
        BarUtil.addBarIcon(this, viewHolder?.toolbar, "缓存", R.mipmap.delete, R.mipmap.cancel, View.OnClickListener {
            isSelectAll = !isSelectAll
            if (llbottom?.visibility == View.GONE) {
                llbottom?.visibility = View.VISIBLE
                isDelete = true
            } else {
                llbottom?.visibility = View.GONE
                isDelete = false
            }
            adapter?.notifyDataSetChanged()
        })
    }

    override fun iniEvent() {
        super.iniEvent()
        btnAll?.setOnClickListener {
            BKLog.d("点击了全选")
            isSelectAll = !isSelectAll
            adapter?.notifyDataSetChanged()
        }
        btnDelete?.setOnClickListener {
            BKLog.d("点击了删除")
            for (deleteItem in selectItems) {
                //删除数据库中的数据
                courseDao?.delete(deleteItem)
                //删除本地缓存的数据
                FileUtil.del(File(m3u8DownManager?.path + File.separator + m3u8DownManager?.dir + M3u8Utils.m3u8Unique(deleteItem.column_m3u8_url)))
                //删除RecyclerView刷新
                for (i in 1..(adapter?.data?.size!! - 1)) {
                    if (deleteItem == adapter?.data?.get(i)) {
                        adapter?.data?.remove(deleteItem)
                        adapter?.notifyItemRemoved(i)
                        adapter?.notifyItemChanged(i)
                        break
                    }
                }
            }
        }
    }


    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1),
                arrayOf(CacheListTopHolder::class.java, M3u8ViewHolder::class.java),
                arrayOf(CacheListTopBean::class.java, CourseDbBean::class.java),
                arrayOf(R.layout.activity_cache_list_top, R.layout.item_down_m3u8)
        )
    }

    override fun requestMoreApi() {}

    override fun onResume() {
        super.onResume()
        requestRefreshApi()
    }

    override fun requestRefreshApi() {
        //从数据库中获取
        val datas = PonkoApp.courseDao?.selectBySpecialId(accept_special_id)
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

    private fun down(datas: ArrayList<CourseDbBean>?) {
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

            override fun onQueue(vid: String?, url: String?) {
                BKLog.d(TAG, "M3u8DownTasker $vid 添加到队列中....")
                val courseDbBean = CourseDbBean()
                courseDbBean.column_vid = vid!!
                courseDbBean.column_m3u8_url = url!!
                courseDbBean.column_complete = 1
                courseDbBean.column_state = DOWN_STATE_READY
                updateRv(vid, url, courseDbBean, UPDATE_QUEUE)
            }

            override fun onStart(vid: String, url: String, m3u8Analysis: ArrayList<String>) {
                BKLog.d(TAG, "M3u8DownTasker 下载准备中....")
                val courseDbBean = CourseDbBean()
                courseDbBean.column_vid = vid
                courseDbBean.column_m3u8_url = url
                courseDbBean.column_complete = 0
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
                courseDbBean.column_complete = 0
                courseDbBean.column_state = DOWN_STATE_PROCESS
                updateRv(vid, url, courseDbBean, UPDATE_PROCESS)
            }

            override fun onError(vid: String, url: String, msg: String) {
                BKLog.d(TAG, "M3u8DownTasker下载错误")
                val courseDbBean = CourseDbBean()
                courseDbBean.column_vid = vid
                courseDbBean.column_m3u8_url = url
                courseDbBean.column_complete = 0
                courseDbBean.column_state = DOWN_STATE_ERROR
                updateRv(vid, url, courseDbBean, UPDATE_ERROR)
            }
        }
    }

    /**
     * 更新Rv界面
     */
    private fun updateRv(vid: String, m3u8: String, value: CourseDbBean?, type: Int) {
        var progressIndex = -1
        for (i in 1..(adapter?.data?.size!! - 1)) {
            val courseDbBean = adapter?.data!![i] as CourseDbBean
            if (vid == courseDbBean.column_vid) {
                progressIndex = i
                when (type) {
                    UPDATE_PROCESS -> {
                        courseDbBean.column_progress = value?.column_progress!!
                        courseDbBean.column_state = value.column_state
                        courseDbBean.column_vid = vid
                        courseDbBean.column_m3u8_url = m3u8
                        //下载中状态更新到数据库当中
                        PonkoApp.courseDao?.downProgressUpdate(vid, value.column_progress)
                    }
                    UPDATE_COMPLETE -> {
                        courseDbBean.column_complete = value?.column_complete!! //1代表成功
                        courseDbBean.column_vid = vid
                        courseDbBean.column_m3u8_url = m3u8
                        courseDbBean.column_state = value.column_state

                        //下载完成状态更新到数据库中
                        val cacheM3u8 = PonkoApp.m3u8DownManager?.path + File.separator + PonkoApp.m3u8DownManager?.dir + File.separator + M3u8Utils.m3u8Unique(m3u8) + File.separator + M3u8Utils.m3u8FileName(m3u8)
                        PonkoApp.courseDao?.downCompleteUpdate(vid, cacheM3u8, m3u8, 1)
                    }

                    UPDATE_QUEUE -> {
                        courseDbBean.column_state = value?.column_state!!
                        courseDbBean.column_vid = vid
                        courseDbBean.column_m3u8_url = m3u8
                        PonkoApp.courseDao?.downQueueUpdate(vid)
                    }

                    UPDATE_ERROR -> {
                        courseDbBean.column_state = value?.column_state!!
                        courseDbBean.column_vid = vid
                        courseDbBean.column_m3u8_url = m3u8
                        PonkoApp.courseDao?.downErrorUpdate(vid)
                    }

                    UPDATE_STATE -> {
                        courseDbBean.column_state = value?.column_state!!
                        courseDbBean.column_vid = vid
                        courseDbBean.column_m3u8_url = m3u8
                        PonkoApp.courseDao?.downStateUpdate(vid)
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
        val data = ArrayList<Any>()
        data.add(CacheListTopBean(
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
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

    /**
     * 缓存顶部内容
     */
    class CacheListTopHolder(view: View) : BaseViewHolder(view) {
        private class ViewHolder private constructor(val imageView5: ImageView, val tvSection: TextView, val btnDownAll: Button) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val imageView5 = rootView.findViewById<View>(R.id.imageView5) as ImageView
                    val tvSection = rootView.findViewById<View>(R.id.tv_section) as TextView
                    val btnDownAll = rootView.findViewById<View>(R.id.btn_down_all) as Button
                    return ViewHolder(imageView5, tvSection, btnDownAll)
                }
            }
        }

        private var ui: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = ViewHolder.create(itemView)
            }
            val cacheListTopBean = d as CacheListTopBean
            val context = itemView.context
            itemView.setOnClickListener {
                BKLog.d("跳转到本课程其他章节")
                StudyCacheActivity.start(context, cacheListTopBean.id, cacheListTopBean.teacher, cacheListTopBean.num.toLong(), cacheListTopBean.duration.toLong())
            }
            ui?.btnDownAll?.setOnClickListener {
                BKLog.d("全部开始")
                context.sendBroadcast(Intent(ACTION_DOWN_ALL))
                //为了安全起见 首先将运行队列全部移除
                m3u8DownManager?.dispatcher?.removeAll()
            }
        }
    }

    class CacheListTopBean(var id: String, var title: String, var teacher: String, var num: Int, var duration: Int)

    /**
     * 下载任务页面ViewHolder
     */
    class M3u8ViewHolder(view: View) : BaseViewHolder(view) {
        private var viewHolder: ViewHolder? = null
        private var context: Context? = null
        private var courseDbBean: CourseDbBean? = null
        @SuppressLint("SetTextI18n")
        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }
            courseDbBean = d as CourseDbBean
            context = itemView.context

            //是否是删除模式
            displayMode(courseDbBean!!)

            //任务名称
            viewHolder?.tvCourseName?.text = courseDbBean?.column_title

            //下载状态信息提示显示
            displayDownState(courseDbBean)

            //设置界面内容
            Glide.with(context, courseDbBean?.column_cover, viewHolder?.ivCover) //设置封面

            //下载修改“进度提示字”和“进度条”
            progress(courseDbBean!!)

            //监听
            itemView.setOnClickListener {
                when (isDelete) {
                    true -> {
                        //删除模式点击处理
                        deleteClickItem(courseDbBean!!)
                    }
                    false -> {
                        //普通下载模式点击处理
                        ordinaryClickItem(context!!, courseDbBean!!)
                    }
                }
            }
        }

        /**
         * 删除模式、普通下载模式
         */
        private fun displayMode(courseDbBean: CourseDbBean) {
            if (isDelete) {
                if (isSelectAll) {
                    selectItems.add(courseDbBean)
                } else {
                    selectItems.remove(courseDbBean)
                }
                viewHolder?.cb?.visibility = View.VISIBLE
            } else {
                viewHolder?.cb?.visibility = View.GONE
            }
        }

        /**
         * 下载状态显示
         */
        private fun displayDownState(courseDbBean: CourseDbBean?) {

            //数据库中状态不是很准确
            //viewHolder?.tvState?.text = courseDbBean?.column_state

            //对上面状态的补充
            if (courseDbBean?.column_complete == 1) {
                viewHolder?.tvState?.text = DOWN_STATE_COMPLETE    //下载状态提示字
                BKLog.d(TAG, "${courseDbBean.column_title}任务处于 - 【完成状态】")
            } else {
                when {
                    m3u8DownManager?.isReady(courseDbBean?.column_vid!!) == true -> {
                        viewHolder?.tvState?.text = DOWN_STATE_READY
                        BKLog.d(TAG, "${courseDbBean?.column_title}任务处于 - 【队列状态】")
                    }
                    m3u8DownManager?.isRun(courseDbBean?.column_vid!!) == true -> {
                        viewHolder?.tvState?.text = DOWN_STATE_PROCESS
                        BKLog.d(TAG, "${courseDbBean?.column_title}任务处于 - 【运行状态】")
                    }
                    else -> {
                        if (courseDbBean?.column_state == CourseDbBean.DOWN_STATE_ERROR) {
                            viewHolder?.tvState?.text = CourseDbBean.DOWN_STATE_ERROR
                        } else {
                            viewHolder?.tvState?.text = DOWN_STATE_CLICK_DONW   //点击下载
                        }
                        BKLog.d(TAG, "${courseDbBean?.column_title}任务处于 - 【默认状态，点击下载】")
                    }
                }
            }
            BKLog.d(TAG, "${courseDbBean?.column_title}任务处于 - 【${courseDbBean?.column_state}】")
        }

        /**
         * 非删除模式点击item处理
         */
        private fun ordinaryClickItem(context: Context, courseDbBean: CourseDbBean) {
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

        /**
         * 删除模式点击item处理
         */
        private fun deleteClickItem(courseDbBean: CourseDbBean) {
            if (viewHolder?.cb?.isChecked == true) {
                viewHolder?.cb?.isChecked = false
                selectItems.remove(courseDbBean)
            } else {
                selectItems.add(courseDbBean)
                viewHolder?.cb?.isChecked = true
            }
        }

        /**
         * 下载完成点击item处理
         */
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

        /**
         * 没有下载完成点击item处理
         */
        private fun notDownComplete(courseDbBean: CourseDbBean) {
            //处于下载状态点击暂停
            when {
                m3u8DownManager?.isRun(courseDbBean.column_m3u8_url) == true -> {
                    //处于下载状态 点击item 暂停处理 PS：将任务从队列中移除，下载恢复的时候需要重新加入队列
                    m3u8DownManager?.pause(courseDbBean.column_m3u8_url)
                    viewHolder?.tvState?.text = DOWN_STATE_PAUSE     //点击暂停后
                    Toast.makeText(context, "暂停", Toast.LENGTH_SHORT).show()
                    BKLog.d(TAG, "${courseDbBean.column_title}任务处于 - 【暂停状态】")
                }
                m3u8DownManager?.isReady(courseDbBean.column_vid) == true -> {
                    //处于队列状态 点击item 提示处理
                    Toast.makeText(context, "亲该任务已加入到下载队列了...", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    //处于恢复状态 点击item 重新加入任务
                    m3u8DownManager?.resume(
                            M3u8DownTask.Builder()
                                    .vid(courseDbBean.column_vid)
                                    .name(courseDbBean.column_title)
                                    .m3u8(courseDbBean.column_m3u8_url)
                                    .fileSize(courseDbBean.column_total.toLong())
                                    .build())
                    viewHolder?.tvState?.text = DOWN_STATE_START
                    BKLog.d(TAG, "${courseDbBean.column_title}任务处于 - 【恢复状态】")
                    Toast.makeText(context, "恢复任务", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /**
         * 进度显示处理
         */
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

        private class ViewHolder private constructor(val cb: CheckBox, val ivCover: ImageView, val tvCourseName: TextView, val pb: ProgressBar, val tvState: TextView, val tvProcessTotal: TextView) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val cb = rootView.findViewById<View>(R.id.cb) as CheckBox
                    val ivCover = rootView.findViewById<View>(R.id.iv_cover) as ImageView
                    val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                    val pb = rootView.findViewById<View>(R.id.pb) as ProgressBar
                    val tvState = rootView.findViewById<View>(R.id.tv_state) as TextView
                    val tvProcessTotal = rootView.findViewById<View>(R.id.tv_process_total) as TextView
                    return ViewHolder(cb, ivCover, tvCourseName, pb, tvState, tvProcessTotal)
                }
            }
        }

    }
}

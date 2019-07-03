package com.ponko.cn.module.my.constract

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.constant.Constants.ACTION_DOWN_ALL
import com.ponko.cn.constant.Constants.ACTION_DOWN_All_PAUSE
import com.ponko.cn.constant.Constants.ACTION_DOWN_CLICK_PAUSE
import com.ponko.cn.constant.Constants.ACTION_DOWN_CLICK_READY
import com.ponko.cn.db.bean.CourseDbBean
import com.ponko.cn.module.m3u8downer.core.*
import com.ponko.cn.module.my.option.CacheListAct
import com.ponko.cn.module.my.option.CacheListAct.Companion.accept_special_id
import com.ponko.cn.module.study.v1.StudyCacheActivity
import com.ponko.cn.module.study.v1.StudyCourseDetailActivity
import com.ponko.cn.utils.Glide
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.utils.FileUtil
import com.xm.lib.media.broadcast.BroadcastManager
import java.io.File

/**
 * 我的缓存列表契约类 MVP 模式
 */
class CacheListContract {
    /**
     * 视图层
     */
    interface V {
        /**
         * 刷新下载列表 - 选中
         * @param isSelect 是否选中
         */
        fun updateSelectDownList(isSelect: Boolean)

        /**
         * 刷新下载列表 - 删除
         */
        fun updateDeleteDownList(courseDbBean: CourseDbBean)

        /**
         * 弹出对话框
         * @param title 标题
         * @param msg 消息提醒
         */
        fun showDeleteDialog(title: String, msg: String)

        /**
         *  获取缓存列表数据成功
         *  @param datas 缓存数据
         */
        fun requestCacheListSuccess(datas: ArrayList<CourseDbBean>?)

        /**
         * 监听了播放状态 - 更新RecyclerView列表 进度 下载状态 ....
         */
        fun updateDownUI(vid: String?, m3u8: String?, value: CourseDbBean?, type: Int)

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
            private var isDownAll: Boolean? = false

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
                    isDownAll = !isDownAll!!
                    if (isDownAll == true) {
                        context.sendBroadcast(Intent(ACTION_DOWN_ALL))
                        ui?.btnDownAll?.text = "全部暂停"
                    } else {
                        ui?.btnDownAll?.text = "全部开始"
                        context.sendBroadcast(Intent(ACTION_DOWN_All_PAUSE))
                    }
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

            companion object {
                @Deprecated("")
                var DOWN_STATE = "点击下载"
            }


            private var adapter: BaseRvAdapter? = null
            private var pos: Int = 0

            @SuppressLint("SetTextI18n")
            override fun bindData(adapter: BaseRvAdapter, d: Any, position: Int) {
                super.bindData(adapter, d, position)
                this.adapter = adapter
                this.pos = position
            }

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

//                //监听
//                viewHolder?.cb?.setOnClickListener {
//                    //点击选中
//                    courseDbBean?.isSelect = !courseDbBean?.isSelect!!
//                }
//                itemView.setOnClickListener {
//                    when (CacheListAct.isDelete) {
//                        true -> {
//                            //删除模式点击处理
//                            deleteClickItem(courseDbBean!!)
//                            //点击选中
//                            courseDbBean?.isSelect = !courseDbBean?.isSelect!!
//                        }
//                        false -> {
//                            //普通下载模式点击处理
//                            ordinaryClickItem(context!!, courseDbBean!!)
//                        }
//                    }
//                }
            }

            /**
             * 删除模式、普通下载模式
             */
            private fun displayMode(courseDbBean: CourseDbBean) {
                if (CacheListAct.isDelete) {
                    viewHolder?.cb?.visibility = View.VISIBLE
                    //监听
                    viewHolder?.cb?.setOnClickListener {
                        //点击选中
                        courseDbBean.isSelect = !courseDbBean.isSelect
                    }
                    itemView.setOnClickListener {
                        //删除模式点击处理
                        deleteClickItem(courseDbBean)
                        //点击选中
                        courseDbBean.isSelect = !courseDbBean.isSelect
                    }
                } else {
                    //非删除模式
                    viewHolder?.cb?.visibility = View.GONE
                    viewHolder?.cb?.isChecked = false
                    courseDbBean.isSelect = false
                    itemView.setOnClickListener {
                        //普通下载模式点击处理
                        ordinaryClickItem(context!!, courseDbBean)
                    }
                }

                if (courseDbBean.isSelect) {
                    BKLog.d("选中")
                    viewHolder?.cb?.isChecked = true
                } else {
                    BKLog.d("未选中")
                    viewHolder?.cb?.isChecked = false
                }
            }

            @Deprecated("")
            private fun addSelectItem() {
                var addFlag = true
                for (course in CacheListAct.selectItems) {
                    if (course.column_vid == courseDbBean?.column_vid) {
                        addFlag = false
                        break
                    }
                }
                if (addFlag) {
                    CacheListAct.selectItems.add(courseDbBean)
                    BKLog.d("${courseDbBean?.column_title}添加到选中队列中")
                }
                print()
            }

            @Deprecated("")
            private fun print() {
                for (item in CacheListAct.selectItems) {
                    BKLog.d("添加选中 - ${item?.column_title}")
                }
            }

            @Deprecated("")
            private fun deleteSelectItem() {
                for (course in CacheListAct.selectItems) {
                    if (course.column_vid == courseDbBean?.column_vid) {
                        CacheListAct.selectItems.remove(course)
                        BKLog.d("${courseDbBean?.column_title}添加到选中队列中")
                    }
                }
                print()
            }

            /**
             * 下载状态显示
             */
            private fun displayDownState(courseDbBean: CourseDbBean?) {
                //数据库中状态不是很准确
                //viewHolder?.tvState?.text = courseDbBean?.column_state
                //对上面状态的补充
//                if (courseDbBean?.column_complete == 1) {
//                    //viewHolder?.tvState?.text = CacheListAct.DOWN_STATE_COMPLETE    //下载状态提示字
//                    BKLog.d(CacheListAct.TAG, "${courseDbBean.column_title}任务处于 - 【完成状态】")
//                    DOWN_STATE = CacheListAct.DOWN_STATE_COMPLETE    //下载状态提示字
//                } else {
//                    when {
//                        PonkoApp.m3u8DownManager?.isReady(courseDbBean?.column_vid!!) == true -> {
//                            //viewHolder?.tvState?.text = CacheListAct.DOWN_STATE_READY
//                            BKLog.d(CacheListAct.TAG, "${courseDbBean?.column_title}任务处于 - 【队列状态】")
//                            DOWN_STATE = CacheListAct.DOWN_STATE_READY
//                        }
//                        PonkoApp.m3u8DownManager?.isRun(courseDbBean?.column_vid!!) == true -> {
//                            //viewHolder?.tvState?.text = CacheListAct.DOWN_STATE_PROCESS
//                            BKLog.d(CacheListAct.TAG, "${courseDbBean?.column_title}任务处于 - 【运行状态】")
//                            DOWN_STATE = CacheListAct.DOWN_STATE_PROCESS
//                        }
//                        else -> {
//                            if (courseDbBean?.column_state == CourseDbBean.DOWN_STATE_ERROR) {
//                                //viewHolder?.tvState?.text = CourseDbBean.DOWN_STATE_ERROR
//                                BKLog.d(CacheListAct.TAG, "${courseDbBean.column_title}任务处于 - 【错误状态】")
//                                DOWN_STATE = CourseDbBean.DOWN_STATE_ERROR
//                            } else {
//                                viewHolder?.tvState?.text = CacheListAct.DOWN_STATE_CLICK_DONW   //点击下载
//                                BKLog.d(CacheListAct.TAG, "${courseDbBean?.column_title}任务处于 - 【默认状态，点击下载】")
//                                DOWN_STATE = CacheListAct.DOWN_STATE_CLICK_DONW
//                            }
//                        }
//                    }
//                }

                //设置状态图标
                when {
                    PonkoApp.m3u8DownManager?.isReady(courseDbBean?.column_vid!!) == true -> {
                        viewHolder?.ivState?.setImageResource(R.mipmap.down_wait)
                        viewHolder?.tvState?.text = CacheListAct.DOWN_STATE_READY
                    }
                    PonkoApp.m3u8DownManager?.isRun(courseDbBean?.column_vid!!) == true -> {
                        viewHolder?.tvState?.text = courseDbBean?.column_state
                        viewHolder?.ivState?.setImageResource(R.mipmap.down_load)
                    }
                    courseDbBean?.column_state == "已暂停" -> {
                        viewHolder?.tvState?.text = courseDbBean.column_state
                        viewHolder?.ivState?.setImageResource(R.mipmap.down_pause)
                    }
                    courseDbBean?.column_state == "已完成" -> {
                        viewHolder?.tvState?.text = courseDbBean.column_state
                        //viewHolder?.ivState?.visibility = View.GONE
                        viewHolder?.ivState?.setImageResource(R.mipmap.down_play)
                        // viewHolder?.flState?.setBackgroundColor(Color.TRANSPARENT)
                    }
                    else -> {
                        viewHolder?.tvState?.text = courseDbBean?.column_state
                        viewHolder?.ivState?.setImageResource(R.mipmap.down_pause)
                    }
                }
//                if (PonkoApp.m3u8DownManager?.isReady(courseDbBean?.column_vid!!) == true) {
//                    viewHolder?.tvState?.text = CacheListAct.DOWN_STATE_READY
//                } else {
//                    viewHolder?.tvState?.text = courseDbBean?.column_state
//                }

                //BKLog.d(CacheListAct.TAG, "tvState ${courseDbBean?.column_state}")
            }

            /**
             * 非删除模式点击item处理
             */
            private fun ordinaryClickItem(context: Context, courseDbBean: CourseDbBean) {
                BKLog.d(CacheListAct.TAG, "点击下载任务item")
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
                viewHolder?.cb?.isChecked = viewHolder?.cb?.isChecked != true
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
                    BKLog.e(CacheListAct.TAG, "请检查专题数据库")
                }
                //StudyCourseDetailActivity.startFromCacheCourse(context, value_typeId, value_teachers, value_num, value_duration, courseDbBean.column_vid)
            }

            /**
             * 没有下载完成点击item处理
             */
            private fun notDownComplete(courseDbBean: CourseDbBean) {
                //处于下载状态点击暂停
                when {
                    PonkoApp.m3u8DownManager?.isRun(courseDbBean.column_m3u8_url) == true -> {
                        // PS:如果使用广播就会出现 状态文字为空的现象
//                        val intent = Intent(Constants.ACTION_DOWN_CLICK_PAUSE)
//                        intent.putExtra("vid", courseDbBean.column_vid)
//                        intent.putExtra("m3u8", courseDbBean.column_m3u8_url)
//                        context?.sendBroadcast(intent)
                        //处于下载状态 点击item 暂停处理 PS：将任务从队列中移除，下载恢复的时候需要重新加入队列
                        PonkoApp.m3u8DownManager?.pause(courseDbBean.column_vid, courseDbBean.column_m3u8_url)
                        Toast.makeText(context, "已暂停...", Toast.LENGTH_SHORT).show()

                    }
                    PonkoApp.m3u8DownManager?.isReady(courseDbBean.column_vid) == true -> {
                        //处于队列状态 点击item 提示处理  PS:如果使用广播就会出现 状态文字为空的现象
//                        val intent = Intent(Constants.ACTION_DOWN_CLICK_READY)
//                        intent.putExtra("vid", courseDbBean.column_vid)
//                        intent.putExtra("m3u8", courseDbBean.column_m3u8_url)
//                        intent.putExtra("title", courseDbBean.column_title)
//                        intent.putExtra("total", courseDbBean.column_total)
//                        context?.sendBroadcast(intent)
                        PonkoApp.m3u8DownManager?.pauseCurrent()
                        PonkoApp.m3u8DownManager?.pause(courseDbBean.column_vid, courseDbBean.column_m3u8_url)
                        PonkoApp.m3u8DownManager?.resume(
                                M3u8DownTask.Builder()
                                        .vid(courseDbBean.column_vid)
                                        .name(courseDbBean.column_title)
                                        .m3u8(courseDbBean.column_m3u8_url)
                                        .fileSize(courseDbBean.column_total.toLong())
                                        .build())
                        Toast.makeText(context, "队列...", Toast.LENGTH_SHORT).show()
                        //Toast.makeText(context, "队列...${courseDbBean.column_title}", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        //处于恢复状态 点击item 重新加入任务
                        PonkoApp.m3u8DownManager?.resume(
                                M3u8DownTask.Builder()
                                        .vid(courseDbBean.column_vid)
                                        .name(courseDbBean.column_title)
                                        .m3u8(courseDbBean.column_m3u8_url)
                                        .fileSize(courseDbBean.column_total.toLong())
                                        .build())
                        viewHolder?.tvState?.text = CacheListAct.DOWN_STATE_START
                        BKLog.d(CacheListAct.TAG, "${courseDbBean.column_title}任务处于 - 【恢复状态】")
                        Toast.makeText(context, "恢复任务", Toast.LENGTH_SHORT).show()
                        DOWN_STATE = "恢复"
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

            private class ViewHolder private constructor(val cb: CheckBox, val ivCover: ImageView, val tvCourseName: TextView, val pb: ProgressBar, val tvState: TextView, val tvProcessTotal: TextView, val ivState: ImageView, val flState: FrameLayout) {
                companion object {

                    fun create(rootView: View): ViewHolder {
                        val cb = rootView.findViewById<View>(R.id.cb) as CheckBox
                        val ivCover = rootView.findViewById<View>(R.id.iv_cover) as ImageView
                        val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                        val pb = rootView.findViewById<View>(R.id.pb) as ProgressBar
                        val tvState = rootView.findViewById<View>(R.id.tv_state) as TextView
                        val tvProcessTotal = rootView.findViewById<View>(R.id.tv_process_total) as TextView
                        val ivState = rootView.findViewById<View>(R.id.iv_state) as ImageView
                        val flState = rootView.findViewById<View>(R.id.fl_state) as FrameLayout
                        return ViewHolder(cb, ivCover, tvCourseName, pb, tvState, tvProcessTotal, ivState, flState)
                    }
                }
            }
        }
    }

    /**
     * 数据层
     */
    class M {

        val UPDATE_PROCESS = 1
        val UPDATE_COMPLETE = 2
        val UPDATE_STATE = 3
        val UPDATE_QUEUE = 4
        val UPDATE_ERROR = 5

        val DOWN_STATE_START = "下载准备中...."
        val DOWN_STATE_COMPLETE = "下载完成"
        val DOWN_STATE_PROCESS = "下载中..."
        val DOWN_STATE_ERROR = "下载错误"
        val DOWN_STATE_PAUSE = "已暂停"
        val DOWN_STATE_READY = "队列中..."
        val DOWN_STATE_CLICK_DONW = "点击下载"
        var isSelectAll: Boolean = false
    }

    /**
     * 控制层
     */
    class Present(private val context: Context, private val v: V) {
        /**
         * 数据层
         */
        private val m = M()
        /**
         * RecyclerView适配器
         */
        private var adapter: BaseRvAdapter? = null
        /**
         * 广播管理
         */
        private var broadcastManager: BroadcastManager? = null
        /**
         * 广播接受者
         */
        private var downAllBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val datas = PonkoApp.courseDao?.selectBySpecialId(CacheListAct.accept_special_id)
                var state = ""
                when (intent?.action) {
                    ACTION_DOWN_ALL -> {
                        //为了安全起见 首先将运行队列全部移除
                        PonkoApp.m3u8DownManager?.dispatcher?.removeAll()
                        down(datas)
                        state = m.DOWN_STATE_READY
                        BKLog.d(CacheListAct.TAG, "点击开始所有任务，刷新列表页面")
                    }
                    ACTION_DOWN_All_PAUSE -> {
                        //为了安全起见 首先将运行队列全部移除
                        PonkoApp.m3u8DownManager?.dispatcher?.removeAll()
                        state = m.DOWN_STATE_PAUSE
                        BKLog.d(CacheListAct.TAG, "点击暂停所有任务，刷新列表页面")
                    }
                    ACTION_DOWN_CLICK_PAUSE -> {
                        val vid = intent.getStringExtra("vid")
                        val m3u8 = intent.getStringExtra("m3u8")
                        //处于下载状态 点击item 暂停处理 PS：将任务从队列中移除，下载恢复的时候需要重新加入队列
                        PonkoApp.m3u8DownManager?.pause(vid, m3u8)
                        BKLog.d(CacheListAct.TAG, "点击暂停")
                    }
                    ACTION_DOWN_CLICK_READY -> {
                        BKLog.d(CacheListAct.TAG, "点击队列任务")
                        val vid = intent.getStringExtra("vid")
                        val m3u8 = intent.getStringExtra("m3u8")
                        val title = intent.getStringExtra("title")
                        val total = intent.getIntExtra("total", 0)
                        PonkoApp.m3u8DownManager?.pauseCurrent()
                        PonkoApp.m3u8DownManager?.pause(vid, m3u8)
                        PonkoApp.m3u8DownManager?.resume(
                                M3u8DownTask.Builder()
                                        .vid(vid)
                                        .name(title)
                                        .m3u8(m3u8)
                                        .fileSize(total.toLong())
                                        .build())
                        //再重新加入队列
                    }
                }

                //任务全部加载队列中
                for (data in datas!!) {
                    data.column_state = state
                }

                for (bean in adapter?.data!!) {
                    if (bean is CourseDbBean) {
                        val courseDbBean = bean as CourseDbBean
                        if (courseDbBean.column_complete != 1) {
                            courseDbBean.column_state = state
                        }
                    }
                }


                //刷新列表页面
                adapter?.notifyDataSetChanged()
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
                PonkoApp.m3u8DownManager?.newTasker(m3u8DownTask)?.enqueue(null)
            }
        }

        fun registerDownAllBroadCast() {
            if (broadcastManager == null) {
                broadcastManager = BroadcastManager.create(context)
            }
            broadcastManager?.registerReceiver(ACTION_DOWN_ALL, downAllBroadcastReceiver)
            broadcastManager?.registerReceiver(ACTION_DOWN_All_PAUSE, downAllBroadcastReceiver)
            broadcastManager?.registerReceiver(ACTION_DOWN_CLICK_PAUSE, downAllBroadcastReceiver)
            broadcastManager?.registerReceiver(ACTION_DOWN_CLICK_READY, downAllBroadcastReceiver)
        }

        fun unRegisterDownAllBroadCast() {
            CacheListAct.isDelete = false
            CacheListAct.isSelectAll = false
            CacheListAct.selectItems.clear()
            broadcastManager?.unRegisterReceiver(downAllBroadcastReceiver)
        }

        /**
         * 获取适配器
         */
        fun getAdapter(): BaseRvAdapter {
            adapter = object : BaseRvAdapter() {}
            return adapter!!
        }

        /**
         * 点击全选按钮
         */
        fun clickAllBtn() {
            BKLog.d("点击了全选")
            m.isSelectAll = !m.isSelectAll
            v.updateSelectDownList(m.isSelectAll)
        }

        /**
         * 点击删除按钮
         */
        fun clickDeleteBtn() {
            BKLog.d("点击了删除")
            val title = "确定要删除已缓存的课程？"
            val msg = "删除后不可恢复"
            v.showDeleteDialog(title, msg)
        }

        /**
         * 确认删除处理
         */
        fun enterDelete() {
            BKLog.d("确认删除")
            val iterator = copyAdapterData()?.iterator()
            while (iterator?.hasNext()!!) {
                val data = iterator.next()
                if (data is CourseDbBean) {
                    val courseDbBean = data as CourseDbBean
                    if (courseDbBean.isSelect) {
                        BKLog.d("选中删除 - ${courseDbBean.column_title}")
                        //删除下载库中的任务信息
                        PonkoApp.m3u8DownManager?.dao?.delete2(courseDbBean.column_vid)
                        //暂停下载线程
                        PonkoApp.m3u8DownManager?.pause(courseDbBean.column_vid, courseDbBean.column_m3u8_url)
                        //删除数据库中的数据
                        PonkoApp.courseDao?.deleteByVid(courseDbBean)
                        //删除本地缓存的数据
                        if (!TextUtils.isEmpty(PonkoApp.m3u8DownManager?.dir) && !TextUtils.isEmpty(M3u8Utils.m3u8Unique(courseDbBean.column_m3u8_url))) {
                            FileUtil.del(File(PonkoApp.m3u8DownManager?.path + File.separator + PonkoApp.m3u8DownManager?.dir + File.separator + M3u8Utils.m3u8Unique(courseDbBean.column_m3u8_url)))
                        }
                        //删除并让RecyclerView刷新
                        //notifyItem(courseDbBean)
                        v.updateDeleteDownList(courseDbBean)
                    }
                }
            }
        }

        /**
         * 获取数据
         */
        private fun copyAdapterData(): ArrayList<Any>? {
            val copyAdapterData = ArrayList<Any>()
            for (copyData in adapter?.data!!) {
                copyAdapterData.add(copyData)
            }
            return copyAdapterData
        }

        /**
         * 请求缓存列表数据 从数据库中查询
         */
        fun requestRefreshApi() {
            //从数据库中获取
            val datas = PonkoApp.courseDao?.selectBySpecialId(accept_special_id)
            //检查权限
            checkPermission()
            //下载监听
            downListener()
            v.requestCacheListSuccess(datas)
        }

        /**
         * 检查权限
         */
        @SuppressLint("CheckResult")
        private fun checkPermission() {
            RxPermissions(context as AppCompatActivity)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { aBoolean ->
                        if (aBoolean!!) {
                            //当所有权限都允许之后，返回true
                            //Toast.makeText(this, "文件授权成功", Toast.LENGTH_SHORT).show()

                        } else {
                            //只要有一个权限禁止，返回false，
                            //下一次申请只申请没通过申请的权限
                            BKLog.d("permissions", "btn_more_sametime：$aBoolean")
                        }
                    }
        }

        private fun downListener() {
            PonkoApp.m3u8DownManager?.listener = object : OnDownListener {

                override fun onPause(vid: String, url: String) {
                    BKLog.d(CacheListAct.TAG, "M3u8DownTasker $vid 已暂停....")
                    val courseDbBean = CourseDbBean()
                    courseDbBean.column_vid = vid
                    courseDbBean.column_m3u8_url = url
                    courseDbBean.column_complete = 0
                    courseDbBean.column_state = CacheListAct.DOWN_STATE_PAUSE
                    v.updateDownUI(vid, url, courseDbBean, CacheListAct.UPDATE_PAUSE)
                }

                override fun onQueue(vid: String?, url: String?) {
                    BKLog.d(CacheListAct.TAG, "M3u8DownTasker $vid 添加到队列中....")
                    val courseDbBean = CourseDbBean()
                    courseDbBean.column_vid = vid!!
                    courseDbBean.column_m3u8_url = url!!
                    courseDbBean.column_complete = 1
                    courseDbBean.column_state = CacheListAct.DOWN_STATE_READY
                    v.updateDownUI(vid, url, courseDbBean, CacheListAct.UPDATE_QUEUE)
                }

                override fun onStart(vid: String, url: String, m3u8Analysis: ArrayList<String>) {
                    BKLog.d(CacheListAct.TAG, "M3u8DownTasker 下载准备中....")
                    val courseDbBean = CourseDbBean()
                    courseDbBean.column_vid = vid
                    courseDbBean.column_m3u8_url = url
                    courseDbBean.column_complete = 0
                    courseDbBean.column_state = CacheListAct.DOWN_STATE_START
                    v.updateDownUI(vid, url, courseDbBean, CacheListAct.UPDATE_STATE)
                }

                override fun onComplete(vid: String, url: String) {
                    BKLog.d(CacheListAct.TAG, "M3u8DownTasker $url 下载完成")
                    val courseDbBean = CourseDbBean()
                    courseDbBean.column_vid = vid
                    courseDbBean.column_m3u8_url = url
                    courseDbBean.column_complete = 1
                    courseDbBean.column_state = CacheListAct.DOWN_STATE_COMPLETE
                    v.updateDownUI(vid, url, courseDbBean, CacheListAct.UPDATE_COMPLETE)
                }

                override fun onProcess(vid: String, url: String, progress: Int) {
                    BKLog.d(CacheListAct.TAG, "M3u8DownTasker $url 下载进度")
                    val courseDbBean = CourseDbBean()
                    courseDbBean.column_vid = vid
                    courseDbBean.column_m3u8_url = url
                    courseDbBean.column_progress = progress
                    courseDbBean.column_complete = 0
                    courseDbBean.column_state = CacheListAct.DOWN_STATE_PROCESS
                    v.updateDownUI(vid, url, courseDbBean, CacheListAct.UPDATE_PROCESS)
                }

                override fun onError(vid: String, url: String, msg: String) {
                    BKLog.d(CacheListAct.TAG, "M3u8DownTasker下载错误")
                    val courseDbBean = CourseDbBean()
                    courseDbBean.column_vid = vid
                    courseDbBean.column_m3u8_url = url
                    courseDbBean.column_complete = 0
                    courseDbBean.column_state = CacheListAct.DOWN_STATE_ERROR
                    v.updateDownUI(vid, url, courseDbBean, CacheListAct.UPDATE_ERROR)
                }
            }
        }
    }
}
package com.ponko.cn.module.study.v1.constract

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.db.bean.CourseDbBean
import com.ponko.cn.db.bean.CourseSpecialDbBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.m3u8downer.core.M3u8DownTask
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.my.option.CacheAct
import com.ponko.cn.module.study.v1.StudyCacheActivity
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.helper.TimerHelper
import com.xm.lib.common.http.NetworkUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.NumUtil
import com.xm.lib.component.OnCancelListener
import com.xm.lib.component.OnEnterListener
import retrofit2.Call
import retrofit2.Response

/**
 * 课程学习详情，下载页面契约类 - MVP模式
 */
class StudyCacheContract {
    /**
     * 视图层
     */
    interface V {
        /**
         * 请求课程接口成功
         */
        fun requestCourseDetailApiSuccess(coursesDetailCBean: CoursesDetailCBean?)

        /**
         * 请求课程接口失败
         */
        fun requestCourseDetailApiFailure()

        /**
         * 专题-课程ViewHolder
         */
        open class CacheListViewHolder(view: View) : BaseViewHolder(view) {

            private class ViewHolder private constructor(val tvTitle: TextView, val rv: RecyclerView) {
                companion object {

                    fun create(rootView: View): ViewHolder {
                        val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                        val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                        return ViewHolder(tvTitle, rv)
                    }
                }
            }

            private var viewHolder: ViewHolder? = null
            override fun bindData(d: Any, position: Int) {
                if (viewHolder == null) {
                    viewHolder = ViewHolder.create(itemView)
                }
                val chaptersBean = d as CoursesDetailCBean.ChaptersBean
                val context = itemView.context
                viewHolder?.tvTitle?.text = chaptersBean.chapter_name
                val adapter = object : BaseRvAdapter() {}
                adapter.addItemViewDelegate(0, CacheItemViewHolder::class.java, CoursesDetailCBean.ChaptersBean.SectionsBean::class.java, R.layout.item_cache)
                adapter.data?.addAll(chaptersBean.sections)
                viewHolder?.rv?.adapter = adapter
                viewHolder?.rv?.layoutManager = LinearLayoutManager(context)
            }
        }

        /**
         * 课程item列表ViewHolder
         */
        open class CacheItemViewHolder(view: View) : BaseViewHolder(view) {

            private class ViewHolder private constructor(val cb: CheckBox, val tvCourseName: TextView, val tvCourseTime: TextView, val tvLocal: TextView) {
                companion object {

                    fun create(rootView: View): ViewHolder {
                        val cb = rootView.findViewById<View>(R.id.cb) as CheckBox
                        val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                        val tvCourseTime = rootView.findViewById<View>(R.id.tv_course_time) as TextView
                        val tvLocal = rootView.findViewById<View>(R.id.tv_local) as TextView
                        return ViewHolder(cb, tvCourseName, tvCourseTime, tvLocal)
                    }
                }
            }

            private var viewHolder: ViewHolder? = null
            @SuppressLint("ResourceAsColor")
            override fun bindData(d: Any, position: Int) {
                if (viewHolder == null) {
                    viewHolder = ViewHolder.create(itemView)
                }
                val context = itemView.context
                val sectionsBean = d as CoursesDetailCBean.ChaptersBean.SectionsBean
                //设置课程章节
                viewHolder?.tvCourseName?.text = sectionsBean.name
                //课程时间
                viewHolder?.tvCourseTime?.text = NumUtil.getDecimalPoint(sectionsBean.duration.toInt() / 60f)
                //本地是否缓存了
                when {
                    PonkoApp.courseDao?.isComplete(sectionsBean.vid) == true -> displayLocal()
                    PonkoApp.courseDao?.exist(sectionsBean.vid) == false -> {
                        //选中状态
                        displayGeneral(sectionsBean)
                        viewHolder?.cb?.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                select(sectionsBean)
                            } else {
                                unSelect(sectionsBean)
                            }
                        }
                        itemView.setOnClickListener {
                            val select = if (isChecked()) {
                                unSelect(sectionsBean)
                            } else {
                                select(sectionsBean)
                            }
                            BKLog.d(StudyCacheActivity.TAG, "点击了${sectionsBean.name} - $select")
                        }
                    }
                    else -> {
                        viewHolder?.cb?.isChecked = false
                        viewHolder?.cb?.isEnabled = false
                        viewHolder?.tvLocal?.visibility = View.VISIBLE
                        viewHolder?.tvLocal?.text = "队列中"
                        viewHolder?.tvLocal?.setBackgroundColor(context.resources.getColor(R.color.red))
                    }
                }
            }

            private fun isChecked(): Boolean {
                return viewHolder?.cb?.isChecked!!
            }

            private fun select(sectionsBean: CoursesDetailCBean.ChaptersBean.SectionsBean): String {
                viewHolder?.cb?.isChecked = true
                if (!StudyCacheActivity.SleSections.contains(sectionsBean)) {
                    StudyCacheActivity.SleSections.add(sectionsBean)
                }
                //ToastUtil.show("选中--------" + StudyCacheActivity.SleSections.toString())
                return "选中"
            }

            private fun unSelect(sectionsBean: CoursesDetailCBean.ChaptersBean.SectionsBean): String {
                viewHolder?.cb?.isChecked = false
                if (StudyCacheActivity.SleSections.contains(sectionsBean)) {
                    StudyCacheActivity.SleSections.remove(sectionsBean)
                }
                //ToastUtil.show("未选中--------" + StudyCacheActivity.SleSections.toString())
                return "未选中"
            }

            private fun displayGeneral(sectionsBean: CoursesDetailCBean.ChaptersBean.SectionsBean) {
                viewHolder?.cb?.isChecked = StudyCacheActivity.isSelectAll
                viewHolder?.cb?.isEnabled = true
                if (StudyCacheActivity.isSelectAll) {
                    StudyCacheActivity.SleSections.add(sectionsBean)
                } else {
                    StudyCacheActivity.SleSections.remove(sectionsBean)
                }
            }

            private fun displayLocal() {
                viewHolder?.tvLocal?.visibility = View.VISIBLE
                //不能点击
                viewHolder?.cb?.isChecked = true
                viewHolder?.cb?.isEnabled = false
                //修改颜色样式
                viewHolder?.tvCourseName?.setTextColor(Color.parseColor("#888888"))
                viewHolder?.tvCourseTime?.setTextColor(Color.parseColor("#888888"))
            }
        }
    }

    /**
     * 数据层
     */
    class M {
        /**
         * 专题唯一标识
         */
        var typeId: String = ""
        /**
         * 专题老师
         */
        var teachers: String = ""
        /**
         * 专题课程数量
         */
        var num: Int = 0
        /**
         * 专题课程总时长
         */
        var duration: Int = 0
        /**
         * 请求的课程详情实体信息
         */
        var coursesDetailCBean: CoursesDetailCBean? = null

        /**
         * 请求课程详情信息
         */
        fun requestCourseDetailApi(typeId: String, callback: HttpCallBack<CoursesDetailCBean>) {
            PonkoApp.studyApi?.getCourseDetail(typeId)?.enqueue(callback)
        }
    }

    /**
     * 控制层
     */
    class Present(val context: Context, val v: V) {

        private val m = M()
        /**
         * 适配器
         */
        private var adapter: BaseRvAdapter? = null
        /**
         * 定时器
         */
        private var timerHelper: TimerHelper? = TimerHelper()

        /**
         * 获取RecyclerView适配器
         */
        fun getAdapter(): BaseRvAdapter? {
            adapter = object : BaseRvAdapter() {}
            return adapter
        }

        /**
         * 点击全选
         */
        fun clickAllSelect() {
            BKLog.d(StudyCacheActivity.TAG, "点击全选")
            StudyCacheActivity.isSelectAll = !StudyCacheActivity.isSelectAll
            adapter?.notifyDataSetChanged()
        }

        /**
         * 点击下载
         */
        fun clickDown() {
            if (!NetworkUtil.isNetworkConnected(context)) {
                ToastUtil.show("缓存失败,请检查您的网络...")
                return
            }

            if (StudyCacheActivity.SleSections.isEmpty()) {
                ToastUtil.show("未选中数据")
                return
            }

            BKLog.d(StudyCacheActivity.TAG, "下载")
            //跳转到下载测试页面
            //M3u8DownerTextAct.start(this, typeId, teachers, num.toLong(), duration.toLong())
            //专题加入数据库中
            insertToCourseDb()
            //显示加载进度框
            DialogUtil.showProcess(context)
//            intoCachePage()
        }

        private fun intoCachePage() {
            timerHelper?.start(object : TimerHelper.OnDelayTimerListener {
                override fun onDelayTimerFinish() {
                    //隐藏加载进度框
                    DialogUtil.hideProcess()
                    //开始下载
                    down(PonkoApp.courseDao?.selectAll())
//                    down(PonkoApp.courseDao?.selectBySpecialId(typeId))//ps: 根据专题去寻找课程哟
                    DialogUtil.show(context, "提示", "是否进入缓存页面？", true, object : OnEnterListener {
                        override fun onEnter(dlg: AlertDialog) {
                            ActivityUtil.startActivity(context, Intent(context, CacheAct::class.java))
                            (context as Activity).finish()
                        }
                    }, object : OnCancelListener {
                        override fun onCancel(dlg: AlertDialog) {

                            dlg.dismiss()
                        }
                    })
                    //ToastUtil.show("已加入缓存队列")
                }
            }, 10)
        }

        /**
         * 点击查看缓存列表
         */
        fun clickLook() {
            ActivityUtil.startActivity(context, Intent(context, CacheAct::class.java))
            (context as Activity).finish()
        }

        /**
         * 插入到课程数据库表中
         */
        private fun insertToCourseDb() {
            //下载专题存入数据
            //val courseSpecialDao = CourseSpecialDao(PonkoApp.dbHelp?.writableDatabase)
            val courseSpecialDbBean = CourseSpecialDbBean()
            courseSpecialDbBean.uid = "uid"
            courseSpecialDbBean.special_id = m.typeId
            courseSpecialDbBean.title = m.coursesDetailCBean?.title!!
            courseSpecialDbBean.cover = m.coursesDetailCBean?.image!!
            courseSpecialDbBean.teacher = m.teachers
            courseSpecialDbBean.num = m.num
            PonkoApp.courseSpecialDao?.insert(courseSpecialDbBean)
            BKLog.d(StudyCacheActivity.TAG, ">专题信息插入数据库:" + PonkoApp.courseSpecialDao?.selectAll().toString())

            //下载专题下的课程列表 PS:后台有些视频信息大小会有值，有些没有值
            //val courseDao = CourseDao(PonkoApp.dbHelp?.writableDatabase)
            var count = 0
            for (section in StudyCacheActivity.SleSections) {

                if (MediaUitl.fileSize(section) == 0 && false) {
                    MediaUitl.getUrlByVid(section.vid, object : MediaUitl.OnPlayUrlListener {
                        override fun onFailure() {
                            BKLog.e(StudyCacheActivity.TAG, "获取视频信息失败")
                            ToastUtil.show("获取视频信息失败,缓存失败...")
                        }

                        override fun onSuccess(url: String, size: Int?) {
                            val courseDbBean = CourseDbBean()
                            courseDbBean.column_uid = "uuid"
                            courseDbBean.column_special_id = m.typeId
                            courseDbBean.column_course_id = section.id
                            courseDbBean.column_cover = section.avatar
                            courseDbBean.column_title = section.name
                            courseDbBean.column_total = size!!
                            courseDbBean.column_progress = 0               //ps:进度回调中更新此字段
                            courseDbBean.column_complete = 0               //ps:完成回调中更新此字段
                            courseDbBean.column_m3u8_url = ""              //ps:调用保利威视接口时，更新此字段，其实不填也没有关，系详细请查看MediaUitl.getUrlByVid(...)
                            courseDbBean.column_key_ts_url = ""
                            courseDbBean.column_down_path = ""             //ps:完成回调中更新此字段
                            courseDbBean.column_state = "点击下载"
                            courseDbBean.column_vid = section.vid
                            PonkoApp.courseDao?.insert(courseDbBean)
                            BKLog.d(StudyCacheActivity.TAG, "课程信息插入数据库，接口返回的大小为0，所以请求了视频信息接口" + courseDbBean.toString())
                            if (count == (StudyCacheActivity.SleSections.size - 1)) {
                                //ToastUtil.show("课程信息插入数据库 1 --------------------" + PonkoApp.courseDao?.selectAll().toString())
                                intoCachePage()
                            }
                            count++
                        }
                    })
                } else {
                    val courseDbBean = CourseDbBean()
                    courseDbBean.column_uid = "uuid"
                    courseDbBean.column_special_id = m.typeId
                    courseDbBean.column_course_id = section.id
                    courseDbBean.column_cover = section.avatar
                    courseDbBean.column_title = section.name
                    courseDbBean.column_total = MediaUitl.fileSize(section)
                    courseDbBean.column_progress = 0               //ps:进度回调中更新此字段
                    courseDbBean.column_complete = 0               //ps:完成回调中更新此字段
                    courseDbBean.column_m3u8_url = ""              //ps:调用保利威视接口时，更新此字段，其实不填也没有关，系详细请查看MediaUitl.getUrlByVid(...)
                    courseDbBean.column_key_ts_url = ""
                    courseDbBean.column_down_path = ""             //ps:完成回调中更新此字段
                    courseDbBean.column_state = "点击下载"
                    courseDbBean.column_vid = section.vid
                    PonkoApp.courseDao?.insert(courseDbBean)
                    BKLog.d(StudyCacheActivity.TAG, "课程信息插入数据库:" + courseDbBean.toString())
                    if (count == (StudyCacheActivity.SleSections.size - 1)) {
                        //ToastUtil.show("课程信息插入数据库 2 --------------------" + PonkoApp.courseDao?.selectAll().toString())
                        intoCachePage()
                    }
                    count++
                }
            }
//            StudyCacheActivity.SleSections.clear()
        }

        /**
         * 创建下载任务
         */
        private fun down(datas: ArrayList<CourseDbBean>?) {
            for (course in datas?.iterator()!!) {
                if (TextUtils.isEmpty(course.column_vid))
                    continue
                val m3u8DownTask = M3u8DownTask.Builder()
                        .vid(course.column_vid)
                        .m3u8(course.column_m3u8_url)
                        .name(course.column_title)
                        .fileSize(course.column_total.toLong())
                        .build()
                PonkoApp.m3u8DownManager?.newTasker(m3u8DownTask)?.enqueue(null)
            }
        }

        /**
         * 窗口销毁
         */
        fun onDestroy() {
            StudyCacheActivity.SleSections.clear()
            StudyCacheActivity.isSelectAll = false
            timerHelper?.stop()
            DialogUtil.hide()
            DialogUtil.hideProcess()
        }

        /**
         * 获取其他窗口传递过来的数据
         */
        fun getIntentExtra(intent: Intent?) {
            try {
                m.typeId = intent?.getStringExtra("typeId")!!
                m.teachers = intent.getStringExtra("teachers")
                m.num = intent.getLongExtra("num", 0L).toInt()
                m.duration = intent.getLongExtra("duration", 0).toInt()
                BKLog.d(StudyCacheActivity.TAG, "接受课程详情页发送过来的信息，专题id:${m.typeId}老师${m.teachers} 集数${m.num} 总时长${m.duration / 60f / 60f}小时")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /**
         * 请求数据课程列表数据
         */
        fun requestCourseDetailApi() {
            m.requestCourseDetailApi(m.typeId, object : HttpCallBack<CoursesDetailCBean>() {
                override fun onSuccess(call: Call<CoursesDetailCBean>?, response: Response<CoursesDetailCBean>?) {
                    m.coursesDetailCBean = response?.body()
                    v.requestCourseDetailApiSuccess(m.coursesDetailCBean)
                }

                override fun onFailure(call: Call<CoursesDetailCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    v.requestCourseDetailApiFailure()
                }
            })
        }
    }
}
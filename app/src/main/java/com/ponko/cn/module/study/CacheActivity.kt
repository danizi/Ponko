package com.ponko.cn.module.study

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.courseDao
import com.ponko.cn.app.PonkoApp.Companion.courseSpecialDao
import com.ponko.cn.app.PonkoApp.Companion.m3u8DownManager
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.db.bean.CourseDbBean
import com.ponko.cn.db.bean.CourseSpecialDbBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.m3u8downer.core.M3u8DownTask
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.MediaUitl.fileSize
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.NumUtil
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.common.util.TimerHelper
import retrofit2.Call
import retrofit2.Response

/**
 * 专题-选集缓存页面
 */
class CacheActivity : RefreshLoadAct<Any, CoursesDetailCBean>() {

    companion object {
        /**
         * 日志标识
         */
        private const val TAG = "CacheActivity"
        /**
         * 选中专题下的课程数量
         */
        val SleSections = ArrayList<CoursesDetailCBean.ChaptersBean.SectionsBean>()

        /**
         * @param context  上下文对象
         * @param typeId   专题唯一标识
         * @param teachers 专题老师
         * @param num      专题课程数量
         * @param duration 专题课程总时长
         */
        fun start(context: Context, typeId: String, teachers: String, num: Long, duration: Long) {
            val intent = Intent(context, CacheActivity::class.java)
            intent.putExtra("typeId", typeId)
            intent.putExtra("teachers", teachers)
            intent.putExtra("num", num)
            intent.putExtra("duration", duration)
            ActivityUtil.startActivity(context, intent)
        }
    }

    /**
     * 专题唯一标识
     */
    private var typeId: String = ""
    /**
     * 专题老师
     */
    private var teachers: String = ""
    /**
     * 专题课程数量
     */
    private var num: Int = 0
    /**
     * 专题课程总时长
     */
    private var duration: Int = 0
    /**
     * 全选按钮
     */
    private var btnAllSelect: Button? = null
    /**
     * 下载按钮
     */
    private var btnDown: Button? = null
    /**
     * 专题-选集返回实体
     */
    private var coursesDetailCBean: CoursesDetailCBean? = null
    /**
     * 定时器
     */
    private var timerHelper: TimerHelper? = TimerHelper()

    override fun getLayoutId(): Int {
        return R.layout.activity_cache2
    }

    override fun initDisplay() {
        disableLoad = true
        disableRefresh = true
        addItemDecoration = false
        super.initDisplay()
        BarUtil.addBar1(this, viewHolder?.toolbar, "选集缓存")
    }

    override fun findViews() {
        super.findViews()
        btnAllSelect = findViewById(R.id.btn_all_select)
        btnDown = findViewById(R.id.btn_down)
    }

    override fun iniEvent() {
        super.iniEvent()
        btnAllSelect?.setOnClickListener {
            BKLog.d(TAG, "点击全选")
        }
        btnDown?.setOnClickListener {
            BKLog.d(TAG, "下载")
            //跳转到下载测试页面
            //M3u8DownerTextAct.start(this, typeId, teachers, num.toLong(), duration.toLong())
            //专题加入数据库中
            insertToCourseDb()
            timerHelper?.start(object : TimerHelper.OnDelayTimerListener {
                override fun onDelayTimerFinish() {
                    //开始下载
                    down(PonkoApp.courseDao?.selectAll())
                }
            }, 1000)
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

    private fun insertToCourseDb() {
        //下载专题存入数据
        //val courseSpecialDao = CourseSpecialDao(PonkoApp.dbHelp?.writableDatabase)
        val courseSpecialDbBean = CourseSpecialDbBean()
        courseSpecialDbBean.uid = "uid"
        courseSpecialDbBean.special_id = typeId
        courseSpecialDbBean.title = coursesDetailCBean?.title!!
        courseSpecialDbBean.cover = coursesDetailCBean?.image!!
        courseSpecialDbBean.teacher = teachers
        courseSpecialDbBean.num = num
        courseSpecialDao?.insert(courseSpecialDbBean)
        BKLog.d(TAG, ">专题信息插入数据库:" + courseSpecialDbBean.toString())

        //下载专题下的课程列表 PS:后台有些视频信息大小会有值，有些没有值-蛋疼
        //val courseDao = CourseDao(PonkoApp.dbHelp?.writableDatabase)
        for (section in SleSections) {
            if (fileSize(section) == 0) {
                MediaUitl.getUrlByVid(section.vid, object : MediaUitl.OnPlayUrlListener {
                    override fun onFailure() {
                        BKLog.e(TAG, "获取视频信息失败")
                    }

                    override fun onSuccess(url: String, size: Int?) {
                        val courseDbBean = CourseDbBean()
                        courseDbBean.column_uid = "uuid"
                        courseDbBean.column_special_id = typeId
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
                        courseDao?.insert(courseDbBean)
                        BKLog.d(TAG, "课程信息插入数据库，接口返回的大小为0，所以请求了视频信息接口" + courseDbBean.toString())
                    }
                })
            } else {
                val courseDbBean = CourseDbBean()
                courseDbBean.column_uid = "uuid"
                courseDbBean.column_special_id = typeId
                courseDbBean.column_course_id = section.id
                courseDbBean.column_cover = section.avatar
                courseDbBean.column_title = section.name
                courseDbBean.column_total = fileSize(section)
                courseDbBean.column_progress = 0               //ps:进度回调中更新此字段
                courseDbBean.column_complete = 0               //ps:完成回调中更新此字段
                courseDbBean.column_m3u8_url = ""              //ps:调用保利威视接口时，更新此字段，其实不填也没有关，系详细请查看MediaUitl.getUrlByVid(...)
                courseDbBean.column_key_ts_url = ""
                courseDbBean.column_down_path = ""             //ps:完成回调中更新此字段
                courseDbBean.column_state = "点击下载"
                courseDbBean.column_vid = section.vid
                courseDao?.insert(courseDbBean)
                BKLog.d(TAG, "课程信息插入数据库:" + courseDbBean.toString())

            }
        }
        SleSections.clear()
    }

    override fun iniData() {
        typeId = intent.getStringExtra("typeId")
        teachers = intent.getStringExtra("teachers")
        num = intent.getLongExtra("num", 0L).toInt()
        duration = intent.getLongExtra("duration", 0).toInt()
        BKLog.d(TAG, "接受课程详情页发送过来的信息，专题id:$typeId 老师$teachers 集数$num 总时长${duration / 60f / 60f}小时")
        super.iniData()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(CacheListViewHolder::class.java),
                arrayOf(CoursesDetailCBean.ChaptersBean::class.java),
                arrayOf(R.layout.item_cache_list)
        )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        PonkoApp.studyApi?.getCourseDetail(typeId)?.enqueue(object : HttpCallBack<CoursesDetailCBean>() {
            override fun onSuccess(call: Call<CoursesDetailCBean>?, response: Response<CoursesDetailCBean>?) {
                coursesDetailCBean = response?.body()
                requestRefreshSuccess(coursesDetailCBean)
            }
        })
    }

    override fun multiTypeData(body: CoursesDetailCBean?): List<Any> {
        return body?.chapters!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

    /**
     * 专题-课程ViewHolder
     */
    private class CacheListViewHolder(view: View) : BaseViewHolder(view) {

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
    private class CacheItemViewHolder(view: View) : BaseViewHolder(view) {

        private class ViewHolder private constructor(val textView2: CheckBox, val tvCourseName: TextView, val tvCourseTime: TextView) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val textView2 = rootView.findViewById<View>(R.id.textView2) as CheckBox
                    val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                    val tvCourseTime = rootView.findViewById<View>(R.id.tv_course_time) as TextView
                    return ViewHolder(textView2, tvCourseName, tvCourseTime)
                }
            }
        }

        private var viewHolder: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }
            val context = itemView.context
            val sectionsBean = d as CoursesDetailCBean.ChaptersBean.SectionsBean
            viewHolder?.tvCourseName?.text = sectionsBean.name
            viewHolder?.tvCourseTime?.text = NumUtil.getDecimalPoint(sectionsBean.duration.toInt() / 60f)
            itemView.setOnClickListener {
                //viewHolder?.textView2?.setBackgroundResource(R.mipmap.collection_p_check)
                //viewHolder?.textView2?.setBackgroundResource(R.drawable.gray_circle)
                if (viewHolder?.textView2?.isChecked == true) {
                    BKLog.d(TAG, "取消选择")
                    viewHolder?.textView2?.isChecked = false
                    SleSections.remove(sectionsBean)
                } else {
                    BKLog.d(TAG, "选择")
                    viewHolder?.textView2?.isChecked = true
                    SleSections.add(sectionsBean)
                }

                BKLog.d(TAG, "点击了${sectionsBean.name}")
            }
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_cache2)
//    }

    override fun onDestroy() {
        super.onDestroy()
        SleSections.clear()
        timerHelper?.stop()
    }
}

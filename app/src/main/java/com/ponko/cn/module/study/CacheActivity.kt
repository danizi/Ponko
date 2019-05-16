package com.ponko.cn.module.study

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.google.gson.Gson
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.app.PonkoApp.Companion.courseDao
import com.ponko.cn.app.PonkoApp.Companion.courseSpecialDao
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.bean.M3u8InfoBean
import com.ponko.cn.db.bean.CourseDbBean
import com.ponko.cn.db.bean.CourseSpecialDbBean
import com.ponko.cn.db.dao.CourseDao
import com.ponko.cn.db.dao.CourseSpecialDao
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.m3u8downer.M3u8DownerTextAct
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.NumUtil
import com.xm.lib.downloader.DownManager
import com.xm.lib.downloader.task.DownTask
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
            M3u8DownerTextAct.start(this, typeId, teachers, num.toLong(), duration.toLong())
            //专题加入数据库中
            insertToCourseDb()
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
        //BKLog.d(TAG, ">专题数据库内容:" + courseSpecialDao?.selectAll()?.toArray())

        //下载专题下的课程列表
        //val courseDao = CourseDao(PonkoApp.dbHelp?.writableDatabase)
        for (section in SleSections) {
            val courseDbBean = CourseDbBean()
            courseDbBean.column_uid = "uuid"
            courseDbBean.column_special_id = typeId
            courseDbBean.column_course_id = section.id
            courseDbBean.column_cover = section.avatar
            courseDbBean.column_title = section.name
            courseDbBean.column_total = section.filesize1
            courseDbBean.column_progress = 0
            courseDbBean.column_complete = 0
            courseDbBean.column_m3u8_url = ""
            courseDbBean.column_key_ts_url = Gson().toJson(M3u8InfoBean())
            courseDbBean.column_down_path = ""
            courseDbBean.column_state = "点击下载"
            courseDbBean.column_vid = section.vid
            courseDao?.insert(courseDbBean)
        }
        //BKLog.d(TAG, "课程数据库内容:" + courseDao?.selectAll()?.toArray())
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
    }
}

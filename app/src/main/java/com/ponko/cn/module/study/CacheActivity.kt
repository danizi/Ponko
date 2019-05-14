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
 * 课程缓存页面
 */
class CacheActivity : RefreshLoadAct<Any, CoursesDetailCBean>() {

    companion object {
        val SleSections = ArrayList<CoursesDetailCBean.ChaptersBean.SectionsBean>()
        fun start(context: Context, typeId: String, teachers: String, num: Long, duration: Long) {
            val intent = Intent(context, CacheActivity::class.java)
            intent.putExtra("typeId", typeId)
            intent.putExtra("teachers", teachers)
            intent.putExtra("num", num)
            intent.putExtra("duration", duration)
            ActivityUtil.startActivity(context, intent)
        }
    }

    private var typeId: String = ""
    private var teachers: String = ""
    private var num: Int = 0
    private var duration: Int = 0
    private var btnAllSelect: Button? = null
    private var btnDown: Button? = null

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
            BKLog.d("点击全选")
        }
        btnDown?.setOnClickListener {
            BKLog.d("下载")

            startActivity(Intent(this,M3u8DownerTextAct::class.java))

            //专题加入数据库中
            val courseSpecialDao = CourseSpecialDao(PonkoApp.dbHelp?.writableDatabase)
            val courseSpecialDbBean = CourseSpecialDbBean()
            courseSpecialDbBean.uid = "uid"
            courseSpecialDbBean.special_id = typeId
            courseSpecialDbBean.title = coursesDetailCBean?.title!!
            courseSpecialDbBean.cover = coursesDetailCBean?.image!!
            courseSpecialDbBean.teacher = teachers
            courseSpecialDbBean.num = num
            courseSpecialDao.insert(courseSpecialDbBean)

            //课程保存到数据库中
            val courseDao = CourseDao(PonkoApp.dbHelp?.writableDatabase)
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
                courseDbBean.column_m3u8_url = section.hls1
                courseDbBean.column_key_ts_url = Gson().toJson(M3u8InfoBean())
                courseDbBean.column_down_path = "XmDown/sadfasdfsdaf"
                courseDao.insert(courseDbBean)
            }
            BKLog.d("专题数据库内容:" + courseSpecialDao.selectAll().toArray())
            BKLog.d("课程数据库内容:" + courseDao.selectAll().toArray())

            //下载
//            val downManager=  DownManager.createDownManager(this)
//            val task = DownTask()
//            task.url = ""
//            downManager.createDownTasker(task)
            SleSections.clear()
        }
    }

    override fun iniData() {
        typeId = intent.getStringExtra("typeId")
        teachers = intent.getStringExtra("teachers")
        num = intent.getIntExtra("num", 0)
        duration = intent.getIntExtra("duration", 0)
        BKLog.d("接受课程详情页发送过来的信息，专题id:$typeId 老师$teachers 集数$num 总时长${duration / 60f / 60f}小时")
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

    private var coursesDetailCBean: CoursesDetailCBean? = null
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
     * 缓存列表
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
     * 缓存item列表
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
                    BKLog.d("取消选择")
                    viewHolder?.textView2?.isChecked = false
                    SleSections.remove(sectionsBean)
                } else {
                    BKLog.d("选择")
                    viewHolder?.textView2?.isChecked = true
                    SleSections.add(sectionsBean)
                }

                BKLog.d("点击了${sectionsBean.name}")
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

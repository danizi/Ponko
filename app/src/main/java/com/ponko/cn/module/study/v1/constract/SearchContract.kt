package com.ponko.cn.module.study.v1.constract

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.SearchCBean
import com.ponko.cn.bean.SearchRecordCBean
import com.ponko.cn.constant.Constants
import com.ponko.cn.constant.Constants.ACTION_CLICK_SEARCH_TEACHER_ITEM
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.m3u8downer.core.M3u8Utils
import com.ponko.cn.module.study.v1.StudyCourseDetailActivity
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.NumUtil
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.media.broadcast.BroadcastManager
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import retrofit2.Call
import retrofit2.Response

/**
 * 搜索页面契约类 - MVP模式
 */
class SearchContract {
    /**
     * 视图层
     */
    interface V {
        /**
         * 展示搜索结果
         */
        fun displaySearchResult(body: SearchCBean?)

        /**
         * 刷新搜索页面
         */
        fun notifySearchPage()

        /**
         * 搜索框显示
         */
        fun displaySearchText(record: String?)

        /**
         * 最近学习记录
         */
        class StudyRecordViewHold(view: View) : BaseViewHolder(view) {

            private class ViewHolder private constructor(val linearLayout: LinearLayout, val rv: RecyclerView) {
                companion object {
                    fun create(rootView: View): ViewHolder {
                        val linearLayout = rootView.findViewById<View>(R.id.linearLayout) as LinearLayout
                        val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                        return ViewHolder(linearLayout, rv)
                    }
                }
            }

            private var ui: ViewHolder? = null

            override fun bindData(d: Any, position: Int) {
                if (ui == null) {
                    ui = ViewHolder.create(itemView)
                }
                val context = itemView.context
                val studyRecordBean = d as M.StudyRecordBean
                val adapter = object : BaseRvAdapter() {}
                adapter.addItemViewDelegate(0, SubStudyRecordViewHold::class.java, SearchRecordCBean.SectionsBean::class.java, R.layout.item_search_sub_tv)
                adapter.data?.addAll(studyRecordBean.sections!!)
                ui?.rv?.layoutManager = LinearLayoutManager(context)
                ui?.rv?.adapter = adapter
                ui?.rv?.isFocusableInTouchMode = false
                ui?.rv?.requestFocus()
            }
        }

        /**
         * 授课老师
         */
        class GiveInstructionViewHold(view: View) : BaseViewHolder(view) {
            private class ViewHolder private constructor(val linearLayout: LinearLayout, val viewTagFlow: TagFlowLayout) {
                companion object {

                    fun create(rootView: View): ViewHolder {
                        val linearLayout = rootView.findViewById<View>(R.id.linearLayout) as LinearLayout
                        val viewTagFlow = rootView.findViewById<View>(R.id.view_tag_flow) as TagFlowLayout
                        return ViewHolder(linearLayout, viewTagFlow)
                    }
                }
            }

            private var ui: ViewHolder? = null
            override fun bindData(d: Any, position: Int) {
                if (ui == null) {
                    ui = ViewHolder.create(itemView)
                }
                val giveInstructionBean = d as M.GiveInstructionBean
                val context = itemView.context
                ui?.viewTagFlow?.adapter = object : TagAdapter<String>(giveInstructionBean.teachers) {
                    override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                        val view = ViewUtil.viewById(context, R.layout.item_search_flow_tag, ui?.viewTagFlow)
                        val tv = view?.findViewById<TextView>(R.id.tv_tag)!!
                        tv.text = t
                        return tv
                    }
                }
                ui?.viewTagFlow?.setOnTagClickListener { view, position, parent ->
                    BKLog.d("点击了${giveInstructionBean.teachers!![position]}")
                    val intent = Intent()
                    intent.putExtra("search", giveInstructionBean.teachers!![position])
                    intent.action = ACTION_CLICK_SEARCH_TEACHER_ITEM
                    context.sendBroadcast(intent)
                    true
                }
            }
        }

        /**
         * 最近搜索记录
         */
        class SearchRecordViewHold(view: View) : BaseViewHolder(view) {

            private class ViewHolder private constructor(val linearLayout: LinearLayout, val rv: RecyclerView) {
                companion object {

                    fun create(rootView: View): ViewHolder {
                        val linearLayout = rootView.findViewById<View>(R.id.linearLayout) as LinearLayout
                        val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                        return ViewHolder(linearLayout, rv)
                    }
                }
            }

            private var ui: ViewHolder? = null
            val adapter = object : BaseRvAdapter() {}

            override fun bindData(d: Any, position: Int) {
                if (ui == null) {
                    ui = ViewHolder.create(itemView)
                }
                val context = itemView.context
                val searchRecordBean = d as M.SearchRecordBean
                //val adapter = object : BaseRvAdapter() {}
                adapter.addItemViewDelegate(0, SubSearchRecordViewHold::class.java, String::class.java, R.layout.item_search_sub_tv)
                adapter.data?.addAll(searchRecordBean.search!!)
                ui?.rv?.layoutManager = LinearLayoutManager(context)
                ui?.rv?.adapter = adapter
                ui?.rv?.isFocusableInTouchMode = false
                ui?.rv?.requestFocus()
            }
        }

        /**
         * 学习子记录显示
         */
        class SubStudyRecordViewHold(view: View) : BaseViewHolder(view) {


            private class ViewHolder private constructor(val tv: TextView, val clear: RelativeLayout, val divider: View) {
                companion object {

                    fun create(rootView: View): ViewHolder {
                        val tv = rootView.findViewById<View>(R.id.tv) as TextView
                        val clear = rootView.findViewById<View>(R.id.rl_clear) as RelativeLayout
                        val divider = rootView.findViewById(R.id.divider) as View
                        return ViewHolder(tv, clear, divider)
                    }
                }
            }

            private var ui: ViewHolder? = null

            override fun bindData(d: Any, position: Int) {}

            override fun bindData(adapter: BaseRvAdapter, d: Any, position: Int) {
                super.bindData(adapter, d, position)
                if (ui == null) {
                    ui = ViewHolder.create(itemView)
                }
                val context = itemView.context
                val sectionsBean = d as SearchRecordCBean.SectionsBean
                ui?.tv?.text = sectionsBean.name
                itemView.setOnClickListener {
                    BKLog.d("跳转到课程")
                    StudyCourseDetailActivity.startFromSearch(context, sectionsBean.courseId, "", 0L, 0L, sectionsBean.name)
                }
            }
        }

        /**
         * 搜索子记录显示
         */
        class SubSearchRecordViewHold(view: View) : BaseViewHolder(view) {

            private class ViewHolder private constructor(val tv: TextView, val clear: RelativeLayout, val divider: View) {
                companion object {

                    fun create(rootView: View): ViewHolder {
                        val tv = rootView.findViewById<View>(R.id.tv) as TextView
                        val clear = rootView.findViewById<View>(R.id.rl_clear) as RelativeLayout
                        val divider = rootView.findViewById(R.id.divider) as View
                        return ViewHolder(tv, clear, divider)
                    }
                }
            }

            private var ui: ViewHolder? = null


            override fun bindData(adapter: BaseRvAdapter, d: Any, position: Int) {
                if (ui == null) {
                    ui = ViewHolder.create(itemView)
                }
                val context = itemView.context
                val record = d as String
                ui?.tv?.text = record
                ui?.clear?.visibility = View.VISIBLE
                itemView.setOnClickListener {
                    BKLog.d("点击搜索记录条目")
                    val intent = Intent(Constants.ACTION_CLICK_SEARCH_RECORD_ITEM)
                    intent.putExtra("search", record)
                    context.sendBroadcast(intent)
                }
                ui?.clear?.setOnClickListener {
                    for (i in 0..(adapter.data?.size!! - 1)) {
                        if ((adapter.data!![i] as String) == record) {
                            adapter.data!!.remove(record)
                            adapter.notifyItemRemoved(i)
                            adapter.notifyItemChanged(i)

                            val records = ArrayList<String>()
                            for (record in adapter.data!!) {
                                records.add(record as String)
                            }
                            CacheUtil.putSearchRecord(M3u8Utils.listToStr(records)!!)

                            BKLog.d("点击删除${record}搜索记录条目")
                            break
                        }
                    }
                }
            }

            override fun bindData(d: Any, position: Int) {

            }
        }


        /**
         * 搜索[结果]课程
         */
        class CoursesViewHold(view: View) : BaseViewHolder(view) {

            private class ViewHolder private constructor(val ivCover: ImageView, val tvCourseTitle: TextView, val tvTeacher: TextView, val courseNumber: TextView, val imageView2: ImageView) {
                companion object {

                    fun create(rootView: View): ViewHolder {
                        val ivCover = rootView.findViewById<View>(R.id.iv_cover) as ImageView
                        val tvCourseTitle = rootView.findViewById<View>(R.id.tv_course_title) as TextView
                        val tvTeacher = rootView.findViewById<View>(R.id.tv_teacher) as TextView
                        val courseNumber = rootView.findViewById<View>(R.id.course_number) as TextView
                        val imageView2 = rootView.findViewById<View>(R.id.imageView2) as ImageView
                        return ViewHolder(ivCover, tvCourseTitle, tvTeacher, courseNumber, imageView2)
                    }
                }
            }

            private var ui: ViewHolder? = null
            @SuppressLint("SetTextI18n")
            override fun bindData(d: Any, position: Int) {
                if (ui == null) {
                    ui = ViewHolder.create(itemView)
                }
                val coursesBean = d as SearchCBean.CoursesBean
                val context = itemView.context

                Glide.with(context, coursesBean.image, ui?.ivCover)
                ui?.tvCourseTitle?.text = coursesBean.title.toString()

                var teachers = ""
                for (i in 0..(coursesBean.teachers.size - 1)) {
                    teachers += if (i == 0) {
                        coursesBean.teachers[i]
                    } else {
                        " | " + coursesBean.teachers[i]
                    }
                }
                ui?.tvTeacher?.text = "${teachers}老师"
                ui?.courseNumber?.text = "共${coursesBean.num}集 | ${NumUtil.getDecimalPoint(coursesBean.duration?.toFloat()!! / 60f / 60f)}小时"
                itemView.setOnClickListener {
                    StudyCourseDetailActivity.start(context, coursesBean.id, teachers, coursesBean.num.toLong(), coursesBean.duration.toLong())
                }
            }
        }

        /**
         * 搜索[结果]章节
         */
        class SectionsViewHold(view: View) : BaseViewHolder(view) {

            private class ViewHolder private constructor(val tv: TextView, val rlClear: RelativeLayout, val divider: View) {
                companion object {

                    fun create(rootView: View): ViewHolder {
                        val tv = rootView.findViewById<View>(R.id.tv) as TextView
                        val rlClear = rootView.findViewById<View>(R.id.rl_clear) as RelativeLayout
                        val divider = rootView.findViewById(R.id.divider) as View
                        return ViewHolder(tv, rlClear, divider)
                    }
                }
            }

            private var ui: ViewHolder? = null
            override fun bindData(d: Any, position: Int) {
                if (ui == null) {
                    ui = ViewHolder.create(itemView)
                }
                val sectionsBean = d as SearchCBean.SectionsBean
                val context = itemView.context
                ui?.tv?.text = sectionsBean.sectionName
                itemView.setOnClickListener {
                    BKLog.d("点击章节：${sectionsBean.sectionName}")
                    StudyCourseDetailActivity.start(context, sectionsBean.courseId, "", 0L, 0L)
                }
            }
        }
    }

    /**
     * 数据层
     */
    class M {
        class StudyRecordBean(var sections: List<SearchRecordCBean.SectionsBean>?)
        class GiveInstructionBean(var teachers: List<String>?)
        class SearchRecordBean(var search: List<String>?)
    }

    /**
     * 控制层
     */
    class Present(val context: Context, val v: V) {
        val m = M()
        /**
         * 广播管理者
         */
        private var broadcastManager: BroadcastManager? = null
        /**
         * 关键字搜索接受广播
         */
        private var searchReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Constants.ACTION_CLICK_SEARCH_RECORD_ITEM || intent?.action == ACTION_CLICK_SEARCH_TEACHER_ITEM) {
                    val record = intent.getStringExtra("search")
                    search(record)
                    v.displaySearchText(record)
                }
            }
        }

        /**
         * 用户点击搜索
         */
        fun clickKeyBoardSearch(record: String?) {
            if (!TextUtils.isEmpty(record)) {
                //保存关键字到sp文件中
                val cacheRecord = CacheUtil.getSearchRecord()
                if (TextUtils.isEmpty(cacheRecord)) {
                    CacheUtil.putSearchRecord("$record,")
                } else {
                    val records = M3u8Utils.strToList(cacheRecord)
                    if (!records?.contains(record!!)!!) {
                        records.add(0, record!!)
                    }
                    CacheUtil.putSearchRecord(M3u8Utils.listToStr(records)!!)
                }
                //刷新搜索记录界面
                v.notifySearchPage()

            }
        }

        /**
         * 注册广播
         */
        fun registerSearchReceiver() {
            if (broadcastManager == null) {
                broadcastManager = BroadcastManager.create(context)
            }
            broadcastManager?.registerReceiver(Constants.ACTION_CLICK_SEARCH_RECORD_ITEM, searchReceiver)
            broadcastManager?.registerReceiver(ACTION_CLICK_SEARCH_TEACHER_ITEM, searchReceiver)
        }

        /**
         * 解除广播
         */
        fun unRegisterSearchReceiver() {
            broadcastManager?.unRegisterReceiver(searchReceiver)
        }

        /**
         * 请求搜索关键字接口
         */
        fun search(record: String?) {
            PonkoApp.searchApi?.search(record!!)?.enqueue(object : HttpCallBack<SearchCBean>() {
                override fun onSuccess(call: Call<SearchCBean>?, response: Response<SearchCBean>?) {
                    v.displaySearchResult(response?.body())
                }

                override fun onFailure(call: Call<SearchCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    ToastUtil.show("搜索失败:$msg")
                }
            })
        }
    }
}
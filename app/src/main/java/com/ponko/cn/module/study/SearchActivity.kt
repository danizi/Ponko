package com.ponko.cn.module.study

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp.Companion.searchApi
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.CoursesCBean
import com.ponko.cn.bean.SearchCBean
import com.ponko.cn.bean.SearchRecordCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.m3u8downer.core.M3u8Utils
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.NumUtil
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.XmStateView
import com.xm.lib.media.broadcast.BroadcastManager
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import retrofit2.Call
import retrofit2.Response

/**
 * 搜索模块
 */
class SearchActivity : RefreshLoadAct<Any, SearchRecordCBean>() {
    /**
     * 搜索页面容器
     */
    private var flResultContainer: FrameLayout? = null
    /**
     * 搜索结果页面
     */
    private var rvSearchResult: RecyclerView? = null
    /**
     * 搜索结果状态页面
     */
    private var viewState: XmStateView? = null

    /**
     * 适配器
     */
    private var adp: BaseRvAdapter? = null
    /**
     * 保存用户搜索的关键字
     */
    private var record: String? = ""
    /**
     * 广播管理
     */
    private var broadcastManager: BroadcastManager? = null
    private var searchReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == SEARCH_ACTION) {
                search(intent.getStringExtra("search"))
            }
        }
    }

    companion object {
        private const val SEARCH_ACTION = "broadcast.action.search"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun findViews() {
        super.findViews()
        rvSearchResult = findViewById(R.id.rv_search_result)
        flResultContainer = findViewById(R.id.fl_result_container)
        viewState = findViewById(R.id.view_state)
    }

    override fun initDisplay() {
        disableRefresh = true
        disableLoad = true
        addItemDecoration = false
        super.initDisplay()
        BarUtil.addSearchBar2(this, viewHolder?.toolbar, "取消",
                View.OnClickListener {
                    flResultContainer?.visibility = View.INVISIBLE
                    viewState?.hide()
                },
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        record = s.toString()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    }
                },
                TextView.OnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        BKLog.d("搜索关键字:$record")
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
                            val searchRecordViewHold = adp?.getTypeViewHolder(2) as SearchRecordViewHold
                            searchRecordViewHold.adapter.data?.add(0, record!!)
                            searchRecordViewHold.adapter.notifyItemInserted(0)
                            searchRecordViewHold.adapter.notifyItemChanged(0)

                            //请求搜索界面页面
                            search(record)
                        }
                    }
                    true
                })
    }

    private fun search(record: String?) {
        searchApi?.search(record!!)?.enqueue(object : HttpCallBack<SearchCBean>() {
            override fun onSuccess(call: Call<SearchCBean>?, response: Response<SearchCBean>?) {
                flResultContainer?.visibility = View.VISIBLE
                val body = response?.body()
                if (body?.courses?.isEmpty()!! && body.sections?.isEmpty()!!) {
                    flResultContainer?.visibility = View.INVISIBLE
                    viewState?.showNoData("未搜索到相关信息...")
                    return
                }

                val adapter = object : BaseRvAdapter() {}
                if (!body.courses?.isEmpty()!!) {
                    adapter.data?.addAll(body.courses!!)
                    adapter.addItemViewDelegate(0, CoursesViewHold::class.java, SearchCBean.CoursesBean::class.java, R.layout.item_course_introduction)
                }
                if (!body.sections?.isEmpty()!!) {
                    adapter.data?.addAll(body.sections!!)
                    adapter.addItemViewDelegate(1, SectionsViewHold::class.java, SearchCBean.SectionsBean::class.java, R.layout.item_search_result_sub_tv)
                }
                rvSearchResult?.layoutManager = LinearLayoutManager(this@SearchActivity)
                rvSearchResult?.adapter = adapter
            }
        })
    }

    override fun iniData() {
        super.iniData()
        broadcastManager = BroadcastManager.create(this)
        broadcastManager?.registerReceiver(SEARCH_ACTION, searchReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcastManager?.unRegisterReceiver(searchReceiver)
    }

    /**
     * 搜索结果课程
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
     * 搜索结果章节
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
            }
        }
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1, 2),
                arrayOf(StudyRecordViewHold::class.java, GiveInstructionViewHold::class.java, SearchRecordViewHold::class.java),
                arrayOf(StudyRecordBean::class.java, GiveInstructionBean::class.java, SearchRecordBean::class.java),
                arrayOf(R.layout.item_search_study_record, R.layout.item_search_give_instruction, R.layout.item_search_record)
        )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        searchApi?.searchRecord()?.enqueue(object : HttpCallBack<SearchRecordCBean>() {
            override fun onSuccess(call: Call<SearchRecordCBean>?, response: Response<SearchRecordCBean>?) {
                requestRefreshSuccess(response?.body())
            }

            override fun onFailure(call: Call<SearchRecordCBean>?, msg: String?) {
                super.onFailure(call, msg)
                requestRefreshFailure()
            }
        })
    }

    override fun multiTypeData(body: SearchRecordCBean?): List<Any> {
        val data = ArrayList<Any>()
        //学习记录
        if (!body?.sections?.isEmpty()!!) {
            data.add(StudyRecordBean(body.sections))
        }
        //授课老师
        if (!body.teachers?.isEmpty()!!) {
            data.add(GiveInstructionBean(body.teachers))
        }
        //读取sp文件的关键字
        val cacheRecord = CacheUtil.getSearchRecord()
        val searchList = M3u8Utils.strToList(cacheRecord)
        searchList?.remove("")
        val record10 = ArrayList<String>()
        for (record in searchList!!) {
            record10.add(record)
        }
        if (!record10.isEmpty()) {
            data.add(SearchRecordBean(record10))
        }
        return data
    }

    override fun adapter(): BaseRvAdapter? {
        adp = object : BaseRvAdapter() {}
        return adp
    }

    override fun presenter(): Any {
        return Any()
    }

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
            val studyRecordBean = d as StudyRecordBean
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
            val giveInstructionBean = d as GiveInstructionBean
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
                intent.action = "broadcast.action.search"
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
            val searchRecordBean = d as SearchRecordBean
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

        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = ViewHolder.create(itemView)
            }
            val context = itemView.context
            val sectionsBean = d as SearchRecordCBean.SectionsBean
            ui?.tv?.text = sectionsBean.name
            itemView.setOnClickListener {
                BKLog.d("跳转到课程")
                StudyCourseDetailActivity.start(context, "", "", 0L, 0L)
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

    class StudyRecordBean(var sections: List<SearchRecordCBean.SectionsBean>?)
    class GiveInstructionBean(var teachers: List<String>?)
    class SearchRecordBean(var search: List<String>?)
}

package com.ponko.cn.module.study.v1

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp.Companion.searchApi
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.SearchCBean
import com.ponko.cn.bean.SearchRecordCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.m3u8downer.core.M3u8Utils
import com.ponko.cn.module.study.v1.constract.SearchContract
import com.ponko.cn.utils.CacheUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.XmStateView
import retrofit2.Call
import retrofit2.Response

/**
 * 搜索模块 ps：后续需要修改，写的复杂了
 */
class SearchActivity : RefreshLoadAct<SearchContract.Present, SearchRecordCBean>(), SearchContract.V {
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
     * 搜索框
     */
    private var tvSearch: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p?.registerSearchReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        p?.unRegisterSearchReceiver()
    }

    override fun presenter(): SearchContract.Present {
        return SearchContract.Present(context = this, v = this)
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
        addSearchBar2(this, viewHolder?.toolbar, "取消",
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
                        p?.clickKeyBoardSearch(record)
                    }
                    true
                })
    }

    private fun addSearchBar2(context: Context?, toolbar: Toolbar?, right: String? = "取消", rightListener: View.OnClickListener?, watcher: TextWatcher?, editorActionListener: TextView.OnEditorActionListener?) {
        val barView = ViewUtil.viewById(context, R.layout.bar_search2, null)
        toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar?.visibility = View.VISIBLE
        tvSearch = barView?.findViewById(R.id.tv_search) as EditText
        val tvCancel = barView.findViewById(R.id.tv_cancel) as TextView
        val ivBack = barView.findViewById(R.id.iv_back) as ImageView
        tvCancel.text = right
        tvCancel.setOnClickListener(rightListener)
        tvSearch?.addTextChangedListener(watcher)
        tvSearch?.setOnEditorActionListener(editorActionListener)
        ivBack.setOnClickListener {
            (context as Activity).finish()
        }
    }

    override fun notifySearchPage() {
        try {
            val searchRecordViewHold = adp?.getTypeViewHolder((adp?.data?.size!! - 1)) as SearchContract.V.SearchRecordViewHold
            searchRecordViewHold.adapter.data?.add(0, record!!)
            searchRecordViewHold.adapter.notifyItemInserted(0)
            searchRecordViewHold.adapter.notifyItemChanged(0)
            //请求搜索界面页面
            p?.search(record)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun displaySearchText(record: String?) {
        tvSearch?.setText(record)
    }

    override fun displaySearchResult(body: SearchCBean?) {
        flResultContainer?.visibility = View.VISIBLE
        if (body?.courses?.isEmpty()!! && body.sections?.isEmpty()!!) {
            flResultContainer?.visibility = View.INVISIBLE
            viewState?.showNoData("未搜索到相关信息...")
            return
        }

        val adapter = object : BaseRvAdapter() {}
        if (!body.courses?.isEmpty()!!) {
            adapter.data?.addAll(body.courses!!)
            adapter.addItemViewDelegate(0, SearchContract.V.CoursesViewHold::class.java, SearchCBean.CoursesBean::class.java, R.layout.item_course_introduction)
        }
        if (!body.sections?.isEmpty()!!) {
            adapter.data?.addAll(body.sections!!)
            adapter.addItemViewDelegate(1, SearchContract.V.SectionsViewHold::class.java, SearchCBean.SectionsBean::class.java, R.layout.item_search_result_sub_tv)
        }
        rvSearchResult?.layoutManager = LinearLayoutManager(this@SearchActivity)
        rvSearchResult?.adapter = adapter
    }


    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1, 2),
                arrayOf(SearchContract.V.StudyRecordViewHold::class.java, SearchContract.V.GiveInstructionViewHold::class.java, SearchContract.V.SearchRecordViewHold::class.java),
                arrayOf(SearchContract.M.StudyRecordBean::class.java, SearchContract.M.GiveInstructionBean::class.java, SearchContract.M.SearchRecordBean::class.java),
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
            data.add(SearchContract.M.StudyRecordBean(body.sections))
        }
        //授课老师
        if (!body.teachers?.isEmpty()!!) {
            data.add(SearchContract.M.GiveInstructionBean(body.teachers))
        }
        //读取sp文件的关键字
        val cacheRecord = CacheUtil.getSearchRecord()
        val searchList = M3u8Utils.strToList(cacheRecord)
        searchList?.remove("")
        val record10 = ArrayList<String>()
        for (record in searchList!!) {
            record10.add(record)
        }
//        if (!record10.isEmpty()) {
        data.add(SearchContract.M.SearchRecordBean(record10))
//        }
        return data
    }

    override fun adapter(): BaseRvAdapter? {
        adp = object : BaseRvAdapter() {}
        return adp
    }
}

package com.ponko.cn.module.my.option.collect

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.db.bean.CourseCollectSectionDbBean
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.study.v1.StudyCourseDetailActivity
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder

/**
 * 专题下缓存的列表
 */
class CollectListActivity : RefreshLoadAct<Any, ArrayList<CourseCollectSectionDbBean>>() {

    companion object {
        fun start(context: Context, title: String, courseId: String) {
            val intent = Intent(context,CollectListActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("courseId", courseId)
            ActivityUtil.startActivity(context, intent)
        }
    }

    private var title: String = ""
    private var courseId: String = ""

    override fun initDisplay() {
        disableLoad = true
        disableRefresh = true
        addItemDecoration = false
        super.initDisplay()
        title = intent.getStringExtra("title")
        courseId = intent.getStringExtra("courseId")
        BarUtil.addBar1(this, viewHolder?.toolbar, title, "", null)
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(CollectViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_search_result_sub_tv)
        )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        requestRefreshSuccess(PonkoApp.collectSectionDao?.selectByCourseId(courseId))
    }

    override fun multiTypeData(body: ArrayList<CourseCollectSectionDbBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }
    /**
     * 課程ViewHolder
     */
    class CollectViewHolder(view: View) : BaseViewHolder(view) {
        private var ui: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = ViewHolder.create(itemView)
            }
            val context = itemView.context
            val collectSectionDbBean = d as CourseCollectSectionDbBean
            ui?.tv?.text = collectSectionDbBean.column_section_name
            itemView.setOnClickListener {
                StudyCourseDetailActivity.start(context, collectSectionDbBean.column_course_id, "", 0L, 0L)
            }
        }

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
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_collect_list)
//    }
}

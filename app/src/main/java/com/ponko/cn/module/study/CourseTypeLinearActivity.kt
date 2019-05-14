package com.ponko.cn.module.study

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.InternalCourse
import com.ponko.cn.bean.OutInternalCourse
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.NumUtil
import retrofit2.Call
import retrofit2.Response


/**
 * 专题下的课程列表
 */
class CourseTypeLinearActivity : RefreshLoadAct<Any, ArrayList<OutInternalCourse>>() {
    companion object {
        fun start(context: Context?, title: String?, typeId: String?) {
            val intent = Intent(context, CourseTypeLinearActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("typeId", typeId)
            ActivityUtil.startActivity(context, intent)
        }
    }

    private var title: String? = ""
    private var typeId: String? = ""

    override fun initDisplay() {
        title = intent.getStringExtra("title")
        typeId = intent.getStringExtra("typeId")
        addItemDecoration = false
        disableLoad = true
        BarUtil.addBar1(this, viewHolder?.toolbar, title!!, "搜索", View.OnClickListener {
            BKLog.d("点击搜索")
            ActivityUtil.startActivity(this, Intent(this, SearchActivity::class.java))
        })
        super.initDisplay()
        viewHolder?.rv?.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(CourseTypeLinearViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_course_linear)
        )
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        PonkoApp.studyApi?.getSpecialAllCourse(typeId!!)?.enqueue(object : HttpCallBack<ArrayList<OutInternalCourse>>() {
            override fun onSuccess(call: Call<ArrayList<OutInternalCourse>>?, response: Response<ArrayList<OutInternalCourse>>?) {
                requestRefreshSuccess(response?.body())
            }
        })
    }

    override fun multiTypeData(body: ArrayList<OutInternalCourse>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

    private class CourseTypeLinearViewHolder(view: View) : BaseViewHolder(view) {

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
            val outLinearCourse = d as OutInternalCourse
            val context = itemView.context
            viewHolder?.tvTitle?.text = outLinearCourse.title
            val adapter = object : BaseRvAdapter() {}
            adapter.data?.addAll(outLinearCourse.courses!!)
            adapter.addItemViewDelegate(0, CourseViewHolder::class.java, InternalCourse::class.java, R.layout.item_course_introduction)
            viewHolder?.rv?.adapter = adapter
            viewHolder?.rv?.layoutManager = LinearLayoutManager(context)
            viewHolder?.rv?.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private class CourseViewHolder(view: View) : BaseViewHolder(view) {

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

        private var viewHolder: ViewHolder? = null

        @SuppressLint("SetTextI18n")
        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ViewHolder.create(itemView)
            }
            val internalCourse = d as InternalCourse
            val context = itemView.context
            Glide.with(context, internalCourse.image, viewHolder?.ivCover)
            viewHolder?.tvCourseTitle?.text = internalCourse.title.toString()

            var teachers = ""
            for (i in 0..(internalCourse.teachers.size - 1)) {
                teachers += if (i == 0) {
                    internalCourse.teachers[i]
                } else {
                    " | " + internalCourse.teachers[i]
                }
            }
            viewHolder?.tvTeacher?.text = "${teachers}老师"
            viewHolder?.courseNumber?.text = "共${internalCourse.num}集 | ${NumUtil.getDecimalPoint(internalCourse.duration?.toFloat()!! / 60f/60f)}小时"
            itemView.setOnClickListener {
                StudyCourseDetailActivity.start(context, internalCourse.id,teachers,internalCourse.num,internalCourse.duration!!)
            }
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_course_type_list)
//    }
}

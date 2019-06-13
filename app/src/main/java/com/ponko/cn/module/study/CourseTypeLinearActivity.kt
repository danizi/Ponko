package com.ponko.cn.module.study

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.CourseAllCBean
import com.ponko.cn.bean.InternalCourse
import com.ponko.cn.bean.OutInternalCourse
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.NumUtil
import retrofit2.Call
import retrofit2.Response


/**
 * 专题下的课程列表
 */
class CourseTypeLinearActivity : RefreshLoadAct<Any, CourseAllCBean>() {
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
        bar()
        super.initDisplay()
    }

    private fun bar(t: String? = "") {
        val tempTitle: String = if (TextUtils.isEmpty(t)) {
            title!!
        } else {
            t!!
        }
        BarUtil.addBar1(this, viewHolder?.toolbar, tempTitle, "搜索", View.OnClickListener {
            BKLog.d("点击搜索")
            ActivityUtil.startActivity(this, Intent(this, SearchActivity::class.java))
        })
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
        PonkoApp.studyApi?.getSpecialAllCourse(typeId!!)?.enqueue(object : HttpCallBack<CourseAllCBean>() {
            override fun onSuccess(call: Call<CourseAllCBean>?, response: Response<CourseAllCBean>?) {
                bar(response?.body()?.title)
                requestRefreshSuccess(response?.body())
            }

            override fun onFailure(call: Call<CourseAllCBean>?, msg: String?) {
                super.onFailure(call, msg)
                requestRefreshFailure()
            }
        })
    }

    override fun multiTypeData(body: CourseAllCBean?): List<Any> {
        return body?.types!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

    open class CourseTypeLinearViewHolder(view: View) : BaseViewHolder(view) {

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
            val typesBean = d as CourseAllCBean.TypesBean
            val context = itemView.context
            viewHolder?.tvTitle?.text = typesBean.title
            val adapter = object : BaseRvAdapter() {}
            adapter.data?.addAll(typesBean.courses)
            adapter.addItemViewDelegate(0, CourseViewHolder::class.java, InternalCourse::class.java, R.layout.item_course_introduction)
            viewHolder?.rv?.adapter = adapter
            viewHolder?.rv?.layoutManager = LinearLayoutManager(context)
            viewHolder?.rv?.addItemDecoration(MyItemDecoration.divider(context, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))  //https://www.jianshu.com/p/86aaaa49ed3e
            viewHolder?.rv?.isFocusableInTouchMode = false
        }
    }

    open class CourseViewHolder(view: View) : BaseViewHolder(view) {

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
            val coursesBean = d as CourseAllCBean.TypesBean.CoursesBean
            val context = itemView.context
            Glide.with(context, coursesBean.image, viewHolder?.ivCover)
            viewHolder?.tvCourseTitle?.text = coursesBean.title.toString()

            var teachers = ""
            for (i in 0..(coursesBean.teachers.size - 1)) {
                teachers += if (i == 0) {
                    coursesBean.teachers[i]
                } else {
                    " | " + coursesBean.teachers[i]
                }
            }
            viewHolder?.tvTeacher?.text = "${teachers}老师"
            viewHolder?.courseNumber?.text = "共${coursesBean.num}集 | ${NumUtil.getDecimalPoint(coursesBean.duration.toFloat() / 60f / 60f)}小时"
            itemView.setOnClickListener {
                StudyCourseDetailActivity.start(context, coursesBean.id, teachers, coursesBean.num.toLong(), coursesBean.duration.toLong())
            }
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_course_type_list)
//    }
}

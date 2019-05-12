package com.ponko.cn.module.study

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.widget.RelativeLayout
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.bean.StudyCourseBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.study.holder.CourseSectionViewHolder
import com.ponko.cn.utils.ActivityUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.decoration.GridItemDecoration
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.common.util.ViewUtil
import retrofit2.Call
import retrofit2.Response

/**
 * B2B/B2C 类别下的所有专题列表
 */
class CourseTypeGridActivity : RefreshLoadAct<Any, ArrayList<MainCBean.TypesBeanX.TypesBean>>() {
    private var title = ""
    private var typeId = ""

    companion object {
        fun start(context: Context, title: String, typeId: String) {
            val intent = Intent(context, CourseTypeGridActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("typeId", typeId)
            ActivityUtil.startActivity(context, intent)
        }
    }

    override fun initDisplay() {
        title = intent.getStringExtra("title")
        typeId = intent.getStringExtra("typeId")
        addBar1(title)
        disableLoad = true
        addItemDecoration=false
        super.initDisplay()
        viewHolder?.rv?.layoutManager = GridLayoutManager(this, 2)
        viewHolder?.rv?.addItemDecoration(GridItemDecoration.Builder(this)
                .setHorizontalSpan(ScreenUtil.dip2px(this, 15).toFloat())
                .setVerticalSpan(ScreenUtil.dip2px(this, 15).toFloat())
                .setColorResource(R.color.white)
                .setShowLastLine(false)
                .build())
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(CourseSectionViewHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_study_course_section)
        )
    }

    override fun requestMoreApi() {
    }

    override fun requestRefreshApi() {
        PonkoApp.studyApi?.getSpecialList(typeId)?.enqueue(object : HttpCallBack<ArrayList<MainCBean.TypesBeanX.TypesBean>>() {
            override fun onSuccess(call: Call<ArrayList<MainCBean.TypesBeanX.TypesBean>>?, response: Response<ArrayList<MainCBean.TypesBeanX.TypesBean>>?) {
                requestRefreshSuccess(response?.body())
            }
        })
    }

    override fun multiTypeData(body: ArrayList<MainCBean.TypesBeanX.TypesBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_course_type_grid
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_course_type_grid)
//    }
}

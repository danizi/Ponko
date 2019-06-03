package com.ponko.cn.module.study

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.text.TextUtils
import android.widget.Button
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
import com.ponko.cn.module.my.option.CacheAct
import com.ponko.cn.module.study.constract.StudyCacheContract
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.decoration.MyItemDecoration
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimerHelper
import com.xm.lib.component.OnCancelListener
import com.xm.lib.component.OnEnterListener
import retrofit2.Call
import retrofit2.Response

/**
 * 专题-选集缓存页面
 */
class StudyCacheActivity : RefreshLoadAct<StudyCacheContract.Present, CoursesDetailCBean>(), StudyCacheContract.V {

    companion object {
        /**
         * 日志标识
         */
        const val TAG = "StudyCacheActivity"
        /**
         * 选中专题下的课程数量
         */
        val SleSections = ArrayList<CoursesDetailCBean.ChaptersBean.SectionsBean>()
        /**
         * 是否全选
         */
        var isSelectAll = false

        /**
         * @param context  上下文对象
         * @param typeId   专题唯一标识
         * @param teachers 专题老师
         * @param num      专题课程数量
         * @param duration 专题课程总时长
         */
        fun start(context: Context, typeId: String, teachers: String, num: Long, duration: Long) {
            val intent = Intent(context, StudyCacheActivity::class.java)
            intent.putExtra("typeId", typeId)
            intent.putExtra("teachers", teachers)
            intent.putExtra("num", num)
            intent.putExtra("duration", duration)
            ActivityUtil.startActivity(context, intent)
        }
    }

    private var btnAllSelect: Button? = null
    private var btnDown: Button? = null
    private var btnLook: Button? = null

    override fun onDestroy() {
        super.onDestroy()
        //清除状态
        p?.onDestroy()
    }

    override fun multiTypeData(body: CoursesDetailCBean?): List<Any> {
        return body?.chapters!!
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(StudyCacheContract.V.CacheListViewHolder::class.java),
                arrayOf(CoursesDetailCBean.ChaptersBean::class.java),
                arrayOf(R.layout.item_cache_list)
        )
    }

    override fun presenter(): StudyCacheContract.Present {
        return StudyCacheContract.Present(context = this, v = this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cache2
    }

    override fun initDisplay() {
        disableLoad = true
        disableRefresh = true
        addItemDecoration = false
        super.initDisplay()
        BarUtil.addBar1(this, viewHolder?.toolbar, "选集缓存")
        viewHolder?.rv?.addItemDecoration(MyItemDecoration.divider(this, DividerItemDecoration.VERTICAL, R.drawable.shape_question_diveder_1))
    }

    override fun findViews() {
        super.findViews()
        btnAllSelect = findViewById(R.id.btn_all_select)
        btnDown = findViewById(R.id.btn_down)
        btnLook = findViewById(R.id.btn_look)
    }

    override fun iniData() {
        p?.getIntentExtra(intent)
        super.iniData()
    }

    override fun iniEvent() {
        super.iniEvent()
        btnAllSelect?.setOnClickListener {
            p?.clickAllSelect()
        }
        btnDown?.setOnClickListener {
            p?.clickDown()
        }
        btnLook?.setOnClickListener {
            p?.clickLook()
        }
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        p?.requestCourseDetailApi()
    }

    override fun requestCourseDetailApiSuccess(coursesDetailCBean: CoursesDetailCBean?) {
        requestRefreshSuccess(coursesDetailCBean)
    }

    override fun requestCourseDetailApiFailure() {
        requestRefreshFailure()
    }

    override fun adapter(): BaseRvAdapter? {
        return p?.getAdapter()
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_cache2)
//    }
}

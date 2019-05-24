package com.ponko.cn.module.study

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.media.AttachmentComplete
import com.ponko.cn.module.media.AttachmentGesture
import com.ponko.cn.module.media.AttachmentPre
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.utils.ActivityUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.media.base.XmVideoView
import retrofit2.Call
import retrofit2.Response


class StudyCourseDetailActivity : PonkoBaseAct<Any>() {

    companion object {
        const val TAG = "StudyCourseDetailActivity"
        const val TYPE_FROM_GENERAL = "from_general"
        const val TYPE_FROM_CACHE = "from_cache"
        /**
         * 点击专题中课程使用该方法
         * @param typeId   专题ID
         * @param teachers 老师
         * @param num      课程总集数
         * @param duration 课程总时间 （单位：秒）
         */
        fun start(context: Context?, typeId: String?, teachers: String, num: Long?, duration: Long) {
            val intent = Intent(context, StudyCourseDetailActivity::class.java)
            intent.putExtra("type", TYPE_FROM_GENERAL)
            intent.putExtra("typeId", typeId)
            intent.putExtra("teachers", teachers)
            intent.putExtra("num", num)
            intent.putExtra("duration", duration)
            ActivityUtil.startActivity(context, intent)
        }

        /**
         * 缓存完成课程跳转使用该方法
         * @param typeId   专题ID
         * @param teachers 老师
         * @param num      课程总集数
         * @param duration 课程总时间 （单位：秒）
         * @param vid      课程视频唯一标识
         */
        fun startFromCacheCourse(context: Context?, typeId: String?, teachers: String, num: Long?, duration: Long, vid: String) {
            val intent = Intent(context, StudyCourseDetailActivity::class.java)
            intent.putExtra("type", TYPE_FROM_CACHE)
            intent.putExtra("typeId", typeId)
            intent.putExtra("teachers", teachers)
            intent.putExtra("num", num)
            intent.putExtra("duration", duration)
            intent.putExtra("vid", vid)
            ActivityUtil.startActivity(context, intent)
        }

        /**
         * 搜索页面跳转使用该方法 todo 缺失专题信息啊
         */
        fun startFromSearch(context: Context?) {

        }
    }

    /**
     * 主窗口UI
     */
    private var viewHolder: ViewHolder? = null
    /**
     * 专题数据bean
     */
    private var coursesDetailCBean: CoursesDetailCBean? = null
    /**
     * 哪个窗口跳转标识
     */
    private var type = ""
    /**
     * 视频唯一标识
     */
    private var vid = ""
    /**
     * 专题唯一标识
     */
    private var typeId = ""
    /**
     * 专题老师
     */
    private var teachers = ""
    /**
     * 专题课程数量
     */
    private var num = 0L
    /**
     * 专题总时间
     */
    private var duration = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //接受的信息，需要转发到缓存页面中。
        type = intent.getStringExtra("type")
        typeId = intent.getStringExtra("typeId")
        teachers = intent.getStringExtra("teachers")
        num = intent.getLongExtra("num", 0L)
        duration = intent.getLongExtra("duration", 0L)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewHolder?.video!!.onDestroy()//销毁播放器
    }

    override fun presenter(): Any {
        return Any()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_study_course_detail
    }

    override fun findViews() {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
    }

    override fun initDisplay() {
        super.initDisplay()
        com.jaeger.library.StatusBarUtil.setColor(this, this.resources.getColor(R.color.black), 0) //系统栏颜色
    }

    override fun iniEvent() {
        super.iniEvent()
        viewHolder?.ivColect?.setOnClickListener {
            BKLog.d("点击收藏")
        }
        viewHolder?.ivDown?.setOnClickListener {
            BKLog.d("点击下载缓存页面")
            StudyCacheActivity.start(this, typeId, teachers, num, duration)
        }
        viewHolder?.ivShare?.setOnClickListener {
            BKLog.d("点击分享")
        }
    }

    override fun iniData() {
        super.iniData()
        initData(this, viewHolder?.video!!)
    }

    private fun initData(context: Context?, xmVideoView: XmVideoView) {
        //接受的信息，需要转发到缓存页面中。
        type = intent.getStringExtra("type")
        typeId = intent.getStringExtra("typeId")
        teachers = intent.getStringExtra("teachers")
        num = intent.getLongExtra("num", 0L)
        duration = intent.getLongExtra("duration", 0L)
        when (type) {
            TYPE_FROM_CACHE -> {
                vid = intent.getStringExtra("vid")
                BKLog.d(TAG, "点击了“已”缓存课程跳转过来")
            }
            TYPE_FROM_GENERAL -> {
                BKLog.d(TAG, "点击了“未”缓存课程跳转过来")
            }
        }
        BKLog.d("专题id:$typeId 老师$teachers 集数$num 总时长${duration / 60f / 60f}小时")

        //绑定的页面
        val preUrl = ""
        val playUrl = ""
        val attachmentPre = AttachmentPre(context, preUrl)
        attachmentPre.url = playUrl
        val attachmentControl = AttachmentControl(context)
        val attachmentGesture = AttachmentGesture(context)
        val attachmentComplete = AttachmentComplete(context)
        xmVideoView.bindAttachmentView(attachmentPre)      //预览附着页面
        xmVideoView.bindAttachmentView(attachmentControl)  //控制器附着页面
        xmVideoView.bindAttachmentView(attachmentGesture)  //手势附着页面(调节亮度和音量)
        xmVideoView.bindAttachmentView(attachmentComplete) //播放完成附着页面
        //播放器回调观察者
        xmVideoView.addPlayerObserver(attachmentPre)
        xmVideoView.addPlayerObserver(attachmentControl)
        xmVideoView.addPlayerObserver(attachmentGesture)
        xmVideoView.addPlayerObserver(attachmentComplete)
        //手势观察者
        xmVideoView.addGestureObserver(attachmentPre)
        xmVideoView.addGestureObserver(attachmentControl)
        xmVideoView.addGestureObserver(attachmentGesture)
        xmVideoView.addGestureObserver(attachmentComplete)
        //各种状态（断网、音量、电话、插上耳机、电量...）观察者
        xmVideoView.addPhoneStateObserver(attachmentPre)
        xmVideoView.addPhoneStateObserver(attachmentControl)
        xmVideoView.addPhoneStateObserver(attachmentGesture)
        xmVideoView.addPhoneStateObserver(attachmentComplete)

        //请求专题信息接口
        PonkoApp.studyApi?.getCourseDetail(typeId)?.enqueue(object : HttpCallBack<CoursesDetailCBean>() {
            override fun onSuccess(call: Call<CoursesDetailCBean>?, response: Response<CoursesDetailCBean>?) {
                //专题下课程信息保存
                coursesDetailCBean = response?.body()

                //设置预览界面
                setPre(getVid())

                //设置控制界面，传递视频列表到横向控制界面
                setControl()

                //视频列表展示设置
                list()
            }

            fun setControl() {
                for (i in 0..(viewHolder?.video?.childCount!! - 1)) {
                    if (viewHolder?.video?.getChildAt(i) is AttachmentControl) {
                        (viewHolder?.video?.getChildAt(i) as AttachmentControl).courseDetail = coursesDetailCBean
                        break
                    }
                }
            }

            fun setPre(vid: String?) {
                MediaUitl.getM3u8Url(vid, object : MediaUitl.OnPlayUrlListener {
                    override fun onFailure() {
                        this@StudyCourseDetailActivity.runOnUiThread {
                            Toast.makeText(this@StudyCourseDetailActivity, "获取播放地址失败 - ", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onSuccess(url: String, size: Int?) {
                        //然后请求视频地址
                        val attachmentPre = viewHolder?.video?.getChildAt(0) as AttachmentPre
                        this@StudyCourseDetailActivity.runOnUiThread {
                            attachmentPre.load(url, coursesDetailCBean?.image!!)   //todo 窗口有销毁的可能
                        }
                    }
                })
            }

            fun getVid(): String? {
                val playUrl = if (TextUtils.isEmpty(vid)) {
                    coursesDetailCBean?.chapters!![0].sections[0].vid  //PS 游客模式vid获取不到
                } else {
                    vid
                }
                BKLog.d("vid:$playUrl")
                return playUrl
            }

            /**
             * 展示视频列表
             */
            fun list() {
                val adp = MyExtendableListViewAdapter(coursesDetailCBean)

                viewHolder?.expandList?.setAdapter(adp)
                for (i in 0..(adp.child.size - 1)) {
                    viewHolder?.expandList?.expandGroup(i)
                }
//                viewHolder?.expandList?.setOnGroupExpandListener { groupPosition ->
//                    val count = MyExtendableListViewAdapter(coursesDetailCBean).groupCount
//                    for (i in 0 until count) {
//                        if (i != groupPosition) {
//                            viewHolder?.expandList?.collapseGroup(i)
//                        }
//                    }
//                }
                viewHolder?.expandList?.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                    BKLog.d("点击：$groupPosition  $childPosition")
                    true
                }
            }
        })
    }


    /**
     * 二级列表的适配器
     */
    private class MyExtendableListViewAdapter(courseInfo: CoursesDetailCBean?) : BaseExpandableListAdapter() {
        val group = ArrayList<String>()
        val child = ArrayList<List<CoursesDetailCBean.ChaptersBean.SectionsBean>>()

        init {
            for (chapter in courseInfo?.chapters!!) {
                group.add(chapter.chapter_name!!)
                child.add(chapter?.sections!!)
            }
        }

        /**
         * 主分组
         */
        override fun getGroup(groupPosition: Int): Any {
            return group[groupPosition]
        }

        /**
         * 指定位置上的子元素是否可选中
         */
        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
            return true
        }

        /**
         * 分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
         */
        override fun hasStableIds(): Boolean {
            return true
        }

        /**
         * 获取显示指定主组的视图对象
         */
        override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
            val groupViewHolder: GroupViewHolder
            var view = convertView
            if (view == null) {
                groupViewHolder = GroupViewHolder.create(parent?.context!!, parent)
                view = groupViewHolder.rootView
                view.tag = groupViewHolder
            } else {
                groupViewHolder = convertView?.tag as GroupViewHolder
            }
            groupViewHolder.tvSection.text = group[groupPosition]
            return view
        }

        /**
         * 子分组数量
         */
        override fun getChildrenCount(groupPosition: Int): Int {
            return child[groupPosition].size
        }

        /**
         * 获取子选项的ID, 这个ID必须是唯一的
         */
        override fun getChild(groupPosition: Int, childPosition: Int): Any {
            return childPosition
        }

        /**
         * 取指定分组的ID, 这个ID必须是唯一的
         */
        override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
        }

        /**
         * 取得显示给定分组给定子位置的数据用的视图
         */
        @SuppressLint("SetTextI18n")
        override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
            val childViewHolder: ChildViewHolder
            var view = convertView
            if (view == null) {
                childViewHolder = ChildViewHolder.create(parent?.context!!, parent)
                view = childViewHolder.rootView
                view.tag = childViewHolder
            } else {
                childViewHolder = convertView?.tag as ChildViewHolder
            }
            childViewHolder.tvCourseName.text = child[groupPosition][childPosition].name
            childViewHolder.tvTime.text = TimeUtil.hhmmss(child[groupPosition][childPosition].duration.toLong()) + " | "
            childViewHolder.tvPos.text = childPosition.toString()
            childViewHolder.tvProcess.text = (child[groupPosition][childPosition].progress_duration / child[groupPosition][childPosition].duration.toInt()).toString()
            return view
        }

        /**
         *获取子选项的ID, 这个ID必须是唯一的
         */
        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return childPosition.toLong()
        }

        /**
         * 主分组数量
         */
        override fun getGroupCount(): Int {
            return group.size
        }

        private class GroupViewHolder private constructor(val rootView: View, val tvSection: TextView) {
            companion object {

                fun create(context: Context, parent: ViewGroup?): GroupViewHolder {
                    val rootView = LayoutInflater.from(context).inflate(R.layout.item_study_course_detail_section, parent, false)
                    val tvSection = rootView.findViewById<View>(R.id.tv_section) as TextView
                    return GroupViewHolder(rootView, tvSection)
                }
            }
        }

        private class ChildViewHolder private constructor(val rootView: View, val tvPos: TextView, val tvCourseName: TextView, val tvTime: TextView, val tvProcess: TextView) {
            companion object {

                fun create(context: Context, parent: ViewGroup?): ChildViewHolder {
                    val rootView = LayoutInflater.from(context).inflate(R.layout.item_study_course_detail_course, parent, false)
                    val tvPos = rootView.findViewById<View>(R.id.tv_pos) as TextView
                    val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                    val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                    val tvProcess = rootView.findViewById<View>(R.id.tv_process) as TextView

                    return ChildViewHolder(rootView, tvPos, tvCourseName, tvTime, tvProcess)
                }
            }
        }


    }

    /**
     * 课程详情窗口ViewHolder
     */
    private class ViewHolder private constructor(val video: XmVideoView, val constraintLayout5: ConstraintLayout, val ivShare: ImageView, val ivColect: ImageView, val ivDown: ImageView, val expandList: ExpandableListView) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val video = rootView.findViewById<View>(R.id.video) as XmVideoView
                val constraintLayout5 = rootView.findViewById<View>(R.id.constraintLayout5) as ConstraintLayout
                val ivShare = rootView.findViewById<View>(R.id.iv_share) as ImageView
                val ivColect = rootView.findViewById<View>(R.id.iv_colect) as ImageView
                val ivDown = rootView.findViewById<View>(R.id.iv_down) as ImageView
                val expandList = rootView.findViewById<View>(R.id.expand_list) as ExpandableListView
                return ViewHolder(video, constraintLayout5, ivShare, ivColect, ivDown, expandList)
            }
        }
    }
}

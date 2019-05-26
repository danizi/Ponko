package com.ponko.cn.module.study

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.ponko.cn.bean.MediaBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.media.AttachmentComplete
import com.ponko.cn.module.media.AttachmentGesture
import com.ponko.cn.module.media.AttachmentPre
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.module.media.control.ControlViewHolder
import com.ponko.cn.module.study.constract.StudyCourseDetailContract
import com.ponko.cn.utils.ActivityUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.media.base.XmVideoView
import retrofit2.Call
import retrofit2.Response


class StudyCourseDetailActivity : PonkoBaseAct<StudyCourseDetailContract.Present>(), StudyCourseDetailContract.V {

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
         * 搜索页面跳转使用该方法 todo 缺失专题信息啊 缺少老师信息
         */
        fun startFromSearch(context: Context?) {

        }
    }

    /**
     * 主窗口UI
     */
    private var viewHolder: ViewHolder? = null
    /**
     * 附着页面控制器
     */
    private var attachmentControl: AttachmentControl? = null
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

    override fun presenter(): StudyCourseDetailContract.Present {
        return StudyCourseDetailContract.Present(context = this, v = this)
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

        //初始化播放器，以及附着页面
        MediaUitl.initXmVideoView(xmVideoView, this@StudyCourseDetailActivity)
        attachmentControl = xmVideoView.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl

        //请求专题信息接口
        PonkoApp.studyApi?.getCourseDetail(typeId)?.enqueue(object : HttpCallBack<CoursesDetailCBean>() {
            override fun onSuccess(call: Call<CoursesDetailCBean>?, response: Response<CoursesDetailCBean>?) {
                //专题下课程信息保存
                coursesDetailCBean = response?.body()
                //设置视频信息
                attachmentControl?.setMediaInfo(MediaBean(coursesDetailCBean?.chapters!!))
                //设置配置信息
                attachmentControl?.setConfigInfo(MediaBean.ConfigBean())
                //设置分享信息
                val shareUrl = coursesDetailCBean?.share_base_url!! + "/" + coursesDetailCBean?.chapters!![0].sections[0].id
                val shareTitle = coursesDetailCBean?.title
                val shareDes = coursesDetailCBean?.features
                attachmentControl?.setShareInfo(MediaBean.ShareBean(shareUrl, shareTitle, shareDes))

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
                val attachmentPre = viewHolder?.video?.getChildAt(0) as AttachmentPre
                attachmentPre.load(vid = vid, preUrl = coursesDetailCBean?.image)   //todo 窗口有销毁的可能
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

                //设置监听
                //只展开一个组
//                viewHolder?.expandList?.setOnGroupExpandListener { groupPosition ->
//                    val count = MyExtendableListViewAdapter(coursesDetailCBean).groupCount
//                    for (i in 0 until count) {
//                        if (i != groupPosition) {
//                            viewHolder?.expandList?.collapseGroup(i)
//                        }
//                    }
//                }
                viewHolder?.expandList?.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                    val vid = coursesDetailCBean?.chapters!![groupPosition].sections[childPosition].vid
                    val name = coursesDetailCBean?.chapters!![groupPosition].sections[childPosition].name
                    val id = coursesDetailCBean?.chapters!![groupPosition].sections[childPosition].id
                    BKLog.d("点击：$groupPosition  $childPosition vid:$name")
                    //设置分享信息
                    val shareUrl = coursesDetailCBean?.share_base_url!! + "/" + id
                    val shareTitle = coursesDetailCBean?.title
                    val shareDes = coursesDetailCBean?.features
                    attachmentControl?.setShareInfo(MediaBean.ShareBean(shareUrl, shareTitle, shareDes))
                    attachmentControl?.showLoading()
                    attachmentControl?.start(vid)
                    //刷新
                    adp.clickItemChildPos = childPosition
                    adp.clickItemGroupPos = groupPosition
                    adp.notifyDataSetChanged()
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
        var clickItemChildPos = 0
        var clickItemGroupPos = 0

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
            BKLog.d("getGroupView clickItemChildPos->$clickItemChildPos clickItemGroupPos->$clickItemGroupPos")
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
            BKLog.d("getChildView clickItemChildPos->$clickItemChildPos clickItemGroupPos->$clickItemGroupPos")

            if (clickItemChildPos == childPosition && groupPosition == clickItemGroupPos) {
                childViewHolder.tvCourseName.setTextColor(Color.RED)
                childViewHolder.tvTime.setTextColor(Color.RED)
                childViewHolder.tvPos.setTextColor(Color.RED)
                childViewHolder.tvProcess.setTextColor(Color.RED)
            } else {
                childViewHolder.tvCourseName.setTextColor(Color.BLACK)
                childViewHolder.tvTime.setTextColor(Color.BLACK)
                childViewHolder.tvPos.setTextColor(Color.BLACK)
                childViewHolder.tvProcess.setTextColor(Color.BLACK)
            }
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

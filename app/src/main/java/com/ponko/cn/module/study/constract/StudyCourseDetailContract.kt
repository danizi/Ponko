package com.ponko.cn.module.study.constract

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.study.StudyCacheActivity
import com.ponko.cn.utils.ShareUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.media.base.XmVideoView
import retrofit2.Call
import retrofit2.Response

/**
 * 课程学习详情页面契约类 - MVP模式
 */
class StudyCourseDetailContract {
    /**
     * UI层
     */
    interface V {
        /**
         * 设置播放器控制器配置信息
         * @param coursesDetailCBean 请求课程接口回调实体
         */
        fun setVideoControlConfigInfo(coursesDetailCBean: CoursesDetailCBean?)

        /**
         * 设置播放器控制器分享信息 - 默认状态
         * @param coursesDetailCBean 请求课程接口回调实体
         */
        fun setVideoControlShareInfo(coursesDetailCBean: CoursesDetailCBean?)

        /**
         * 设置播放器控制器分享信息 - 点击状态
         * @param shareUrl   分享链接
         * @param shareTitle 分享标题
         * @param shareDes   分享描述
         */
        fun setVideoControlShareInfo(shareUrl: String, shareTitle: String?, shareDes: String?)

        /**
         * 设置播放器控制器播放列表信息
         * @param coursesDetailCBean 请求课程接口回调实体
         */
        fun setVideoControlPlayListInfo(coursesDetailCBean: CoursesDetailCBean?)

        /**
         * 设置播放器预览页面
         * @param coursesDetailCBean 请求课程接口回调实体
         */
        fun setVideoPre(coursesDetailCBean: CoursesDetailCBean?)

        /**
         * 设置显示播放列表
         * @param myExtendableListAdp 展开列表适配器
         */
        fun displayVideoExtendableList(myExtendableListAdp: MyExtendableListViewAdapter?)

        /**
         * 播放视频
         * @param coursesDetailCBean 请求课程接口回调实体
         * @param vid 视频唯一标识
         * @param groupPosition 点击展开列表的父组位置
         * @param childPosition 点击展开列表的子组位置
         */
        fun start(coursesDetailCBean:CoursesDetailCBean,vid: String, groupPosition: Int, childPosition: Int)

        /**
         * 二级列表的适配器
         */
        open class MyExtendableListViewAdapter(courseInfo: CoursesDetailCBean?) : BaseExpandableListAdapter() {
            val group = ArrayList<String>()
            val child = ArrayList<List<CoursesDetailCBean.ChaptersBean.SectionsBean>>()
            var clickItemChildPos = 0
            var clickItemGroupPos = 0
            var isPay = false

            init {
                for (chapter in courseInfo?.chapters!!) {
                    group.add(chapter.chapter_name!!)
                    child.add(chapter?.sections!!)
                }
                isPay = courseInfo.isPossess
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
                val isFree = child[groupPosition][childPosition].isFree
                val name = child[groupPosition][childPosition].name
                val vid = child[groupPosition][childPosition].vid
                val progress = child[groupPosition][childPosition].progress_duration.toFloat()
                val duration = child[groupPosition][childPosition].duration.toFloat()
                childViewHolder.tvPos.text = (childPosition + 1).toString()
                childViewHolder.tvCourseName.text = name
                childViewHolder.tvTime.text = TimeUtil.hhmmss(duration.toLong() * 1000) + " | "
                childViewHolder.tvProcess.text = (progress / duration * 100).toString() + "%"

                //课程是否本地缓存，缓存显示提示
                if (PonkoApp.courseDao?.isComplete(vid) == true) {
                    childViewHolder.tvLocal.visibility = View.VISIBLE
                } else {
                    childViewHolder.tvLocal.visibility = View.GONE
                }

                // 当前位置状态
                if (clickItemChildPos == childPosition && groupPosition == clickItemGroupPos) {
                    //选中状态
                    childViewHolder.tvPos.isEnabled = true
                    childViewHolder.tvPos.text = ""
                    childViewHolder.tvCourseName.isEnabled = true
                    childViewHolder.tvTime.isEnabled = true
                    childViewHolder.tvProcess.isEnabled = true
                } else {
                    //非选中状态
                    //课程是否免费
                    if (isPay || isFree) {
                        childViewHolder.tvPos.isEnabled = false
                        childViewHolder.tvCourseName.isEnabled = false
                        childViewHolder.tvTime.isEnabled = false
                        childViewHolder.tvProcess.isEnabled = false
                    } else {
                        childViewHolder.tvPos.setBackgroundResource(R.mipmap.free_lock)
                        childViewHolder.tvPos.width = 20
                        childViewHolder.tvPos.height = 20
                        childViewHolder.tvPos.text = ""
                    }
                }
                BKLog.d("getChildView clickItemChildPos->$clickItemChildPos clickItemGroupPos->$clickItemGroupPos")
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

            private class ChildViewHolder private constructor(val rootView: View, val tvPos: TextView, val tvCourseName: TextView, val tvTime: TextView, val tvProcess: TextView, val tvLocal: TextView) {
                companion object {

                    fun create(context: Context, parent: ViewGroup?): ChildViewHolder {
                        val rootView = LayoutInflater.from(context).inflate(R.layout.item_study_course_detail_course, parent, false)
                        val tvPos = rootView.findViewById<View>(R.id.tv_pos) as TextView
                        val tvCourseName = rootView.findViewById<View>(R.id.tv_course_name) as TextView
                        val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                        val tvProcess = rootView.findViewById<View>(R.id.tv_process) as TextView
                        val tvLocal = rootView.findViewById<View>(R.id.tv_local) as TextView

                        return ChildViewHolder(rootView, tvPos, tvCourseName, tvTime, tvProcess, tvLocal)
                    }
                }
            }
        }

        /**
         * 课程详情窗口ViewHolder
         */
        open class ViewHolder private constructor(val video: XmVideoView, val constraintLayout5: ConstraintLayout, val ivShare: ImageView, val ivColect: ImageView, val ivDown: ImageView, val expandList: ExpandableListView) {
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

    /**
     * 数据层
     */
    class M {
        /**
         * 哪个窗口跳转标识
         */
        var type = ""
        /**
         * 视频唯一标识
         */
        var vid = ""
        /**
         * 专题唯一标识
         */
        var typeId = ""
        /**
         * 专题老师
         */
        var teachers = ""
        /**
         * 专题课程数量
         */
        var num = 0L
        /**
         * 专题总时间
         */
        var duration = 0L
        /**
         * 点击位置记录
         */
        var clickItemGroupPos: Int = 0
        var clickItemChildPos: Int = 0
        /**
         * 是否订购
         */
        var isPay = false
        /**
         * 请求的接口信息
         */
        var coursesDetailCBean: CoursesDetailCBean? = null

        /**
         * 张开列表适配器
         */
        var myExtendableListAdp: V.MyExtendableListViewAdapter? = null

        /**
         * 请求详情课程接口
         */
        fun requestCourseDetailApi(typeId: String, callback: HttpCallBack<CoursesDetailCBean>?) {
            if (TextUtils.isEmpty(typeId)) {
                return
            }
            if (callback == null) {
                return
            }
            PonkoApp.studyApi?.getCourseDetail(typeId)?.enqueue(callback)
        }
    }

    /**
     * 控制层
     * @param context 上下文对象
     * @param v UI层
     */
    class Present(val context: Context, val v: V) {
        private val model = M()

        /**
         * 点击了收藏
         */
        fun clickCollect() {
            if (model.isPay) {
                BKLog.d("点击收藏")
            } else {
                ToastUtil.show("请先购买订购")
            }
        }

        /**
         * 点击了下载
         */
        fun clickDown() {
            if (model.isPay) {
                BKLog.d("点击下载缓存页面")
                StudyCacheActivity.start(context, model.typeId, model.teachers, model.num, model.duration)
            } else {
                ToastUtil.show("请先购买订购")
            }
        }

        /**
         * 点击了分享
         */
        fun clickShare() {
            if (model.isPay) {
                BKLog.d("点击分享")
                val id = model.coursesDetailCBean?.chapters!![model.clickItemGroupPos]!!.sections[model.clickItemChildPos].id
                //设置分享信息
                val shareUrl = model.coursesDetailCBean?.share_base_url!! + "/" + id
                val shareTitle = model.coursesDetailCBean?.title
                val shareDes = model.coursesDetailCBean?.features
                ShareUtil.showShareDlg(context, shareUrl, shareTitle, shareDes)
            } else {
                ToastUtil.show("请先购买订购")
            }
        }

        /**
         * 点击了展开列表中的item
         */
        fun clickExpandListItem(parent: ExpandableListView, v: View, groupPosition: Int, childPosition: Int, id: Long): Boolean? {
            if (model.isPay) {
                //设置分享信息
                val cid = model.coursesDetailCBean?.chapters!![groupPosition].sections[childPosition].id
                val shareUrl = model.coursesDetailCBean?.share_base_url!! + "/" + cid
                val shareTitle = model.coursesDetailCBean?.title
                val shareDes = model.coursesDetailCBean?.features
                this.v.setVideoControlShareInfo(shareUrl, shareTitle, shareDes)

                //点击播放
                val vid = model.coursesDetailCBean?.chapters!![groupPosition].sections[childPosition].vid
                this.v.start(model.coursesDetailCBean!!,vid, groupPosition, childPosition)

                //点击之后刷新item选中状态
                model.myExtendableListAdp?.clickItemChildPos = childPosition
                model.myExtendableListAdp?.clickItemGroupPos = groupPosition
                model.myExtendableListAdp?.notifyDataSetChanged()

                //打印日志
                val name = model.coursesDetailCBean?.chapters!![groupPosition].sections[childPosition].name
                BKLog.d("点击：$groupPosition  $childPosition vid:$name")
            } else {
                ToastUtil.show("请先购买课程！")
            }
            return true
        }

        /**
         * 获取跳转信息
         */
        fun getIntentExtra(intent: Intent?) {
            model.type = intent?.getStringExtra("type")!!
            model.typeId = intent.getStringExtra("typeId")
            model.teachers = intent.getStringExtra("teachers")
            model.num = intent.getLongExtra("num", 0L)
            model.duration = intent.getLongExtra("duration", 0L)
        }

        /**
         * 请求课程详情
         */
        fun requestCourseDetailApi() {
            model.requestCourseDetailApi(model.typeId, object : HttpCallBack<CoursesDetailCBean>() {
                override fun onSuccess(call: Call<CoursesDetailCBean>?, response: Response<CoursesDetailCBean>?) {
                    //保存请求的实体
                    model.coursesDetailCBean = response?.body()
                    //当前课程是否订购
                    model.isPay = model.coursesDetailCBean?.isPossess!!
                    //为控制器设置配置信息
                    v.setVideoControlConfigInfo(model.coursesDetailCBean)
                    //为控制器设置分享信息
                    v.setVideoControlShareInfo(model.coursesDetailCBean)
                    //为控制器设置播放列表信息
                    v.setVideoControlPlayListInfo(model.coursesDetailCBean)
                    //为预览设置视频vid和预览图片
                    v.setVideoPre(model.coursesDetailCBean)
                    //设置展示列表
                    model.myExtendableListAdp = StudyCourseDetailContract.V.MyExtendableListViewAdapter(model.coursesDetailCBean)
                    v.displayVideoExtendableList(model.myExtendableListAdp)
                }
            })
        }
    }
}
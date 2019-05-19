package com.ponko.cn.module.free

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp.Companion.freeApi
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.CoursesListCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import retrofit2.Call
import retrofit2.Response


class FreeMoreAct : RefreshLoadAct<Any, CoursesListCBean>() {

    companion object {
        fun start(context: Context?, title: String, typeId: String) {
            val intent = Intent(context, FreeMoreAct::class.java)
            intent.putExtra("title", title)
            intent.putExtra("typeId", typeId)
            context?.startActivity(intent)
        }
    }

    private var typeId: String = ""
    private var title: String = ""

    override fun initDisplay() {
        disableRefresh = true
        disableLoad = true
        super.initDisplay()
        typeId = intent.getStringExtra("typeId")
        title = intent.getStringExtra("title")
        BarUtil.addBar1(this, viewHolder?.toolbar, title)
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0, 1),
                arrayOf(MoreTopViewHolder::class.java, MoreContentViewHolder::class.java),
                arrayOf(MoreTopBean::class.java, CoursesListCBean.ListBean::class.java),
                arrayOf(R.layout.item_free_more_top, R.layout.item_free_more_content))
    }

    override fun requestMoreApi() {}

    override fun requestRefreshApi() {
        freeApi?.freeList(typeId)?.enqueue(object : HttpCallBack<CoursesListCBean>() {
            override fun onSuccess(call: Call<CoursesListCBean>?, response: Response<CoursesListCBean>?) {
                requestRefreshSuccess(response?.body())
            }
        })
    }

    override fun multiTypeData(body: CoursesListCBean?): List<Any> {
        val data = ArrayList<Any>()
        data.add(MoreTopBean(
                body?.id,
                body?.title,
                body?.summary,
                body?.summary2,
                body?.summary3,
                body?.logo_url,
                body?.pay_url,
                body?.sort!!
        ))
        if (!body.list?.isEmpty()!!) {
            data.addAll(body.list)
        }
        return data
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

    /**
     * 更多頂部
     */
    class MoreTopViewHolder(view: View) : BaseViewHolder(view) {

        private class ViewHolder private constructor(val ivLogo: ImageView, val button: Button, val llDes: LinearLayout, val llBook: LinearLayout, val ivBook: ImageView, val tvBook: TextView, val llListen: LinearLayout, val ivListen: ImageView, val tvListen: TextView, val tv3: TextView) {
            companion object {
                fun create(rootView: View): ViewHolder {
                    val ivLogo = rootView.findViewById<View>(R.id.iv_logo) as ImageView
                    val button = rootView.findViewById<View>(R.id.button) as Button
                    val llDes = rootView.findViewById<View>(R.id.ll_des) as LinearLayout
                    val llBook = rootView.findViewById<View>(R.id.ll_book) as LinearLayout
                    val ivBook = rootView.findViewById<View>(R.id.iv_book) as ImageView
                    val tvBook = rootView.findViewById<View>(R.id.tv_book) as TextView
                    val llListen = rootView.findViewById<View>(R.id.ll_listen) as LinearLayout
                    val ivListen = rootView.findViewById<View>(R.id.iv_listen) as ImageView
                    val tvListen = rootView.findViewById<View>(R.id.tv_listen) as TextView
                    val tv3 = rootView.findViewById<View>(R.id.tv_3) as TextView
                    return ViewHolder(ivLogo, button, llDes, llBook, ivBook, tvBook, llListen, ivListen, tvListen, tv3)
                }
            }
        }

        private var ui: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = ViewHolder.create(itemView)
            }
            val moreTopBean = d as MoreTopBean
            val context = itemView.context
            Glide.with(context, moreTopBean.logo_url, ui?.ivLogo)
            ui?.button?.setOnClickListener {
                IntoTargetUtil.target(context, "pay", moreTopBean.pay_url)
                BKLog.d("点击支付按钮")
            }
            ui?.tvBook?.text = moreTopBean.summary
            ui?.tvListen?.text = moreTopBean.summary2
            ui?.tv3?.text = moreTopBean.summary3
        }
    }

    /**
     * 课程内容页面
     */
    class MoreContentViewHolder(view: View) : BaseViewHolder(view) {


        private class ViewHolder private constructor(val clVideo: ConstraintLayout, val ivAvatar: ImageView, val ivPlayPause: ImageView, val tvTitle: TextView, val clDetail: LinearLayout, val tvDes: TextView) {
            companion object {

                fun create(rootView: View): ViewHolder {
                    val clVideo = rootView.findViewById<View>(R.id.cl_video) as ConstraintLayout
                    val ivAvatar = rootView.findViewById<View>(R.id.iv_avatar) as ImageView
                    val ivPlayPause = rootView.findViewById<View>(R.id.iv_play_pause) as ImageView
                    val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                    val clDetail = rootView.findViewById<View>(R.id.cl_detail) as LinearLayout
                    val tvDes = rootView.findViewById<View>(R.id.tv_des) as TextView
                    return ViewHolder(clVideo, ivAvatar, ivPlayPause, tvTitle, clDetail, tvDes)
                }
            }
        }

        private var ui: ViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = ViewHolder.create(itemView)
            }
            val listBean = d as CoursesListCBean.ListBean
            val context = itemView.context
            ui?.tvTitle?.text = listBean.title
            ui?.tvDes?.text = listBean.summary
            ui?.clDetail?.setOnClickListener {
                FreeDetailsAct.start(context, listBean.id)
                BKLog.d("点击详情")
            }
            ui?.ivAvatar?.setOnClickListener {
                FreeDetailsAct.start(context, listBean.id)
                BKLog.d("点击详情")
            }
            Glide.with(context, listBean.avatar, ui?.ivAvatar)
        }
    }

    class MoreTopBean(var id: String?, var title: String?, var summary: String?, var summary2: String?, var summary3: String?, var logo_url: String?, var pay_url: String?, var sort: Int)
    class MoreContentBean(var list: List<CoursesListCBean.ListBean>?)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_free_more)
//    }
}

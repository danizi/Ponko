package com.ponko.cn.module.my.option.acount

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.SpannableString
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp.Companion.myApi
import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.bean.ProfileCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.BarUtil
import com.ponko.cn.utils.Glide
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Response

class PersonalActivity : AppCompatActivity() {


    private var viewHolder: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal)

        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }

        //设置标题
        BarUtil.addBar1(this, viewHolder?.toolbar, "个人信息", "保存", View.OnClickListener {
            requestSavePersonInfoApi()
        })

        //请求个人信息
        requestPersonApi()
    }

    /**
     * 请求个人信息
     */
    private fun requestPersonApi() {
        myApi?.basic_info()?.enqueue(object : HttpCallBack<ProfileCBean>() {
            override fun onSuccess(call: Call<ProfileCBean>?, response: Response<ProfileCBean>?) {
                val body = response?.body()
                setDisplayInfo(body)
                setDisplayAccount(body)
                setDisplayOther(body)
            }

            /**
             * 展示个人信息
             */
            private fun setDisplayInfo(body: ProfileCBean?) {
                val adapter = object : BaseRvAdapter() {}
                adapter.addItemViewDelegate(0, AccountItemViewHolder::class.java, String::class.java, R.layout.item_account_my_edit)
                adapter.data?.add(body?.account?.avatar!!)//头像
                adapter.data?.add(body?.account?.nickname!!)//昵称
                adapter.data?.add(body?.account?.realName!!)//名字
                viewHolder?.rvAccount?.adapter = adapter
                viewHolder?.rvAccount?.layoutManager = LinearLayoutManager(this@PersonalActivity)
            }

            /**
             * 展示社交账号
             */
            private fun setDisplayAccount(body: ProfileCBean?) {
                val adapter = object : BaseRvAdapter() {}
                adapter.addItemViewDelegate(0, CommunicationItemViewHolder::class.java, String::class.java, R.layout.item_account_my_edit)
                adapter.data?.add(body?.account?.qq!!)//qq
                adapter.data?.add(body?.account?.weiXin!!)//微信
                adapter.data?.add(body?.account?.email!!) //邮箱
                viewHolder?.rvCommunication?.adapter = adapter
                viewHolder?.rvCommunication?.layoutManager = LinearLayoutManager(this@PersonalActivity)
            }

            /**
             * 展示其他 公司 行业
             */
            private fun setDisplayOther(body: ProfileCBean?) {
                val adapter = object : BaseRvAdapter() {}
                adapter.addItemViewDelegate(0, OtherItemViewHolder::class.java, String::class.java, R.layout.item_account_my_edit)
                adapter.data?.add(body?.account?.companyName!!)   //公司名称
                adapter.data?.add(body?.account?.industry!!)   //行业
                adapter.data?.add(body?.account?.city!!)   //城市
                adapter.data?.add(body?.account?.intention!!)   //用途
                viewHolder?.rvOther?.adapter = adapter
                viewHolder?.rvOther?.layoutManager = LinearLayoutManager(this@PersonalActivity)

            }

            override fun onFailure(call: Call<ProfileCBean>?, msg: String?) {
                super.onFailure(call, msg)
                BKLog.e("请求个人信息失败")
            }
        })
    }

    /**
     * 保存个人信息
     */
    private fun requestSavePersonInfoApi() {
        val accountBean = ProfileCBean.AccountBean()
        accountBean.avatar = viewHolder?.rvAccount?.layoutManager?.getChildAt(0)?.findViewById<EditText>(R.id.et)?.text.toString()//头像
        accountBean.nickname = viewHolder?.rvAccount?.layoutManager?.getChildAt(1)?.findViewById<EditText>(R.id.et)?.text.toString()//昵称
        accountBean.realName = viewHolder?.rvAccount?.layoutManager?.getChildAt(2)?.findViewById<EditText>(R.id.et)?.text.toString()//名字

        accountBean.qq = viewHolder?.rvCommunication?.layoutManager?.getChildAt(0)?.findViewById<EditText>(R.id.et)?.text.toString()//qq
        accountBean.weiXin = viewHolder?.rvCommunication?.layoutManager?.getChildAt(1)?.findViewById<EditText>(R.id.et)?.text.toString()//微信
        accountBean.email = viewHolder?.rvCommunication?.layoutManager?.getChildAt(2)?.findViewById<EditText>(R.id.et)?.text.toString() //邮箱

        accountBean.companyName = viewHolder?.rvOther?.layoutManager?.getChildAt(0)?.findViewById<EditText>(R.id.et)?.text.toString() //公司名称
        accountBean.industry = viewHolder?.rvOther?.layoutManager?.getChildAt(1)?.findViewById<EditText>(R.id.et)?.text.toString() //行业
        accountBean.city = viewHolder?.rvOther?.layoutManager?.getChildAt(2)?.findViewById<EditText>(R.id.et)?.text.toString() //城市
        accountBean.intention = viewHolder?.rvOther?.layoutManager?.getChildAt(3)?.findViewById<EditText>(R.id.et)?.text.toString() //用途

        val params = HashMap<String, String>()
        params["nickname"] = accountBean.nickname
        params["realName"] = accountBean.realName
        params["email"] = accountBean.email
        params["weiXin"] = accountBean.weiXin
        params["qq"] = accountBean.qq
        params["currentIndustry"] = accountBean.industry
        params["currentCompanyName"] = accountBean.companyName
        params["intention"] = accountBean.intention
        myApi?.saveProfile(params)?.enqueue(object : HttpCallBack<GeneralBean>() {
            override fun onSuccess(call: Call<GeneralBean>?, response: Response<GeneralBean>?) {
                BKLog.d("保存编辑好的个人信息${accountBean.toString()}")
                Toast.makeText(this@PersonalActivity, "保存信息成功", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Activity UI ViewHolder
     */
    private class ViewHolder private constructor(val toolbar: Toolbar, val tvAccount: TextView, val rvAccount: RecyclerView, val tvCommunication: TextView, val rvCommunication: RecyclerView, val tvOther: TextView, val rvOther: RecyclerView) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val toolbar = rootView.findViewById<View>(R.id.toolbar) as Toolbar
                val tvAccount = rootView.findViewById<View>(R.id.tv_account) as TextView
                val rvAccount = rootView.findViewById<View>(R.id.rv_account) as RecyclerView
                val tvCommunication = rootView.findViewById<View>(R.id.tv_communication) as TextView
                val rvCommunication = rootView.findViewById<View>(R.id.rv_communication) as RecyclerView
                val tvOther = rootView.findViewById<View>(R.id.tv_other) as TextView
                val rvOther = rootView.findViewById<View>(R.id.rv_other) as RecyclerView
                return ViewHolder(toolbar, tvAccount, rvAccount, tvCommunication, rvCommunication, tvOther, rvOther)
            }
        }
    }

    private class ItemBean(var content: HashMap<String, String>)

    /**
     * 个人信息ViewHolder 头像 昵称 名字
     */
    open class AccountItemViewHolder(view: View) : BaseViewHolder(view) {
        private var viewHolder: ItemViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ItemViewHolder.create(itemView)
            }
            val des = d as String
            val context = itemView.context
            if (position == 0) {
                viewHolder?.ivArrow?.visibility = View.VISIBLE
                viewHolder?.et?.visibility = View.INVISIBLE
                Glide.with(context, des, viewHolder?.ivArrow)
                viewHolder?.ivArrow?.setOnClickListener {
                    BKLog.d("点击头像")
//                    ActivityUtil.startActivity(context, Intent(context,CusTakePhotoActivity::class.java))
                }
            } else {
                viewHolder?.ivArrow?.visibility = View.INVISIBLE
                viewHolder?.et?.visibility = View.VISIBLE
                if (TextUtils.isEmpty(des)) {
                    viewHolder?.et?.hint = SpannableString("请输入内容")
                } else {
                    viewHolder?.et?.setText(des)
                }
            }
        }
    }

    /**
     * 账号ViewHolder QQ 微信 邮箱
     */
    open class CommunicationItemViewHolder(view: View) : BaseViewHolder(view) {
        private var viewHolder: ItemViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ItemViewHolder.create(itemView)
            }
            val des = d as String
            val context = itemView.context
            viewHolder?.ivArrow?.visibility = View.INVISIBLE
            viewHolder?.et?.visibility = View.VISIBLE
            if (TextUtils.isEmpty(des)) {
                viewHolder?.et?.hint = SpannableString("请输入内容")
            } else {
                viewHolder?.et?.setText(des)
            }
        }
    }

    /**
     * 其他ViewHolder 公司名称 行业 城市 用途
     */
    open class OtherItemViewHolder(view: View) : BaseViewHolder(view) {
        private var viewHolder: ItemViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ItemViewHolder.create(itemView)
            }
            val des = d as String
            val context = itemView.context
            viewHolder?.ivArrow?.visibility = View.INVISIBLE
            viewHolder?.et?.visibility = View.VISIBLE
            if (TextUtils.isEmpty(des)) {
                viewHolder?.et?.hint = SpannableString("请输入内容")
            } else {
                viewHolder?.et?.setText(des)
            }
        }

    }

    /**
     * 编辑条目ViewHolder
     */
    open class ItemViewHolder private constructor(val tv: TextView, val ivArrow: CircleImageView, val et: EditText, val divider: View) {
        companion object {

            fun create(rootView: View): ItemViewHolder {
                val tv = rootView.findViewById<View>(R.id.tv) as TextView
                val ivArrow = rootView.findViewById<View>(R.id.iv_arrow) as CircleImageView  //图片
                val et = rootView.findViewById<View>(R.id.et) as EditText
                val divider = rootView.findViewById(R.id.divider) as View
                return ItemViewHolder(tv, ivArrow, et, divider)
            }
        }
    }

}

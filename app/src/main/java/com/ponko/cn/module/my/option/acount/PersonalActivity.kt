package com.ponko.cn.module.my.option.acount

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.utils.BarUtil
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import de.hdodenhof.circleimageview.CircleImageView

class PersonalActivity : AppCompatActivity() {

    private var viewHolder: ViewHolder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal)
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(this)
        }
        BarUtil.addBar1(this, viewHolder?.toolbar, "收件地址", "个人信息", View.OnClickListener {
            BKLog.d("保存个人信息")
        })
    }

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

    private class AccountItemViewHolder(view: View) : BaseViewHolder(view) {
        private var viewHolder: ItemViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ItemViewHolder.create(itemView)
            }
            val itemBean = d as ItemBean
        }
    }

    private class CommunicationItemViewHolder(view: View) : BaseViewHolder(view) {
        private var viewHolder: ItemViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ItemViewHolder.create(itemView)
            }
            val itemBean = d as ItemBean
        }
    }

    private class OtherItemViewHolder(view: View) : BaseViewHolder(view) {
        private var viewHolder: ItemViewHolder? = null

        override fun bindData(d: Any, position: Int) {
            if (viewHolder == null) {
                viewHolder = ItemViewHolder.create(itemView)
            }
            val itemBean = d as ItemBean
        }
    }

    private class ItemViewHolder private constructor(val tv: TextView, val ivArrow: CircleImageView, val et: EditText, val divider: View) {
        companion object {

            fun create(rootView: View): ItemViewHolder {
                val tv = rootView.findViewById<View>(R.id.tv) as TextView
                val ivArrow = rootView.findViewById<View>(R.id.iv_arrow) as CircleImageView
                val et = rootView.findViewById<View>(R.id.et) as EditText
                val divider = rootView.findViewById(R.id.divider) as View
                return ItemViewHolder(tv, ivArrow, et, divider)
            }
        }
    }

}

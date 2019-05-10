package com.ponko.cn.module.my.option

import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.BindItemViewHolderBean
import com.ponko.cn.bean.RecordCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.common.RefreshLoadAct
import com.ponko.cn.module.common.RefreshLoadFrg
import com.ponko.cn.module.my.holder.MyStudyRecordHolder
import com.xm.lib.common.base.rv.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response

/**
 * 学习历史
 */
class HistoryActivity : RefreshLoadAct<Any, ArrayList<RecordCBean.HistoryRecordBean>>() {

    override fun initDisplay() {
        addItemDecoration=false
        addBar1("学习历史")
        super.initDisplay()
    }

    override fun bindItemViewHolderData(): BindItemViewHolderBean {
        return BindItemViewHolderBean.create(
                arrayOf(0),
                arrayOf(MyStudyRecordHolder::class.java),
                arrayOf(Any::class.java),
                arrayOf(R.layout.item_course_study_record))
    }

    override fun requestMoreApi() {
        PonkoApp.myApi?.getRecordList(++page)?.enqueue(object : HttpCallBack<RecordCBean>() {
            override fun onSuccess(call: Call<RecordCBean>?, response: Response<RecordCBean>?) {
                val data = ArrayList<RecordCBean.HistoryRecordBean>()
                if(response?.body()?.recordWithToday!=null){
                    data.addAll(response.body()?.recordWithToday!!)
                }
                if(response?.body()?.recordWithEarlier?.isNotEmpty()!!){
                    data.addAll(response.body()?.recordWithEarlier!!)
                }

                requestMoreSuccess(data)
            }
        })
    }


    override fun requestRefreshApi() {
        PonkoApp.myApi?.getRecordList()?.enqueue(object : HttpCallBack<RecordCBean>() {
            override fun onSuccess(call: Call<RecordCBean>?, response: Response<RecordCBean>?) {
                val data = ArrayList<RecordCBean.HistoryRecordBean>()
                if(response?.body()?.recordWithToday!=null){
                    data.addAll(response.body()?.recordWithToday!!)
                }
                if(response?.body()?.recordWithEarlier?.isNotEmpty()!!){
                    data.addAll(response.body()?.recordWithEarlier!!)
                }
                requestRefreshSuccess(data)
            }
        })
    }

    override fun multiTypeData(body: ArrayList<RecordCBean.HistoryRecordBean>?): List<Any> {
        return body!!
    }

    override fun adapter(): BaseRvAdapter? {
        return object : BaseRvAdapter() {}
    }

    override fun presenter(): Any {
        return Any()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_history)
//    }
}

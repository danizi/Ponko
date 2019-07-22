package com.ponko.cn.module.my.v1.constract

import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.StoreProfileCMoreBean
import com.ponko.cn.http.HttpCallBack
import retrofit2.Call
import retrofit2.Response

/**
 * ViewPager Fragment契约类页面
 */
class ExchangeContract {
    interface V {
        fun reqeustExchangeRefreshApiSuccess(body: ArrayList<StoreProfileCMoreBean>?)
        fun reqeustExchangeMoreApiSuccess(body: ArrayList<StoreProfileCMoreBean>?)
    }

    class M {
        /**
         * 请求积分兑换数据
         */
        fun reqeustExchangeRefreshApi(cid: String, callback: HttpCallBack<ArrayList<StoreProfileCMoreBean>>) {
            reqeustExchangeMoreApi(cid, 1, callback)

        }

        /**
         * 请求积分兑换更多数据
         */
        fun reqeustExchangeMoreApi(cid: String, page: Int, callback: HttpCallBack<ArrayList<StoreProfileCMoreBean>>) {
            PonkoApp.myApi?.homeMore(cid, page)?.enqueue(callback)
        }
    }

    class Present(private val v: V?) {
        /**
         * 上拉加载开关
         */
        private val isDebug = PonkoApp.UI_DEBUG
        private val m = M()

        fun reqeustExchangeRefreshApi(cid: String) {
            m.reqeustExchangeRefreshApi(cid, object : HttpCallBack<ArrayList<StoreProfileCMoreBean>>() {
                override fun onSuccess(call: Call<ArrayList<StoreProfileCMoreBean>>?, response: Response<ArrayList<StoreProfileCMoreBean>>?) {
                    v?.reqeustExchangeRefreshApiSuccess(response?.body())
                }
            })
        }

        fun reqeustExchangeMoreApi(cid: String, page: Int) {
            if (isDebug) {
                val body = ArrayList<StoreProfileCMoreBean>()
                val testBean = StoreProfileCMoreBean()
                testBean.id = 2
                testBean.name = "测试-课程"
                testBean.layout = "bar"
                testBean.sort = 2
                testBean.stores = ArrayList<StoreProfileCMoreBean.StoresBean>()
                for (i in 0..9) {
                    val storesBean = StoreProfileCMoreBean.StoresBean()
                    storesBean.id = "47975b0c6f4e11e9b5c00242ac130004"
                    storesBean.name = "测试-帮课大学特训营第7期-$i"
                    storesBean.type = "COMMODITY"
                    storesBean.scores = 400
                    storesBean.picture = "http://cdn.tradestudy.cn/upload/product/20190506/a78e5a61aec6a4beca0c30387a759b2c.jpg"
                    testBean.stores.add(storesBean)
                }
                body.add(testBean)
                v?.reqeustExchangeMoreApiSuccess(body)
            } else {
                m.reqeustExchangeMoreApi(cid, page, object : HttpCallBack<ArrayList<StoreProfileCMoreBean>>() {
                    override fun onSuccess(call: Call<ArrayList<StoreProfileCMoreBean>>?, response: Response<ArrayList<StoreProfileCMoreBean>>?) {
                        val data = response?.body()
                        if (data != null && !data.isEmpty()) {
                            v?.reqeustExchangeMoreApiSuccess(response.body())
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<StoreProfileCMoreBean>>?, msg: String?) {
                        super.onFailure(call, msg)
                    }
                })
            }
        }
    }
}
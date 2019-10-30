package com.ponko.cn.api

import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.bean.OrderCBean
import com.ponko.cn.bean.ProductInfoCBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface PayApi {
    /**
     * @param payWay weiXin/alipay
     */
    @PUT("order")
    fun createProductOrder(@Query("payWay") payWay: String,
                           @Query("productId") productId: String,
                           @Query("tid") tid: String): Call<OrderCBean>

    /**
     * 订单信息
     */
    @GET("product/info")
    fun productInfo(@Query("id") productId: String, @Query("tid") tid: String=""): Call<ProductInfoCBean>

    /**
     * 查询订单是否完成
     */
    @GET("order/finish")
    fun orderFinish(@Query("sn") key: String): Call<GeneralBean>

}
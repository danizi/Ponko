package com.ponko.cn.api

import com.ponko.cn.bean.OrderCBean
import retrofit2.Call
import retrofit2.http.PUT
import retrofit2.http.Query

interface PayApi {
    /**
     * @param payWay weiXin/alipay
     */
    @PUT("order")
    fun createProductOrder(@Query("payWay") payWay: String,
                           @Query("productId") productId: String): Call<OrderCBean>
}
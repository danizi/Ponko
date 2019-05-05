package com.ponko.cn.api

import com.ponko.cn.bean.AddressBean
import com.ponko.cn.bean.GeneralBean
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AddressApi {
    @POST("address/save")
    @FormUrlEncoded
    abstract fun saveAddress(@FieldMap params: Map<String, String>): Call<GeneralBean>

    @GET("address")
    abstract fun getAddress(): Call<AddressBean>
}
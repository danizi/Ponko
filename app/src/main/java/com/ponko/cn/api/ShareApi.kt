package com.ponko.cn.api

import com.ponko.cn.bean.ShareCBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ShareApi {
    /**
     * 分享接口
     */
    @GET("app/share")
    fun share(@Query("type") type: String, @Query("id") id: String):Call<ShareCBean>
}
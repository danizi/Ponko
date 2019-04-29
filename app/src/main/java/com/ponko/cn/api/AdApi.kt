package com.ponko.cn.api

import com.ponko.cn.bean.AdCBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AdApi {
    /**
     * 开头广告
     *
     * @return
     */
    @GET("app/launch/screen")
    fun screen(): Call<AdCBean>

    /**
     * 首页广告
     *
     * @return
     */
    @GET("app/launch/home")
    fun home(): Call<AdCBean>

    /**
     * 反馈用户点击广告的状态，反馈首页广告用户点击状态
     *
     * @param id 广告唯一标识 AdCBean.id
     * @param action 1 关闭广告 2 进入广告
     * @return
     */
    @POST("app/launch/record")
    fun feedback(@Query("id") id: String?, @Query("action") action: Int): Call<Any>
}
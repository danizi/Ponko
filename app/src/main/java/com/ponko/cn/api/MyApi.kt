package com.ponko.cn.api

import com.ponko.cn.bean.*
import com.xm.lib.common.http.NetBean
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface MyApi {

    ////////////////////////////////////
    ///        其他                   //
    ////////////////////////////////////
    /**
     * 开通学籍
     *
     * @return
     */
    @GET("products")
    fun products(): Call<Any>

    /**
     * 邀请好友
     *
     * @return
     */
    @GET("invites/products")
    fun invites(): Call<InviteFriendsBean>


    ////////////////////////////////////
    ///        个人信息相关           //
    ////////////////////////////////////
    /**
     * 获取个人信息
     *
     * @return
     */
    @GET("main?type=profile")
    fun basic_info(): Call<Any>

    /**
     * 上传个人信息，不包括头像
     */
    @POST("user/save")
    @FormUrlEncoded
    fun saveProfile(@FieldMap params: Map<String, String>): Call<GeneralBean>

    /**
     * 头像图片上传
     */
    @POST("image/upload")
    @Multipart
    fun saveProfilePhoto(@Part body: MultipartBody.Part): Call<GeneralBean>


    ////////////////////////////////////
    ///        积分商城相关           //
    ////////////////////////////////////
    /**
     * 积分 '获取' 记录
     */
    @GET("scores/histories/acquisition")
    fun integralObtainRecord(@Query("pn") page: String?): Call<StoreObtainLogBean>

    /**
     * 积分 '兑换' 记录
     */
    @GET("scores/histories/exchanged")
    fun integralObtainExchanged(@Query("pn") page: String?): Call<Any>

    /**
     * 积分排行榜
     */
    @GET("scores/ranking")
    fun rank(): Call<RankingV2>

    /**
     * 主界面
     */
    @GET("store/profile")
    fun home(): Call<StoreProfileBean>

    /**
     * 主界面上拉加载更多
     */
    @GET("store/profile/more")
    fun homeMore(@Query("cid") cid: String?, @Query("page") page: String?): Call<Any>

    /**
     * 积分任务
     */
    @GET("scores/tasks")
    fun tasks(@Query("pn") page: String?): Call<StoreTaskBean>


    /**
     * 签到
     */
    @POST("scores/tasks/signin")
    fun signin(): Call<NetBean>

    /**
     * 积分商品详情信息
     */
    @GET("store/product/detail")
    fun detail(@Query("id") id: String?): Call<StoreDetailBean>
}
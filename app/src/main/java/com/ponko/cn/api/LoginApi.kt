package com.ponko.cn.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface LoginApi {

    @POST("user/register/sms")
    fun registerSms(@Query("phone") phone: String):Call<Any>

    @POST("secure/phone/sms")
    fun resetPhoneSms(@Query("phone") phone: String)

    @POST("secure/password/sms")
    fun resetPasswordSms(@Query("phone") phone: String)

    @POST("user/login")
    fun login(@Query("phone") phone: String, @Query("password") password: String = "", @Query("code") code: String = ""):Call<Any>

    /**
     * @param deviceId 设备唯一标识，游客模式到注册需要携带该字段
     */
    @POST("user/register")
    fun register(@Query("phone") phone: String, @Query("password") password: String, @Query("securityCode") securityCode: String, @Query("deviceId") deviceId: String)

    @POST("user/login/oauth/bind")
    fun wxbind(@Query("phone") phone: String, @Query("password") password: String, @Query("code") code: String, @Query("token") token: String, @Query("type") type: String="wechat")


    ////////////////////////////////////
    ///        账号绑定相关           //
    ////////////////////////////////////
    /**
     * 微信绑定
     * @param map  params.put("token", unionId!!)
     *              params.put("type", "wechat")
     */
    @POST("user/bind-oauth")
    fun wechatBind(@QueryMap map: Map<String, String>): Call<ResponseBody>

    /**
     * 微信解绑
     * @param type wechat
     */
    @HTTP(method = "DELETE", path = "user/bind-oauth", hasBody = true)
    fun wechatUnBint(@Query("type") type: String): Call<ResponseBody>
}
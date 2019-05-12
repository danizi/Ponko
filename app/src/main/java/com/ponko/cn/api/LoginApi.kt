package com.ponko.cn.api

import com.ponko.cn.bean.GeneralBean
import com.ponko.cn.bean.OauthBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface LoginApi {

    ////////////////////////////////////
    ///        發送短信相關           //
    ////////////////////////////////////
    @POST("user/register/sms")
    fun registerSms(@Query("phone") phone: String): Call<GeneralBean>

    @POST("secure/phone/sms")
    fun resetPhoneSms(@Query("phone") phone: String): Call<GeneralBean>

    @POST("secure/password/sms")
    fun resetPasswordSms(@Query("phone") phone: String): Call<GeneralBean>

    @POST("user/forgot/sms")
    fun forgetPasswordSms(@Query("phone") phone: String): Call<GeneralBean>

    ////////////////////////////////////
    ///        检查验证码相關         //
    ////////////////////////////////////
    @POST("user/forgot/token")
    @FormUrlEncoded
    fun checkForgetCode(@Field("phone") phone: String, @Field("securityCode") code: String): Call<GeneralBean>

    @POST("secure/password/token")
    @FormUrlEncoded
    fun checkResetPwdCode(@Field("securityCode") code: String): Call<GeneralBean>

    @POST("secure/phone/token")
    @FormUrlEncoded
    fun checkResetPhoneCode(@Field("securityCode") code: String): Call<GeneralBean>

    ////////////////////////////////////
    ///        賬號相关               //
    ////////////////////////////////////
    @POST("user/login")
    fun login(@Query("phone") phone: String, @Query("password") password: String = "", @Query("code") code: String = ""): Call<GeneralBean>

    /**
     * @param deviceId 设备唯一标识，游客模式到注册需要携带该字段
     */
    @POST("user/register")
    fun register(@Query("phone") phone: String, @Query("password") password: String, @Query("securityCode") securityCode: String, @Query("deviceId") deviceId: String): Call<GeneralBean>

    @POST("user/forgot/reset_password")
    @FormUrlEncoded
    fun resetPassword(@Field("newPassword") newPassword: String?, @Query("securityCode") securityCode: String): Call<GeneralBean>

    /**
     * 賬號創建加綁定
     */
    @POST("user/login/oauth/bind")
    fun wxbind(@Query("phone") phone: String, @Query("password") password: String, @Query("code") code: String, @Query("type") type: String = "wechat"): Call<GeneralBean>


    ////////////////////////////////////
    ///        账号绑定相关           //
    ////////////////////////////////////
    /**
     * 微信绑定
     * @param map  params.put("token", unionId!!)
     *              params.put("type", "wechat")
     */
    @POST("user/bind-oauth")
    fun wechatBind(@QueryMap map: Map<String, String>): Call<GeneralBean>

    /**
     * 微信解绑
     * @param type wechat
     */
    @HTTP(method = "DELETE", path = "user/bind-oauth", hasBody = true)
    fun wechatUnBint(@Query("type") type: String): Call<ResponseBody>

    /**
     * 微信鉴权
     * @param code 微信鉴权回调code
     */
    @POST("user/login/oauth")
    fun weChatOauth(@Query("type") type: String?="wechat", @Query("code") code: String): Call<OauthBean>

    @POST("user/guest")
    @FormUrlEncoded
    fun touristsSignIn(@Field("deviceId") deviceId: String): Call<GeneralBean>
}
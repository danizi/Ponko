package com.ponko.cn.api

import com.ponko.cn.bean.*
import com.xm.lib.common.http.NetBean
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface MyApi {

    ////////////////////////////////////
    ///        个人信息相关           //
    ////////////////////////////////////
    /**
     * 获取个人信息
     *
     * @return
     */
    @GET("main?type=profile")
    fun basic_info(): Call<ProfileCBean>

    /**
     * 上传个人信息 ps：头像是在选取头像的时候就上传到后台了
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
    ///        其他                   //
    ////////////////////////////////////
    /**
     * 开通学籍
     *
     * @return
     */
    @GET("products")
    fun products(): Call<List<OpenCBean>>

    /**
     * 邀请好友
     *
     * @return
     */
    @GET("invites/products")
    fun invites(): Call<InviteFriendsBean>


    ////////////////////////////////////
    ///        积分商城相关           //
    ////////////////////////////////////
    /**
     * 积分 '获取' 记录
     */
    @GET("scores/histories/acquisition")
    fun integralObtainRecord(@Query("pn") page: Int? = 1): Call<StoreObtainLogBean>

    /**
     * 积分 '兑换' 记录
     */
    @GET("scores/histories/exchanged")
    fun integralObtainExchanged(@Query("pn") page: Int? = 1): Call<ArrayList<ExchangedHistoriesCBean>>

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
     * @param cid   1 2 .
     * @param page  从1开始
     */
    @GET("store/profile/more")
    fun homeMore(@Query("cid") cid: String?, @Query("page") page: Int?): Call<ArrayList<StoreProfileCMoreBean>>

    /**
     * 积分任务
     */
    @GET("scores/tasks")
    fun tasks(@Query("pn") page: Int? = 1): Call<StoreTaskBean>

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


    ////////////////////////////////////
    ///        收获地址相关           //
    ////////////////////////////////////
    @POST("address/save")
    @FormUrlEncoded
    abstract fun saveAddress(@FieldMap params: Map<String, String>): Call<GeneralBean>

    @GET("address")
    abstract fun getAddress(): Call<AddressBean>


    ////////////////////////////////////
    ///        收藏相关               //
    ////////////////////////////////////
    @GET("stars")
    fun getCollections(): Call<Collections>

    @HTTP(method = "DELETE", path = "stars/sections", hasBody = false)
    // @DELETE("stars/sections")
    fun deleteSection(@Query("ids") array: String): Call<NetBean>

    @HTTP(method = "DELETE", path = "stars/courses", hasBody = false)
    fun deleteMultiple(@Query("ids") array: String): Call<NetBean>

    /**
     * 获取某个课程下的小节
     */
    @GET("stars/courses")
    fun getSections(@Query("id") courseId: String): Call<MutableList<SectionCBean>>


    ////////////////////////////////////
    ///        学习历史               //
    ////////////////////////////////////
    @GET("study/histories")
    fun getRecordList(@Query("page") pageIndex: Int? = 1): Call<RecordCBean>


    ////////////////////////////////////
    ///        消息提醒               //
    ////////////////////////////////////
    @GET("message")
    fun getRemindList(@Query("pn") query: Int? = 1): Call<ArrayList<RemindCBean>>

    @POST("message/read")
    @FormUrlEncoded
    fun readRemind(@Field("id") id: String): Call<GeneralBean>

    /**
     * 353版本及以后调用的接口
     */
    @GET("message")
    fun getRemindListV2(@Query("pn") query: Int? = 1): Call<RemindCBeanV2>

    @POST("message/read")
    @FormUrlEncoded
    fun readRemindV2(@Field("id") id: String): Call<ReadMsg>

    /**
     * @param ids 为空则删除所有消息，xxx,xxxx删除指定id消息
     */
    @DELETE("message/clean")
    fun cleanRemind(@Query("ids") ids: String? = ""): Call<GeneralBean>


    ////////////////////////////////////
    ///        学习时长排行           //
    ////////////////////////////////////
    @GET("ranking")
    fun learnRanking(): Call<LearnRankingCBean>


    ////////////////////////////////////
    ///        积分兑换相关           //
    ////////////////////////////////////
    @POST("exchange")
    @FormUrlEncoded
    fun exchangeCode(@Field("code") code: String): Call<GeneralBean>

    /**
     * 兑换商品
     */
    @FormUrlEncoded
    @POST("store/exchange")
    fun exchangeProduct(@Field("id") id: String): Call<GeneralBean>

    @Deprecated("")
    @GET("exchange/courses")
    fun exchangedCourse(): Call<MutableList<InternalCourseCBean>>

    @Deprecated("使用scores/histories/exchanged这个接口")
    @GET("exchange_list")
    fun exchangedProducts(): Call<MutableList<ExchangeProductCBean>>


    ////////////////////////////////////
    ///        常见问题帮助           //
    ////////////////////////////////////
    @GET("help")
    fun getHelpList(): Call<MutableList<HelpCBean>>

    ////////////////////////////////////
    ///        邀请相关               //
    ////////////////////////////////////
    @GET("invites/templates")
    fun shareInvites(@Query("productId") productId: String): Call<Invite>

    @GET("invites/records")
    //  @FormUrlEncoded
    fun shareInviteRecords(): Call<MutableList<InviteRecordBean>>

    @POST("invites/save")
    @FormUrlEncoded
    fun shareSave(@Field("image") url: String, @Field("saying") say: String): Call<GeneralBean>

}
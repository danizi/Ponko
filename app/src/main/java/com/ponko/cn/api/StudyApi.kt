package com.ponko.cn.api

import com.ponko.cn.bean.*
import com.xm.lib.common.http.NetBean
import retrofit2.Call
import retrofit2.http.*

interface StudyApi {
    /**
     * 学习首页450之前版本
     */
    @GET("main")
    fun main(@Query("old") old: String? = "1"): Call<MainCBean>

    /**
     * 学习首页450版本
     */
    @GET("main")
    fun main2(): Call<Main2CBean>

    /**
     * 课程专题所有课程
     * @param typeId 课程类型标识
     */
    @GET("course/top-types")
    fun getSpecialList(@Query("typeId") typeId: String?): Call<ArrayList<MainCBean.TypesBeanX.TypesBean>>

    /**
     * 获取专题下的所有课程
     */
    @Deprecated("老版本")
    @GET("course/all")
    fun getSpecialAllCourse2(@Query("typeId") typeId: String): Call<ArrayList<OutInternalCourse>>

    @GET("course/all")
    fun getSpecialAllCourse(@Query("typeId") typeId: String): Call<ArrayList<CourseAllCBeanV2>>

    @GET("course/all")
    fun getSpecialAllCourseV2(@Query("typeId") typeId: String): Call<CourseAllCBean>

    /**
     * 课程详情
     */
    @GET("course")
    fun getCourseDetail(@Query("courseId") courseId: String): Call<CoursesDetailCBean>

    /**
     * 收藏
     */
    @FormUrlEncoded
    @POST("stars/section")
    fun addCollection(@Field("courseId") courseId: String, @Field("sectionId") sectionId: String): Call<Any>

    /**
     * 上传课程播放信息 userid    保利威视提供的唯一标识
     *                  secretKey 保利威视提供的key
     *                  vid       播放器视频的唯一标识
     * @param params format = "json"
     *                ptime  = Long.toString(System.currentTimeMillis());
     *                sign   = "format=" + format + "&ptime=" + ptime + "&vid=" + vid + secretKey; SHA1签名方式
     */
    @Deprecated("")
    @FormUrlEncoded
    @POST("http://api.polyv.net/v2/video/{userid}/get-video-msg")
    fun getVideoInfo(@FieldMap params: Map<String, String>): Call<Any>

    /**
     * 上传学习视频进度
     * params :
     *        completed  是否完成
     *        duration   视频总时长
     *        position   视频进度（单位秒）
     *        courseId   课程id
     *        sectionId  章节id
     */
    @FormUrlEncoded
    @POST("study/histories/update")
    fun updateVideoInfo(@FieldMap params: Map<String, String>): Call<NetBean>
}
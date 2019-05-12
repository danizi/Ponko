package com.ponko.cn.api

import com.ponko.cn.bean.MainCBean
import com.ponko.cn.bean.OutInternalCourse
import com.ponko.cn.bean.StudyCourseBean
import retrofit2.Call
import retrofit2.http.*

interface StudyApi {
    /**
     * 学习首页
     */
    @GET("main")
    fun main(): Call<MainCBean>

    /**
     * 课程专题所有课程
     * @param typeId 课程类型标识
     */
    @GET("course/top-types")
    fun getSpecialList(@Query("typeId") typeId: String?): Call<ArrayList<MainCBean.TypesBeanX.TypesBean>>

    /**
     * 获取专题下的所有课程
     */
    @GET("course/all")
    fun getInternalCourse(@Query("typeId") typeId: String): Call<ArrayList<OutInternalCourse>>

    /**
     * 收藏
     */
    @FormUrlEncoded
    @POST("stars/section")
    fun addCollection(@Field("courseId") courseId: String, @Field("sectionId") sectionId: String): Call<Any>

    /**
     * 上传课程播放信息
     */
    @FormUrlEncoded
    @POST("study/histories/update")
    fun updateVideoInfo(@FieldMap params: Map<String, String>): Call<Any>
}
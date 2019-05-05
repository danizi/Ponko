package com.ponko.cn.api

import com.ponko.cn.bean.MainCBean
import retrofit2.Call
import retrofit2.http.GET

interface StudyApi {
    @GET("main")
    fun main(): Call<MainCBean>

    /**
     * 获取指定类型课程的所有视频
     * @param typeId 课程类型标识
     */
//    @GET("course/top-types")
//    fun getCourse(@Query("typeId") typeId: String?): Call<ArrayList<StudyCourseBean>>

}
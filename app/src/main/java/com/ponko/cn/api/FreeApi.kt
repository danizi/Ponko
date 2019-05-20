package com.ponko.cn.api

import com.ponko.cn.bean.CoursesCBean
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.bean.CoursesListCBean
import com.ponko.cn.bean.DetailCBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FreeApi {
    /**
     * 试听主页面
     *
     * @return
     */
    @GET("courses/trial")
    fun freeHome(): Call<CoursesCBean>

    /**
     * 试听列表
     * courses/trial/ListBean?typeId=上面接口返回的
     *
     * @return
     */
    @GET("courses/trial/list")
    fun freeList(@Query("typeId") typeId: String): Call<CoursesListCBean>

    /**
     * 试听详情页面
     * courses/trial/detail/info?id=上面接口返回的
     *
     * @return
     */
    @GET("courses/trial/detail")
    fun freeDetail(@Query("id") id: String): Call<DetailCBean>
}
package com.ponko.cn.api

import com.ponko.cn.bean.ActivityCBean
import com.ponko.cn.bean.AnalysisCBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface InterflowApi {
    @GET("activities")
    fun getParties(@Query("page") page: Int, @Query("size") size: Int): Call<ArrayList<ActivityCBean>>

    @GET("analysis")
    fun getAnalysis(@Query("page") page: Int, @Query("size") size: Int): Call<AnalysisCBean>

    @GET("analysis")
    fun getAnalysis(@Query("typeId") typeId: String="", @Query("page") page: Int, @Query("size") size: Int): Call<AnalysisCBean>

}
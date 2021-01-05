package cn.leizy.retrofitdemo.retrofit

import cn.leizy.retrofitdemo.retrofit.ann.Field
import cn.leizy.retrofitdemo.retrofit.ann.GET
import cn.leizy.retrofitdemo.retrofit.ann.POST
import cn.leizy.retrofitdemo.retrofit.ann.Query
import okhttp3.Call

/**
 * @author Created by wulei
 * @date 2021/1/5, 005
 * @description
 */
interface Api {
    @POST("/v3/weather/weatherInfo")
    fun postWeather(@Field("city") city: String?, @Field("key") key: String?): Call


    @GET("/v3/weather/weatherInfo")
    fun getWeather(@Query("city") city: String?, @Query("key") key: String?): Call
}
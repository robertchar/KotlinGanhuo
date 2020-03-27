package com.ganhuo.app.retrofit

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *    author : zkk
 *    date   : 2020-03-24 11:46
 *    desc   :retrofit接口类api
 */
interface RetrofitService {
    /**
     * 首页banner轮播
     * https://gank.io/api/v2/banners
     */
    @GET("banners")
    fun getBanner(): Deferred<String>

    /**
     * 分类 API
     * https://gank.io/api/v2/categories/<category_type> 请求方式: GET
     * category_type 可接受参数 Article | GanHuo | Girl
     */
    @GET("categories/{category_type}")
    fun getCategories(@Path("category_type") type: String): Deferred<String>

    /**
     * 分类数据 API
     * https://gank.io/api/v2/data/category/<category>/type/<type>/page/<page>/count/<count>
     *     请求方式: GET
    注：
    category 可接受参数 All(所有分类) | Article | GanHuo | Girl
    type 可接受参数 All(全部类型) | Android | iOS | Flutter | Girl ...，即分类API返回的类型数据
    count： [10, 50]
    page: >=1
     *https://gank.io/api/v2/data/category/GanHuo/type/Android/page/1/count/10
     */
    @GET("data/category/{category}/type/{type}/page/{page}/count/{count}")
    fun getgetCategoriesData(
        @Path("category") category: String,
        @Path("type") type: String,
        @Path("page") page: Int,
        @Path("count") count: Int
    ): Deferred<String>

    /**
     * 本周最热 API
        https://gank.io/api/v2/hot/<hot_type>/category/<category>/count/<count>
        请求方式: GET
        注：
        hot_type 可接受参数 views（浏览数） | likes（点赞数） | comments（评论数）❌
        category 可接受参数 Article | GanHuo | Girl
        count： [1, 20]
     */
    @GET("hot/{hot_type}/category/{category}/count/{count}")
    fun getHotData(
        @Path("hot_type") hot_type: String,
        @Path("category") category: String,
        @Path("count") count: Int
    ): Deferred<String>

}
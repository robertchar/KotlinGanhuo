package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-08 10:09
 *    desc   :
 */
data class NewsListBean(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
) {

    data class Data(
        val digest: String,
        val imgList: List<String>,
        val newsId: String,
        val postTime: String,
        val source: String,
        val title: String,
        val videoList: Any
    )
}
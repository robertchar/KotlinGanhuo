package com.ganhuo.app.bean

import java.io.Serializable

/**
 *    author : zkk
 *    date   : 2020-03-25 16:52
 *    desc   :
 */
data class HotDataBean(
    val category: String,
    val `data`: List<Data>,
    val hot: String,
    val status: Int
) : Serializable {

    data class Data(
        val _id: String,
        val author: String,
        val category: String,
        val content: String,
        val createdAt: String,
        val desc: String,
        val images: List<String>,
        val likeCounts: Int,
        val publishedAt: String,
        val stars: Int,
        val title: String,
        val type: String,
        val url: String,
        val views: Int
    ) : Serializable
}
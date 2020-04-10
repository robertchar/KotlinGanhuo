package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-08 11:04
 *    desc   :
 */
data class NewsDetailsBean(
    val code: Int,
    val `data`: Data,
    val msg: String
) {

    data class Data(
        val content: String,
        val cover: String,
        val docid: String,
        val images: List<Image>,
        val ptime: String,
        val source: String,
        val title: String
    ) {

        data class Image(
            val imgSrc: String,
            val position: String,
            val size: String
        )
    }
}
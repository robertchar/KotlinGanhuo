package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-03-24 14:03
 *    desc   :
 */
data class BannerBean(
    val `data`: List<Data>,
    val status: Int
) {

    data class Data(
        val image: String,
        val title: String,
        val url: String
    )
}
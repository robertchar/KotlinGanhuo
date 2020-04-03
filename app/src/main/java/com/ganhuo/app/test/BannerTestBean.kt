package com.ganhuo.app.test

/**
 *    author : zkk
 *    date   : 2020-03-30 10:22
 *    desc   :
 */
data class BannerTestBean(
    val `data`: List<Data>,
    val errorCode: Int,
    val errorMsg: String
) {

    data class Data(
        val desc: String,
        val id: Int,
        val imagePath: String,
        val isVisible: Int,
        val order: Int,
        val title: String,
        val type: Int,
        val url: String
    )
}
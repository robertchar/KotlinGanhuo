package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-03-31 15:44
 *    desc   :
 */
data class BannerWanBean(
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
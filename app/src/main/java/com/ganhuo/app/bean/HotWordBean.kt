package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-03 12:57
 *    desc   :热词
 */
data class HotWordBean(
    val `data`: List<Data>,
    val errorCode: Int,
    val errorMsg: String
) {

    data class Data(
        val id: Int,
        val link: String,
        val name: String,
        val order: Int,
        val visible: Int
    )
}
package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-03 13:25
 *    desc   :
 */
data class NetBean(
    val `data`: List<Data>,
    val errorCode: Int,
    val errorMsg: String
) {

    data class Data(
        val icon: String,
        val id: Int,
        val link: String,
        val name: String,
        val order: Int,
        val visible: Int
    )
}
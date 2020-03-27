package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-03-24 14:03
 *    desc   :
 */
data class HotTagBean(
    val data: MutableList<Data>,
    val title: String,
    val type: String
) {
    data class Data(
        val title: String,
        val category: String
    )
}
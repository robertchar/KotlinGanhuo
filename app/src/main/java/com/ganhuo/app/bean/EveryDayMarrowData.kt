package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-03-31 13:44
 *    desc   :
 */
data class EveryDayMarrowData(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
) {

    data class Data(
        val author: String,
        val content: String
    )
}
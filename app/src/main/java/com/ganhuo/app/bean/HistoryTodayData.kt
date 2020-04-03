package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-03-31 13:17
 *    desc   :
 */
data class HistoryTodayData(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
) {

    data class Data(
        val day: Int,
        val details: String,
        val month: Int,
        val picUrl: String,
        val title: String,
        val year: String
    )
}
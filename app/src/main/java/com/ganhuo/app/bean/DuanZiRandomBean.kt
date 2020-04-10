package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-10 12:00
 *    desc   :
 */
data class DuanZiRandomBean(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
) {

    data class Data(
        val content: String,
        val updateTime: String
    )
}
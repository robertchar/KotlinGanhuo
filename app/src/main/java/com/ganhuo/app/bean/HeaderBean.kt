package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-03-31 9:31
 *    desc   :
 */
data class HeaderBean(
    val code: Int,
    val msg: String,
    val url: Url
) {

    data class Url(
        val ali: String
    )
}
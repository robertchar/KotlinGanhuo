package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-07 17:53
 *    desc   :
 */
data class NewsTitleBean(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
) {

    data class Data(
        val typeId: Int,
        val typeName: String
    )
}
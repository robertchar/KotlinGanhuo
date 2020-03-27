package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-03-24 16:03
 *    desc   :
 */
data class ChildMagicDataBean(
    val `data`: List<Data>,
    val status: Int
) {

    data class Data(
        val _id: String,
        val coverImageUrl: String,
        val desc: String,
        val title: String,
        val type: String
    )
}
package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-03-26 11:14
 *    desc   :
 */
data class MarrowDataBean(
    val author: Author,
    val code: Int,
    val `data`: List<Data>,
    val msg: String
) {

    data class Author(
        val desc: String,
        val name: String
    )

    data class Data(
        val author: String,
        val cate: String,
        val content: String,
        val created_at: String,
        val title: String
    )
}
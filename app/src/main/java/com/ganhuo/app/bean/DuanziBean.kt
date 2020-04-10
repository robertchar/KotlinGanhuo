package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-10 11:47
 *    desc   :
 */
data class DuanziBean(
    val code: Int,
    val `data`: Data,
    val msg: String
) {

    data class Data(
        val limit: Int,
        val list: List<X>,
        val page: Int,
        val totalCount: Int,
        val totalPage: Int
    ) {

        data class X(
            val content: String,
            val updateTime: String
        )
    }
}
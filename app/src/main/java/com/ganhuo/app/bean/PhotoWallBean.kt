package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-10 14:42
 *    desc   :
 */
data class PhotoWallBean(
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
            val imageFileLength: Int,
            val imageSize: String,
            val imageUrl: String
        )
    }
}
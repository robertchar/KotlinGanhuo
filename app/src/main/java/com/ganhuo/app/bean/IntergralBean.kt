package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-02 17:16
 *    desc   :
 */
data class IntergralBean(
    val `data`: Data,
    val errorCode: Int,
    val errorMsg: String
) {

    data class Data(
        val curPage: Int,
        val datas: List<DataX>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
    ) {

        data class DataX(
            val coinCount: Int,
            val level: Int,
            val rank: Int,
            val userId: Int,
            val username: String
        )
    }
}
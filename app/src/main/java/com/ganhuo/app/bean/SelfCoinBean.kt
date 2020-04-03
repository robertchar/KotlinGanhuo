package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-02 17:45
 *    desc   :
 */
data class SelfCoinBean(
    val `data`: Data,
    val errorCode: Int,
    val errorMsg: String
) {

    data class Data(
        val coinCount: Int,
        val level: Int,
        val rank: Int,
        val userId: Int,
        val username: String
    )
}
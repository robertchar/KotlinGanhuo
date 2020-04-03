package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-02 15:49
 *    desc   :
 */
data class LoginBean(
    val `data`: Data,
    val errorCode: Int,
    val errorMsg: String
)

data class Data(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)
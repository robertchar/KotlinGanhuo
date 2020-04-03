package com.ganhuo.app.bean

import java.io.Serializable

/**
 *    author : zkk
 *    date   : 2020-04-02 10:17
 *    desc   :
 */
data class SystemTiteData(
    val `data`: List<Data>,
    val errorCode: Int,
    val errorMsg: String
)  {

    data class Data(
        val children: List<Children>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int
    ) : Serializable {
        data class Children(
            val children: List<Any>,
            val courseId: Int,
            val id: Int,
            val name: String,
            val order: Int,
            val parentChapterId: Int,
            val userControlSetTop: Boolean,
            val visible: Int
        ):Serializable
    }
}


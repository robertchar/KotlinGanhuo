package com.ganhuo.app.bean

/**
 *    author : zkk
 *    date   : 2020-04-03 13:09
 *    desc   :
 */
data class NavigationBean(
    val `data`: List<Data>,
    val errorCode: Int,
    val errorMsg: String
) {

    data class Data(
        val articles: List<Article>,
        val cid: Int,
        val name: String
    ) {

        data class Article(
            val apkLink: String,
            val audit: Int,
            val author: String,
            val canEdit: Boolean,
            val chapterId: Int,
            val chapterName: String,
            val collect: Boolean,
            val courseId: Int,
            val desc: String,
            val descMd: String,
            val envelopePic: String,
            val fresh: Boolean,
            val id: Int,
            val link: String,
            val niceDate: String,
            val niceShareDate: String,
            val origin: String,
            val prefix: String,
            val projectLink: String,
            val publishTime: Long,
            val selfVisible: Int,
            val shareDate: Long,
            val shareUser: String,
            val superChapterId: Int,
            val superChapterName: String,
            val tags: List<Any>,
            val title: String,
            val type: Int,
            val userId: Int,
            val visible: Int,
            val zan: Int
        )
    }
}
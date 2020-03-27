package com.ganhuo.app.ext

import android.util.Log
import android.view.View
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred

/**
 *    author : zkk
 *    date   : 2020-03-05 16:40
 *    desc   :
 */
fun loge(tag: String, content: String?) {
    Log.e(tag, content ?: tag)
}

/**
 * save cookie string
 */
fun encodeCookie(cookies: List<String>): String {
    val sb = StringBuilder()
    val set = HashSet<String>()
    cookies
        .map { cookie ->
            cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }
        .forEach {
            it.filterNot { set.contains(it) }.forEach { set.add(it) }
        }

    val ite = set.iterator()
    while (ite.hasNext()) {
        val cookie = ite.next()
        sb.append(cookie).append(";")
    }

    val last = sb.lastIndexOf(";")
    if (sb.length - 1 == last) {
        sb.deleteCharAt(last)
    }

    return sb.toString()
}

fun Deferred<Any>?.cancelByActive() = this?.run {
    try {
        if (isActive) {
            cancel()
        }
    } catch (e: Exception) {

    }
}

//扩展函数
fun View.slideExit() {
    if (translationY == 0f) {
        animate().translationY(-height.toFloat())
    }
}

fun View.slideEnter() {
    if (translationY < 0f) animate().translationY(0f)
}


/**
 * get random color
 * @return 16777215 is FFFFFF, 0 is 000000
 */
fun getRandomColor(): String = "#${Integer.toHexString((Math.random() * 16777215).toInt())}"

/**
 * In disappear assist cheng (cancel) will be submitted to the Job Cancellation Exception Exception.
 */
inline fun tryCatch(catchBlock: (Throwable) -> Unit = {}, tryBlock: () -> Unit) {
    try {
        tryBlock()
    } catch (_: CancellationException) {

    } catch (t: Throwable) {
        catchBlock(t)
    }
}
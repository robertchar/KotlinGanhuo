package com.ganhuo.app.utils

import android.util.Log
import com.ganhuo.app.BuildConfig

/**
 *    author : zkk
 *    date   : 2020-03-06 10:56
 *    desc   :工具类
 */
object LogUtils {
    private val isDebug: Boolean = BuildConfig.LOG_DEBUG
    private val TAG: String = "日常打log"

    /**
     *包装log.d日志
     */
    fun d(msg: String) {
        if (isDebug) {
            Log.d(TAG, msg)
        }
    }

    /**
     *包装log.e日志
     */
    fun e(msg: String) {
        if (isDebug) {
            Log.e(TAG, msg)
        }
    }

    /**
     * v类型的log.v日志
     */
    fun v(msg: String) {
        if (isDebug) {
            Log.v(TAG, msg)
        }
    }
}
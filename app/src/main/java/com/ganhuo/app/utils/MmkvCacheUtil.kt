package com.ganhuo.app.utils

import com.tencent.mmkv.MMKV

/**
 *    author : zkk
 *    date   : 2020-03-18 17:47
 *    desc   :MMKV工具
 */
object MmkvCacheUtil {

    /**
     * 获取缓存数据
     */
    fun getMarrowHistoryData(): String {
        val kv = MMKV.mmkvWithID("cache")
        return kv.decodeString("marrow")
    }

    fun setMarrowHistoryData(searchResponseStr: String) {
        val kv = MMKV.mmkvWithID("cache")
        kv.encode("marrow", searchResponseStr)
    }

}
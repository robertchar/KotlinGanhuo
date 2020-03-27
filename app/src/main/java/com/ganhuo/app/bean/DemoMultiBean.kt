package com.ganhuo.app.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 *    author : zkk
 *    date   : 2020-03-25 12:58
 *    desc   :
 */
data class DemoMultiBean(
    val name: String, override val itemType: Int
) : MultiItemEntity
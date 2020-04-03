package com.ganhuo.app.help

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager

/**
 *    author : zkk
 *    date   : 2020-03-31 14:09
 *    desc   :RecyclerView$LayoutParams cannot be cast to com.google.android.flexbox.FlexItem
 */
class MyFlexboxLayoutManager : FlexboxLayoutManager {
    constructor(context: Context) : super(context)

    constructor(context: Context, flexDirection: Int) : super(context, flexDirection)

    constructor(context: Context, flexDirection: Int, flexWrap: Int) : super(
        context,
        flexDirection,
        flexWrap
    )


    /**
     * 将LayoutParams转换成新的FlexboxLayoutManager.LayoutParams
     */
    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): RecyclerView.LayoutParams {
        return when (lp) {
            //TODO:可能需要适配，特殊处理"+"的宽度
            is RecyclerView.LayoutParams -> LayoutParams(lp)
            is ViewGroup.MarginLayoutParams -> LayoutParams(lp)
            else -> LayoutParams(lp)
        }
    }
}
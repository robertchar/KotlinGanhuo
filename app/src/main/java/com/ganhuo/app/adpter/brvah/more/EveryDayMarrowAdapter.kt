package com.ganhuo.app.adpter.brvah.more

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.EveryDayMarrowData
import com.ganhuo.app.bean.HotTagBean
import com.google.android.flexbox.AlignSelf
import com.google.android.flexbox.FlexboxLayoutManager


class EveryDayMarrowAdapter(data: MutableList<EveryDayMarrowData.Data>?) :
    BaseQuickAdapter<EveryDayMarrowData.Data, BaseViewHolder>(R.layout.item_hot_two, data) {
    @SuppressLint("WrongConstant")
    override fun convert(helper: BaseViewHolder, item: EveryDayMarrowData.Data) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.tv_option, item.content)
        val textView = helper.getView<TextView>(R.id.tv_option)
        val lp: ViewGroup.LayoutParams = textView.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            val fireboxLp = lp as FlexboxLayoutManager.LayoutParams
            fireboxLp.flexGrow = 1.0f
            fireboxLp.alignSelf=AlignSelf.FLEX_END
        }
    }
}
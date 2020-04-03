package com.ganhuo.app.adpter.brvah.more

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.SquareData

class WanSquareAdapter(data: MutableList<SquareData.Data.DataX>?) :
    BaseQuickAdapter<SquareData.Data.DataX, BaseViewHolder>(
        R.layout.item_wan_article,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: SquareData.Data.DataX) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.homeItemTitle, item.title)
            .setText(R.id.homeItemAuthor, item.author)
            .setText(R.id.homeItemType, item.chapterName)
            .setTextColor(R.id.homeItemType, context.resources.getColor(R.color.colorPrimary))
            .setText(R.id.homeItemDate, item.niceDate)
    }
}
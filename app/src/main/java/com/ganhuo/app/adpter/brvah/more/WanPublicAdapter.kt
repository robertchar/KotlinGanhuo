package com.ganhuo.app.adpter.brvah.more

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.PublicHistoryData
import com.ganhuo.app.bean.WanArticleDataBean

class WanPublicAdapter(data: MutableList<PublicHistoryData.Data.DataX>?) :
    BaseQuickAdapter<PublicHistoryData.Data.DataX, BaseViewHolder>(
        R.layout.item_wan_article,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: PublicHistoryData.Data.DataX) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.homeItemTitle, item.title)
            .setText(R.id.homeItemAuthor, item.author)
            .setText(R.id.homeItemType, item.chapterName)
            .setTextColor(R.id.homeItemType, context.resources.getColor(R.color.colorPrimary))
            .setText(R.id.homeItemDate, item.niceDate)
    }
}
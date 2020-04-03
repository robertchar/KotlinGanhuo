package com.ganhuo.app.adpter.brvah.more

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.TopWanDdata
import com.ganhuo.app.bean.WanArticleDataBean
import com.ganhuo.app.bean.WanProjectBean

class WanTopAdapter(data: MutableList<TopWanDdata.Data>?) :
    BaseQuickAdapter<TopWanDdata.Data, BaseViewHolder>(
        R.layout.item_wan_article,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: TopWanDdata.Data) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.homeItemTitle, item.title)
            .setText(R.id.homeItemAuthor, item.author)
            .setText(R.id.homeItemType, item.chapterName)
            .setTextColor(R.id.homeItemType, context.resources.getColor(R.color.colorPrimary))
            .setText(R.id.homeItemDate, item.niceDate)
    }
}
package com.ganhuo.app.adpter.brvah.more

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.KnowSystemData

class TypeArticleAdapter(datas: MutableList<KnowSystemData.Data.DataX>?) :
    BaseQuickAdapter<KnowSystemData.Data.DataX, BaseViewHolder>(R.layout.home_list_item, datas) {
    override fun convert(helper: BaseViewHolder, item: KnowSystemData.Data.DataX) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.homeItemTitle, item.title)
            .setText(R.id.homeItemAuthor, item.author)
            .setVisible(R.id.homeItemType, false)
            .setText(R.id.homeItemDate, item.niceDate)
            .setImageResource(
                R.id.homeItemLike,
                if (item.collect) R.drawable.ic_action_like else R.drawable.ic_action_no_like
            )
    }

}
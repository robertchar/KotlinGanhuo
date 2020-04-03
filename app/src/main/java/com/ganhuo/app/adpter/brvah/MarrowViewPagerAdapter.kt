package com.ganhuo.app.adpter.brvah

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.MarrowDataBean

class MarrowViewPagerAdapter(data: MutableList<MarrowDataBean.Data>?) :
    BaseQuickAdapter<MarrowDataBean.Data, BaseViewHolder>(R.layout.item_marrow_viewpager_list, data) {
    override fun convert(helper: BaseViewHolder, item: MarrowDataBean.Data) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.title, item.title)
            .setText(R.id.author, "作者："+item.author)
            .setText(R.id.cate, "文章类型：" + if (item.cate.equals("everyday")){"每日一文"}else if (item.cate.equals("yilin")){"意林杂志网"} else "随机" )
            .setText(R.id.created_at, item.created_at)
    }
}
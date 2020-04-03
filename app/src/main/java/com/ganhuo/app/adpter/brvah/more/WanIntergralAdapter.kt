package com.ganhuo.app.adpter.brvah.more

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.IntergralBean

class WanIntergralAdapter(data: MutableList<IntergralBean.Data.DataX>?) :
    BaseQuickAdapter<IntergralBean.Data.DataX, BaseViewHolder>(
        R.layout.item_wan_intergral,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: IntergralBean.Data.DataX) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.name, item.username)
            .setText(R.id.coin, item.coinCount.toString())
    }
}
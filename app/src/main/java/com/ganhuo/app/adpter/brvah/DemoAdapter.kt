package com.ganhuo.app.adpter.brvah

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R

class DemoAdapter(data: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_home_list, data) {
    override fun convert(helper: BaseViewHolder, item: String) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.tv_desc, item)
    }
}
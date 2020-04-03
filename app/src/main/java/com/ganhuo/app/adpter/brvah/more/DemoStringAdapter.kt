package com.ganhuo.app.adpter.brvah.more

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R

class DemoStringAdapter(data: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_string_list, data) {
    override fun convert(helper: BaseViewHolder, item: String) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.tv_desc, item)
    }
}
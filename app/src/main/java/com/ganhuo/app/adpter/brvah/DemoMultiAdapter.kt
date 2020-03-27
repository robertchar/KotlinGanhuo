package com.ganhuo.app.adpter.brvah

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.DemoMultiBean

/**
 *    author : zkk
 *    date   : 2020-03-25 12:38
 *    desc   :
 */
class DemoMultiAdapter : BaseMultiItemQuickAdapter<DemoMultiBean, BaseViewHolder>() {

    override fun convert(helper: BaseViewHolder, item: DemoMultiBean) {
        addItemType(0, R.layout.item_hot_fragent)
        addItemType(1, R.layout.header_view)
        val itemViewType = helper.itemViewType
        if (itemViewType == 0) {
item
        } else {

        }
    }
}
package com.ganhuo.app.adpter.brvah.more

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.SortTitleData

/**
 *    author : zkk
 *    date   : 2020-03-09 14:15
 *    desc   :
 */
class SortTypeAdapter(datas: MutableList<SortTitleData.Data>?) :
    BaseQuickAdapter<SortTitleData.Data, BaseViewHolder>(R.layout.type_sort_item, datas) {
    override fun convert(helper: BaseViewHolder, item: SortTitleData.Data) {
        item ?: return
        helper.setText(R.id.typeItemFirst, item.name)
    }

}
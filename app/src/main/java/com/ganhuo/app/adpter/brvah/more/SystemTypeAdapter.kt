package com.ganhuo.app.adpter.brvah.more

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.SystemTiteData

/**
 *    author : zkk
 *    date   : 2020-03-09 14:15
 *    desc   :
 */
class SystemTypeAdapter(datas: MutableList<SystemTiteData.Data>?) :
    BaseQuickAdapter<SystemTiteData.Data, BaseViewHolder>(R.layout.type_list_item, datas) {
    override fun convert(helper: BaseViewHolder, item: SystemTiteData.Data) {
        item ?: return
        helper.setText(R.id.typeItemFirst, item.name)
        item.children?.let { children ->
            helper.setText(
                R.id.typeItemSecond,
                children.joinToString("     ", transform = { child ->
                    child.name
                })

            )
        }
    }

}
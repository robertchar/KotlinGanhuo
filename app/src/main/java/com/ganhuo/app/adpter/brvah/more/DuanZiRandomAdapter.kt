package com.ganhuo.app.adpter.brvah.more

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.DuanZiRandomBean
import com.ganhuo.app.bean.DuanziBean

class DuanZiRandomAdapter(data: MutableList<DuanZiRandomBean.Data>?) :
    BaseQuickAdapter<DuanZiRandomBean.Data, BaseViewHolder>(R.layout.item_category_list, data) {
    override fun convert(helper: BaseViewHolder, item: DuanZiRandomBean.Data) {
        @Suppress("DEPRECATION")
        helper.setText(
            R.id.tv_title,
            item.content + System.lineSeparator() + "时间：${item.updateTime}"
        )
        val imageView = helper.getView<ImageView>(R.id.image)
        imageView.visibility=View.GONE
    }
}
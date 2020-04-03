package com.ganhuo.app.adpter.brvah.more

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.CategoryDataBean
import com.ganhuo.app.bean.HistoryTodayData

class HiatoryTodayAdapter(data: MutableList<HistoryTodayData.Data>?) :
    BaseQuickAdapter<HistoryTodayData.Data, BaseViewHolder>(R.layout.item_category_list, data) {
    override fun convert(helper: BaseViewHolder, item: HistoryTodayData.Data) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.tv_title, item.title + System.lineSeparator()+"时间：${item.year}年${item.month}月${item.day}")
        val imageView = helper.getView<ImageView>(R.id.image)
        val picUrl = item.picUrl
        if (picUrl.isNotEmpty())
            Glide.with(context).load(picUrl).placeholder(R.drawable.image_loading)
                .error(R.drawable.image_failed).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).into(imageView)
    }
}
package com.ganhuo.app.adpter.brvah

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.HotDataBean

class HotDataAdapter(data: MutableList<HotDataBean.Data>?) :
    BaseQuickAdapter<HotDataBean.Data, BaseViewHolder>(R.layout.item_hot_activity, data) {
    override fun convert(helper: BaseViewHolder, item: HotDataBean.Data) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.title, item.desc)
        val imageView = helper.getView<ImageView>(R.id.image)
        if (item.images.isNotEmpty())
            Glide.with(context).load(item.images[0]).placeholder(R.drawable.image_loading)
                .error(R.drawable.image_failed).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(imageView)
    }
}
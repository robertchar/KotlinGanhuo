package com.ganhuo.app.adpter.brvah

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.CategoryDataBean

class CategoryDataAdapter(data: MutableList<CategoryDataBean.Data>?) :
    BaseQuickAdapter<CategoryDataBean.Data, BaseViewHolder>(R.layout.item_category_list, data) {
    override fun convert(helper: BaseViewHolder, item: CategoryDataBean.Data) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.tv_title, item.desc)
        val imageView = helper.getView<ImageView>(R.id.image)
        val images = item.images
        if (images.isNotEmpty())
            Glide.with(context).load(images[0]).placeholder(R.drawable.image_loading)
                .error(R.drawable.image_failed).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(imageView)
    }
}
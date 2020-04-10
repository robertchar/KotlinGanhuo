package com.ganhuo.app.adpter.brvah.more

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.PhotoWallBean
import com.google.android.flexbox.AlignSelf
import com.google.android.flexbox.FlexboxLayoutManager

class PhotoWallAdapter(data: MutableList<PhotoWallBean.Data.X>?) :
    BaseQuickAdapter<PhotoWallBean.Data.X, BaseViewHolder>(R.layout.item_image, data) {
    override fun convert(helper: BaseViewHolder, item: PhotoWallBean.Data.X) {
        @Suppress("DEPRECATION")
        val imageView = helper.getView<ImageView>(R.id.image)
        Glide.with(context).load(item.imageUrl)
            .placeholder(R.drawable.image_loading)
            .dontAnimate()
            .error(R.drawable.image_failed).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(imageView)
        val layoutParams = imageView.layoutParams
        if (layoutParams is FlexboxLayoutManager.LayoutParams) {
            val fireboxLp = layoutParams as FlexboxLayoutManager.LayoutParams
            fireboxLp.flexGrow = 1.0f
        }
    }
}
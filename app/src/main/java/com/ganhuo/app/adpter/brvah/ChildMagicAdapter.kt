package com.ganhuo.app.adpter.brvah

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.ChildMagicDataBean

class ChildMagicAdapter(data: MutableList<ChildMagicDataBean.Data>?) :
    BaseQuickAdapter<ChildMagicDataBean.Data, BaseViewHolder>(R.layout.item_home_list, data) {
    override fun convert(helper: BaseViewHolder, item: ChildMagicDataBean.Data) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.tv_desc, item.desc)
            .setText(R.id.tv_title, item.title)
        val imageView = helper.getView(R.id.image) as ImageView
        Glide.with(context).load(item.coverImageUrl).placeholder(R.drawable.image_loading)
            .error(R.drawable.image_failed).diskCacheStrategy(
            DiskCacheStrategy.ALL
        ).into(imageView)
    }
}
package com.ganhuo.app.adpter.brvah.more

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.NewsListBean
import com.ganhuo.app.utils.isDestroy

class NewsListAdapter(data: MutableList<NewsListBean.Data>?) :
    BaseQuickAdapter<NewsListBean.Data, BaseViewHolder>(R.layout.item_home_list, data) {
    override fun convert(helper: BaseViewHolder, item: NewsListBean.Data) {
        @Suppress("DEPRECATION")
        helper.setText(R.id.tv_desc, item.title)
            .setText(R.id.tv_title, item.postTime)
        val imageView = helper.getView<ImageView>(R.id.image)
        val url = item.imgList
        if (url != null && url.isNotEmpty())
            Glide.with(context).load(url[0]).placeholder(R.drawable.image_loading)
                .error(R.drawable.image_failed).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).into(imageView)
    }
}
package com.ganhuo.app.adpter.brvah

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ganhuo.app.R
import com.ganhuo.app.bean.BannerBean
import com.ganhuo.app.bean.NewsDetailsBean
import com.ganhuo.app.utils.isDestroy
import com.youth.banner.adapter.BannerAdapter
import org.jetbrains.anko.find

/**
 *    author : zkk
 *    date   : 2020-03-24 14:07
 *    desc   :banner自定义布局,图片+标题
 */
class ImageNewsDetailAdapter(datas: MutableList<NewsDetailsBean.Data.Image>?) :
    BannerAdapter<NewsDetailsBean.Data.Image, ImageNewsDetailAdapter.BannerViewHolder>(datas) {
    override fun onCreateHolder(
        parent: ViewGroup?,
        viewType: Int
    ): ImageNewsDetailAdapter.BannerViewHolder {
        val view =
            LayoutInflater.from(parent?.context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: ImageNewsDetailAdapter.BannerViewHolder?,
        data: NewsDetailsBean.Data.Image?,
        position: Int,
        size: Int
    ) {
        holder?.view?.context?.let {
            if (!isDestroy(it as AppCompatActivity)) {
                Glide.with(it).load(data?.imgSrc)
                    .placeholder(R.drawable.image_loading).error(R.drawable.image_failed)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv)
            }
        }
        holder?.tv?.text = data?.position
    }

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: View = itemView
        val iv: ImageView = itemView.find(R.id.iv) as ImageView
        val tv: TextView

        init {
            tv = itemView.find(R.id.tv) as TextView
        }
    }
}
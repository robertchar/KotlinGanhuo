package com.ganhuo.app.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ganhuo.app.R
import com.ganhuo.app.bean.BannerBean
import com.youth.banner.adapter.BannerAdapter
import org.jetbrains.anko.find

/**
 *    author : zkk
 *    date   : 2020-03-24 14:07
 *    desc   :banner自定义布局
 */
class ImageAdapter(datas: MutableList<BannerBean.Data>?) :
    BannerAdapter<BannerBean.Data, ImageAdapter.BannerViewHolder>(datas) {
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageAdapter.BannerViewHolder {
        val view =
            LayoutInflater.from(parent?.context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: ImageAdapter.BannerViewHolder?,
        data: BannerBean.Data?,
        position: Int,
        size: Int
    ) {
        holder?.view?.context?.let { Glide.with(it).load(data?.image)
            .placeholder(R.drawable.image_loading).error(R.drawable.image_failed)
            .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv) }
        holder?.tv?.text = data?.title
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
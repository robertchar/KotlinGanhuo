package com.ganhuo.app.adpter.brvah.more

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
import com.ganhuo.app.bean.BannerWanBean
import com.ganhuo.app.utils.isDestroy
import com.youth.banner.adapter.BannerAdapter
import org.jetbrains.anko.find

/**
 *    author : zkk
 *    date   : 2020-03-31 15:42
 *    desc   :
 */
class ImageTitleAdapter(datas: MutableList<BannerWanBean.Data>?) :
    BannerAdapter<BannerWanBean.Data, ImageTitleAdapter.ImageHolder>(
        datas
    ) {
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val view =
            LayoutInflater.from(parent?.context).inflate(R.layout.item_banner, parent, false)
        return ImageHolder(view)
    }

    override fun onBindView(
        holder: ImageHolder?,
        data: BannerWanBean.Data?,
        position: Int,
        size: Int
    ) {
        holder?.view?.context?.let {
            if (!isDestroy(it as AppCompatActivity))
                Glide.with(it).load(data?.imagePath)
                    .placeholder(R.drawable.image_loading).error(R.drawable.image_failed)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv)
        }
        holder?.tv?.text = data?.title
    }

    class ImageHolder(item: View) : RecyclerView.ViewHolder(item) {
        val view: View = itemView
        val iv: ImageView = itemView.find(R.id.iv) as ImageView
        val tv: TextView = itemView.find(R.id.tv) as TextView

    }
}
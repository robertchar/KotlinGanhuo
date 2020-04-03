package com.ganhuo.app.help.pictureselector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.ImageViewTarget
import com.ganhuo.app.R
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.tools.MediaUtils
import com.luck.picture.lib.widget.longimage.ImageSource
import com.luck.picture.lib.widget.longimage.ImageViewState
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView


/**
 *    author : zkk
 *    date   : 2020-03-30 16:32
 *    desc   :必须复写传入
 */
class GlideEngine : ImageEngine {
    //    线程安全的懒汉式单例
    companion object {
        private var instance: GlideEngine? = null
            get() {
                if (field == null) {
                    field = GlideEngine()
                }
                return field
            }

        @Synchronized
        fun get(): GlideEngine {
            return instance!!
        }
    }

    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .into(imageView);
    }

    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?,
        callback: OnImageCompleteCallback?
    ) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : ImageViewTarget<Bitmap?>(imageView) {
                override fun onLoadStarted(@Nullable placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    callback?.onShowLoading()
                }

                override fun onLoadFailed(@Nullable errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    callback?.onHideLoading()
                }

                override fun setResource(@Nullable resource: Bitmap?) {
                    callback?.onHideLoading()
                    if (resource != null) {
                        val eqLongImage: Boolean = MediaUtils.isLongImg(
                            resource.width,
                            resource.height
                        )
                        longImageView!!.visibility = if (eqLongImage) View.VISIBLE else View.GONE
                        imageView.visibility = if (eqLongImage) View.GONE else View.VISIBLE
                        if (eqLongImage) {
                            // 加载长图
                            longImageView!!.isQuickScaleEnabled = true
                            longImageView!!.isZoomEnabled = true
                            longImageView!!.isPanEnabled = true
                            longImageView!!.setDoubleTapZoomDuration(100)
                            longImageView!!.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                            longImageView!!.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
                            longImageView!!.setImage(
                                ImageSource.bitmap(resource),
                                ImageViewState(0F, PointF(0F, 0F), 0)
                            )
                        } else {
                            // 普通图片
                            imageView.setImageBitmap(resource)
                        }
                    }
                }
            })
    }

    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?
    ) {
    }

    override fun loadAsGifImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .asGif()
            .load(url)
            .into(imageView);
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .override(200, 200)
            .centerCrop()
            .apply(RequestOptions().placeholder(R.drawable.picture_image_placeholder))
            .into(imageView)
    }

    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .override(180, 180)
            .centerCrop()
            .sizeMultiplier(0.5f)
            .apply(RequestOptions().placeholder(R.drawable.picture_image_placeholder))
            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.resources, resource)
                    circularBitmapDrawable.cornerRadius = 8f
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
    }
}
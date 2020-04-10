package com.ganhuo.app.adpter.brvah.more

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ganhuo.app.R
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.utils.isDestroy
import kotlinx.android.synthetic.main.activity_photoview.*

/**
 *    author : zkk
 *    date   : 2020-04-10 15:20
 *    desc   :图片放大缩小
 */
class PhotoViewActivity : BaseActivity() {
    private var url: String? = null

    override fun setLayoutId(): Int {
        return R.layout.activity_photoview
    }

    override fun initData() {
        super.initData()
        intent.extras?.run {
            url = getString(Constant.PHOTO_VIEW)
        }
        photoView.run {
            setOnPhotoTapListener { view, x, y ->
                //点击事件
                finish()
            }
        }
        if (!isDestroy(this)) {
            Glide.with(this).load(url)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_failed).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).into(photoView)
        }
    }
}
package com.ganhuo.app.ui.activity.more

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.DemoStringAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.base.Preference
import com.ganhuo.app.bean.HeaderBean
import com.ganhuo.app.help.pictureselector.GlideEngine
import com.ganhuo.app.utils.LogUtils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_more.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File


/**
 *    author : zkk
 *    date   : 2020-03-30 14:42
 *    desc   :更多
 */
class MoreActivity : BaseActivity() {
    private var urlString: String by Preference("head", "")
    private val adapterString: DemoStringAdapter by lazy { DemoStringAdapter(listData) }
    private var listData = mutableListOf("历史上的今天", "每日精美语句", "玩Android客户端")

    override fun setLayoutId(): Int {
        return R.layout.activity_more
    }

    override fun initData() {
        super.initData()
        btn_upload.run {
            setOnClickListener { upLoadHead() }
        }
        adapterString.run {
            setOnItemClickListener(adapterListener)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@MoreActivity)
            adapter = adapterString
        }
        //保存的头像设置
        setSaveHead()
    }

    //点击事件
    private val adapterListener: OnItemClickListener =
        OnItemClickListener { adapter, view, position ->
            when (position) {
                0 -> {
                    //历史上的今天
                    startActivity<HistoryTodayActivity>()
                }
                1 -> {
                    //每日精美语句
                    startActivity<EveryDayMarrowActivity>()
                }
                2 -> {
                    //wan
                    startActivity<WanAndroidActivity>()
                }
                else -> {
                }
            }
        }

    private fun setSaveHead() {
        if (!TextUtils.isEmpty(urlString)) {
            LogUtils.d("urlString:$urlString")
            Glide.with(this@MoreActivity).load(urlString)
                .placeholder(R.drawable.head_default)
                .error(R.drawable.head_default).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).apply(RequestOptions.bitmapTransform(CircleCrop()))//设置圆图片
                .into(head_more)
            //高斯模糊
            Glide.with(this@MoreActivity).load(urlString)
                .placeholder(R.drawable.image_failed)
                .error(R.drawable.image_failed).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).apply( //高斯模糊
                    RequestOptions.bitmapTransform(
                        BlurTransformation(
                            23,
                            3
                        )
                    )
                )  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(bg_image)
        }
    }

    private fun upLoadHead() {
        //选择头像上传
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
            .previewImage(true)// 是否可预览图片 true or false
            .isCamera(true)// 是否显示拍照按钮 true or false
            .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
            .enableCrop(true)// 是否裁剪 true or false
            .compress(true)// 是否压缩 true or false
            .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
            .loadImageEngine(GlideEngine.get())// 外部传入图片加载引擎，必传项
            .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            // 图片选择结果回调
            val selectList = PictureSelector.obtainMultipleResult(data)
            selectList.forEach {
                if (it.isCut) {
                    val cutPath = it.cutPath
                    LogUtils.d("裁剪后地址：$cutPath")
                    //上传头像
                    updateHeadNet(cutPath)
                }

            }

        }
    }

    //Fuel上传头像
    var urlHead: String = "https://api.abcyun.co/api/picbed/index/type/ali/token/5d8f31cf6a8ab"
    private fun updateHeadNet(cutPath: String?) {
        showLoadingDialog("上传中...")
        doAsync {
            Fuel.upload(urlHead)
                .add(FileDataPart(File(cutPath), name = "image", filename = "messenger_header.png"))
                .responseString { _, _, result ->
                    result.fold({ sucess ->
                        hideLoadingDialog()
                        LogUtils.d("请求1：$sucess")
                        val fromJson = Gson().fromJson<HeaderBean>(
                            sucess,
                            object : TypeToken<HeaderBean>() {}.type
                        )
                        uiThread {
                            val code = fromJson.code
                            val msg = fromJson.msg
                            toast(msg)
                            if (code == 200) {
                                urlString = fromJson?.url?.ali.toString()
                                Glide.with(this@MoreActivity)
                                    .load(urlString)
                                    .placeholder(R.drawable.head_default)
                                    .error(R.drawable.head_default).diskCacheStrategy(
                                        DiskCacheStrategy.ALL
                                    ).apply(RequestOptions.bitmapTransform(CircleCrop()))//设置圆图片
                                    .into(head_more)
                                //高斯模糊
                                Glide.with(this@MoreActivity).load(urlString)
                                    .placeholder(R.drawable.image_failed)
                                    .error(R.drawable.image_failed).diskCacheStrategy(
                                        DiskCacheStrategy.ALL
                                    ).apply( //高斯模糊
                                        RequestOptions.bitmapTransform(
                                            BlurTransformation(
                                                23,
                                                3
                                            )
                                        )
                                    )  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                                    .into(bg_image)
                            }
                        }
                    }, { err ->
                        hideLoadingDialog()
                        LogUtils.d("请求2：" + err.message)
                        uiThread {
                            toast(err.message.toString())
                        }
                    })


                }
        }
    }

}
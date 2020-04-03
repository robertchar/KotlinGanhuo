package com.ganhuo.app.ui.fragment.wan

import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.ganhuo.app.R
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.base.Preference
import com.ganhuo.app.base.boolean
import com.ganhuo.app.base.string
import com.ganhuo.app.ui.activity.more.AnswerActivity
import com.ganhuo.app.ui.activity.more.IntergralActivity
import com.ganhuo.app.ui.activity.more.RegistAndLogInActivity
import com.ganhuo.app.utils.LogUtils
import com.ganhuo.app.widget.LinearItem
import com.github.kittinunf.fuel.Fuel
import com.gyf.immersionbar.ImmersionBar
import com.tencent.mmkv.MMKV
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_more.bg_image
import kotlinx.android.synthetic.main.activity_more.btn_upload
import kotlinx.android.synthetic.main.activity_more.head_more
import kotlinx.android.synthetic.main.fragment_me_wan.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-03-31 14:55
 *    desc   :wan我的
 */
class MeWanFragment : BaseFragment() {
    private var urlString: String by Preference("head", "")
    private val mmkv: MMKV by lazy { MMKV.defaultMMKV() }
    private var isLogin by mmkv.boolean("isLogin", false)
    private var userName by mmkv.string("userName", "")

    override fun getLayout(): Int {
        return R.layout.fragment_me_wan
    }

    companion object {
        fun newInstance(): MeWanFragment {
            return MeWanFragment()
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    override fun initData() {
        super.initData()
        //保存的头像设置
        setSaveHead()
        btn_upload.setOnClickListener {
            if (isLogin) {
                logOut()
            } else {
                btn_upload.text = getString(R.string.string_logout)
                user_name.text = userName
                startActivity<RegistAndLogInActivity>()
            }
        }
        //积分
        item_intergral.setItemClickListener(object : LinearItem.OnLlClick {
            override fun itemClick() {
                if (isLogin) {
                    startActivity<IntergralActivity>()
                } else {
                    startActivity<RegistAndLogInActivity>()
                }
            }

        })
        item_answer.setItemClickListener(object : LinearItem.OnLlClick {
            override fun itemClick() {
                startActivity<AnswerActivity>()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isLogin) {
            btn_upload.text = getString(R.string.string_logout)
            user_name.text = userName
        } else {
            btn_upload.text = getString(R.string.string_login)
            user_name.text = "暂无用户名"
        }
    }

    //退出
    private fun logOut() {
        showLoadingDialog()
        doAsync {
            Fuel.get("https://www.wanandroid.com/user/logout/json")
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        hideLoadingDialog()
                        uiThread {
                            isLogin = false
                            btn_upload.text = getString(R.string.string_login)
                            user_name.text = "暂无用户名"
                            startActivity<RegistAndLogInActivity>()
                        }
                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            toast(err.toString())
                        }
                    })
                }
        }

    }

    private fun setSaveHead() {
        if (!TextUtils.isEmpty(urlString)) {
            LogUtils.d("urlString:$urlString")
            activity?.let {
                Glide.with(it).load(urlString)
                    .placeholder(R.drawable.head_default)
                    .error(R.drawable.head_default).diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    ).apply(RequestOptions.bitmapTransform(CircleCrop()))//设置圆图片
                    .into(head_more)
            }
            //高斯模糊
            activity?.let {
                Glide.with(it).load(urlString)
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
    }
}
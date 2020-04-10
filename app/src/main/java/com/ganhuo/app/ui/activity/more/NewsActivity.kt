package com.ganhuo.app.ui.activity.more

import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ganhuo.app.R
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.NewsTitleBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.utils.LogUtils
import com.ganhuo.app.utils.isDestroy
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-04-07 17:40
 *    desc   :新闻GridPager
 */
class NewsActivity : BaseActivity() {
    private var imageNetList = arrayListOf(
        "https://i.postimg.cc/B66NK7XP/more.png",
        "https://i.postimg.cc/7hJVBC1x/image.png",
        "https://i.postimg.cc/sXNm9Ph6/image.png",
        "https://i.postimg.cc/9z3BsW1y/image.png"
    )

    override fun setLayoutId(): Int {
        return R.layout.activity_news
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        toolbar.run {
            title = "新闻"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        getNewsTitle()
    }

    //    获取所有新闻类型列表
    private fun getNewsTitle() {
        doAsync {
            Fuel.get("https://www.mxnzp.com/news/types")
                .header("app_id" to Constant.APP_ID)
                .header("app_secret" to Constant.APP_KEY)
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        LogUtils.d("sucess:$sucess")
                        val fromJson = Gson().fromJson<NewsTitleBean>(
                            sucess,
                            object : TypeToken<NewsTitleBean>() {}.type
                        )
                        if (fromJson.code == 1) {
                            setGridViewPagerData(fromJson)
                        }else {
                            toast(fromJson.msg)
                        }
                    }, { err ->
                        uiThread {
                            toast(err.toString())
                        }
                    })
                }
        }
    }

    //设置数据
    private fun setGridViewPagerData(data: NewsTitleBean?) {
        val dataList = data?.data
        val size = dataList?.size
        if (size != null) {
            gridviewpager
                // 设置数据总数量
                .setDataAllCount(size)
                // 设置背景图片(此时设置的背景色无效，以背景图片为主)
                .setBackgroundImageLoader {
                    it.setImageResource(R.drawable.style_btn_login)
                }
                // 数据绑定
                .setImageTextLoaderInterface { imageView, textView, position ->
                    // 自己进行数据的绑定，灵活度更高，不受任何限制
                    Glide.with(this).load(imageNetList[position % imageNetList.size])
                        .placeholder(R.drawable.image_loading)
                        .error(R.drawable.image_failed).diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).into(imageView)
                    textView.text = dataList[position].typeName
                }
                // Item点击
                .setGridItemClickListener {
                    val typeId = dataList[it].typeId
                    val typeName = dataList[it].typeName
                    startActivity<NewsListActivity>("typeName" to typeName, "typeId" to typeId)
                }.show()
        }
    }
}
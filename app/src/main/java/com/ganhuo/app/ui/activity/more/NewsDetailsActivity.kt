package com.ganhuo.app.ui.activity.more

import android.os.Build
import android.view.View
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.ImageNewsDetailAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.NewsDetailsBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.utils.LogUtils
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youth.banner.config.BannerConfig
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.util.BannerUtils
import kotlinx.android.synthetic.main.activity_html.webView
import kotlinx.android.synthetic.main.activity_news_details.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-04-07 17:40
 *    desc   :新闻
 */
class NewsDetailsActivity : BaseActivity() {
    private var titleDetail: String? = null
    private var newsId: String? = null
    private val imageAdapter by lazy { ImageNewsDetailAdapter(null) }

    override fun setLayoutId(): Int {
        return R.layout.activity_news_details
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        initWebView()
        intent.extras?.run {
            titleDetail = getString("title")
            newsId = getString("newsId")
        }
        toolbar.run {
            title = titleDetail
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        bannerNews.run {
            adapter = imageAdapter
            indicator = CircleIndicator(this@NewsDetailsActivity)
            setIndicatorSelectedColorRes(R.color.white)
            setIndicatorNormalColorRes(R.color.black)
            setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
            setIndicatorSpace(BannerUtils.dp2px(20F))
            setIndicatorMargins(
                IndicatorConfig.Margins(
                    0, 0,
                    BannerConfig.INDICATOR_MARGIN, BannerUtils.dp2px(12f).toInt()
                )
            )
        }
        getNewsDetails()
    }

    //根据新闻id获取新闻详情
    private fun getNewsDetails() {
        doAsync {
            val parameters = listOf("newsId" to newsId)
            Fuel.get("https://www.mxnzp.com/news/details", parameters)
                .header("app_id" to Constant.APP_ID)
                .header("app_secret" to Constant.APP_KEY)
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        LogUtils.d("sucess:$sucess")
                        val fromJson = Gson().fromJson<NewsDetailsBean>(
                            sucess,
                            object : TypeToken<NewsDetailsBean>() {}.type
                        )
                        uiThread {
                            if (fromJson.code == 1) {
                                initUI(fromJson.data)
                            } else {
                                toast(fromJson.msg)
                            }
                        }
                    }, { err ->
                        uiThread {
                            toast(err.toString())
                        }
                    })
                }
        }
    }

    private fun initUI(data: NewsDetailsBean.Data?) {
        webView.loadDataWithBaseURL(
            null,
            data?.let { getHtmlData(it.content) },
            "text/html",
            "utf-8",
            null
        )
        val images = data?.images
        val size = images?.size
        if (size != null) {
            if (size > 0) {
                bannerNews.visibility = View.VISIBLE
                imageAdapter.setDatas(images)
                imageAdapter.notifyDataSetChanged()
            } else {
                bannerNews.visibility = View.GONE
            }
        } else {
            bannerNews.visibility = View.GONE
        }
    }

    /**
     * 富文本适配
     */
    private fun getHtmlData(bodyHTML: String): String {
        var head =
            "<head>" + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " + "<style>img{max-width: 100%; width:auto; height:auto;}</style>" + "</head>";
        return "<html>$head<body>$bodyHTML</body></html>";
    }

    private fun initWebView() {
        //支持javascript
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");

        webView.settings.javaScriptEnabled = false;
        // 设置可以支持缩放
        webView.settings.setSupportZoom(false);
        //关闭保存密码功能，安全监测要求添加
        webView.settings.setSavePassword(false);
        webView.settings.allowFileAccess = false;
        //设置出现缩放工具
        webView.settings.builtInZoomControls = true;
        //扩大比例的缩放
        webView.settings.useWideViewPort = true;
        //自适应屏幕
        webView.settings.loadWithOverviewMode = true;
        if (Build.VERSION.SDK_INT >= 19)
            webView.settings.layoutAlgorithm =
                android.webkit.WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING;
        else {
            webView.settings.layoutAlgorithm =
                android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
        }
    }

    override fun onStart() {
        super.onStart()
        //开始轮播
        bannerNews.start()
    }

    override fun onStop() {
        super.onStop()
        //结束轮播
        bannerNews.stop()
    }
}
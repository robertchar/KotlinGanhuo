package com.ganhuo.app.ui.activity

import android.os.Build
import androidx.annotation.RequiresApi
import com.ganhuo.app.R
import com.ganhuo.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_html.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 *    author : zkk
 *    date   : 2020-03-25 17:13
 *    desc   :webview加载为html富文本
 */
class TextHtmlActivity : BaseActivity() {
    private var content: String? = null
    private var titleData: String? = null

    override fun setLayoutId(): Int {
        return R.layout.activity_html
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.statusBarDarkFont(true).titleBar(toolbar).init()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initData() {
        super.initData()
        initWebView()
        intent.extras?.run {
            content = this.getString("content")
            titleData = this.getString("title")
        }
        toolbar.run {
            title = titleData
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        webView.run {
            loadDataWithBaseURL(null, content, "text/html", "utf-8", null)
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
}
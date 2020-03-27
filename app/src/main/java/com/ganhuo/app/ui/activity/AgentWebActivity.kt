package com.ganhuo.app.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.LinearLayout
import com.ganhuo.app.R
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.constant.Constant
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import kotlinx.android.synthetic.main.activity_agentweb.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 *    author : zkk
 *    date   : 2020-03-09 10:10
 *    desc   :AgentWeb
 */
class AgentWebActivity : BaseActivity() {
    private var agentWeb: AgentWeb? = null
    private lateinit var shareUrl: String
    override fun setLayoutId(): Int {
        return R.layout.activity_agentweb
    }

    override fun cancelRequest() {

    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolbar).init()
    }

    override fun initData() {
        toolbar.run {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        intent.extras?.let {
            shareUrl = it.getString(Constant.CONTENT_URL_KEY).toString()
            val title = it.getString(Constant.CONTENT_TITLE_KEY)
            agentWeb = AgentWeb.with(this)
                .setAgentWebParent(container, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(shareUrl)
            //获取网页的标题
            agentWeb?.webCreator?.webView?.webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    toolbar.title = title
                    super.onReceivedTitle(view, title)
                }
            }
        }
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (agentWeb?.handleKeyEvent(keyCode, event)!!) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    //右上menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_agent, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menuBrowser -> {
                Intent().run {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse(shareUrl)
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
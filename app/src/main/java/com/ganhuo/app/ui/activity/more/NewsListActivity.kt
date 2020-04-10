package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.NewsListAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.NewsListBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.utils.LogUtils
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_news_list.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-04-08 9:48
 *    desc   :新闻
 */
class NewsListActivity : BaseActivity() {
    private var typeName: String? = null
    private var typeId: Int? = null
    private var page = 1
    private val newsListAdapter: NewsListAdapter by lazy { NewsListAdapter(null) }
    override fun setLayoutId(): Int {
        return R.layout.activity_news_list
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        intent.extras?.run {
            typeName = getString("typeName")
            typeId = getInt("typeId")
        }
        toolbar.run {
            title = typeName
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        newsListAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(this@NewsListActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataNews = newsListAdapter.data[position]
                val newsId = dataNews?.newsId
                val title = dataNews?.title
                startActivity<NewsDetailsActivity>("title" to title, "newsId" to newsId)
            }
        }
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@NewsListActivity)
            setHasFixedSize(true)
            adapter = newsListAdapter
        }
        refresh()
    }

    private fun loadMore() {
        page++
        getNewsData(false)
    }

    private fun refresh() {
        page = 1
        getNewsData(true)
    }

    private fun getNewsData(refresh: Boolean) {
        doAsync {
            val parameters = listOf("typeId" to typeId, "page" to page)
            Fuel.get("https://www.mxnzp.com/news/list", parameters)
                .header("app_id" to Constant.APP_ID)
                .header("app_secret" to Constant.APP_KEY)
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        LogUtils.d("sucess:$sucess")
                        val fromJson = Gson().fromJson<NewsListBean>(
                            sucess,
                            object : TypeToken<NewsListBean>() {}.type
                        )
                        uiThread {
                            if (fromJson.code == 1) {
                                val toMutableList = fromJson.data.toMutableList()
                                if (refresh) {
                                    newsListAdapter.setNewData(toMutableList)
                                    smartRefreshLayout.finishRefresh(true)
                                } else {
                                    newsListAdapter.addData(toMutableList)
                                    smartRefreshLayout.finishLoadMore(true)
                                }
                            } else {
                                if (refresh) {
                                    newsListAdapter.setNewData(null)
                                    smartRefreshLayout.finishRefresh(false)
                                } else {
                                    smartRefreshLayout.finishLoadMore(false)
                                }
                                toast(fromJson.msg)
                            }
                        }
                    }, { err ->
                        uiThread {
                            if (refresh) {
                                newsListAdapter.setNewData(null)
                                smartRefreshLayout.finishRefresh(false)
                            } else {
                                smartRefreshLayout.finishLoadMore(false)
                            }
                            toast(err.toString())
                        }
                    })
                }
        }
    }
}
package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.WanSearchAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.SearchData
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.activity.AgentWebActivity
import com.ganhuo.app.utils.LogUtils
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_project_wan.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class SearchDetailsActivity : BaseActivity() {
    private var name: String? = null
    private var page = 0
    private val wanSearchAdapter by lazy { WanSearchAdapter(null) }

    override fun setLayoutId(): Int {
        return R.layout.activity_sort_details
    }

    override fun initData() {
        super.initData()
        initExtras()
        toolbar.run {
            title = name
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        wanSearchAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(this@SearchDetailsActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataProject = wanSearchAdapter.data
                val link = dataProject[position]?.link
                startActivity<AgentWebActivity>(
                    Constant.CONTENT_URL_KEY to link
                )
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@SearchDetailsActivity)
            setHasFixedSize(true)
            adapter = wanSearchAdapter
        }
        refresh()
    }

    private fun initExtras() {
        intent.extras?.let { extras ->
            extras.getString(Constant.CONTENT_CHILDREN_DATA_KEY)?.let {
                name = it as String
            }
        }
    }


    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    private fun refresh() {
        page = 0
        getSearchtData(true)
    }

    private fun loadMore() {
        page++
        getSearchtData(false)
    }

    // 搜索
    private fun getSearchtData(refresh: Boolean) {
        doAsync {
            val parameters = listOf("k" to name)
            Fuel.post("https://www.wanandroid.com/article/query/${page}/json", parameters)
                .responseString { request, response, result ->
                    result.fold({ success ->
                        LogUtils.d("sucess>>$success")
                        val fromJson = Gson().fromJson<SearchData>(
                            success,
                            object : TypeToken<SearchData>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.datas.toMutableList()
                            if (refresh) {
                                wanSearchAdapter.setNewData(toMutableList)
                                smartRefreshLayout.finishRefresh(true)
                            } else {
                                wanSearchAdapter.addData(toMutableList)
                                smartRefreshLayout.finishLoadMore(true)
                            }
                        }
                    },
                        { err ->
                            uiThread {
                                toast(err.toString())
                                if (refresh) {
                                    wanSearchAdapter.setNewData(null)
                                    smartRefreshLayout.finishRefresh(false)
                                } else {
                                    smartRefreshLayout.finishLoadMore(false)
                                }
                            }
                        })
                }
        }
    }
}

package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.WanSortAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.SortData
import com.ganhuo.app.bean.SortTitleData
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.activity.AgentWebActivity
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_project_wan.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class SortDetailsActivity : BaseActivity() {
    private var data: SortTitleData.Data? = null
    private var page = 1
    private val wanSortAdapter by lazy { WanSortAdapter(null) }

    override fun setLayoutId(): Int {
        return R.layout.activity_sort_details
    }

    override fun initData() {
        super.initData()
        initExtras()
        toolbar.run {
            title=data!!.name
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        wanSortAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(this@SortDetailsActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataProject = wanSortAdapter.data
                val link = dataProject[position]?.link
                startActivity<AgentWebActivity>(
                    Constant.CONTENT_URL_KEY to link
                )
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@SortDetailsActivity)
            setHasFixedSize(true)
            adapter = wanSortAdapter
        }
        refresh()
    }

    private fun initExtras() {
        intent.extras?.let { extras ->
            extras.getSerializable(Constant.CONTENT_CHILDREN_DATA_KEY)?.let {
                data = it as SortTitleData.Data
            }
        }
    }


    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    private fun refresh() {
        page = 1
        getSortData(true)
    }

    private fun loadMore() {
        page++
        getSortData(false)
    }

    //    项目列表数据 ?cid=294
    private fun getSortData(refresh: Boolean) {
        doAsync {
            val parameters = listOf("cid" to data!!.id)
            Fuel.get("https://www.wanandroid.com/project/list/${page}/json", parameters)
                .responseString { request, response, result ->
                    result.fold({ success ->
                        val fromJson = Gson().fromJson<SortData>(
                            success,
                            object : TypeToken<SortData>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.datas.toMutableList()
                            if (refresh) {
                                wanSortAdapter.setNewData(toMutableList)
                                smartRefreshLayout.finishRefresh(true)
                            } else {
                                wanSortAdapter.addData(toMutableList)
                                smartRefreshLayout.finishLoadMore(true)
                            }
                        }
                    },
                        { err ->
                            uiThread {
                                toast(err.toString())
                                if (refresh) {
                                    wanSortAdapter.setNewData(null)
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

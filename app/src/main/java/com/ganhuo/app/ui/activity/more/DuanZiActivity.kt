package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.DuanZiAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.DuanziBean
import com.ganhuo.app.constant.Constant
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_duanzi.*
import kotlinx.android.synthetic.main.activity_today.recyclerView
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-03-31 11:44
 *    desc   :段子乐
 */
class DuanZiActivity : BaseActivity() {
    private var spanCpunt = 2//2行
    private val duanZiAdapter by lazy { DuanZiAdapter(null) }
    private var page = 1

    override fun setLayoutId(): Int {
        return R.layout.activity_duanzi
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        toolbar.run {
            title = "段子乐"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        duanZiAdapter.run {
            setEmptyView(
                LayoutInflater.from(this@DuanZiActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataHistory = duanZiAdapter.data[position]
                val details = dataHistory.content
                if (details.isNotEmpty())
                    MaterialDialog(this@DuanZiActivity).show {
                        message(text = details)
                        positiveButton(R.string.string_sure)
                    }

            }
        }
        recyclerView.run {
            layoutManager =
                StaggeredGridLayoutManager(spanCpunt, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = duanZiAdapter
        }
        refresh()
    }
    private fun refresh() {
        page = 1
        getDuanZiData(true)
    }

    private fun loadMore() {
        page++
        getDuanZiData(false)
    }
    //段子乐
    private fun getDuanZiData(refresh: Boolean) {
        showLoadingDialog("")
        var url = "https://www.mxnzp.com/api/jokes/list"
        doAsync {
            val parameters = listOf("page" to page)
            Fuel.post(url, parameters)
                .header("app_id" to Constant.APP_ID)
                .header("app_secret" to Constant.APP_KEY)
                .responseString { request, response, result ->
                    result.fold({ success ->
                        hideLoadingDialog()
                        uiThread {
                            val fromJson = Gson().fromJson<DuanziBean>(
                                success,
                                object : TypeToken<DuanziBean>() {}.type
                            )
                            if (fromJson.code == 1) {
                                val data = fromJson.data.list.toMutableList()
                                if (refresh) {
                                    smartRefreshLayout.finishRefresh(true)
                                    duanZiAdapter.setNewData(data)
                                } else {
                                    smartRefreshLayout.finishLoadMore(true)
                                    duanZiAdapter.addData(data)
                                }
                            } else {
                                if (refresh) {
                                    duanZiAdapter.setNewData(null)
                                    smartRefreshLayout.finishRefresh(false)
                                } else {
                                    smartRefreshLayout.finishLoadMore(false)
                                }
                                toast(fromJson.msg)
                            }
                        }

                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            if (refresh) {
                                duanZiAdapter.setNewData(null)
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
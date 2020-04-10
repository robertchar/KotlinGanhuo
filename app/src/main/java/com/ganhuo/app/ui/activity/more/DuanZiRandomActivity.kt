package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.DuanZiRandomAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.DuanZiRandomBean
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
 *    desc   :段子乐(随机)
 */
class DuanZiRandomActivity : BaseActivity() {
    private var spanCpunt = 2//2行
    private val duanZiAdapter by lazy { DuanZiRandomAdapter(null) }

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
            title = "段子乐(随机)"
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
                LayoutInflater.from(this@DuanZiRandomActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataHistory = duanZiAdapter.data[position]
                val details = dataHistory.content
                if (details.isNotEmpty())
                    MaterialDialog(this@DuanZiRandomActivity).show {
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
        getDuanZiData(true)
    }

    private fun loadMore() {
        getDuanZiData(false)
    }
    //段子乐
    private fun getDuanZiData(refresh: Boolean) {
        showLoadingDialog("")
        var url = "https://www.mxnzp.com/api/jokes/list/random"
        doAsync {
            Fuel.post(url)
                .header("app_id" to Constant.APP_ID)
                .header("app_secret" to Constant.APP_KEY)
                .responseString { request, response, result ->
                    result.fold({ success ->
                        hideLoadingDialog()
                        uiThread {
                            val fromJson = Gson().fromJson<DuanZiRandomBean>(
                                success,
                                object : TypeToken<DuanZiRandomBean>() {}.type
                            )
                            if (fromJson.code == 1) {
                                val data = fromJson.data.toMutableList()
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
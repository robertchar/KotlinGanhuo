package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.HiatoryTodayAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.HistoryTodayData
import com.ganhuo.app.constant.Constant
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_today.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-03-31 11:44
 *    desc   :历史上的今天
 */
class HistoryTodayActivity : BaseActivity() {
    private var spanCpunt = 2//2行
    private val hiatoryTodayAdapter by lazy { HiatoryTodayAdapter(null) }
    override fun setLayoutId(): Int {
        return R.layout.activity_today
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        toolbar.run {
            title = "历史上的今天"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        hiatoryTodayAdapter.run {
            setEmptyView(
                LayoutInflater.from(this@HistoryTodayActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataHistory = hiatoryTodayAdapter.data[position]
                val details = dataHistory.details
                if (details.isNotEmpty())
                    MaterialDialog(this@HistoryTodayActivity).show {
                        message(text = details)
                        positiveButton(R.string.string_sure)
                    }

            }
        }
        recyclerView.run {
            layoutManager =
                StaggeredGridLayoutManager(spanCpunt, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = hiatoryTodayAdapter
        }
        getFuelData(1)
    }

    private fun getFuelData(type: Int) {
        showLoadingDialog("")
        var url = "https://www.mxnzp.com/api/history/today"
        doAsync {
            val parameters = listOf("type" to type)
            Fuel.post(url, parameters)
                .header("app_id" to Constant.APP_ID)
                .header("app_secret" to Constant.APP_KEY)
                .responseString { request, response, result ->
                    result.fold({ success ->
                        hideLoadingDialog()
                        uiThread {
                            val fromJson = Gson().fromJson<HistoryTodayData>(
                                success,
                                object : TypeToken<HistoryTodayData>() {}.type
                            )
                            val data = fromJson.data.toMutableList()
                            hiatoryTodayAdapter.setNewData(data)
                        }

                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            hiatoryTodayAdapter.setNewData(null)
                            toast(err.toString())
                        }
                    })
                }
        }
    }
}
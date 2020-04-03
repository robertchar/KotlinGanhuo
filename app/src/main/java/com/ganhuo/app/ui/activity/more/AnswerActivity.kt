package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.WanAnswerAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.AnwserData
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.activity.AgentWebActivity
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_intergral.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-04-02 17:10
 *    desc   :问答
 */
class AnswerActivity : BaseActivity() {
    private var page = 0
    private val wanAnswerAdapter by lazy { WanAnswerAdapter(null) }
    override fun setLayoutId(): Int {
        return R.layout.activity_intergral
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        toolbar.run {
            title = "问答"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        wanAnswerAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(this@AnswerActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataProject = wanAnswerAdapter.data
                val link = dataProject[position]?.link
                startActivity<AgentWebActivity>(
                    Constant.CONTENT_URL_KEY to link
                )
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@AnswerActivity)
            setHasFixedSize(true)
            adapter = wanAnswerAdapter
        }
        refresh()
    }


    private fun refresh() {
        page = 0
        getIntergralData(true)
    }

    private fun loadMore() {
        page++
        getIntergralData(false)
    }

    //    积分排行榜接口
    private fun getIntergralData(refresh: Boolean) {
        showLoadingDialog()
        doAsync {
            Fuel.get("https://wanandroid.com/wenda/list/${page}/json")
                .responseString { request, response, result ->
                    result.fold({ success ->
                        hideLoadingDialog()
                        val fromJson = Gson().fromJson<AnwserData>(
                            success,
                            object : TypeToken<AnwserData>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.datas.toMutableList()
                            if (refresh) {
                                wanAnswerAdapter.setNewData(toMutableList)
                                smartRefreshLayout.finishRefresh(true)
                            } else {
                                wanAnswerAdapter.addData(toMutableList)
                                smartRefreshLayout.finishLoadMore(true)
                            }
                        }
                    },
                        { err ->
                            hideLoadingDialog()
                            uiThread {
                                toast(err.toString())
                                if (refresh) {
                                    wanAnswerAdapter.setNewData(null)
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
package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.WanIntergralAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.IntergralBean
import com.ganhuo.app.bean.SelfCoinBean
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_intergral.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-04-02 17:10
 *    desc   :积分
 */
class IntergralActivity : BaseActivity() {
    private var page = 1
    private val wanIntergralAdapter by lazy { WanIntergralAdapter(null) }
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
            title = "积分"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        wanIntergralAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(this@IntergralActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )

        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@IntergralActivity)
            setHasFixedSize(true)
            adapter = wanIntergralAdapter
        }
        refresh()
        //自己的积分
        getSelfCoin()
    }

    //自己的积分
    private fun getSelfCoin() {
        doAsync {
            Fuel.get("https://www.wanandroid.com/lg/coin/userinfo/json")
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        val fromJson = Gson().fromJson<SelfCoinBean>(
                            sucess,
                            object : TypeToken<SelfCoinBean>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                toast(fromJson.errorMsg)
                            } else {
                                val data = fromJson.data
                                self_name.text = data.username
                                user_coin.text = data.coinCount.toString()
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

    private fun refresh() {
        page = 1
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
            Fuel.get("https://www.wanandroid.com/coin/rank/${page}/json")
                .responseString { request, response, result ->
                    result.fold({ success ->
                        hideLoadingDialog()
                        val fromJson = Gson().fromJson<IntergralBean>(
                            success,
                            object : TypeToken<IntergralBean>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.datas.toMutableList()
                            if (refresh) {
                                wanIntergralAdapter.setNewData(toMutableList)
                                smartRefreshLayout.finishRefresh(true)
                            } else {
                                wanIntergralAdapter.addData(toMutableList)
                                smartRefreshLayout.finishLoadMore(true)
                            }
                        }
                    },
                        { err ->
                            hideLoadingDialog()
                            uiThread {
                                toast(err.toString())
                                if (refresh) {
                                    wanIntergralAdapter.setNewData(null)
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
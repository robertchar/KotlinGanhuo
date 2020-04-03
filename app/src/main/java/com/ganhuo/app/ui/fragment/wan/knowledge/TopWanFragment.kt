package com.ganhuo.app.ui.fragment.wan.knowledge

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.WanTopAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.TopWanDdata
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.activity.AgentWebActivity
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_project_wan.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-03-31 14:55
 *    desc   :wan置顶
 */
class TopWanFragment : BaseFragment() {
    private var refresh = true
    private val wanTopAdapter by lazy { WanTopAdapter(null) }
    override fun getLayout(): Int {
        return R.layout.fragment_top_wan
    }

    companion object {
        fun newInstance(): TopWanFragment {
            return TopWanFragment()
        }
    }


    override fun initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    override fun initData() {
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        wanTopAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(activity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataProject = wanTopAdapter.data
                val link = dataProject[position]?.link
                startActivity<AgentWebActivity>(
                    Constant.CONTENT_URL_KEY to link
                )
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = wanTopAdapter
        }
        refresh()
    }

    private fun refresh() {
        refresh = true
        getTopData()
    }

    private fun loadMore() {
        refresh = false
        getTopData()
    }

    //置顶文章
    private fun getTopData() {
        doAsync {
            Fuel.get("https://www.wanandroid.com/article/top/json")
                .responseString { request, response, result ->
                    result.fold({ success ->
                        val fromJson = Gson().fromJson<TopWanDdata>(
                            success,
                            object : TypeToken<TopWanDdata>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.toMutableList()
                            wanTopAdapter.setNewData(toMutableList)
                            if (refresh) {
                                smartRefreshLayout.finishRefresh(true)
                            } else {
                                smartRefreshLayout.finishLoadMore(true)
                            }
                        }
                    },
                        { err ->
                            uiThread {
                                toast(err.toString())
                                wanTopAdapter.setNewData(null)
                                if (refresh) {
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
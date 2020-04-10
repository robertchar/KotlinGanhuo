package com.ganhuo.app.ui.fragment.wan.knowledge

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.WanSquareAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.SquareData
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
 *    desc   :wan广场
 */
class SquareWanFragment : BaseFragment() {
    private val wanSquareAdapter by lazy { WanSquareAdapter(null) }
    private var page = 0
    override fun getLayout(): Int {
        return R.layout.fragment_square_wan
    }

    companion object {
        fun newInstance(): SquareWanFragment {
            return SquareWanFragment()
        }
    }

    override fun initData() {
        super.initData()
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        wanSquareAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(activity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataProject = wanSquareAdapter.data
                val link = dataProject[position]?.link
                startActivity<AgentWebActivity>(
                    Constant.CONTENT_URL_KEY to link
                )
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = wanSquareAdapter
        }
        refresh()
    }

    private fun refresh() {
        page = 0
        getSquareData(true)
    }

    private fun loadMore() {
        page++
        getSquareData(false)
    }

    //    广场列表数据
    private fun getSquareData(refresh: Boolean) {
        doAsync {
            Fuel.get("https://wanandroid.com/user_article/list/${page}/json")
                .responseString { request, response, result ->
                    result.fold({ success ->
                        val fromJson = Gson().fromJson<SquareData>(
                            success,
                            object : TypeToken<SquareData>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.datas.toMutableList()
                            if (refresh) {
                                wanSquareAdapter.setNewData(toMutableList)
                                smartRefreshLayout.finishRefresh(true)
                            } else {
                                wanSquareAdapter.addData(toMutableList)
                                smartRefreshLayout.finishLoadMore(true)
                            }
                        }
                    },
                        { err ->
                            uiThread {
                                toast(err.toString())
                                if (refresh) {
                                    wanSquareAdapter.setNewData(null)
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
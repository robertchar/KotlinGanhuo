package com.ganhuo.app.ui.fragment.wan.knowledge

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.SortTypeAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.SortTitleData
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.activity.more.SortDetailsActivity
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_project_wan.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-03-31 14:55
 *    desc   :wan分类
 */
class SortWanFragment : BaseFragment() {
    private val wanTitleAdapter by lazy { SortTypeAdapter(null) }
    override fun getLayout(): Int {
        return R.layout.fragment_sort_wan
    }

    companion object {
        fun newInstance(): SortWanFragment {
            return SortWanFragment()
        }
    }

    override fun initData() {
        super.initData()
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        wanTitleAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(activity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { _, view, position ->
                val dataProject = wanTitleAdapter.data
                Intent(activity, SortDetailsActivity::class.java).run {
                    putExtra(Constant.CONTENT_CHILDREN_DATA_KEY, dataProject[position])
                    startActivity(this)
                }
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = wanTitleAdapter
        }
        refresh()
    }

    private fun refresh() {
        getSortData(true)
    }

    private fun loadMore() {
        getSortData(false)
    }

    //    项目分类
    private fun getSortData(refresh: Boolean) {
        doAsync {
            Fuel.get("https://www.wanandroid.com/project/tree/json")
                .responseString { request, response, result ->
                    result.fold({ success ->
                        val fromJson = Gson().fromJson<SortTitleData>(
                            success,
                            object : TypeToken<SortTitleData>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.toMutableList()
                            wanTitleAdapter.setNewData(toMutableList)
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
                                wanTitleAdapter.setNewData(null)
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
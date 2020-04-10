package com.ganhuo.app.ui.fragment.wan.knowledge

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.SystemTypeAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.SystemTiteData
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.activity.more.MagicIndicatorActivity
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
 *    desc   :wan知识体系
 */
class SystemWanFragment : BaseFragment() {
    private var refresh = true
    private val wanTitleAdapter by lazy { SystemTypeAdapter(null) }
    override fun getLayout(): Int {
        return R.layout.fragment_system_wan
    }


    companion object {
        fun newInstance(): SystemWanFragment {
            return SystemWanFragment()
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
                Intent(activity, MagicIndicatorActivity::class.java).run {
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
        refresh = true
        getSystemData()
    }

    private fun loadMore() {
        refresh = false
        getSystemData()
    }

    //体系数据
    private fun getSystemData() {
        doAsync {
            Fuel.get("https://www.wanandroid.com/tree/json")
                .responseString { request, response, result ->
                    result.fold({ success ->
                        val fromJson = Gson().fromJson<SystemTiteData>(
                            success,
                            object : TypeToken<SystemTiteData>() {}.type
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
package com.ganhuo.app.ui.fragment.wan

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.WanProjectAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.WanProjectBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.activity.AgentWebActivity
import com.ganhuo.app.utils.LogUtils
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
 *    desc   :wan项目
 */
class ProjectWanFragment : BaseFragment() {
    private var refresh = true
    private var page = 0
    private val wanProjectAdapter by lazy { WanProjectAdapter(null) }
    override fun getLayout(): Int {
        return R.layout.fragment_project_wan
    }

    //    线程安全的懒汉式单例
    companion object {
        fun newInstance(): ProjectWanFragment {
            return ProjectWanFragment()
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    override fun initData() {
        super.initData()
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        wanProjectAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(activity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataProject = wanProjectAdapter.data
                val link = dataProject[position]?.link
                val title = dataProject[position]?.title
                startActivity<AgentWebActivity>(
                    Constant.CONTENT_URL_KEY to link,
                    Constant.CONTENT_TITLE_KEY to title
                )
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = wanProjectAdapter
        }
        refresh()
    }

    private fun refresh() {
        refresh = true
        page = 0
        getProjectData()
    }

    private fun loadMore() {
        refresh = false
        page++
        getProjectData()
    }

    private fun getProjectData() {
        showLoadingDialog("")
        doAsync {
            val parameters = listOf("page" to page)
            Fuel.get("https://www.wanandroid.com/article/listproject/${page}/json", parameters)
                .responseString { _, _, result ->
                    result.fold({ success ->
                        hideLoadingDialog()
                        LogUtils.d("article:${success}")
                        val fromJson = Gson().fromJson<WanProjectBean>(
                            success,
                            object : TypeToken<WanProjectBean>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.datas.toMutableList()
                            if (refresh) {
                                smartRefreshLayout.finishRefresh(true)
                                wanProjectAdapter.setNewData(toMutableList)
                            } else {
                                smartRefreshLayout.finishLoadMore(true)
                                wanProjectAdapter.addData(toMutableList)
                            }
                        }
                    }, { err ->
                        hideLoadingDialog()
                        LogUtils.d("article2:${err.toString()}")
                        uiThread {
                            toast(err.toString())
                            wanProjectAdapter.setNewData(null)
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
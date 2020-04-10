package com.ganhuo.app.ui.fragment

import android.text.TextUtils
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.MarrowAdapter
import com.ganhuo.app.adpter.brvah.MarrowViewPagerAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.MarrowDataBean
import com.ganhuo.app.ui.activity.TextHtmlActivity
import com.ganhuo.app.utils.MmkvCacheUtil
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Parameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.recyclerView
import kotlinx.android.synthetic.main.fragment_list.smartRefreshLayout
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-03-24 12:31
 *    desc   :美文（Fuel使用）viewPager2使用(设置垂直和水平)
 */
class MarrowFragment : BaseFragment() {
    private var page = 1
    private var refresh = true
    private val marrowAdapter by lazy { MarrowAdapter(null) }
    private val marrowViewPagerAdapter by lazy { MarrowViewPagerAdapter(null) }

    override fun getLayout(): Int = R.layout.fragment_search

    companion object {
        fun newInstance(): MarrowFragment {
            return MarrowFragment()
        }
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        toolbar.run {
            title = "美文欣赏"
        }
        smartRefreshLayout.run {
            setOnLoadMoreListener { loadMore() }
            setOnRefreshListener { refresh() }
        }
        marrowAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(activity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataMarr = marrowAdapter.data
                val dataBean = dataMarr[position]
                startActivity<TextHtmlActivity>(
                    "content" to dataBean.content,
                    "title" to dataBean.title
                )
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = marrowAdapter
        }
        viewPager2.run {
            adapter = marrowViewPagerAdapter
        }
        getCashData()
    }

    private fun getCashData() {
        doAsync {
            //从缓存中拿数据
            val marrowHistoryData = MmkvCacheUtil.getMarrowHistoryData()
            uiThread {
                if (TextUtils.isEmpty(marrowHistoryData)) {
                    return@uiThread
                }
                val fromJson = Gson().fromJson<MarrowDataBean>(
                    marrowHistoryData,
                    object : TypeToken<MarrowDataBean>() {}.type
                )
                val toMutableList = fromJson.data.toMutableList()
                marrowAdapter.setNewData(toMutableList)
                marrowViewPagerAdapter.setNewData(toMutableList)
            }
        }


    }

    private fun refresh() {
        refresh = true
        page = 1
        getData()
    }

    private fun loadMore() {
        refresh = false
        page++
        getData()
    }

    private fun getData() {
        val params = listOf("page" to page)
        getFuelNetData(params)
    }

    //    Fuel使用
    private fun getFuelNetData(params: Parameters) {
        showLoadingDialog("")
        doAsync {
            Fuel.get("https://v1.alapi.cn/api/mryw/list", params)
                .responseString { equest, response, result ->
                    result.fold({ d ->
                        //成功
                        hideLoadingDialog()
                        val fromJson = Gson().fromJson<MarrowDataBean>(
                            d,
                            object : TypeToken<MarrowDataBean>() {}.type
                        )
                        val toMutableList = fromJson.data.toMutableList()
                        uiThread {
                            if (refresh) {
                                marrowAdapter.setNewData(toMutableList)
                                smartRefreshLayout.finishRefresh(true)
                                //保存
                                MmkvCacheUtil.setMarrowHistoryData(d)
                            } else {
                                marrowAdapter.addData(toMutableList)
                                smartRefreshLayout.finishLoadMore(true)
                            }
                        }
                    }, { error ->
                        //失败
                        hideLoadingDialog()
                        uiThread {
                            if (refresh) {
                                marrowAdapter.setNewData(null)
                                smartRefreshLayout.finishRefresh(false)
                            } else {
                                smartRefreshLayout.finishLoadMore(false)
                            }
                            toast(error.toString())
                        }
                    })
                }
        }

    }
}
package com.ganhuo.app.ui.fragment.wan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.WanPublicAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.PublicHistoryData
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.activity.AgentWebActivity
import com.ganhuo.app.utils.LogUtils
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread

class PublicMagicIndicatorChildFragment : BaseFragment() {
    private var typeKey: Int? = null
    private var fresh = true
    private var page = 1
    private val wanPublicAdapter: WanPublicAdapter by lazy { WanPublicAdapter(null) }

    override fun getLayout(): Int = R.layout.fragment_list

    companion object {
        fun newInstance(type: Int): PublicMagicIndicatorChildFragment {
            val fragment = PublicMagicIndicatorChildFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_TYPE_KEY, type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initData() {
        super.initData()
        typeKey = arguments?.getInt(Constant.CONTENT_TYPE_KEY)
        LogUtils.d("type:" + typeKey)
        smartRefreshLayout.run {
            ////设置 Header 为 Material风格
            setRefreshHeader(MaterialHeader(activity).setShowBezierWave(true))
            //设置 Footer 为 球脉冲 样式
            setRefreshFooter(BallPulseFooter(activity).setSpinnerStyle(SpinnerStyle.Scale))
            setOnRefreshListener { fresh() }
            setOnLoadMoreListener { loadMore() }
        }
        wanPublicAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(activity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val commonDatas = wanPublicAdapter.data[position]
                Intent(activity, AgentWebActivity::class.java).run {
                    this.putExtra(Constant.CONTENT_URL_KEY, commonDatas.link)//标题
                    activity?.startActivity(this)
                }
            }
        }
        recyclerView.run {
            LogUtils.d("recyclerView:")
            layoutManager = LinearLayoutManager(activity)
            adapter = wanPublicAdapter
        }
        fresh()
    }

    private fun loadMore() {
        fresh = false
        page++
        getData()
    }

    private fun fresh() {
        fresh = true
        page = 1
        getData()
    }

    //    查看某个公众号历史数据
    private fun getData() {
        doAsync {
            Fuel.get("https://wanandroid.com/wxarticle/list/${typeKey}/${page}/json")
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        val fromJson =
                            Gson().fromJson<PublicHistoryData>(
                                sucess,
                                object : TypeToken<PublicHistoryData>() {}.type
                            )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.datas.toMutableList()
                            if (fresh) {
                                wanPublicAdapter.setNewData(toMutableList)
                                smartRefreshLayout.finishRefresh(true);
                            } else {
                                wanPublicAdapter.addData(toMutableList)
                                smartRefreshLayout.finishLoadMore(true);
                            }
                        }
                    }, { err ->
                        uiThread {
                            toast(err.toString())
                            if (fresh) {
                                wanPublicAdapter.setNewData(null)
                                smartRefreshLayout.finishRefresh(false);
                            } else {
                                smartRefreshLayout.finishLoadMore(false);
                            }
                        }
                    })
                }
        }
    }

}

package com.ganhuo.app.ui.fragment.wan.knowledge

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.TypeArticleAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.KnowSystemData
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.activity.AgentWebActivity
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread


/**
 *    author : zkk
 *    date   : 2020-03-17 11:57
 *    desc   :MagicIndicator
 */
class SystemMagicIndicatorChildFragment : BaseFragment() {
    private var cid = 0
    private var page = 0
    private val typeArticleAdapter: TypeArticleAdapter by lazy { TypeArticleAdapter(null) }

    override fun getLayout(): Int {
        return R.layout.fragment_list
    }

    companion object {
        fun newInstance(cid: Int): SystemMagicIndicatorChildFragment {
            val fragment = SystemMagicIndicatorChildFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initData() {
        super.initData()
        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: 0
        smartRefreshLayout.run {
            ////设置 Header 为 Material风格
            setRefreshHeader(MaterialHeader(activity).setShowBezierWave(true))
            //设置 Footer 为 球脉冲 样式
            setRefreshFooter(BallPulseFooter(activity).setSpinnerStyle(SpinnerStyle.Scale))
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = typeArticleAdapter
        }
        typeArticleAdapter.run {
            animationEnable = true
            setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft)
            setEmptyView(R.layout.fragment_home_empty)
            setOnItemClickListener { adapter, view, position ->
                val commonDatas = typeArticleAdapter.data
                Intent(activity, AgentWebActivity::class.java).run {
                    this.putExtra(Constant.CONTENT_URL_KEY, commonDatas[position].link)
                    activity?.startActivity(this)
                }
            }
        }
        initRefreshData()
        getData(true)//初始化就加载数据
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    private fun initRefreshData() {
        //下拉刷新
        smartRefreshLayout.setOnRefreshListener { refresh() }
        //加载更多
        smartRefreshLayout.setOnLoadMoreListener { loadmore() }
    }

    private fun loadmore() {
        page++
        getData(false)
    }

    private fun refresh() {
        page = 0
        getData(true)
    }

    /**
     * 获取数据
     * ?cid=60
     */
    private fun getData(refresh: Boolean) {
        doAsync {
            val parameters = listOf("cid" to cid)
            Fuel.get("https://www.wanandroid.com/article/list/${page}/json", parameters)
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        val fromJson = Gson().fromJson<KnowSystemData>(sucess, KnowSystemData::class.javaObjectType)
                        val mutableList = fromJson.data.datas.toMutableList()
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            if (refresh) {
                                smartRefreshLayout.finishRefresh(true)
                                typeArticleAdapter.setNewData(mutableList)
                            } else {
                                smartRefreshLayout.finishLoadMore(true)
                                typeArticleAdapter.addData(mutableList)
                            }
                        }
                    }, { err ->
                        uiThread {
                            toast(err.toString())
                            if (refresh) {
                                smartRefreshLayout.finishRefresh(false)
                                typeArticleAdapter.setNewData(null)
                            } else {
                                smartRefreshLayout.finishLoadMore(false)
                            }
                        }
                    })
                }
        }
    }
}
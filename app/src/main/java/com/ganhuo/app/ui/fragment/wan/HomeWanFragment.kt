package com.ganhuo.app.ui.fragment.wan

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.ImageTitleAdapter
import com.ganhuo.app.adpter.brvah.more.WanArticleAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.base.string
import com.ganhuo.app.bean.BannerWanBean
import com.ganhuo.app.bean.WanArticleDataBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.activity.AgentWebActivity
import com.ganhuo.app.utils.LogUtils
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import com.tencent.mmkv.MMKV
import com.youth.banner.config.BannerConfig
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.util.BannerUtils
import kotlinx.android.synthetic.main.fragment_home_wan.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-03-31 14:55
 *    desc   :wan首页
 */
class HomeWanFragment : BaseFragment() {
    private val imageTitleAdapter by lazy { ImageTitleAdapter(null) }
    private val wanArticleAdapter by lazy { WanArticleAdapter(null) }
    private var refresh = true
    private var page = 0

    private val mmkv: MMKV by lazy { MMKV.defaultMMKV() }
    private var wanBannerString by mmkv.string("wan", "")

    override fun getLayout(): Int {
        return R.layout.fragment_home_wan
    }

    companion object {
        fun newInstance(): HomeWanFragment {
            return HomeWanFragment()
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    override fun initData() {
        super.initData()
        bannerWan.run {
            adapter = imageTitleAdapter
            //设置指示器
            activity?.let {
                bannerWan.indicator = CircleIndicator(it)
                bannerWan.setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
                bannerWan.setIndicatorMargins(
                    IndicatorConfig.Margins(
                        0, 0,
                        BannerConfig.INDICATOR_MARGIN, BannerUtils.dp2px(12f).toInt()
                    )
                )
            }
            //圆角
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bannerWan.setBannerRound(BannerUtils.dp2px(5f))
            }
            setOnBannerListener(object : OnBannerListener<BannerWanBean.Data> {
                override fun onBannerChanged(position: Int) {
                }

                override fun OnBannerClick(data: BannerWanBean.Data?, position: Int) {
                    Intent(activity, AgentWebActivity::class.java).run {
                        putExtra(Constant.CONTENT_URL_KEY, data?.url)
                        putExtra(Constant.CONTENT_TITLE_KEY, data?.title)
                        startActivity(this)
                    }
                }
            })
        }
        wanArticleAdapter.run {
            setEmptyView(
                LayoutInflater.from(activity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataArt = wanArticleAdapter.data
                val link = dataArt[position]?.link
                val title = dataArt[position]?.title
                startActivity<AgentWebActivity>(
                    Constant.CONTENT_URL_KEY to link,
                    Constant.CONTENT_TITLE_KEY to title
                )
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = wanArticleAdapter
        }
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        setBanerFromCash()//从cash中获取banner数据
        getArticleData()
    }

    //从cash中获取banner数据
    private fun setBanerFromCash() {
        LogUtils.d("bannerStr:${wanBannerString.isEmpty()}")
        if (wanBannerString.isEmpty()) {
            getBannerData()
        } else {
            val fromJson = Gson().fromJson<BannerWanBean>(
                wanBannerString,
                object : TypeToken<BannerWanBean>() {}.type
            )
            val toMutableList = fromJson.data.toMutableList()
            imageTitleAdapter.setDatas(toMutableList)
            imageTitleAdapter.notifyDataSetChanged()
        }
    }

    private fun refresh() {
        refresh = true
        page = 0
        getArticleData()
        getBannerData()
    }

    private fun loadMore() {
        refresh = false
        page++
        getArticleData()
        getBannerData()
    }

    //    首页文章列表
    private fun getArticleData() {
        showLoadingDialog("")
        doAsync {
            val parameters = listOf("page" to page)
            Fuel.get("https://www.wanandroid.com/article/list/${page}/json", parameters)
                .responseString { _, _, result ->
                    result.fold({ success ->
                        hideLoadingDialog()
                        LogUtils.d("article:${success}")
                        val fromJson = Gson().fromJson<WanArticleDataBean>(
                            success,
                            object : TypeToken<WanArticleDataBean>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.datas.toMutableList()
                            if (refresh) {
                                smartRefreshLayout.finishRefresh(true)
                                wanArticleAdapter.setNewData(toMutableList)
                            } else {
                                smartRefreshLayout.finishLoadMore(true)
                                wanArticleAdapter.addData(toMutableList)
                            }
                        }
                    }, { err ->
                        hideLoadingDialog()
                        LogUtils.d("article2:${err.toString()}")
                        uiThread {
                            toast(err.toString())
                            wanArticleAdapter.setNewData(null)
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

    //获取banner
    private fun getBannerData() {
        doAsync {
            Fuel.get("https://www.wanandroid.com/banner/json")
                .responseString { _, _, result ->
                    result.fold({ success ->
                        val fromJson = Gson().fromJson<BannerWanBean>(
                            success,
                            object : TypeToken<BannerWanBean>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            //保存cash数据
                            wanBannerString = success
                            val toMutableList = fromJson.data.toMutableList()
                            imageTitleAdapter.setDatas(toMutableList)
                            imageTitleAdapter.notifyDataSetChanged()
                        }
                    }, { err ->
                        uiThread {
                            toast(err.toString())
                        }
                    })
                }
        }
    }

    /**
     * 如果你需要考虑更好的体验，可以这么操作
     */
    override fun onStart() {
        super.onStart()
        bannerWan.start()
    }

    override fun onStop() {
        super.onStop()
        bannerWan.stop()
    }
}
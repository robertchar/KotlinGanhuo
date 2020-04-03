package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.HotWordAdapter
import com.ganhuo.app.adpter.brvah.more.NavigationOneAdapter
import com.ganhuo.app.adpter.brvah.more.NetAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.HotWordBean
import com.ganhuo.app.bean.NavigationBean
import com.ganhuo.app.bean.NetBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.help.MyFlexboxLayoutManager
import com.ganhuo.app.ui.activity.AgentWebActivity
import com.github.kittinunf.fuel.Fuel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_history_search.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-04-01 14:08
 *    desc   :搜索历史(流式布局)
 */
class HistorySearchActivity : BaseActivity() {
    //热词
    private val hotAdapter by lazy { HotWordAdapter(null) }

    //导航
    private val navAdapter by lazy { NavigationOneAdapter(null) }

    //常用网站
    private val netAdapter by lazy { NetAdapter(null) }
    override fun setLayoutId(): Int {
        return R.layout.activity_history_search
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        toolbar.run {
            title = "搜索"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        hotAdapter.run {
            setEmptyView(
                LayoutInflater.from(this@HistorySearchActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataHot = hotAdapter.data[position]
                startActivity<SearchDetailsActivity>(Constant.CONTENT_CHILDREN_DATA_KEY to dataHot.name)
            }
        }
        recyclerViewHot.run {
            val flexboxLayoutManager = MyFlexboxLayoutManager(this@HistorySearchActivity)
            //设置主轴排列方式
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            //设置是否换行
            flexboxLayoutManager.flexWrap = FlexWrap.WRAP
            flexboxLayoutManager.alignItems = AlignItems.STRETCH
            //justifyContent 属性定义了项目在主轴上的对齐方式。
            flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
            //替换布局管理器FlexboxLayoutManager
            layoutManager = flexboxLayoutManager
            isNestedScrollingEnabled = false
            adapter = hotAdapter
        }
        navAdapter.run {
            setEmptyView(
                LayoutInflater.from(this@HistorySearchActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setItemClickListener(object : NavigationOneAdapter.onItemClick {
                override fun itemClick(
                    adapter: BaseQuickAdapter<*, *>,
                    view: View,
                    position: Int,
                    type: String
                ) {
                    val data = adapter.data[position] as NavigationBean.Data.Article
                    startActivity<AgentWebActivity>(Constant.CONTENT_URL_KEY to data?.link)
                }
            })
        }
        recyclerViewNav.run {
            layoutManager = LinearLayoutManager(this@HistorySearchActivity)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = navAdapter
        }
        netAdapter.run {
            setEmptyView(
                LayoutInflater.from(this@HistorySearchActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataNet = netAdapter.data[position]
                startActivity<AgentWebActivity>(Constant.CONTENT_URL_KEY to dataNet?.link)
            }
        }
        recyclerViewNet.run {
            val flexboxLayoutManager = MyFlexboxLayoutManager(this@HistorySearchActivity)
            //设置主轴排列方式
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            //设置是否换行
            flexboxLayoutManager.flexWrap = FlexWrap.WRAP
            flexboxLayoutManager.alignItems = AlignItems.STRETCH
            //justifyContent 属性定义了项目在主轴上的对齐方式。
            flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
            //替换布局管理器FlexboxLayoutManager
            layoutManager = flexboxLayoutManager
            isNestedScrollingEnabled = false
            adapter = netAdapter
        }
        getData()
    }

    private fun getData() {
        //热词
        getHotWord()
        //导航
        getNavData()
        //网站
        getNetWord()
    }

    //网站
    private fun getNetWord() {
        showLoadingDialog()
        doAsync {
            Fuel.get("https://www.wanandroid.com/friend/json")
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        hideLoadingDialog()
                        val fromJson = Gson().fromJson<NetBean>(
                            sucess,
                            object : TypeToken<NetBean>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.toMutableList()
                            netAdapter.setNewData(toMutableList)
                        }
                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            toast(err.toString())
                            netAdapter.setNewData(null)
                        }
                    })
                }
        }
    }

    //导航
    private fun getNavData() {
        showLoadingDialog()
        doAsync {
            Fuel.get("https://www.wanandroid.com/navi/json")
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        hideLoadingDialog()
                        val fromJson = Gson().fromJson<NavigationBean>(
                            sucess,
                            object : TypeToken<NavigationBean>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.toMutableList()
                            navAdapter.setNewData(toMutableList)
                        }
                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            toast(err.toString())
                            navAdapter.setNewData(null)
                        }
                    })
                }
        }
    }

    //热词
    private fun getHotWord() {
        showLoadingDialog()
        doAsync {
            Fuel.get("https://www.wanandroid.com//hotkey/json")
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        hideLoadingDialog()
                        val fromJson = Gson().fromJson<HotWordBean>(
                            sucess,
                            object : TypeToken<HotWordBean>() {}.type
                        )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            val toMutableList = fromJson.data.toMutableList()
                            hotAdapter.setNewData(toMutableList)
                        }
                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            toast(err.toString())
                            hotAdapter.setNewData(null)
                        }
                    })
                }
        }
    }
}
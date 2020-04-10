package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.EveryDayMarrowAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.EveryDayMarrowData
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.help.MyFlexboxLayoutManager
import com.github.kittinunf.fuel.Fuel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_today.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-03-31 13:40
 *    desc   :每日精美语句
 */
class EveryDayMarrowActivity : BaseActivity() {
    private val everyDayMarrowAdapter by lazy { EveryDayMarrowAdapter(null) }

    override fun setLayoutId(): Int {
        return R.layout.activity_day_marrow
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        toolbar.run {
            title = "每日精美语句"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        everyDayMarrowAdapter.run {
            setEmptyView(
                LayoutInflater.from(this@EveryDayMarrowActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
        }
        recyclerView.run {
            val flexboxLayoutManager = MyFlexboxLayoutManager(this@EveryDayMarrowActivity)
            //设置主轴排列方式
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            //设置是否换行
            flexboxLayoutManager.flexWrap = FlexWrap.WRAP
            flexboxLayoutManager.alignItems = AlignItems.STRETCH
            //justifyContent 属性定义了项目在主轴上的对齐方式。
            flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
            //替换布局管理器FlexboxLayoutManager
            layoutManager = flexboxLayoutManager
            adapter = everyDayMarrowAdapter
        }
        getData(20)
    }

    private fun getData(count: Int) {
        showLoadingDialog("")
        var url = "https://www.mxnzp.com/api/daily_word/recommend"
        doAsync {
            val parameters = listOf("count" to count)
            Fuel.post(url, parameters)
                .header("app_id" to Constant.APP_ID)
                .header("app_secret" to Constant.APP_KEY)
                .responseString { request, response, result ->
                    result.fold({ success ->
                        hideLoadingDialog()
                        uiThread {
                            val fromJson = Gson().fromJson<EveryDayMarrowData>(
                                success,
                                object : TypeToken<EveryDayMarrowData>() {}.type
                            )
                            if (fromJson.code == 1) {
                                val data = fromJson.data.toMutableList()
                                everyDayMarrowAdapter.setNewData(data)
                            } else {
                                toast(fromJson.msg)
                            }
                        }

                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            everyDayMarrowAdapter.setNewData(null)
                            toast(err.toString())
                        }
                    })
                }
        }
    }
}
package com.ganhuo.app.ui.activity

import android.graphics.Color
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.HotDataAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.HotDataBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.help.RecycleViewDivider
import com.ganhuo.app.presenter.HotpresenterImpl
import com.ganhuo.app.view.BaseView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_hot.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 *    author : zkk
 *    date   : 2020-03-25 16:49
 *    desc   :热门数据
 */
class HotDataActivity : BaseActivity(), BaseView {
    private var count = 10//数目
    private val hotpresenterImpl by lazy { HotpresenterImpl(this) }
    private val hotDataAdapter by lazy { HotDataAdapter(null) }
    private var hot_type: String? = null
    private var category: String? = null

    override fun setLayoutId(): Int {
        return R.layout.activity_hot
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        toolbar.run {
            title = "热门数据"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        intent.extras?.run {
            hot_type = this.getString("hot_type")
            category = this.getString("category")
        }
        hotDataAdapter.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(this@HotDataActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val text = hotDataAdapter.data[position].url
                val title = hotDataAdapter.data[position].title
                startActivity<AgentWebActivity>(
                    Constant.CONTENT_URL_KEY to text,
                    Constant.CONTENT_TITLE_KEY to title
                )
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@HotDataActivity)
            adapter = hotDataAdapter
            addItemDecoration(
                RecycleViewDivider(
                    context,
                    LinearLayout.VERTICAL,
                    2,
                    Color.LTGRAY
                )
            )
        }
        getData()
    }

    override fun cancelRequest() {
        super.cancelRequest()
        hotpresenterImpl.cancelRequest()
    }

    private fun getData() {
        showLoadingDialog("")
        hot_type?.let {
            category?.let { category ->
                hotpresenterImpl.getgetCategoriesData(
                    it,
                    category, count
                )
            }
        }
    }

    override fun onError(fail: String) {
        hideLoadingDialog()
        runOnUiThread {
            hotDataAdapter.setNewData(null)
            toast(fail)
        }
    }

    override fun onSucess(sucess: String) {
        hideLoadingDialog()
        runOnUiThread {
            val fromJson =
                Gson().fromJson<HotDataBean>(sucess, object : TypeToken<HotDataBean>() {}.type)
            if (fromJson.status != 100) {
               return@runOnUiThread
            }
            val toMutableList = fromJson.data.toMutableList()
            hotDataAdapter.setNewData(toMutableList)
        }
    }
}
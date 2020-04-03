package com.ganhuo.app.ui.activity

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.CategoryDataAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.CategoryDataBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.presenter.CategorypresenterImpl
import com.ganhuo.app.view.BaseView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_recycler_image.*
import kotlinx.android.synthetic.main.activity_recycler_image.recyclerView

/**
 *    author : zkk
 *    date   : 2020-03-25 9:46
 *    desc   :RecyclerView图片瀑布流
 */
class RecyclerViewBitmapActivity : BaseActivity(), BaseView {
    private var spanCpunt = 2//2行
    private val categoryDataAdapter by lazy { CategoryDataAdapter(null) }
    private val categorypresenterImpl by lazy { CategorypresenterImpl(this) }
    private var fresh = true
    private var page = 1
    private var count = 10
    private var category: String? = null
    private var type: String? = null

    override fun setLayoutId(): Int {
        return R.layout.activity_recycler_image
    }

    override fun initData() {
        super.initData()
        intent.extras?.run {
            category = this.getString(Constant.CONTENT_TYPE_KEY).toString()//标题
            type = this.getString(Constant.CATEGORY_KEY).toString()//条目中的type
        }
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        categoryDataAdapter.run {
            setEmptyView(
                LayoutInflater.from(this@RecyclerViewBitmapActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val categoryData = this.data
                Intent(this@RecyclerViewBitmapActivity, AgentWebActivity::class.java).run {
                    putExtra(Constant.CONTENT_URL_KEY, categoryData[position]?.url)
                    putExtra(Constant.CONTENT_TITLE_KEY, categoryData[position]?.title)
                    startActivity(this)
                }
            }
        }
        recyclerView.run {
            layoutManager =
                StaggeredGridLayoutManager(spanCpunt, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = categoryDataAdapter
        }
        refresh()
    }

    private fun loadMore() {
        page++
        fresh = false
        getData()
    }

    private fun refresh() {
        page = 1
        fresh = true
        getData()
    }

    private fun getData() {
        showLoadingDialog("")
        category?.let {
            type?.let { type ->
                categorypresenterImpl.getgetCategoriesData(
                    it,
                    type, page, count
                )
            }
        }
    }

    override fun onError(fail: String) {
        hideLoadingDialog()
        runOnUiThread {
            if (fresh) {
                categoryDataAdapter.setNewData(null)
                smartRefreshLayout.finishRefresh(false)
            } else {
                smartRefreshLayout.finishLoadMore(false)
            }
        }
    }

    override fun onSucess(sucess: String) {
        hideLoadingDialog()
        runOnUiThread {
            val fromJson = Gson().fromJson<CategoryDataBean>(
                sucess,
                object : TypeToken<CategoryDataBean>() {}.type
            )
            if (fromJson.status != 100) {
                return@runOnUiThread
            }
            val toMutableList = fromJson.data.toMutableList()
            if (fresh) {
                categoryDataAdapter.setNewData(toMutableList)
                smartRefreshLayout.finishRefresh(true)
            } else {
                categoryDataAdapter.addData(toMutableList)
                smartRefreshLayout.finishLoadMore(true)
            }
        }
    }
}
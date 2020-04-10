package com.ganhuo.app.ui.activity.more

import android.view.LayoutInflater
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.PhotoViewActivity
import com.ganhuo.app.adpter.brvah.more.PhotoWallAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.PhotoWallBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.help.MyFlexboxLayoutManager
import com.github.kittinunf.fuel.Fuel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_duanzi.*
import kotlinx.android.synthetic.main.activity_today.recyclerView
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-03-31 11:44
 *    desc   :照片墙
 */
class PhotoWallActivity : BaseActivity() {
    private var spanCpunt = 2//2行
    private val photoWallAdapter by lazy { PhotoWallAdapter(null) }
    private var page = 1

    override fun setLayoutId(): Int {
        return R.layout.activity_duanzi
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        toolbar.run {
            title = "福利照片"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        smartRefreshLayout.run {
            setOnRefreshListener { refresh() }
            setOnLoadMoreListener { loadMore() }
        }
        photoWallAdapter.run {
            setEmptyView(
                LayoutInflater.from(this@PhotoWallActivity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val dataPhoto = photoWallAdapter.data[position]
                val imageUrl = dataPhoto.imageUrl
                if (imageUrl.isNotEmpty())
                    startActivity<PhotoViewActivity>(Constant.PHOTO_VIEW to imageUrl)
            }
        }
        recyclerView.run {
            val fireboxLayoutManager = MyFlexboxLayoutManager(this@PhotoWallActivity)
            //设置主轴为水平方向，从左到右
            fireboxLayoutManager.flexDirection = FlexDirection.ROW
            //换行
            fireboxLayoutManager.flexWrap = FlexWrap.WRAP
            //设置副轴对齐方式
            fireboxLayoutManager.alignItems = AlignItems.STRETCH
            layoutManager = fireboxLayoutManager
            setHasFixedSize(true)
            adapter = photoWallAdapter
        }
        refresh()
    }

    private fun refresh() {
        page = 1
        getPhotoWallData(true)
    }

    private fun loadMore() {
        page++
        getPhotoWallData(false)
    }

    //照片墙
    private fun getPhotoWallData(refresh: Boolean) {
        showLoadingDialog("")
        var url = "https://www.mxnzp.com/api/image/girl/list"
        doAsync {
            val parameters = listOf("page" to page)
            Fuel.post(url, parameters)
                .header("app_id" to Constant.APP_ID)
                .header("app_secret" to Constant.APP_KEY)
                .responseString { request, response, result ->
                    result.fold({ success ->
                        hideLoadingDialog()
                        uiThread {
                            val fromJson = Gson().fromJson<PhotoWallBean>(
                                success,
                                object : TypeToken<PhotoWallBean>() {}.type
                            )
                            if (fromJson.code == 1) {
                                val data = fromJson.data.list.toMutableList()
                                if (refresh) {
                                    smartRefreshLayout.finishRefresh(true)
                                    photoWallAdapter.setNewData(data)
                                } else {
                                    smartRefreshLayout.finishLoadMore(true)
                                    photoWallAdapter.addData(data)
                                }
                            } else {
                                if (refresh) {
                                    photoWallAdapter.setNewData(null)
                                    smartRefreshLayout.finishRefresh(false)
                                } else {
                                    smartRefreshLayout.finishLoadMore(false)
                                }
                                toast(fromJson.msg)
                            }
                        }

                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            if (refresh) {
                                photoWallAdapter.setNewData(null)
                                smartRefreshLayout.finishRefresh(false)
                            } else {
                                smartRefreshLayout.finishLoadMore(false)
                            }
                            toast(err.toString())
                        }
                    })
                }
        }
    }
}
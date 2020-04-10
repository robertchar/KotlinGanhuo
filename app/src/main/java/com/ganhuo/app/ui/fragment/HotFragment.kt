package com.ganhuo.app.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.HotOneAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.HotTagBean
import com.ganhuo.app.ui.activity.HotDataActivity
import com.ganhuo.app.ui.activity.more.MoreActivity
import com.ganhuo.app.utils.LogUtils
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_hot.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.support.v4.startActivity

/**
 *    author : zkk
 *    date   : 2020-03-24 12:31
 *    desc   :热门
 *    FlexboxLayoutManager搭配RecyclerView实现流式布局的实现方式
 */
class HotFragment : BaseFragment() {
    private val hotOneAdapter by lazy { HotOneAdapter(listData) }

    override fun getLayout(): Int = R.layout.fragment_hot

    companion object {
        fun newInstance(): HotFragment {
            return HotFragment()
        }
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        //添加此句才会显示menu
        setHasOptionsMenu(true)
        toolbar.run {
            title = getString(R.string.string_hot)
            //Fragment中添加toolbar-menu
            inflateMenu(R.menu.menu_more)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuBrowser -> {
                        startActivity<MoreActivity>()
                    }
                    else -> {
                    }
                }
                true
            }
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = hotOneAdapter
        }
        hotOneAdapter.setItemClickListener(object : HotOneAdapter.onItemClick {
            override fun itemClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int,
                type: String
            ) {
                val data = adapter.data[position] as HotTagBean.Data
                LogUtils.d("hotData1:" + data.title + ",type:$type")
                startActivity<HotDataActivity>("hot_type" to type, "category" to data.category)
            }
        })
    }

    //初始化数据
    private var list = mutableListOf(
        HotTagBean.Data("专题分类", "Article"),
        HotTagBean.Data("干货分类", "GanHuo"),
        HotTagBean.Data("妹子图", "Girl")
    )
    private var listData = mutableListOf(
        HotTagBean(list, "浏览数", "views"),
        HotTagBean(list, "点赞数", "likes"),
        HotTagBean(list, "评论数", "comments")
    )

    //随机获取title
    private fun getRandomData(): String {
        val arrayListOf = arrayListOf("暑期美食满99减15", "移动空调", "莫愁前路无知己，天下谁人不识君", "牙刷", "清仓大处理")
        val size = arrayListOf.size
        return arrayListOf[(0 until size).random()]
    }

}
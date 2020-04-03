package com.ganhuo.app.adpter.brvah.more

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ganhuo.app.R
import com.ganhuo.app.bean.HotTagBean
import com.ganhuo.app.bean.NavigationBean
import com.google.android.flexbox.*

class NavigationOneAdapter(data: MutableList<NavigationBean.Data>?) :
    BaseQuickAdapter<NavigationBean.Data, BaseViewHolder>(R.layout.item_hot_one, data) {
    override fun convert(helper: BaseViewHolder, item: NavigationBean.Data) {

        val adpterTwo by lazy { NavigationTwoAdapter(item.articles.toMutableList()) }

        @Suppress("DEPRECATION")
        helper.setText(R.id.tv_title, item.name)
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_item)
        recyclerView.run {
            val flexboxLayoutManager = FlexboxLayoutManager(context)
            //设置主轴排列方式
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            //设置是否换行
            flexboxLayoutManager.flexWrap = FlexWrap.WRAP
            flexboxLayoutManager.alignItems = AlignItems.STRETCH
            //justifyContent 属性定义了项目在主轴上的对齐方式。
            flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
            //替换布局管理器FlexboxLayoutManager
            layoutManager = flexboxLayoutManager
            adapter = adpterTwo
            adpterTwo.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
                listener?.itemClick(baseQuickAdapter, view, i, item.name)
            }
        }
    }

    //自定义监听器
    var listener: onItemClick? = null

    interface onItemClick {
        fun itemClick(
            adapter: BaseQuickAdapter<*, *>,
            view: View,
            position: Int,
            type: String
        )
    }

    fun setItemClickListener(listener: onItemClick) {
        this.listener = listener
    }
}
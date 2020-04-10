package com.ganhuo.app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.ChildMagicAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.ChildMagicDataBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.presenter.TypePresenterImpl
import com.ganhuo.app.ui.activity.RecyclerViewBitmapActivity
import com.ganhuo.app.utils.LogUtils
import com.ganhuo.app.view.TypeView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.support.v4.runOnUiThread

class MagicIndicatorChildFragment : BaseFragment(), TypeView {
    private var typeKey: String? = null
    private var fresh = true
    private val childMagicAdater: ChildMagicAdapter by lazy { ChildMagicAdapter(null) }
    private val typePresenterImpl by lazy { TypePresenterImpl(this) }

    override fun getLayout(): Int = R.layout.fragment_list

    companion object {
        fun newInstance(type: String): MagicIndicatorChildFragment {
            val fragment = MagicIndicatorChildFragment()
            val args = Bundle()
            args.putString(Constant.CONTENT_TYPE_KEY, type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initData() {
        super.initData()
        typeKey = arguments?.getString(Constant.CONTENT_TYPE_KEY)
        LogUtils.d("type:" + typeKey)
        smartRefreshLayout.run {
            ////设置 Header 为 Material风格
            setRefreshHeader(MaterialHeader(activity).setShowBezierWave(true))
            //设置 Footer 为 球脉冲 样式
            setRefreshFooter(BallPulseFooter(activity).setSpinnerStyle(SpinnerStyle.Scale))
            setOnRefreshListener { fresh() }
            setOnLoadMoreListener { loadMore() }
        }
        childMagicAdater.run {
            //设置空布局
            setEmptyView(
                LayoutInflater.from(activity)
                    .inflate(R.layout.fragment_home_empty, null)
            )
            setOnItemClickListener { adapter, view, position ->
                val commonDatas = childMagicAdater.data
                Intent(activity, RecyclerViewBitmapActivity::class.java).run {
                    this.putExtra(Constant.CONTENT_TYPE_KEY, typeKey)//标题
                    this.putExtra(Constant.CATEGORY_KEY, commonDatas[position].type)//item中的type字段值
                    activity?.startActivity(this)
                }
            }
        }
        recyclerView.run {
            LogUtils.d("recyclerView:")
            layoutManager = LinearLayoutManager(activity)
            adapter = childMagicAdater
        }
        fresh()
    }

    private fun loadMore() {
        fresh = false
        getData()
    }

    private fun fresh() {
        fresh = true
        getData()
    }

    private fun getData() {
        typeKey?.let { typePresenterImpl.getCategories(it) }
    }

    override fun onTypeError(fail: String) {
        LogUtils.d("onType:$fail")
        runOnUiThread {
            if (fresh) {
                childMagicAdater.setNewData(null)
                smartRefreshLayout.finishRefresh(false);
            } else {
                smartRefreshLayout.finishLoadMore(false);
            }
        }
    }

    override fun onTypeSucess(sucess: String) {
        runOnUiThread {
            val fromJson = Gson().fromJson<ChildMagicDataBean>(
                sucess,
                object : TypeToken<ChildMagicDataBean>() {}.type
            )
            if (fromJson.status != 100) {
                return@runOnUiThread
            }
            val data = fromJson.data
            childMagicAdater.setNewData(data = data.toMutableList());
            childMagicAdater.notifyDataSetChanged()
            if (fresh) {
                smartRefreshLayout.finishRefresh(true);
            } else {
                smartRefreshLayout.finishLoadMore(true);
            }
        }
    }
}

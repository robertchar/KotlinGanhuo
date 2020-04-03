package com.ganhuo.app.ui.fragment.wan

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.viewpager.widget.ViewPager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.PublicFragmentViewPagerAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.bean.PublicTitleBean
import com.ganhuo.app.widget.ScaleTransitionPagerTitleView
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-03-31 14:55
 *    desc   :wan公众号
 */
class PublicWanFragment : BaseFragment() {
    private var mViewPager: ViewPager? = null
    private var listTitle: MutableList<PublicTitleBean.Data> = mutableListOf<PublicTitleBean.Data>()
    private val fragmentViewPagerAdapter: PublicFragmentViewPagerAdapter by lazy {
        PublicFragmentViewPagerAdapter(
            listTitle,
            activity!!.supportFragmentManager
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_public_wan
    }

    companion object {
        fun newInstance(): PublicWanFragment {
            return PublicWanFragment()
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    override fun initData() {
        super.initData()
        //title
        getPublicTitle()
    }

    private fun magic() {
        mViewPager = find<ViewPager>(R.id.view_pager)
        mViewPager?.run {
            adapter = fragmentViewPagerAdapter
        }
        setMagicIndicator()
    }

    //初始化 MagicIndicator
    private fun setMagicIndicator() {
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = Html.fromHtml(listTitle[index].name)
                    textSize = 20f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener { mViewPager?.setCurrentItem(index, false) }
                }
            }

            override fun getCount(): Int {
                return listTitle.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                    lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
                    roundRadius = UIUtil.dip2px(context, 6.0).toFloat()
                    startInterpolator = AccelerateInterpolator()
                    endInterpolator = DecelerateInterpolator(2.0f)
                    setColors(Color.WHITE)
                }
            }

        }
        magicIndicator.navigator = commonNavigator
        //work with ViewPager:
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private fun getPublicTitle() {
        showLoadingDialog()
        doAsync {
            Fuel.get("https://wanandroid.com/wxarticle/chapters/json")
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        hideLoadingDialog()
                        val fromJson =
                            Gson().fromJson<PublicTitleBean>(
                                sucess,
                                object : TypeToken<PublicTitleBean>() {}.type
                            )
                        uiThread {
                            if (fromJson.errorCode != 0) {
                                return@uiThread
                            }
                            listTitle = fromJson.data.toMutableList()
                            fragmentViewPagerAdapter.notifyDataSetChanged()
                            //magic设置
                            magic()
                        }
                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            toast(err.toString())
                        }

                    })
                }
        }
    }
}
package com.ganhuo.app.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Html
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.TimeUtils
import com.ganhuo.app.R
import com.ganhuo.app.adpter.FragmentViewPagerAdapter
import com.ganhuo.app.adpter.ImageAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.base.long
import com.ganhuo.app.base.string
import com.ganhuo.app.bean.BannerBean
import com.ganhuo.app.bean.TypeBean
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.presenter.HomePresenterImpl
import com.ganhuo.app.ui.activity.AgentWebActivity
import com.ganhuo.app.utils.LogUtils
import com.ganhuo.app.utils.MmkvCacheUtil
import com.ganhuo.app.view.BaseView
import com.ganhuo.app.widget.ScaleTransitionPagerTitleView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import com.tencent.mmkv.MMKV
import com.youth.banner.config.BannerConfig
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.util.BannerUtils
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
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.uiThread


/**
 *    author : zkk
 *    date   : 2020-03-24 12:31
 *    desc   :首页
 */
class HomeFragment() : BaseFragment(), BaseView {
    private val homePresenterImpl by lazy { HomePresenterImpl(this) }
    private val imageAdapter by lazy { ImageAdapter(null) }

    private val mmkv: MMKV by lazy { MMKV.defaultMMKV() }
    private var ganBannerString by mmkv.string("gan", "")
    private var timeFresh by mmkv.long("timeFresh", 0)

    private var mViewPager: ViewPager? = null
    private val list = mutableListOf(
        TypeBean("专题分类", "Article"),
        TypeBean("干货分类", "GanHuo"),
        TypeBean("妹子圈", "Girl")
    )
    private val listData = mutableListOf<String>()
    private val fragmentViewPagerAdapter: FragmentViewPagerAdapter by lazy {
        FragmentViewPagerAdapter(
            list,
            activity!!.supportFragmentManager
        )
    }

    override fun getLayout(): Int = R.layout.fragment_main
    override fun cancelRequest() {
        super.cancelRequest()
        homePresenterImpl.cancelHomeRequest()
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun initData() {
        super.initData()
        banner.run {
            adapter = imageAdapter
            indicator = CircleIndicator(activity)
//            setIndicatorSelectedColorRes(R.color.white)
//            setIndicatorNormalColorRes(R.color.black)
//            setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
//            setIndicatorSpace(BannerUtils.dp2px(20F))
            setIndicatorMargins(
                IndicatorConfig.Margins(
                    0, 0,
                    BannerConfig.INDICATOR_MARGIN, BannerUtils.dp2px(12f).toInt()
                )
            )
//            setIndicatorWidth(10, 20)
            setOnBannerListener(object : OnBannerListener<BannerBean.Data> {
                override fun onBannerChanged(position: Int) {
                }

                override fun OnBannerClick(data: BannerBean.Data?, position: Int) {
                    Intent(activity, AgentWebActivity::class.java).run {
                        putExtra(Constant.CONTENT_URL_KEY, data?.url)
                        putExtra(Constant.CONTENT_TITLE_KEY, data?.title)
                        startActivity(this)
                    }
                }
            })
        }
        //获得缓存
        getCashBanner()
        //magic
        magic()
    }

    private fun getCashBanner() {
        if (ganBannerString.isEmpty() || TimeUtils.isToday(timeFresh)) {
            getBannerData()
        } else {
            val fromJson =
                Gson().fromJson<BannerBean>(
                    ganBannerString,
                    object : TypeToken<BannerBean>() {}.type
                )
            val data = fromJson.data.toMutableList()
            imageAdapter.setDatas(data)
            imageAdapter.notifyDataSetChanged()
        }
    }

    private fun magic() {
        mViewPager = find<ViewPager>(R.id.view_pager)
        listData.clear()
        listData.add("Article")
        listData.add("GanHuo")
        listData.add("Girl")
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
                    text = Html.fromHtml(list[index].title)
                    textSize = 20f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener { mViewPager?.setCurrentItem(index, false) }
                }
            }

            override fun getCount(): Int {
                return list.size
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


    private fun getBannerData() {
        homePresenterImpl.getBanner()
        timeFresh = System.currentTimeMillis()
    }

    override fun onError(fail: String) {
        runOnUiThread {
            imageAdapter.setDatas(null)
        }
    }

    override fun onSucess(sucess: String) {
        runOnUiThread {
            val fromJson =
                Gson().fromJson<BannerBean>(sucess, object : TypeToken<BannerBean>() {}.type)
            if (fromJson.status != 100) {
                return@runOnUiThread
            }
            ganBannerString = sucess
            val data = fromJson.data.toMutableList()
            imageAdapter.setDatas(data)
            imageAdapter.notifyDataSetChanged()
        }

    }

    override fun onStart() {
        super.onStart()
        //开始轮播
        banner.start()
    }

    override fun onStop() {
        super.onStop()
        //结束轮播
        banner.stop()
    }

}
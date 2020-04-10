package com.ganhuo.app.ui.fragment.wan

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.viewpager.widget.ViewPager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.FragmentsViewPagerAdapter
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.ui.fragment.wan.knowledge.SortWanFragment
import com.ganhuo.app.ui.fragment.wan.knowledge.SquareWanFragment
import com.ganhuo.app.ui.fragment.wan.knowledge.SystemWanFragment
import com.ganhuo.app.ui.fragment.wan.knowledge.TopWanFragment
import com.ganhuo.app.widget.ScaleTransitionPagerTitleView
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.jetbrains.anko.support.v4.find

/**
 *    author : zkk
 *    date   : 2020-03-31 14:55
 *    desc   :wan体系
 */
class KnowledgeWanFragment : BaseFragment() {
    private var mViewPager: ViewPager? = null
    private val fragments = arrayListOf(
        SystemWanFragment.newInstance(),
        SquareWanFragment.newInstance(),
        SortWanFragment.newInstance(),
        TopWanFragment.newInstance()
    )
    private val fragmentsTitle = arrayListOf(
        "知识体系",
        "广场",
        "项目分类",
        "置顶"
    )
    private val fragmentViewPagerAdapter: FragmentsViewPagerAdapter by lazy {
        FragmentsViewPagerAdapter(
            fragments,
            activity!!.supportFragmentManager
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_knoeledge_wan
    }

    companion object {
        fun newInstance(): KnowledgeWanFragment {
            return KnowledgeWanFragment()
        }
    }

    override fun initData() {
        super.initData()
        //magic
        magic()
    }

    private fun magic() {
        mViewPager = find<ViewPager>(R.id.viewpager)
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
                    text = Html.fromHtml(fragmentsTitle[index])
                    textSize = 20f
                    normalColor = Color.GRAY
                    selectedColor = Color.BLUE
                    setOnClickListener { mViewPager?.setCurrentItem(index, false) }
                }
            }

            override fun getCount(): Int {
                return fragmentsTitle.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                    lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
                    roundRadius = UIUtil.dip2px(context, 6.0).toFloat()
                    startInterpolator = AccelerateInterpolator()
                    endInterpolator = DecelerateInterpolator(2.0f)
                    setColors(Color.RED)
                }
            }

        }
        magicIndicator.navigator = commonNavigator
        //work with ViewPager:
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }
}
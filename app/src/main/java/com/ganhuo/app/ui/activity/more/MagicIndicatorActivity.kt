package com.ganhuo.app.ui.activity.more

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.ganhuo.app.R
import com.ganhuo.app.adpter.brvah.more.FragmentsViewPagerAdapter
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.bean.SystemTiteData
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ui.fragment.wan.knowledge.SystemMagicIndicatorChildFragment
import com.ganhuo.app.widget.ScaleTransitionPagerTitleView
import kotlinx.android.synthetic.main.activity_magic_indicator.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.jetbrains.anko.find

class MagicIndicatorActivity : BaseActivity() {
    private var mViewPager: ViewPager? = null
    private var data: SystemTiteData.Data? = null
    private val list = mutableListOf<Fragment>()
    private val typeArticlePagerAdapter: FragmentsViewPagerAdapter by lazy {
        FragmentsViewPagerAdapter(
            list,
            supportFragmentManager
        )
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_magic_indicator
    }

    override fun initData() {
        super.initData()
        mViewPager = find<ViewPager>(R.id.view_pager)
        initExtras()
        setMagicIndicator()
    }

    private fun initExtras() {
        intent.extras?.let { extras ->
            extras.getSerializable(Constant.CONTENT_CHILDREN_DATA_KEY)?.let {
                data = it as SystemTiteData.Data
                data!!.children.forEach {
                    list.add(SystemMagicIndicatorChildFragment.newInstance(it.id))
                }
            }
        }
        mViewPager?.run {
            adapter = typeArticlePagerAdapter
        }
    }

    //初始化 MagicIndicator
    private fun setMagicIndicator() {
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(this@MagicIndicatorActivity).apply {
                    text = Html.fromHtml(data?.children?.get(index)?.name)
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

    override fun cancelRequest() {

    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(magicIndicator).init()
    }
}

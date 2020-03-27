package com.ganhuo.app.adpter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.ganhuo.app.bean.TypeBean
import com.ganhuo.app.ui.fragment.MagicIndicatorChildFragment
import com.ganhuo.app.utils.LogUtils

/**
 *    author : zkk
 *    date   : 2020-03-17 13:19
 *    desc   :FragmentStatePagerAdapter,只保留当前页面，当页面离开视线后，就会被消除
 */
class FragmentViewPagerAdapter(val list: MutableList<TypeBean>, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    private val listFragment = mutableListOf<Fragment>()

    init {
        list.forEach { listFragment.add(MagicIndicatorChildFragment.newInstance(it.type)) }
    }

    override fun getItem(position: Int): Fragment = listFragment[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence = list[position].title

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
}
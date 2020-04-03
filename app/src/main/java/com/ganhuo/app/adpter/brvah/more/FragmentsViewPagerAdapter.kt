package com.ganhuo.app.adpter.brvah.more

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

/**
 *    author : zkk
 *    date   : 2020-03-17 13:19
 *    desc   :FragmentStatePagerAdapter,只保留当前页面，当页面离开视线后，就会被消除
 */
class FragmentsViewPagerAdapter(
    val fragments: List<Fragment>,
    fragmentManager: FragmentManager
) :
    FragmentStatePagerAdapter(
        fragmentManager,
        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

}
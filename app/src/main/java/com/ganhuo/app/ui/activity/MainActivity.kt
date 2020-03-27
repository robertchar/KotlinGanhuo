package com.ganhuo.app.ui.activity

import android.Manifest
import androidx.fragment.app.Fragment
import com.ganhuo.app.R
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.ui.fragment.HomeFragment
import com.ganhuo.app.ui.fragment.HotFragment
import com.ganhuo.app.ui.fragment.MarrowFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    //    //fragment不能一直newInstance创建，保证唯一
    private var fragments: Array<BaseFragment> =
        arrayOf(
            HomeFragment.newInstance(),
            MarrowFragment.newInstance(),
            HotFragment.newInstance()
        )

    override fun setLayoutId(): Int = R.layout.activity_main

    override fun initData() {
        super.initData()
        remission()//权限
        navigation.run {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }
        //设置fragment到布局
        switchFragment(fragments[0])
    }

    private fun remission() {
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe {

        }
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.statusBarDarkFont(false).init()
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            return@OnNavigationItemSelectedListener when (it.itemId) {
                R.id.main -> {
                    switchFragment(fragments[0])
                    true
                }
                R.id.search -> {
                    switchFragment(fragments[1])
                    true
                }
                R.id.hot -> {
                    switchFragment(fragments[2])
                    true
                }
                else -> false
            }
        }

    //切换
    var mBeforeFragment: Fragment = Fragment()

    private fun switchFragment(currentFragment: Fragment) {
        if (currentFragment.isAdded) {
            supportFragmentManager.beginTransaction().hide(mBeforeFragment)
                .show(currentFragment).commit()
        } else {
            supportFragmentManager.beginTransaction().hide(mBeforeFragment)
                .add(R.id.container, currentFragment).commit()
        }
        mBeforeFragment = currentFragment
    }
}

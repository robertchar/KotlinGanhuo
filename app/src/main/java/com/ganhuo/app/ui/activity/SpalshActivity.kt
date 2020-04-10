package com.ganhuo.app.ui.activity

import android.content.Intent
import android.view.KeyEvent
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.ganhuo.app.R
import com.ganhuo.app.base.BaseActivity
import com.gyf.immersionbar.BarHide
import kotlinx.android.synthetic.main.activity_splash.*

/**
 *    author : zkk
 *    date   : 2020-03-05 11:38
 *    desc   :引导页
 */
class SpalshActivity : BaseActivity() {
    override fun setLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun cancelRequest() {

    }

    override fun initData() {
        animation()
    }

    private fun animation() {
        val alphaAnimation = AlphaAnimation(0.5f, 1.0f)
        alphaAnimation.duration = 800
        rl_splash.startAnimation(alphaAnimation)
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                redirectTo()
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
    }

    fun redirectTo(): Unit {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.hideBar(BarHide.FLAG_HIDE_BAR).init()//隐藏状态栏或导航栏或两者，不写默认不隐藏
    }

    //Android禁用返回键
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return if (event?.keyCode == KeyEvent.KEYCODE_BACK) true else
            super.dispatchKeyEvent(event)
    }
}
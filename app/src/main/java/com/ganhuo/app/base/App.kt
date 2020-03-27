package com.ganhuo.app.base

import android.annotation.SuppressLint
import android.content.ComponentCallbacks2
import android.os.Build
import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.bumptech.glide.Glide
import com.getkeepsafe.relinker.ReLinker
import com.kotlin.app.ext.DelegatesExt
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits

/**
 *    author : zkk
 *    date   : 2020-03-24 10:35
 *    desc   :
 */
class App : MultiDexApplication() {
    companion object {
        //自定义委托模式，只实例化一次
        var instance: App by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //第三方库初始化
        thirdLibInit()
    }

    private fun thirdLibInit() {
        //SharedPreferences
        Preference.setContext(instance)
        //奔溃页面
        customActivityonCrash()
        //mmkv
        mmkv()
        //今日头条适配
        initAutoSize()
    }

    //今日头条适配
    private fun initAutoSize() {
        AutoSizeConfig.getInstance()
            .setBaseOnWidth(true)
            .unitsManager
            .setSupportDP(false)
            .setSupportSP(false)
            .supportSubunits = Subunits.MM
    }

    private fun mmkv() {
        //初始化MMKV
        val dir = filesDir.absolutePath + "/mmkv"
        if (Build.VERSION.SDK_INT == 19) {
            MMKV.initialize(
                dir
            ) { libName -> ReLinker.loadLibrary(instance, libName) }
        } else {
            MMKV.initialize(dir)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun customActivityonCrash() {
        //如果没有任何配置，程序崩溃显示的是默认的设置
        CustomActivityOnCrash.install(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // low memory clear Glide cache
        Glide.get(instance).clearMemory()
    }

    //释放资源
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        // clear Glide cache
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(instance).clearMemory()
        }
        // trim memory
        Glide.get(instance).trimMemory(level)
    }
}
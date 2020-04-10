package com.ganhuo.app.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.ganhuo.app.R
import com.ganhuo.app.widget.WeiboDialogUtils
import com.gyf.immersionbar.ImmersionBar

/**
 *    author : zkk
 *    date   : 2020-03-24 11:49
 *    desc   :基类
 */
abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var immersionBar: ImmersionBar
    private val imm: InputMethodManager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayoutId())
        initImmersionBar()
        initData()
    }

    open fun initImmersionBar() {
        //在BaseActivity里初始化
        immersionBar = ImmersionBar.with(this)
        immersionBar
            .transparentNavigationBar()     //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为 true)
            .navigationBarDarkIcon(true)//导航栏图标是深色，不写默认为亮色
            .fullScreen(false)////有导航栏的情况下，true则为activity 全屏显示
            .init()
    }

    abstract fun setLayoutId(): Int

    open fun initData() {}
    override fun onDestroy() {
        super.onDestroy()
        //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        cancelRequest()
    }

    protected open fun cancelRequest() {

    }

    override fun finish() {
        if (!isFinishing) {
            // if not finish
            super.finish()
            hideSoftKeyBoard()
        }
    }

    private fun hideSoftKeyBoard() {
        currentFocus?.let { imm.hideSoftInputFromWindow(it.windowToken, 2) }
    }

    fun showLoadingDialog(msg: String) {
        if (loadingDialog == null) {
            loadingDialog =
                WeiboDialogUtils.createLoadingDialog(
                    this,
                    if (TextUtils.isEmpty(msg)) {
                        getString(R.string.string_loading)
                    } else msg
                )
        }
    }

    fun showLoadingDialog() {
        showLoadingDialog("")
    }


    fun hideLoadingDialog() {
        if (loadingDialog != null)
            WeiboDialogUtils.closeDialog(loadingDialog!!)
    }

}
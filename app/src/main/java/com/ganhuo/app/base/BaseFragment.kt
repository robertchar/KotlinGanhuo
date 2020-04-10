package com.ganhuo.app.base

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ganhuo.app.R
import com.ganhuo.app.widget.WeiboDialogUtils
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.ImmersionFragment

/**
 *    author : zkk
 *    date   : 2020-03-05 14:15
 *    desc   :基类（fragment沉浸式状态栏,具体方法看github官网）
 */
abstract class BaseFragment : ImmersionFragment() {
    protected var isFirst: Boolean = true
    private var loadingDialog: Dialog? = null
    private var mView: View? = null
    private var isInited: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mView ?: let { inflater.inflate(getLayout(), container, false) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            if (!isHidden) {
                isInited = true
                initFreshData()
            }
        } else {
            if (!isHidden) {
                isInited = true
                initFreshData()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        onHiddenChange(hidden)
        if (!isInited && !hidden) {
            isInited = true
            initFreshData()
        }
    }

    open fun onHiddenChange(hidden: Boolean) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelRequest()
    }

    open fun cancelRequest() {}
    abstract fun getLayout(): Int
    open fun initData() {

    }

    open fun initFreshData() {

    }

    fun showLoadingDialog(msg: String) {
        if (loadingDialog == null) {
            loadingDialog =
                context?.let {
                    WeiboDialogUtils.createLoadingDialog(
                        it,
                        if (TextUtils.isEmpty(msg)) {
                            getString(R.string.string_loading)
                        } else msg
                    )
                }
        }
    }

    fun showLoadingDialog() {
        showLoadingDialog("")
    }

    fun hideLoadingDialog() {
        if (loadingDialog != null)
            WeiboDialogUtils.closeDialog(loadingDialog!!)
    }
    protected lateinit var immersionBar: ImmersionBar
    override fun initImmersionBar() {
        immersionBar= ImmersionBar.with(this)
        immersionBar .statusBarDarkFont(true)
            .transparentNavigationBar()    //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为 true)
            .navigationBarDarkIcon(true)//导航栏图标是深色，不写默认为亮色
            .fullScreen(false)//有导航栏的情况下，true则为activity 全屏显示
            .init();
    }
}
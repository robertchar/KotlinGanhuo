package com.ganhuo.app.presenter

import com.ganhuo.app.model.HomeModelImpl
import com.ganhuo.app.view.BaseView

/**
 *    author : zkk
 *    date   : 2020-03-24 13:39
 *    desc   :实现类
 */
class HomePresenterImpl(var view: BaseView) : BasePresenter {
    private val homeModelImpl by lazy { HomeModelImpl() }
    fun getBanner() {
        homeModelImpl.getBanner(this)
    }

    fun cancelHomeRequest() {
        homeModelImpl.cancelHomeRequest()
    }

    override fun onError(fail: String) {
        view.onError(fail)
    }

    override fun onSuccess(sucess: String) {
        view.onSucess(sucess)
    }
}
package com.ganhuo.app.presenter

import com.ganhuo.app.model.HotModelImpl
import com.ganhuo.app.view.BaseView

/**
 *    author : zkk
 *    date   : 2020-03-25 10:17
 *    desc   :热门
 */
class HotpresenterImpl(var view: BaseView) : BasePresenter {
    private val hotModelImpl by lazy { HotModelImpl() }
    fun getgetCategoriesData(
        hot_type: String,
        category: String,
        count: Int
    ) {
        hotModelImpl.getHotData(this, hot_type, category, count)
    }

    fun cancelRequest() {
        hotModelImpl.cancelRequest()
    }

    override fun onError(fail: String) {
        view.onError(fail)
    }

    override fun onSuccess(sucess: String) {
        view.onSucess(sucess)
    }
}
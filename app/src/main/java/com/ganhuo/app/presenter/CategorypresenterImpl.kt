package com.ganhuo.app.presenter

import com.ganhuo.app.model.CategoryModelImpl
import com.ganhuo.app.view.BaseView

/**
 *    author : zkk
 *    date   : 2020-03-25 10:17
 *    desc   :
 */
class CategorypresenterImpl(var view: BaseView) : BasePresenter {
    private val categoryModelImpl by lazy { CategoryModelImpl() }
    fun getgetCategoriesData(
        category: String,
        type: String,
        page: Int,
        count: Int
    ) {
        categoryModelImpl.getgetCategoriesData(this, category, type, page, count)
    }

    fun cancelRequest() {
        categoryModelImpl.cancelRequest()
    }

    override fun onError(fail: String) {
        view.onError(fail)
    }

    override fun onSuccess(sucess: String) {
        view.onSucess(sucess)
    }
}
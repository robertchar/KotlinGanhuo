package com.ganhuo.app.presenter

import com.ganhuo.app.model.HomeModelImpl
import com.ganhuo.app.model.TypeModelImpl
import com.ganhuo.app.view.BaseView
import com.ganhuo.app.view.TypeView

/**
 *    author : zkk
 *    date   : 2020-03-24 13:39
 *    desc   :实现类
 */
class TypePresenterImpl(var view: TypeView) : BasePresenter {
    private val typeModelImpl by lazy { TypeModelImpl() }

    fun getCategories(type: String) {
        typeModelImpl.getCategories(this, type)
    }

    fun cancelHomeRequest() {
        typeModelImpl.cancelHomeRequest()
    }

    override fun onError(fail: String) {
        view.onTypeError(fail)
    }

    override fun onSuccess(sucess: String) {
        view.onTypeSucess(sucess)
    }
}
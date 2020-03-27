package com.ganhuo.app.model

import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ext.cancelByActive
import com.ganhuo.app.ext.tryCatch
import com.ganhuo.app.presenter.BasePresenter
import com.ganhuo.app.presenter.HomePresenterImpl
import com.ganhuo.app.presenter.TypePresenterImpl
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

/**
 *    author : zkk
 *    date   : 2020-03-24 13:42
 *    desc   :
 */
class TypeModelImpl : BaseModel {
    private var categories: Deferred<String>? = null

    fun getCategories(listener: TypePresenterImpl, type: String) {
        tryCatch({
            listener.onError(it.toString())
            it.printStackTrace()
        }) {
            GlobalScope.async {
                categories?.cancelByActive()
                categories = RetrofitHelper.retrofitService.getCategories(type)
                val await = categories?.await()
                await ?: let {
                    listener.onError(Constant.REQUEST_BULL)
                    return@async
                }
                listener.onSuccess(await)
            }
        }
    }

    fun cancelHomeRequest() {
        categories?.cancelByActive()
    }
}
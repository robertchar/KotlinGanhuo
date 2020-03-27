package com.ganhuo.app.model

import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ext.cancelByActive
import com.ganhuo.app.ext.tryCatch
import com.ganhuo.app.presenter.BasePresenter
import com.ganhuo.app.presenter.HomePresenterImpl
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

/**
 *    author : zkk
 *    date   : 2020-03-24 13:42
 *    desc   :
 */
class HomeModelImpl : BaseModel {
    private var banner: Deferred<String>? = null

    fun getBanner(listener: HomePresenterImpl) {
        tryCatch({
            listener.onError(it.toString())
            it.printStackTrace()
        }) {
            GlobalScope.async {
                banner?.cancelByActive()
                banner = RetrofitHelper.retrofitService.getBanner()
                val await = banner?.await()
                await ?: let {
                    listener.onError(Constant.REQUEST_BULL)
                    return@async
                }
                listener.onSuccess(await)
            }
        }
    }

    fun cancelHomeRequest() {
        banner?.cancelByActive()
    }
}
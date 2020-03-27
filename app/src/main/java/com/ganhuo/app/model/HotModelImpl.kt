package com.ganhuo.app.model

import RetrofitHelper
import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ext.cancelByActive
import com.ganhuo.app.ext.tryCatch
import com.ganhuo.app.presenter.HotpresenterImpl
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

/**
 *    author : zkk
 *    date   : 2020-03-25 10:15
 *    desc   :
 */
class HotModelImpl : BaseModel {
    private var hot: Deferred<String>? = null

    fun getHotData(
        listener: HotpresenterImpl,
        hot_type: String,
        category: String,
        count: Int
    ) {
        tryCatch({
            listener.onError(it.toString())
            it.printStackTrace()
        }) {
            GlobalScope.async {
                hot?.cancelByActive()
                hot =
                    RetrofitHelper.retrofitService.getHotData(hot_type, category, count)
                val await = hot?.await()
                await ?: let {
                    listener.onError(Constant.REQUEST_BULL)
                    return@async
                }
                listener.onSuccess(await)
            }
        }
    }

    fun cancelRequest() {
        hot?.cancelByActive()
    }
}
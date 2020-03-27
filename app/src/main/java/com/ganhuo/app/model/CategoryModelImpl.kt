package com.ganhuo.app.model

import com.ganhuo.app.constant.Constant
import com.ganhuo.app.ext.cancelByActive
import com.ganhuo.app.ext.tryCatch
import com.ganhuo.app.presenter.CategorypresenterImpl
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

/**
 *    author : zkk
 *    date   : 2020-03-25 10:15
 *    desc   :
 */
class CategoryModelImpl : BaseModel {
    private var categoryDef: Deferred<String>? = null

    fun getgetCategoriesData(
        listener: CategorypresenterImpl,
        category: String,
        type: String,
        page: Int,
        count: Int
    ) {
        tryCatch({
            listener.onError(it.toString())
            it.printStackTrace()
        }) {
            GlobalScope.async {
                categoryDef?.cancelByActive()
                categoryDef =
                    RetrofitHelper.retrofitService.getgetCategoriesData(category, type, page, count)
                val await = categoryDef?.await()
                await ?: let {
                    listener.onError(Constant.REQUEST_BULL)
                    return@async
                }
                listener.onSuccess(await)
            }
        }
    }

    fun cancelRequest() {
        categoryDef?.cancelByActive()
    }
}
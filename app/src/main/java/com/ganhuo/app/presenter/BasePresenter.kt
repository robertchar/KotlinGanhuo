package com.ganhuo.app.presenter

/**
 *    author : zkk
 *    date   : 2020-03-24 13:39
 *    desc   :presenter接口层
 */
interface BasePresenter {
    fun onError(fail: String)
    fun onSuccess(sucess: String)
}
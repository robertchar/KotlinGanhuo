package com.ganhuo.app.view

/**
 *    author : zkk
 *    date   : 2020-03-24 13:35
 *    desc   :view层接口
 */
interface BaseView {
    fun onError(fail: String)
    fun onSucess(sucess: String)
}
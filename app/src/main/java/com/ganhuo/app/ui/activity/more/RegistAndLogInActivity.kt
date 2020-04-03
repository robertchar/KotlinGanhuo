package com.ganhuo.app.ui.activity.more

import com.ganhuo.app.R
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.base.boolean
import com.ganhuo.app.base.string
import com.ganhuo.app.bean.LoginBean
import com.ganhuo.app.utils.LogUtils
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_regist_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

/**
 *    author : zkk
 *    date   : 2020-04-02 15:19
 *    desc   :登陆注册页面
 */
class RegistAndLogInActivity : BaseActivity() {
    private val mmkv: MMKV by lazy { MMKV.defaultMMKV() }
    private var isLogin by mmkv.boolean("isLogin", false)
    private var userName by mmkv.string("userName", "")

    override fun setLayoutId(): Int {
        return R.layout.activity_regist_login
    }

    override fun initData() {
        super.initData()
        btn_login.setOnClickListener {
            login()
        }
        btn_reg.setOnClickListener {
            resist()
        }
    }

    //注册
    private fun resist() {
        val user = edt_user.text.toString().trim()
        val pass = edt_pwd.text.toString().trim()
        if (user.isEmpty()) {
            edt_user.error = "账户不能为空"
            return
        } else if (pass.isEmpty()) {
            edt_pwd.error = "密码不能为空"
            return
        }
        showLoadingDialog()
        doAsync {
            val parameters = listOf("username" to user, "password" to pass, "repassword" to pass)
            Fuel.post("https://www.wanandroid.com/user/register", parameters)
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        hideLoadingDialog()
                        val fromJson = Gson().fromJson<LoginBean>(
                            sucess,
                            object : TypeToken<LoginBean>() {}.type
                        )
                        uiThread {
                            LogUtils.d("sucess:${sucess}")
                            if (fromJson.errorCode != 0) {
                                toast(fromJson.errorMsg)
                            } else {
                                toast("注册成功")
                            }
                        }

                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            toast(err.toString())
                            err.printStackTrace()
                        }
                    })
                }
        }
    }

    //登录
    private fun login() {
        val user = edt_user.text.toString().trim()
        val pass = edt_pwd.text.toString().trim()
        if (user.isEmpty()) {
            edt_user.error = "账户不能为空"
            return
        } else if (pass.isEmpty()) {
            edt_pwd.error = "密码不能为空"
            return
        }
        showLoadingDialog()
        doAsync {
            val parameters = listOf("username" to user, "password" to pass)
            Fuel.post("https://www.wanandroid.com/user/login", parameters)
                .responseString { request, response, result ->
                    result.fold({ sucess ->
                        hideLoadingDialog()
                        val fromJson = Gson().fromJson<LoginBean>(
                            sucess,
                            object : TypeToken<LoginBean>() {}.type
                        )
                        uiThread {
                            LogUtils.d("sucess:${sucess}")
                            if (fromJson.errorCode != 0) {
                                toast(fromJson.errorMsg)
                            } else {
                                toast("登录成功")
                                isLogin = true
                                userName = fromJson.data.username
                                finish()
                            }
                        }

                    }, { err ->
                        hideLoadingDialog()
                        uiThread {
                            toast(err.toString())
                            err.printStackTrace()
                        }
                    })
                }
        }
    }
}
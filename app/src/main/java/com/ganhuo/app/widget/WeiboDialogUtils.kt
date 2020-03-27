package com.ganhuo.app.widget

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.ganhuo.app.R
import org.jetbrains.anko.find

/**
 *    author : zkk
 *    date   : 2020-03-06 11:24
 *    desc   :加载dialog
 */
object WeiboDialogUtils {
    fun createLoadingDialog(ctx: Context, msg: String): Dialog {
        val inflate = LayoutInflater.from(ctx).inflate(R.layout.dialog_loading, null)
        val layout = inflate.find<LinearLayout>(R.id.dialog_loading_view)
        val tipTextView = inflate.find<TextView>(R.id.tipTextView)
        tipTextView.text = msg
        val dialog = Dialog(ctx, R.style.SelfLoadingDialogStyle)
        dialog.setCancelable(true)// 是否可以按“返回键”消失
        dialog.setCanceledOnTouchOutside(false)//点击加载框以外的区域
        dialog.setContentView(
            layout,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        val window = dialog.window
        val attributes = window?.attributes
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.setGravity(Gravity.CENTER)
        window?.attributes = attributes
        dialog.show()
        return dialog
    }

    //关闭dialog
    fun closeDialog(dialog: Dialog) {
        if (dialog.isShowing)
            dialog.dismiss()
    }
}
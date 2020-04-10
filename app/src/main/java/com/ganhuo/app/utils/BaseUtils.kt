package com.ganhuo.app.utils

import android.os.Build
import androidx.appcompat.app.AppCompatActivity

/**
 *    author : zkk
 *    date   : 2020-04-08 12:06
 *    desc   :
 */
fun isDestroy(mActivity: AppCompatActivity): Boolean {
    return mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())
}
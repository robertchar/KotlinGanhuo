package com.kotlin.app.ext

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 *    author : zkk
 *    date   : 2020-03-03 15:01
 *    desc   :
 */
object DelegatesExt {
    fun <T> notNullSingleValue():
            ReadWriteProperty<Any?, T> = NotNullSingleValueVar()

}

//创建一个 notNull 的委托，它只能被赋值一次，如果第二
//次赋值，它就会抛异常
class NotNullSingleValueVar<T>() : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalAccessException("${property.name}not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value =
            if (this.value == null) value else throw IllegalAccessException("${property.name}already initialized")
    }
}
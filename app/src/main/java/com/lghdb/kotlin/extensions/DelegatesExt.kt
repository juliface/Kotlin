package com.lghdb.kotlin.extensions

import android.content.Context
import android.content.SharedPreferences
import java.lang.IllegalArgumentException
import kotlin.reflect.KProperty

/**
 * Created by lghdb on 2018/3/28.
 */
object DelegatesExt{
    fun <T> notNullSingleValue() = NotNullSingleValueVar<T>()

    fun <T> preference(context: Context,name: String, default: T) = Preference(context, name, default)
}

class NotNullSingleValueVar<T>{
    private var value: T? = null

    operator fun getValue(thisRef: Any?,
                          property: KProperty<*>): T
            = value ?: throw  IllegalStateException("${property.name} not initialized")

    operator fun setValue(thisRef: Any?,
                          property: KProperty<*>,
                          value: T){
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} already initialized")
    }
}

/**
 * 属性委托
 */
class Preference<T>(val context: Context, val name:String, val default:T) {

    private val prefs:SharedPreferences by lazy {
        context.getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T  = findPreference(name, default)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun <T> findPreference(name: String, default: T): T = with(prefs){
        val res:Any = when(default){
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("不支持该类型")
        }
        res as T
    }

    private fun <U> putPreference(name: String, value: U) = with(prefs.edit()){
        when(value){
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("不支持保存该类型")
        }.apply()
    }


}
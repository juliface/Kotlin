package com.lghdb.kotlin.extensions

/**
 * Created by lghdb on 2018/3/29.
 */

fun <K, V : Any> Map<K,V?>.toVarargArray(): Array<out Pair<K,V>>
        = map({ Pair(it.key, it.value!!) }).toTypedArray()


inline fun <T , R : Any> Iterable<T>.firstResult(predicate: (T) -> R?): R{
    for (element in this){
        val result = predicate(element)
        if (result != null) return result
    }
    throw NoSuchElementException("函数pred没有找到!")
}
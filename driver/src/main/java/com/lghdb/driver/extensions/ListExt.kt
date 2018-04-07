package com.lghdb.driver.extensions

/**
 * Created by lghdb on 2018/4/5.
 */
fun <T> MutableList<T>.removeButFirst(){

    if (size > 1){
        var t = this[0]
        removeAll(this)
        this.add(0, t)
    }
}

fun <K,V> MutableMap<K,V>.each(f:(K,V) -> Unit){
    for ((k,v) in this) f(k,v)
}

fun <K,V> MutableMap<K,V>.keyAt(index:Int): K?{
    var i = 0
    for((k,v) in this){
        if (i == index) return k
        i ++
    }
    return null
}
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
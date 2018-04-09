package com.lghdb.driver.extensions

/**
 * Created by lghdb on 2018/4/8.
 */
fun Int.getDistance():String
        = when(this){
            in 0..1000 -> {"${toString()}米"}
            else -> {
                val remainder = this % 1000
                val m = if (remainder > 0)  "${toString()}米" else ""
                "${(this / 1000).toString()}公里${m}"
            }
        }


fun getLength(allLength:Int):String{
    if (allLength > 1000) {
        val remainder = allLength % 1000
        val m = if (remainder > 0) remainder.toString() + "米" else ""
        return (allLength / 1000).toString() + "公里" + m
    } else {
        return allLength.toString() + "米"
    }
}
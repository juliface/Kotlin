package com.lghdb.kotlin.extensions

import java.text.DateFormat
import java.util.*

/**
 * Created by lghdb on 2018/3/30.
 */
fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM):String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this)
}
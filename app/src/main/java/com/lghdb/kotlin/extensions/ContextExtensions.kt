package com.lghdb.kotlin.extensions

import android.content.Context
import android.support.v4.content.ContextCompat

/**
 * Created by lghdb on 2018/3/30.
 */
fun Context.color(res: Int): Int = ContextCompat.getColor(this, res)
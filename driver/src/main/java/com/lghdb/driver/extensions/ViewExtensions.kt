package com.lghdb.driver.extensions

import android.content.Context
import android.view.View

val View.ctx: Context
    get() = context
fun View.show(){
    this.visibility = View.VISIBLE
}
fun View.hide(){
    this.visibility = View.GONE
}
fun View.toggle(){
    when(visibility){
        View.GONE -> this.visibility = View.VISIBLE
        View.VISIBLE -> this.visibility = View.GONE
    }
}


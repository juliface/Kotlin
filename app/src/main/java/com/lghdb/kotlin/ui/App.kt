package com.lghdb.kotlin.ui

import android.app.Application
import com.lghdb.kotlin.extensions.DelegatesExt

/**
 * Created by lghdb on 2018/3/28.
 */
class App: Application(){

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
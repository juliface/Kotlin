package com.lghdb.driver.ui

import android.app.Application
import com.lghdb.driver.extensions.DelegatesExt

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
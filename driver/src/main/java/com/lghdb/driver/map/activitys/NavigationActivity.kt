package com.lghdb.driver.map.activitys

import android.app.Activity
import android.os.Bundle
import com.amap.api.navi.AMapNaviView
import com.lghdb.driver.map.Navigation
import com.lghdb.driver.ui.listener.interfaces.DriverAMapNaviViewListener

/**
 * Created by lghdb on 2018/4/9.
 */
abstract class NavigationActivity: Activity(){

    abstract val layoutId:Int
    abstract val mapNaviView:AMapNaviView
    lateinit var navigation: Navigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        actionBar?.hide()
        mapNaviView.setAMapNaviViewListener(object : DriverAMapNaviViewListener {
            override fun onNaviCancel() {
                finish()
            }

            override fun onNaviBackClick() = false
        })
        navigation = Navigation(mapNaviView, savedInstanceState)
    }

    override fun onDestroy(){
        super.onDestroy()
        navigation?.onDestroy()
    }
    override fun onResume(){
        super.onResume()
        navigation?.onResume()
    }

    override fun onPause() {
        super.onPause()
        navigation?.onPause()
    }
}
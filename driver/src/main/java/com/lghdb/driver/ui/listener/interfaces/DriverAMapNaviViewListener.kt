package com.lghdb.driver.ui.listener.interfaces


import com.amap.api.navi.AMapNaviViewListener


/**
 * Created by lghdb on 2018/4/2.
 */

interface DriverAMapNaviViewListener :AMapNaviViewListener{


    override fun onNaviSetting(){}

    override fun onNaviCancel(){}

    override fun onNaviBackClick(): Boolean = false

    override fun onNaviMapMode(var1: Int){}

    override fun onNaviTurnClick(){}

    override fun onNextRoadClick(){}

    override fun onScanViewButtonClick(){}

    override fun onLockMap(var1: Boolean){}

    override fun onNaviViewLoaded(){}

}
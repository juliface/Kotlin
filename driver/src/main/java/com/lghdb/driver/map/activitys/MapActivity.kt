package com.lghdb.driver.map.activitys

import android.app.Activity
import android.os.Bundle
import com.amap.api.maps.MapView
import com.lghdb.driver.map.Map

/**
 * Created by lghdb on 2018/4/9.
 */
abstract class MapActivity: Activity(){

    abstract val aMapView:MapView
    abstract val layoutId:Int
    lateinit var map:Map

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        actionBar?.hide()
        map = Map(aMapView,savedInstanceState)
    }

    override fun onDestroy(){
        super.onDestroy()
        map?.onDestroy()
    }
    override fun onResume(){
        super.onResume()
        map?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map?.onPause()
    }
    override fun onSaveInstanceState(outbundle:Bundle){
        super.onSaveInstanceState(outbundle)
        map?.onSaveInstanceState(outbundle)
    }
}
package com.lghdb.driver.ui.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView

/**
 * Created by lghdb on 2018/4/3.
 */
abstract class MapBaseActivity: AppCompatActivity(){

    var mapView:MapView? = null
    var map:AMap? = null

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}
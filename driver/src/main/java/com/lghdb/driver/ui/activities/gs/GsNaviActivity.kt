package com.lghdb.driver.ui.activities.gs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amap.api.services.core.LatLonPoint
import com.lghdb.driver.R
import com.lghdb.driver.map.Navigation
import com.lghdb.driver.map.ext.toNaviLatLng
import com.lghdb.driver.ui.listener.interfaces.DriverAMapNaviViewListener
import kotlinx.android.synthetic.main.activity_gsnavi.*
import org.jetbrains.anko.toast

/**
 * Created by lghdb on 2018/4/8.
 */
class GsNaviActivity: AppCompatActivity(){

    companion object {
        val START = "GsNaviActivity:start"
        val END = "GsNaviActivity:end"
    }
    private var navigation:Navigation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gsnavi)
        actionBar?.hide()
        navigation = Navigation(gsNavi, savedInstanceState)
        val start = intent.getParcelableExtra<LatLonPoint>(START).toNaviLatLng()
        val end = intent.getParcelableExtra<LatLonPoint>(END).toNaviLatLng()
        gsNavi.setAMapNaviViewListener(object : DriverAMapNaviViewListener{
            override fun onNaviCancel() {
                finish()
            }

            override fun onNaviBackClick() = false
        })
        navigation!!.calculateDriveRoute(mutableListOf(start), mutableListOf(end),
                failure = {
                    onBackPressed()
                    toast("路线规划失败")
                },
                success = {
                    if (it != null && !it.isEmpty()) {
                        navigation!!.selectRouteId(it[0])
                        navigation!!.startGps()
                    }

                })
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
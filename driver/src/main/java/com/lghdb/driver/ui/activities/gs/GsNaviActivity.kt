package com.lghdb.driver.ui.activities.gs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amap.api.navi.AMapNaviView
import com.amap.api.services.core.LatLonPoint
import com.lghdb.driver.R
import com.lghdb.driver.map.Navigation
import com.lghdb.driver.map.activitys.NavigationActivity
import com.lghdb.driver.map.ext.toNaviLatLng
import kotlinx.android.synthetic.main.activity_gsnavi.*
import org.jetbrains.anko.toast

/**
 * Created by lghdb on 2018/4/8.
 */
class GsNaviActivity: NavigationActivity(){

    companion object {
        val START = "GsNaviActivity:start"
        val END = "GsNaviActivity:end"
    }

    override val layoutId: Int
        get() = R.layout.activity_gsnavi

    override val mapNaviView: AMapNaviView
        get() = gsNavi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val start = intent.getParcelableExtra<LatLonPoint>(START).toNaviLatLng()
        val end = intent.getParcelableExtra<LatLonPoint>(END).toNaviLatLng()
        navigation.calculateDriveRoute(mutableListOf(start), mutableListOf(end),
                failure = {
                    onBackPressed()
                    toast("路线规划失败")
                },
                success = {
                    if (it != null && !it.isEmpty()) {
                        navigation.selectRouteId(it[0])
                        navigation.startGps()
                    }

                })
    }
}
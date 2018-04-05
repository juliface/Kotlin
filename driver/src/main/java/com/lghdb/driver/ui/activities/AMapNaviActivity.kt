package com.lghdb.driver.ui.activities

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.amap.api.navi.*
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.enums.PathPlanningStrategy
import com.amap.api.navi.model.NaviLatLng
import com.lghdb.driver.R
import com.lghdb.driver.ui.listener.VoliceController
import com.lghdb.driver.ui.listener.interfaces.DriverAMapNaviViewListener
import com.lghdb.driver.ui.listener.interfaces.DriverNaviListener
import kotlinx.android.synthetic.main.activity_mapnavi.*

/**
 * Created by lghdb on 2018/4/2.
 */
class AMapNaviActivity: AppCompatActivity(),DriverNaviListener, DriverAMapNaviViewListener{

    companion object {
        val START_SITE = "AMapNaviActivity:start_site"
        val END_SITE = "AMapNaviActivity:end_site"
    }


    var mapNavi:AMapNavi? = null
    var mttsManager:VoliceController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapnavi)
        actionBar?.hide()
        navi_view.onCreate(savedInstanceState)
        initNavView()
    }

    private fun initNavView(){
        navi_view.setAMapNaviViewListener(this)
        mapNavi = AMapNavi.getInstance(this)
        mapNavi!!.addAMapNaviListener(this)
        //实例化语音引擎
        mttsManager = VoliceController.instance
        mapNavi!!.addAMapNaviListener(mttsManager)

    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
        //开始导航
        startNavi()
    }

    override fun onCalculateRouteFailure(errorInfo: Int) {
        super.onCalculateRouteFailure(errorInfo)
        Log.v("dm", "--------------------------------------------");
        Log.v("dm", "路线计算失败：错误码=" + errorInfo);
        Log.v("dm", "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.v("dm", "--------------------------------------------");
    }

    override fun onInitNaviSuccess() {
        super.onInitNaviSuccess()
        /**
         * 方法:
         *   var strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
         * 参数:
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         * 说明:
         *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         * 注意:
         *      不走高速与高速优先不能同时为true
         *      高速优先与避免收费不能同时为true
         */
        var start_str = intent.getStringExtra(START_SITE)
        var end_str = intent.getStringExtra(END_SITE)
        val end_list = end_str.split(",")
        var start_list = end_str.split(",")
        mapNavi?.calculateDriveRoute(
                mutableListOf(NaviLatLng(start_list[1].toDouble(), start_list[0].toDouble())),
                mutableListOf(NaviLatLng(end_list[1].toDouble(), end_list[0].toDouble())),
                null,
                PathPlanningStrategy.DRIVING_AVOID_CONGESTION


        )
    }


    private fun startNavi(){
        mapNavi?.startNavi(NaviType.GPS)
    }

    override fun getResources(): Resources  = baseContext.resources

    override fun onResume() {
        super.onResume()
        navi_view?.onResume()
    }

    override fun onPause() {
        super.onPause()
        navi_view?.onPause()
        mttsManager?.stopSpeaking()
    }

    override fun onDestroy() {
        super.onDestroy()
        navi_view?.onDestroy()
        mapNavi?.stopNavi()
        mapNavi?.destroy()
        mttsManager?.destory()
    }

    override fun onNaviCancel() {
        finish()
    }

    override fun onNaviBackClick() = false

}
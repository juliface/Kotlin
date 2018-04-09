package com.lghdb.driver.map

import android.os.Bundle
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviView
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.AMapNaviPath
import com.amap.api.navi.model.NaviLatLng
import com.lghdb.driver.ui.App
import com.lghdb.driver.ui.listener.VoliceController
import com.lghdb.driver.ui.listener.interfaces.DriverNaviListener

/**
 * Created by lghdb on 2018/4/8.
 */
class Navigation(val mapNaviView: AMapNaviView,
                 savedInstanceState: Bundle?){

    /**
     * 起点
     */
    private val startLatLngs:MutableList<NaviLatLng> = mutableListOf()
    /**
     * 终点
     */
    private val endLatLngs:MutableList<NaviLatLng> = mutableListOf()
    /**
     * 途径点
     */
    private val wayLatLngs:MutableList<NaviLatLng> = mutableListOf()


    val naviPaths:HashMap<Int, AMapNaviPath>
        get() = mapNavi.naviPaths

    val naviPath:AMapNaviPath
        get() = mapNavi.naviPath

    private val mapNavi:AMapNavi
    private var navListener:DriverNaviListener? = null
    private val voliceController:VoliceController

    init {
        mapNaviView.onCreate(savedInstanceState)
        mapNavi = AMapNavi.getInstance(App.instance)
        mapNavi.addAMapNaviListener(NaviListener())
        //导航语音
        voliceController = VoliceController.instance
        mapNavi.addAMapNaviListener(voliceController)
    }


    //****************************************************************************
    //*************************起点，终点，途径点的操作******************************
    fun addStart(point:NaviLatLng):Navigation{
        startLatLngs.add(point)
        return this
    }
    fun addEnd(point:NaviLatLng):Navigation{
        endLatLngs.add(point)
        return this
    }
    fun addWay(point:NaviLatLng):Navigation{
        wayLatLngs.add(point)
        return this
    }

    //**************************************************************
    //**********路线规划*********************************************
    /**
     * 路线规划的监听器
     */
    inner class NaviListener: DriverNaviListener{
        override fun onCalculateRouteSuccess(ints: IntArray?) {
            navListener?.onCalculateRouteSuccess(ints)
        }
        override fun onCalculateRouteFailure(p0: Int){
            navListener?.onCalculateRouteFailure(p0)
        }
    }
    /**
     * 规划驾驶路线
     * @param startPoints 起点
     * @param endPoints 终点
     * @param wayPoints 途径点
     * @param strategy 路线规划策略
     * @param failure 规划路线失败的回调函数,默认为不处理
     * @param success 规划路线成功的回调函数
     */
    fun calculateDriveRoute(startPoints:MutableList<NaviLatLng> = startLatLngs,
                            endPoints:MutableList<NaviLatLng> = endLatLngs,
                            wayPoints:MutableList<NaviLatLng> = wayLatLngs,
                            strategy:Int=mapNavi.strategyConvert(true,
                                    false, false, true, true),
                            failure: (Int?)->Unit = {},
                            success:(IntArray?)->Unit){
        navListener = object : DriverNaviListener{
            override fun onCalculateRouteSuccess(ints: IntArray?) {
                success(ints)
            }
            override fun onCalculateRouteFailure(p0: Int){
                failure(p0)
            }
        }
        mapNavi.calculateDriveRoute(startPoints, endPoints,wayPoints,strategy)
    }

    /**
     * 步行路径规划
     * @param startPoint 起点
     * @param endPoint 终点
     * @param failure 路线规划失败的回调函数，默认不处理
     * @param success 路线规划成功的回调函数
     */
    fun calculateWalkRoute(startPoint:NaviLatLng = startLatLngs[0],
                           endPoint:NaviLatLng = endLatLngs[0],
                           failure: (Int?)->Unit = {},
                           success:(IntArray?)->Unit){

        navListener = object : DriverNaviListener{
            override fun onCalculateRouteSuccess(ints: IntArray?) {
                success(ints)
            }
            override fun onCalculateRouteFailure(p0: Int){
                failure(p0)
            }
        }
        mapNavi.calculateWalkRoute(startPoint, endPoint)
    }

    /**
     * 骑行路径规划
     * @param startPoint 起点
     * @param endPoint 终点
     * @param failure 路线规划失败的回调函数，默认不处理
     * @param success 路线规划成功的回调函数
     */
    fun calculateRideRoute(startPoint:NaviLatLng = startLatLngs[0],
                           endPoint:NaviLatLng = endLatLngs[0],
                           failure: (Int?)->Unit = {},
                           success:(IntArray?)->Unit){

        navListener = object : DriverNaviListener{
            override fun onCalculateRouteSuccess(ints: IntArray?) {
                success(ints)
            }
            override fun onCalculateRouteFailure(p0: Int){
                failure(p0)
            }
        }
        mapNavi.calculateRideRoute(startPoint, endPoint)
    }

    /**
     * 选择导航路线
     * @param routeId 路线Id
     */
    fun selectRouteId(routeId:Int){
        mapNavi.selectRouteId(routeId)
    }

    /**
     * 开始模拟导航
     */
    fun startEmulator(){
        mapNavi.startNavi(NaviType.EMULATOR)
    }

    /**
     * 开始实时导航
     */
    fun startGps(){
        mapNavi.startNavi(NaviType.GPS)
    }

    //**************************************************************
    //**********路线规划      END************************************

    //**************************************************************
    //**********关于导航的声明周期************************************
    fun onDestroy(){
        //在activity执行onDestroy时执行mapNaviView.onDestroy()，销毁导航对象
        mapNaviView.onDestroy()
        mapNavi.stopNavi()
        voliceController.destory()
    }
    fun onResume(){
        ////在activity执行onResume时执行mapNaviView.onResume ()，重新渲染导航地图
        mapNaviView.onResume()
    }

    fun onPause() {
        //在activity执行onPause时执行mapNaviView.onPause ()，暂停导航
        mapNaviView.onPause()
        voliceController.stopSpeaking()

    }
}
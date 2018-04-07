package com.lghdb.driver.map

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdate
import com.amap.api.maps.MapView
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.navi.model.AMapNaviPath
import com.amap.api.navi.view.RouteOverLay
import com.lghdb.driver.R
import com.lghdb.driver.extensions.ctx

/**
 * Created by lghdb on 18-4-7.
 */

class Map(val mapView: MapView,val savedInstanceState: Bundle?){
    private var map:AMap
    /**
     * 时候开启小蓝点
     */
    var isMyLocationEnabled:Boolean
        get() = map.isMyLocationEnabled
        set(value) {map.isMyLocationEnabled = value}
    /**
     * 初始化定位蓝点样式类
     */
    var myLocationStyle:MyLocationStyle
        get() = map.myLocationStyle
        set(value) {map.myLocationStyle = value}

    var isMyLocationButtonEnabled:Boolean
        get() = map.uiSettings.isMyLocationButtonEnabled
        set(value) { map.uiSettings.isMyLocationButtonEnabled = value }



    init {
        //初始化地图
        mapView.onCreate(savedInstanceState)
        //显示地图
        map = mapView.map
    }

    /**
     * 通过如下回调方法获取经纬度信息
     * 从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
     */
    fun setOnMyLocationChangeListener(loc: (Location)->Unit): Map {
        map.setOnMyLocationChangeListener { loc }
        return this
    }

    //***********************************************************
    //××××××××××××××××××××移动地图相关×××××××××××××××××××××××××××××
    /**
     * 地图移动的动画效果
     */
    fun moveCamera(cu:CameraUpdate): Map {
        map.moveCamera(cu)
        return this
    }
    //地图移动的回调监听器
    fun setOnCameraChangeListener(cameraChange:(CameraPosition?)->Unit = {},
                                  cameraChangeFinish:(CameraPosition?)->Unit={}){
        map.setOnCameraChangeListener(object :AMap.OnCameraChangeListener{
            override fun onCameraChange(position: CameraPosition?) {
                cameraChange(position)
            }
            override fun onCameraChangeFinish(position: CameraPosition?) {
                cameraChangeFinish(position)
            }
        })
    }
    //***********************************************************
    //××××××××××××××××××××移动地图相关  END××××××××××××××××××××××××

    //***********************************************************
    //××××××××××××××××××××地图绘制点标记Marker××××××××××××××××××××××××
    fun addMarker(marker:MarkerOptions):Map{
        map.addMarker(marker)
        return this
    }
    /**
     * Marker 点击事件
     */
    fun setOnMarkerClickListener(tf:(Marker)->Boolean){
        map.setOnMarkerClickListener { tf(it) }
    }

    /**
     * Marker拖拽的监听
     */
    fun setOnMarkerDragListener(markerDragStart:(Marker?)->Unit = {},
                                markerDragEnd:(Marker?)->Unit={},
                                markerDrag:(Marker?)->Unit={}){
        map.setOnMarkerDragListener(object : AMap.OnMarkerDragListener{
            override fun onMarkerDragStart(p0: Marker?) {markerDragStart(p0)}
            override fun onMarkerDragEnd(p0: Marker?) { markerDragEnd(p0)}
            override fun onMarkerDrag(p0: Marker?) { markerDrag(p0) }
        })
    }

    /**
     * 实现 InfoWindow 样式和内容
     */
    fun setInfoWindowAdapter(layout:Int,render:(Marker?,View)->Unit){
        val markerView = LayoutInflater.from(mapView.ctx).inflate(layout, null)
        map.setInfoWindowAdapter(object : AMap.InfoWindowAdapter{
            override fun getInfoContents(marker: Marker?): View = markerView
            override fun getInfoWindow(marker: Marker?): View {
                render(marker,markerView)
                return markerView
            }
        })
    }

    /**
     * InfoWindow 点击事件
     */
    fun setOnInfoWindowClickListener(click:(Marker)->Unit){
        map.setOnInfoWindowClickListener { click(it) }
    }

    //***********************************************************
    //××××××××××××××××××××地图绘制点标记Marker   END××××××××××××××××


    /**
     * 绘制路线
     */
    fun drawRoute(path: AMapNaviPath){
        RouteOverLay(map, path, mapView.context).apply {
            isTrafficLine = false
        }.addToMap()
    }

    fun onDestroy(){
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy()
    }
    fun onResume(){
        ////在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume()
    }

    fun onPause() {
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause()
    }
    fun onSaveInstanceState(outbundle:Bundle){
        ////在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outbundle)
    }

}
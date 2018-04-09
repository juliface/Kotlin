package com.lghdb.driver.map

import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdate
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.amap.api.navi.model.AMapNaviPath
import com.amap.api.navi.view.RouteOverLay
import com.lghdb.driver.R
import com.lghdb.driver.extensions.ctx
import com.lghdb.driver.ui.App

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
        map.setOnMyLocationChangeListener { loc(it) }
        return this
    }

    fun myLocationEnabled():Map{
        isMyLocationEnabled = true
        myLocationStyle = MyLocationStyle()
                .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
                .strokeColor(Color.TRANSPARENT)
                .radiusFillColor(Color.TRANSPARENT)
        return this
    }

    //***********************************************************
    //××××××××××××××××××××移动地图相关×××××××××××××××××××××××××××××
    /**
     * 按照传入的CameraUpdate参数改变地图状态。
     */
    fun moveCamera(cu:CameraUpdate): Map {
        map.moveCamera(cu)
        return this
    }

    /**
     * 设置缩放级别
     */
    fun zoomTo(tt:Float):Map{
        moveCamera(CameraUpdateFactory.zoomTo(tt))
        return this
    }
    //地图移动的回调监听器
    fun setOnCameraChangeListener(cameraChange:(CameraPosition)->Unit = {},
                                  cameraChangeFinish:(CameraPosition)->Unit={}){
        map.setOnCameraChangeListener(object :AMap.OnCameraChangeListener{
            override fun onCameraChange(position: CameraPosition) {
                cameraChange(position)
            }
            override fun onCameraChangeFinish(position: CameraPosition) {
                cameraChangeFinish(position)
            }
        })
    }
    //***********************************************************
    //××××××××××××××××××××移动地图相关  END××××××××××××××××××××××××

    //***********************************************************
    //××××××××××××××××××××地图绘制点标记Marker××××××××××××××××××××××××
    fun addMarker(marker:MarkerOptions):Marker{
        return map.addMarker(marker)
    }
    fun addMarker(position:LatLng,
                  draggable:Boolean = false):Marker{
        val options = MarkerOptions()
        options.position(position)
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(App.instance.resources,
                R.mipmap.end)))
        options.draggable(draggable)
        return  addMarker(options)
    }
    fun addDraggableMarker(position:LatLng,markerDragEnd:(Marker?)->Unit):Map{
        addMarker(position, true)
        setOnMarkerDragListener(markerDragEnd = markerDragEnd)
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

    //**************************************************************
    //**********关于地图的声明周期************************************
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
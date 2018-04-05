package com.lghdb.driver.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.model.AMapNaviPath
import com.amap.api.navi.model.NaviLatLng
import com.amap.api.navi.view.RouteOverLay
import com.amap.api.services.help.Tip
import com.lghdb.driver.R
import com.lghdb.driver.extensions.onTabSelected
import com.lghdb.driver.ui.listener.interfaces.DriverNaviListener
import kotlinx.android.synthetic.main.activity_line.*
import org.jetbrains.anko.toast


/**
 * Created by lghdb on 2018/4/3.
 */

class DriverLineActivity: MapBaseActivity(){

    companion object {
        val START_SITE = "DriverLineActivity:start_site"
        val END_SITE = "DriverLineActivity:end_site"
        val REQUEST_CODE_POI_SEARCH = 3
    }

    //结束点
    val endList = mutableListOf<NaviLatLng>()
    //开始点
    val startList = mutableListOf<NaviLatLng>()
    //出行类型(驾车0,骑行2和步行1)
    var navigationType:Int = 0
    //获得导航对象
    val mapNavi:AMapNavi = AMapNavi.getInstance(applicationContext)
    val routeOverlays:MutableMap<Int,RouteOverLay> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line)
        actionBar?.hide()
        //初始化地图
        initMap(savedInstanceState)
        initView()
    }

    private fun initMap(savedInstanceState: Bundle?){
        mapView = navi_view
        mapView?.onCreate(savedInstanceState)
        map = mapView!!.map
        map!!.isMyLocationEnabled = true
        map!!.myLocationStyle = MyLocationStyle()
                .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        map!!.setOnMyLocationChangeListener{
            Log.v("info","当前位置(${it.latitude},${it.longitude})")
            startList.add(NaviLatLng(it.latitude,it.longitude))
        }
        //地图移动的动画
        map!!.moveCamera(CameraUpdateFactory.zoomTo(15f))
        //设置导航对象的监听
        mapNavi.addAMapNaviListener(object : DriverNaviListener{
            override fun onCalculateRouteSuccess(p0: IntArray?) {

            }

            override fun onCalculateRouteFailure(p0: Int) {
                toast("路径规划不成功!")
            }


        })
    }

    private fun initView(){
        //点击结束位置的时候打开搜索界面,选择地点
        endAddress.setOnClickListener {
           var intent = Intent(this@DriverLineActivity,PoiSearchActivity::class.java)
                   .putExtra(PoiSearchActivity.END_SITE, endAddress.text.toString())
           startActivityForResult(intent, REQUEST_CODE_POI_SEARCH)
        }
        initTab()

    }

    /**
     * 初始化tab
     */
    private fun initTab(){
        //tab的字体选择器,默认灰色,选择时白色
        tabs.setTabTextColors(Color.LTGRAY, Color.WHITE)
        //设置tab的下划线颜色,默认是粉红色
        tabs.setSelectedTabIndicatorColor(Color.WHITE)
        tabs.addTab(tabs.newTab().setText("驾车"))
        tabs.addTab(tabs.newTab().setText("步行"))
        tabs.addTab(tabs.newTab().setText("骑行"))

        //tab点击事件
        tabs.onTabSelected {
            if(it != null){
                with(it){
                    when(text){
                        "步行" -> navigationType = 1
                        "骑行" -> navigationType = 2
                        else -> navigationType = 0
                    }
                }
                //首先清除线路,然后重新规划线路
            }
        }
    }

    /**
     * 从结束地址搜索界面返回结果
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (REQUEST_CODE_POI_SEARCH == resultCode){
            val tip = data.getParcelableExtra<Tip>("end_address")
            endAddress.setText("到     ${tip.district}")
            endList.add(NaviLatLng(tip.point.latitude,tip.point.longitude))
        }

    }

    /**
     * 计划路线
     */
    private fun calculateRoute(){
        val strategy = mapNavi.strategyConvert(true,
                false, false, true, true)
        when(navigationType){
            0 -> mapNavi.calculateDriveRoute(startList, endList, listOf(), strategy)
            1 -> mapNavi.calculateWalkRoute(startList[0], endList[0])
            else -> mapNavi.calculateRideRoute(startList[0], endList[0])
        }
    }

    /**
     * 绘制路线
     */
    private fun drawRoute(routeId:Int, path:AMapNaviPath){
        map!!.moveCamera(CameraUpdateFactory.changeTilt(0f))
        val routeOverlay = RouteOverLay(map!!, path, this).apply {
            isTrafficLine = false
            addToMap()
        }
        routeOverlays.put(routeId, routeOverlay)

    }

}

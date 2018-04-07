package com.lghdb.driver.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.model.AMapNaviPath
import com.amap.api.navi.model.NaviLatLng
import com.amap.api.navi.view.RouteOverLay
import com.amap.api.services.help.Tip
import com.lghdb.driver.R
import com.lghdb.driver.extensions.each
import com.lghdb.driver.extensions.keyAt
import com.lghdb.driver.extensions.onTabSelected
import com.lghdb.driver.ui.adapter.DriverLineAdapter
import com.lghdb.driver.ui.listener.interfaces.DriverNaviListener
import kotlinx.android.synthetic.main.activity_line.*
import org.jetbrains.anko.toast
import org.jetbrains.anko.startActivity


/**
 * Created by lghdb on 2018/4/3.
 */

class DriverLineActivity: MapBaseActivity(){

    companion object {
        val START_SITE = "DriverLineActivity:start_site"
        val END_SITE = "DriverLineActivity:end_site"
        val REQUEST_CODE_POI_SEARCH = 3
        val REQUEST_CODE_POI_SEARCH_EXIT = -1
    }

    //结束点
    val endList = mutableListOf<NaviLatLng>()
    //开始点
    val startList = mutableListOf<NaviLatLng>()
    //出行类型(驾车0,骑行2和步行1)
    var navigationType:Int = 0
    //获得导航对象
    var mapNavi:AMapNavi? = null
    val routeOverlays:MutableMap<Int,RouteOverLay> = HashMap()
    val ways:MutableList<AMapNaviPath> = mutableListOf()
    var currentPosition: Int = 0
    var lastPosition = -1
    var calculateSuccess = false
    var routeIndex = 0
    var zindex = 0


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
        mapNavi = AMapNavi.getInstance(applicationContext)
        //设置导航对象的监听
        mapNavi!!.addAMapNaviListener(object : DriverNaviListener{
            override fun onCalculateRouteSuccess(ints: IntArray?) {
                if (ints != null && !ints.isEmpty()) {
                    //clear prev route plan
                    routeOverlays.clear()
                    ways.clear()
                    calculateSuccess = true
                    if(ints.size >1){
                        mutlipRoute(ints)
                    }else{
                        oneRoute()
                    }
                }

            }

            override fun onCalculateRouteFailure(p0: Int){
                calculateSuccess = false
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
        rl_rlv_ways.layoutManager = GridLayoutManager(this,3)
        rl_rlv_ways.adapter = DriverLineAdapter(ways, this){
            //设置当前选择的item index
            route, view ->
            lastPosition = currentPosition
            currentPosition = rl_rlv_ways.getChildLayoutPosition(view)
            Log.v("info","当前选中的线路控件：${currentPosition}")
            if (currentPosition == lastPosition) view
            routeIndex = rl_rlv_ways.getChildLayoutPosition(view)
            Log.v("info","当前选中的线路索引：${routeIndex}")
            selectRoute()
            Log.v("info","上次选中的线路控件：${lastPosition}")
            rl_rlv_ways.getChildAt(lastPosition)
        }
        //开始导航
        rl_tv_navistart.setOnClickListener{
            if(startList.size == 0){
                toast("没有获取到当前位置")
            }else if(endList.size == 0){
                toast("没有获取到终点")
            }else{
                if(!calculateSuccess){
                    toast("请先规划路线")
                }else{
                    if(routeIndex > ways.size) routeIndex = 0
                    mapNavi!!.selectRouteId(routeOverlays.keyAt(routeIndex)!!)
                    startActivity<AMapNaviActivity>()
                }
            }
        }
        initTab()
    }

    private fun selectRoute(){
        if(!calculateSuccess){
            toast("没有规划路线")
            return
        }
        if(routeOverlays.size == 1){
            mapNavi!!.selectRouteId(routeOverlays.keyAt(0)!!)
            return
        }
        if(routeIndex >= routeOverlays.size) routeIndex = 0
        val routeid = routeOverlays.keyAt(routeIndex)
        Log.v("info","当前选中的线路：${routeid}")
        for((_, v) in routeOverlays){
            v.setTransparency(0f)
        }
        routeOverlays.get(routeid)!!.apply { setTransparency(1f) }.setZindex(zindex++)
        mapNavi!!.selectRouteId(routeid!!)
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
                clearRoute()
                calculateRoute()
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
            Log.v("info","结束点为：${tip.district}")
            endList.clear()
            Log.v("info","当前有结束点：${endList.size}")
            endList.add(NaviLatLng(tip.point.latitude,tip.point.longitude))
        }

    }

    /**
     * 计划路线
     */
    private fun calculateRoute(){
        if(startList.size >0 && endList.size>0) {
            when (navigationType) {
                0 -> {
                    val strategy = mapNavi!!.strategyConvert(true,
                            false, false, true, true)
                    mapNavi!!.calculateDriveRoute(startList, endList, listOf(), strategy)
                }
                1 -> mapNavi!!.calculateWalkRoute(startList[0], endList[0])
                else -> mapNavi!!.calculateRideRoute(startList[0], endList[0])
            }
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

    /**
     *  clear route line
     */
    private fun clearRoute(){
        routeOverlays.each{ i, routeOverLay -> routeOverLay.removeFromMap() }
        routeOverlays.clear()
        ways.clear()
    }
    /**
     * 计算路程
     */
    fun getLength(allLength:Int):String{
        if (allLength > 1000) {
            val remainder = allLength % 1000
            val m = if (remainder > 0) remainder.toString() + "米" else ""
            return (allLength / 1000).toString() + "公里" + m
        } else {
            return allLength.toString() + "米"
        }
    }

    /**
     * 计算时间
     * @param allTime
     * @return
     */
    fun getTime(allTime: Int): String {
        if (allTime > 3600) {//1小时以上
            val minute = allTime % 3600
            val min = if (minute / 60 != 0) (minute / 60).toString() + "分钟" else ""
            return (allTime / 3600).toString() + "小时" + min
        } else {
            val minute = allTime % 3600
            return (minute / 60).toString() + "分钟"
        }
    }

    override fun onResume() {
        super.onResume()
        clearRoute()
        calculateRoute()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mapNavi?.destroy()
    }

    /**
     * 当路线
     */
    private fun oneRoute(){
        val path = mapNavi!!.naviPath
        drawRoute(-1, path)
        //隐藏列表
        rl_rlv_ways.visibility = View.GONE
        ll_rl_1way.visibility = View.VISIBLE
        rl_tv_time.text = getTime(path.allTime)
        rl_tv_length.text = getLength(path.allLength)
        rl_tv_navistart.text = "开始导航"
    }

    /**
     * 多路线
     */
    private fun mutlipRoute(ints:IntArray){
        val paths = mapNavi!!.naviPaths
        ints.forEach {
            val path = paths.get(it)
            if (path != null){
                drawRoute(it,path)
                ways.add(path)
            }
        }
        Log.v("info","路径规划成功，一共：${ways.size} 条")
        when(ways.size){
            0 -> {
                rl_rlv_ways.visibility = View.GONE
                rl_tv_navistart.text = "准备导航"
            }
            1 -> {
                rl_rlv_ways.visibility = View.GONE
                ll_rl_1way.visibility = View.VISIBLE
                rl_tv_time.text = getTime(ways[0].allTime)
                rl_tv_length.text = getLength(ways[0].allLength)
                rl_tv_navistart.text = "开始导航"
            }
            else -> {
                currentPosition = 0
                lastPosition = -1
                rl_rlv_ways.adapter.notifyDataSetChanged()
                rl_rlv_ways.visibility = View.VISIBLE
                ll_rl_1way.visibility = View.GONE
                rl_tv_navistart.text = "开始导航"
            }
        }
    }

}

package com.lghdb.driver.ui.activities.gs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.lghdb.driver.R
import com.lghdb.driver.extensions.getDistance
import com.lghdb.driver.extensions.hide
import com.lghdb.driver.extensions.removeButFirst
import com.lghdb.driver.extensions.show
import com.lghdb.driver.map.CommonRecyclerViewAdapter
import com.lghdb.driver.map.Map
import com.lghdb.driver.map.ext.nearSearch
import com.lghdb.driver.map.ext.regeocodeSearched
import com.lghdb.driver.map.ext.toLatLngPoint
import kotlinx.android.synthetic.main.activity_gsmain.*
import kotlinx.android.synthetic.main.item_gslist_location.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Created by lghdb on 2018/4/8.
 */

class GsMainActivity: AppCompatActivity(){

    private var points:MutableList<AddressInfo> = mutableListOf()
    private var currentPoint:AddressInfo? = null
    private var marker:Marker? = null
    private var map:Map? = null
    private var mylocation:LatLonPoint? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gsmain)
        actionBar?.hide()

        initView()
        map = Map(mapView,savedInstanceState)
        initMap(map!!)
    }

    private fun initView(){
        locations.layoutManager = LinearLayoutManager(this)
        locations.adapter = CommonRecyclerViewAdapter<AddressInfo>(points,R.layout.item_gslist_location,
                bindFun = {
                    itemView, t ->
                    itemView.locationName.text = t.title
                    itemView.locationDesc.text = t.description
                    itemView.distance.text = t.distance.getDistance()
                },
                itemClick = {
                    end.text = "到     ${it.point.latitude}, ${it.point.longitude}"
                    startActivity<GsNaviActivity>(
                            GsNaviActivity.START to mylocation!!,
                            GsNaviActivity.END to it.point)
                })
    }

    private fun initMap(map:Map){
        //定位按钮
        map.isMyLocationButtonEnabled = true
        //显示定位蓝点
        map.myLocationEnabled().zoomTo(15f) //设置显示级别
        map.setOnMyLocationChangeListener {
            start.text = "起     ${it.latitude}, ${it.longitude}"
            marker = map.addMarker(LatLng(it.latitude, it.longitude))
            currentPoint = AddressInfo("[位置]","", LatLonPoint(it.latitude, it.longitude),0)
            mylocation = LatLonPoint(it.latitude, it.longitude)
            points.add(currentPoint!!)
        }
        map.setOnCameraChangeListener(
                cameraChange = {
                    marker?.position = it?.target
                },
                cameraChangeFinish = {
                    Log.v("info","现在位置：(${it.target.latitude}, ${it.target.longitude})")
                    currentPoint?.point = LatLonPoint(it.target.latitude, it.target.longitude)
                    currentPoint?.point?.regeocodeSearched { regeocodeResult, rCode ->
                        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                            currentPoint?.description = regeocodeResult.regeocodeAddress.formatAddress
                            points.removeAt(0)
                            points.add(0, currentPoint!!)
                        }
                    }
                    //搜索周边的信息
                    it?.target?.toLatLngPoint()?.nearSearch(ctx = this) {
                        poiResult, rcode ->
                        when(rcode){
                        //成功
                            AMapException.CODE_AMAP_SUCCESS -> {
                                Log.v("info","搜索到数据!")
                                points.removeButFirst()
                                poiResult.pois.forEach{
                                    points.add(AddressInfo(it.title,it.snippet,it.latLonPoint,it.distance))
                                }
                                locations.adapter.notifyDataSetChanged()
                                if(points.size>0){
                                    locations.show()
                                    holderview.hide()
                                }
                            }
                        //失败
                            else -> {
                                toast("搜索周边数据失败!")
                                locations.hide()
                                holderview.show()
                            }
                        }
                    }
                }
        )
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
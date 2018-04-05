package com.lghdb.driver.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdate
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.services.help.Tip
import com.lghdb.driver.R
import com.lghdb.driver.data.ViewHoldier
import com.lghdb.driver.ui.adapter.PicLocationListAdapter
import kotlinx.android.synthetic.main.avtivity_pic_location.*
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.model.*
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.lghdb.driver.extensions.removeButFirst
import org.jetbrains.anko.toast


/**
 * Created by lghdb on 2018/4/3.
 */
class PicLocationActivity: Activity(){

    val hovers:MutableList<ViewHoldier> = mutableListOf()
    var mEndMarker:Marker? = null
    var geocodeSearch:GeocodeSearch?= null
    var locationClient:AMapLocationClient? = null
    var mListener:LocationSource.OnLocationChangedListener? = null
    var currentViewHoldier:ViewHoldier? = null
    var query:PoiSearch.Query? = null
    var map:AMap? = null
    /**
     * 第一次定位的标志位
     */
    var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avtivity_pic_location)
        actionBar?.hide()
        initMap(savedInstanceState)


        ll_rl_locations.layoutManager = LinearLayoutManager(this)
        ll_rl_locations.adapter = PicLocationListAdapter(hovers){
            //点击每一项的时候返回值给PoiSearchActivity
            var tip = Tip().apply {
                district = it.address
                setPostion(it.lp)
            }
            val intent = Intent().putExtra("end_address",tip)
            setResult(PoiSearchActivity.POI_LOCATION_MAP, intent)
            finish()
        }
    }
    /**
     * marker点击时跳动一下
     */
    private fun jumpPoint(marker:Marker){
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj = map!!.projection
        val markerLatLng = marker.position
        val markerPoint = proj.toScreenLocation(markerLatLng)
        markerPoint.offset(0, -50)
        val startLatLng = proj.fromScreenLocation(markerPoint)
        val interpolator = BounceInterpolator()
        handler.post(object : Runnable{
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / 500)
                val lng = t * markerLatLng.longitude + (1 - t)* startLatLng.longitude;
                val lat = t * markerLatLng.latitude + (1 - t)* startLatLng.latitude;
                marker.position = LatLng(lat, lng)
                if(t < 1.0) handler.postDelayed(this, 16)
            }
        })

    }

    private fun doSearchQuery(){
        Log.v("info","开始搜索周边")
        val type = "汽车服务|汽车销售|" +
                "汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|" +
                "住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
                "金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";
        query = PoiSearch.Query("", type, "").apply {
            pageSize = 20 //每页20个
            pageNum = 0 //第一页
        }
        val search = PoiSearch(this, query)
        search.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener{
            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {}
            /**
             * 返回POI搜索异步处理的结果。
             */
            override fun onPoiSearched(result: PoiResult, rcode: Int) {
                if(AMapException.CODE_AMAP_SUCCESS == rcode){
                    val points = result.pois
                    hovers.removeButFirst()
                    for(i in points.indices){
                        val pi  = points[i]
                        //第一个始终为当前位置
                        hovers.add(i+1,
                                ViewHoldier(pi.snippet, pi.title, pi.latLonPoint))
                    }
                    //显示数据，隐藏加载的对话框
                    showInfoList()

                }else{
                    ll_progressbar.setVisibility(View.GONE);
                    ll_tv_hint.setText(R.string.no_location);
                }
            }
        })
        //当前范围2000米范围
        search.bound = PoiSearch.SearchBound(currentViewHoldier!!.lp, 2000)
        //开始异步搜索
        search.searchPOIAsyn()
    }

    private fun initMap(savedInstanceState: Bundle?){
        map_view?.onCreate(savedInstanceState)
        map = map_view!!.map
        //显示定位按钮
        map!!.uiSettings.setMyLocationButtonEnabled(true)
        //显示定位蓝点
        map!!.isMyLocationEnabled = true
        map!!.moveCamera(CameraUpdateFactory.zoomTo(15f))
        //设置蓝点的style
        map!!.myLocationStyle = MyLocationStyle()
                .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//定位一次，且将视角移动到地图中心点。
                .strokeColor(Color.TRANSPARENT)
                .radiusFillColor(Color.TRANSPARENT)
        //从定位中获取地址信息
        map!!.setOnMyLocationChangeListener {
            currentViewHoldier = ViewHoldier("[位置]",
                    "",
                    LatLonPoint(it.latitude,
                            it.longitude))
            hovers.add(0,currentViewHoldier!!)
        }
        map!!.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            /**
             * 在地图状态改变过程中回调此方法。
             */
            override fun onCameraChange(cameraPosition: CameraPosition) {
                mEndMarker!!.position = cameraPosition.target
            }
            /**
             * 在地图状态改变完成时回调此方法。
             */
            override fun onCameraChangeFinish(p0: CameraPosition) {
                Log.v("info","在地图状态改变完成时回调此方法")
                showProgressbar()
                //marker的动画
                jumpPoint(mEndMarker!!)

                //设置当前地点的经纬度
                currentViewHoldier!!.lp = LatLonPoint(p0.target.latitude, p0.target.longitude)
                //通过地理编码获取到地址描述信息设置currentViewHoldier的address的值
                val query = RegeocodeQuery(currentViewHoldier!!.lp, 200f,
                        GeocodeSearch.AMAP)
                geocodeSearch!!.getFromLocationAsyn(query)
                doSearchQuery()

            }
        })

        mEndMarker = map!!.addMarker(MarkerOptions().icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources,R.mipmap.end))
        ))
        geocodeSearch = GeocodeSearch(this)
        geocodeSearch!!.setOnGeocodeSearchListener(object: GeocodeSearch.OnGeocodeSearchListener{
            override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) { }
            /**
             * 逆地理编码回调方法
             * 经纬度转位置
             * @param result
             * @param rCode
             */
            override fun onRegeocodeSearched(result: RegeocodeResult, rCode: Int) {
                //只有rCode == 1000时表示获取成功
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {
                        currentViewHoldier!!.address = result.getRegeocodeAddress().getFormatAddress();
                        hovers!!.removeAt(0);
                        hovers!!.add(0,currentViewHoldier!!);
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view?.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        map_view?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        map_view?.onSaveInstanceState(outState)
    }

    /**
     * 显示数据列表，隐藏加载的对话框
     */
    private fun showInfoList(){
        ll_rl_locations.adapter.notifyDataSetChanged()
        ll_rl_locations.visibility = View.VISIBLE
        ll_ll_holderview.visibility = View.GONE
    }

    /**
     * 隐藏数据，显示正在加载对话框
     */
    private fun showProgressbar(){
        //隐藏数据
        ll_rl_locations.visibility = View.GONE
        //显示正在加载对话框
        ll_ll_holderview.visibility = View.VISIBLE
        ll_progressbar.visibility = View.VISIBLE
        ll_tv_hint.setText(R.string.loading)
    }
}
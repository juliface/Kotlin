package com.lghdb.driver.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.navi.model.NaviLatLng
import com.amap.api.services.help.Tip
import com.lghdb.driver.R
import kotlinx.android.synthetic.main.activity_line.*


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line)
        actionBar?.hide()
        ininMap(savedInstanceState)
        initView()
    }

    private fun ininMap(savedInstanceState: Bundle?){
        mapView = navi_view
        mapView?.onCreate(savedInstanceState)
        map = mapView!!.map
        var sytle = MyLocationStyle()
                .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
    }

    private fun initView(){
        //点击结束位置的时候打开搜索界面,选择地点
       endAddress.setOnClickListener {
           var intent = Intent(this@DriverLineActivity,PoiSearchActivity::class.java)
                   .putExtra(PoiSearchActivity.END_SITE, endAddress.text.toString())
           startActivityForResult(intent, REQUEST_CODE_POI_SEARCH)
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
}

package com.lghdb.driver.ui.activities

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amap.api.navi.*
import com.amap.api.navi.enums.NaviType
import com.lghdb.driver.R
import com.lghdb.driver.ui.listener.VoliceController
import com.lghdb.driver.ui.listener.interfaces.DriverAMapNaviViewListener
import com.lghdb.driver.ui.listener.interfaces.DriverNaviListener
import kotlinx.android.synthetic.main.activity_mapnavi.*

/**
 * Created by lghdb on 2018/4/2.
 */
class AMapNaviActivity: AppCompatActivity(),DriverNaviListener, DriverAMapNaviViewListener{


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
        startNavi()

    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {

    }

    override fun onCalculateRouteFailure(errorInfo: Int) {
        super.onCalculateRouteFailure(errorInfo)
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
        mttsManager?.destory()
    }

    override fun onNaviCancel() {
        finish()
    }

    override fun onNaviBackClick() = false

}
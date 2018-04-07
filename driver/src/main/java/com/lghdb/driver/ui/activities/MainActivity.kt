package com.lghdb.driver.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.lghdb.driver.R
import com.lghdb.driver.extensions.sha1
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    var locationClient:AMapLocationClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.hide()
        initLocation()
        Log.v("sha1","当前: ${this.sha1()}")
        start.setOnClickListener{ startActivity<DriverLineActivity>(
                DriverLineActivity.START_SITE to startlng.text.toString(),
                DriverLineActivity.END_SITE to endlng.text.toString())
        }
    }

    private fun initLocation(){
        locationClient = AMapLocationClient(applicationContext).apply {
            setLocationOption(initLocationOption())
            setLocationListener {
                startlng.setText("${it.longitude},${it.latitude}")
                startSite.setText(it.address)
                locationClient?.stopLocation()
            }
        }
        locationClient!!.startLocation()
    }

    private fun initLocationOption():AMapLocationClientOption
            =  AMapLocationClientOption()
            .setOnceLocation(true)
            .setOnceLocationLatest(true)
            .setNeedAddress(true)

    override fun onDestroy() {
        super.onDestroy()
        locationClient?.onDestroy()
    }

}

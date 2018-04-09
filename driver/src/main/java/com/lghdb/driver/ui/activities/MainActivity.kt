package com.lghdb.driver.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.lghdb.driver.R
import com.lghdb.driver.extensions.sha1
import com.lghdb.driver.ui.activities.gs.GsMainActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.hide()

        Log.v("sha1","当前: ${this.sha1()}")

        //开始导航
        startNavi.setOnClickListener{
            startActivity<DriverLineActivity>()
        }
        //开始
        start.setOnClickListener{
            startActivity<GsMainActivity>()
        }

    }

}

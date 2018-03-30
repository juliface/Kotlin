package com.lghdb.kotlin.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.lghdb.kotlin.ui.adapters.ForecastListAdapter
import com.lghdb.kotlin.R
import com.lghdb.kotlin.domain.command.RequestForecastCommand
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), ToolbarManager {

    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
        forecast_list.layoutManager = LinearLayoutManager(this)
        attachToScroll(forecast_list)

        doAsync {
            val result = RequestForecastCommand(94043).execute()
            uiThread {
                forecast_list.adapter = ForecastListAdapter(result) {
                    startActivity<DetailActivity>(
                            DetailActivity.ID to it.id,
                            DetailActivity.CITY_NAME to result.city)
                }
            }
            toolbarTitle = "${result.city} (${result.country})"
        }
    }

}

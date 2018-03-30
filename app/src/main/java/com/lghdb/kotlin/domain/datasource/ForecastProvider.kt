package com.lghdb.kotlin.domain.datasource

import com.lghdb.kotlin.data.db.ForecastDb
import com.lghdb.kotlin.data.server.ForecastServer
import com.lghdb.kotlin.domain.model.Forecast
import com.lghdb.kotlin.domain.model.ForecastList
import com.lghdb.kotlin.extensions.firstResult

/**
 * Created by lghdb on 2018/3/29.
 */
class ForecastProvider(private val sources: List<ForecastDataSource> = ForecastProvider.SOURCES ){

    companion object {
        val DAY_IN_MILLIS = 1000 * 60 * 60 * 24
        val SOURCES = listOf(ForecastDb(), ForecastServer())
    }

    fun requestByZipCode(zipCode:Long, days:Int): ForecastList
        = requestToSources{
        val res = it.requestForecastByZipCode(zipCode, todayTimeSpan())
        if(res!= null && res.size >= days) res else null
    }

    fun requestForecast(id: Long): Forecast = requestToSources {
        it.requestDayForecast(id)
    }


    private fun <T: Any> requestToSources(f: (ForecastDataSource) -> T?): T = sources.firstResult{ f(it) }

    private fun todayTimeSpan() = System.currentTimeMillis() / DAY_IN_MILLIS * DAY_IN_MILLIS
}
package com.lghdb.kotlin.domain.datasource

import com.lghdb.kotlin.domain.model.Forecast
import com.lghdb.kotlin.domain.model.ForecastList

/**
 * Created by lghdb on 2018/3/29.
 */
interface ForecastDataSource {
    fun requestForecastByZipCode(zipCode: Long, date: Long): ForecastList?

    fun requestDayForecast(id: Long): Forecast?
}
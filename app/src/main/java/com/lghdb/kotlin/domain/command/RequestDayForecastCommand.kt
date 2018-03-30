package com.lghdb.kotlin.domain.command

import com.lghdb.kotlin.domain.datasource.ForecastProvider
import com.lghdb.kotlin.domain.model.Forecast

/**
 * Created by lghdb on 2018/3/30.
 */
class RequestDayForecastCommand(
        private val id: Long,
        private val forecastProvider: ForecastProvider = ForecastProvider()):
        Command<Forecast>{

    override fun execute(): Forecast
        = forecastProvider.requestForecast(id)
}
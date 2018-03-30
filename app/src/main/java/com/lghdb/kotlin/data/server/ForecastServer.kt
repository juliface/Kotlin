package com.lghdb.kotlin.data.server

import com.lghdb.kotlin.data.db.ForecastDb
import com.lghdb.kotlin.domain.model.ForecastList
import com.lghdb.kotlin.domain.datasource.ForecastDataSource
import com.lghdb.kotlin.domain.model.Forecast

/**
 * Created by lghdb on 2018/3/29.
 */
class ForecastServer(
        private val dataMapper: ServerDataMapper = ServerDataMapper(),
        private val forecastDb: ForecastDb = ForecastDb()
): ForecastDataSource {

    override fun requestForecastByZipCode(zipCode: Long, date: Long):
            ForecastList? {
        val result = ForecastByZipCodeRequest(zipCode).execute()
        val converted = dataMapper.convertToDomain(zipCode, result)
        forecastDb.saveForecaset(converted)
        return forecastDb.requestForecastByZipCode(zipCode, date)
    }

    /**
     * 从网络通过id获取到天气信息，是不支持的
     */
    override fun requestDayForecast(id: Long): Forecast?
        = throw UnsupportedOperationException()
}
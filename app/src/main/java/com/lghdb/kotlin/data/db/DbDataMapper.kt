package com.lghdb.kotlin.data.db

import com.lghdb.kotlin.domain.model.Forecast
import com.lghdb.kotlin.domain.model.ForecastList

/**
 * Created by lghdb on 2018/3/29.
 */
class DbDataMapper {
    fun convertToDomain(cityForecast: CityForecast) = with(cityForecast){
        var daily = dayForecastlist.map { convertDayToDomain(it) }
        ForecastList(_id, city, country, daily)
    }

    fun  convertDayToDomain(dayForecast: DayForecast) = with(dayForecast){
        Forecast(_id,date, description, high, low, iconUrl)
    }

    fun convertFromDomain(forecastList: ForecastList) = with(forecastList){
        val daily = dailyForecast.map { convertDayFromDomain(id, it) }
        CityForecast(id,city,country, daily)
    }
    private fun convertDayFromDomain(cityId: Long, forecast: Forecast) = with(forecast){
        DayForecast(date,description,high,low,iconUrl,cityId)
    }
}
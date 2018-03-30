package com.lghdb.kotlin.data.db

/**
 * Created by lghdb on 2018/3/28.
 */
object CityForecastTable {
    val NAME = "CityForecast"
    val ID = "_id"  //这个城市的zipCode
    val CITY = "city"
    val COUNTRY = "country"
}

object DayForecastTable {
    val NAME = "DayForecast"
    val ID = "_id"
    val DATE = "date"
    val DESCRIPTION = "description"
    val HIGH = "high"
    val LOW = "low"
    val ICON_URL = "iconUrl"
    val CITY_ID = "cityId"  //城市id和CityForecastTable的id对应
}
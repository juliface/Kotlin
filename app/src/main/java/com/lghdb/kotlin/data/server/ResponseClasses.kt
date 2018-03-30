package com.lghdb.kotlin.data.server

/**
 * Created by lghdb on 2018/3/27.
 * 数据类用于存放从网络获取的天气数据信息
 * 数据格式如下：
 *
 */
data class ForecastResult(val city: City, val list: List<Forecast>)

/**
 * 城市： coord 为经纬度， population 人口
 */
data class City(val id:Long, val name:String, val coord: Coordinates,
                var country:String, val population:Int)

data class Coordinates(val lon:Float, val lat:Float)

/**
 *
 */
data class Forecast(
        val dt:Long, val temp: Temperature, val pressure:Float,
        val humidity:Int, val weather:List<Weather>,
        val speed:Float, val deg:Int, val clouds:Int,
        var rain:Float
)

data class Temperature(val day: Float, val min: Float, val max: Float,
                       val night: Float, val eve: Float, val morn: Float)

data class Weather(val id: Long, val main: String, val description: String,
                   val icon: String)


package com.lghdb.kotlin.data.db

/**
 * Created by lghdb on 2018/3/29.
 * 数据库映射model
 */

import java.util.*

/**
 * 对应表DayForecast,属性的名字必须和数据库列名保持一致
 */
class DayForecast(val map: MutableMap<String, Any?>){

    var _id: Long by map
    var date: Long by map
    var description: String by map
    var high: Int by map
    var low: Int by map
    var iconUrl: String by map
    var cityId: Long by map

    constructor(date:Long, description:String, high:Int, low:Int,
                iconUrl:String,cityId:Long):this(HashMap()){
        this.date = date
        this.description = description
        this.high = high
        this.low = low
        this.iconUrl = iconUrl
        this.cityId = cityId
    }
}

/**
 * 对应表CityForecast,属性的名字必须和数据库列名保持一致
 */
class CityForecast(val map: MutableMap<String, Any?>,
                   val dayForecastlist: List<DayForecast>){
    var _id: Long by map
    var city: String by map
    var country: String by map

    constructor(id:Long, city: String, country: String,
                dayFlist: List<DayForecast>): this(HashMap(), dayFlist){
        this._id = id
        this.city = city
        this.country = country
    }

}

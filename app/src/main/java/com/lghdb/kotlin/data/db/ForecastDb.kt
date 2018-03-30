package com.lghdb.kotlin.data.db

import com.lghdb.kotlin.domain.model.ForecastList
import com.lghdb.kotlin.domain.datasource.ForecastDataSource
import com.lghdb.kotlin.domain.model.Forecast
import com.lghdb.kotlin.extensions.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * Created by lghdb on 2018/3/29.
 * 数据库操作类
 */
class ForecastDb(
        private val forecastDbHelper: ForecastDbHelper = ForecastDbHelper.instance,
        private val dataMapper: DbDataMapper = DbDataMapper())
    : ForecastDataSource{


    /**
     * 根据城市代码和日期查询天气预报
     */
    override fun requestForecastByZipCode(zipCode: Long,
                                          date:Long): ForecastList?
            = forecastDbHelper.use {
        val where = "${DayForecastTable.CITY_ID} = ? and ${DayForecastTable.DATE} >= ?"
        val dailyForecast = select(DayForecastTable.NAME)
                .whereSimple(where, zipCode.toString(), date.toString())
                .parseList{ DayForecast(HashMap(it)) }

        val city = select(CityForecastTable.NAME)
                .whereSimple("${CityForecastTable.ID} = ?", zipCode.toString())
                .parseOpt { CityForecast(HashMap(it), dailyForecast) }
        if (city != null) dataMapper.convertToDomain(city) else null
    }

    /**
     * 通过id查询得到天气预报信息
     */
    override fun requestDayForecast(id: Long) = forecastDbHelper.use{
        val forecast = select(DayForecastTable.NAME).byId(id)
                .parseOpt { DayForecast(HashMap(it)) }
        if (forecast != null) dataMapper.convertDayToDomain(forecast) else null
    }

    fun saveForecaset(forecastlist: ForecastList) = forecastDbHelper.use {
        clear(CityForecastTable.NAME)
        clear(DayForecastTable.NAME)

        with(dataMapper.convertFromDomain(forecastlist)){
            insert(CityForecastTable.NAME, *map.toVarargArray())
            dayForecastlist.forEach{ insert(DayForecastTable.NAME, *it.map.toVarargArray()) }
        }
    }

}
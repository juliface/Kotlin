package com.lghdb.kotlin.domain.command

import com.lghdb.kotlin.domain.model.ForecastList
import com.lghdb.kotlin.domain.datasource.ForecastProvider

/**
 * Created by lghdb on 2018/3/27.
 * 从网络获取数据并转化为程序数据类
 */
class RequestForecastCommand(
        private val zipCode:Long,
        private val forecastProvider: ForecastProvider = ForecastProvider()):
        Command<ForecastList> {

    companion object {
        val DAYS = 7
    }

    override fun execute(): ForecastList {
      return forecastProvider.requestByZipCode(zipCode, DAYS)
    }
}
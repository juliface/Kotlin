package com.lghdb.kotlin

import com.lghdb.kotlin.domain.datasource.ForecastDataSource
import com.lghdb.kotlin.domain.datasource.ForecastProvider
import com.lghdb.kotlin.domain.model.Forecast
import org.junit.Test
import org.junit.Assert.assertNotNull

import org.mockito.Mockito.*

/**
 * Created by lghdb on 2018/3/30.
 */
class SimpleTest{

    @Test fun testDataSource(){
        val ds = mock(ForecastDataSource::class.java)
        `when`(ds.requestDayForecast(0)).then { Forecast(0, 0, "desc", 20, 0, "url") }

        val provider = ForecastProvider(listOf(ds))
        assertNotNull(provider.requestForecast(0))
    }
}
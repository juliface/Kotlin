package com.lghdb.driver.map.ext

import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.lghdb.driver.ui.App

/**
 * Created by lghdb on 2018/4/8.
 */

val type = "汽车服务|汽车销售|" +
        "汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|" +
        "住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
        "金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

/**
 * 逆地理编码（坐标转地址）
 * @param range 搜索的范围
 * @param coordinateType 坐标系类型
 * @param regeocode 搜索的到结果后的回调函数
 */
fun LatLonPoint.regeocodeSearched(range:Float=200f,
                                  coordinateType:String=GeocodeSearch.AMAP,
                                  regeocode:(RegeocodeResult?,Int)->Unit){
    val search = GeocodeSearch(App.instance)
    search.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener{
        override fun onGeocodeSearched(result: GeocodeResult?, recode: Int) {}
        override fun onRegeocodeSearched(result: RegeocodeResult?, recode: Int) {
            regeocode(result,recode)
        }
    })
    val query = RegeocodeQuery(this, range,
            coordinateType)
    search.getFromLocationAsyn(query)
}

/**
 * 搜索附近
 * @param pageSize 页大小
 * @param pageIndex 页索引
 * @param keyWord 搜索的关键字
 * @param stype 搜索的类型
 * @param city 搜索的城市
 * @param range 搜索的范围
 * @param result 搜索的回调函数
 */
fun LatLonPoint.nearSearch(pageSize:Int = 10,
                           pageIndex:Int = 0,
                           keyWord:String = "",
                           stype:String = type,
                           city:String = "",
                           range:Int = 1000,
                           result:(PoiResult,Int)->Unit){

    val query = PoiSearch.Query("", type, "")
    query.pageSize = pageSize
    query.pageNum = pageIndex
    val search = PoiSearch(App.instance, query)
    search.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener{
        override fun onPoiItemSearched(p0: PoiItem, p1: Int) {}
        override fun onPoiSearched(p0: PoiResult, p1: Int) { result(p0,p1) }
    })
    search.searchPOIAsyn()

}
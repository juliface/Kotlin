package com.lghdb.driver.ui.listener.interfaces

import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.model.*
import com.autonavi.tbt.TrafficFacilityInfo
import com.lghdb.driver.ui.listener.VoliceController

/**
 * Created by lghdb on 2018/4/2.
 */
interface DriverNaviListener: AMapNaviListener{

    override fun onCalculateRouteSuccess(p0: IntArray?) {

    }

    override fun onArriveDestination() {


    }

    override fun onArrivedWayPoint(p0: Int) {

    }

    override fun onEndEmulatorNavi() {

    }

    override fun onGetNavigationText(p0: Int, p1: String?) {

    }

    override fun onNaviInfoUpdate(p0: NaviInfo?) {

    }

    override fun onInitNaviFailure() {

    }

    override fun onInitNaviSuccess() {

    }

    override fun onLocationChange(p0: AMapNaviLocation?) {

    }


    override fun onStartNavi(p0: Int) {

    }

    override fun onTrafficStatusUpdate() {

    }

    override fun onGpsOpenStatus(p0: Boolean) {

    }

    override fun updateCameraInfo(p0: Array<out AMapNaviCameraInfo>?) {

    }

    override fun onServiceAreaUpdate(p0: Array<out AMapServiceAreaInfo>?) {

    }

    override fun showCross(p0: AMapNaviCross?) {

    }

    override fun hideCross() {

    }

    override fun showLaneInfo(p0: AMapLaneInfo?) {

    }

    override fun updateIntervalCameraInfo(p0: AMapNaviCameraInfo?, p1: AMapNaviCameraInfo?, p2: Int) {

    }

    override fun onNaviInfoUpdated(p0: AMapNaviInfo?) {

    }

    override fun showModeCross(p0: AMapModelCross?) {

    }

    override fun showLaneInfo(p0: Array<out AMapLaneInfo>?, p1: ByteArray?, p2: ByteArray?) {

    }

    override fun hideModeCross() {

    }

    override fun hideLaneInfo() {

    }

    override fun notifyParallelRoad(p0: Int) {

    }

    override fun OnUpdateTrafficFacility(p0: Array<out AMapNaviTrafficFacilityInfo>?) {

    }

    override fun OnUpdateTrafficFacility(p0: AMapNaviTrafficFacilityInfo) {

    }

    override fun OnUpdateTrafficFacility(p0: TrafficFacilityInfo?) {

    }



    override fun updateAimlessModeStatistics(p0: AimLessModeStat?) {

    }

    override fun updateAimlessModeCongestionInfo(p0: AimLessModeCongestionInfo?) {

    }

    override fun onPlayRing(p0: Int) {

    }

    override fun onCalculateRouteFailure(p0: Int) {
    }

    override fun onGetNavigationText(p0: String?) {

    }

    override fun onReCalculateRouteForTrafficJam() {

    }

    override fun onReCalculateRouteForYaw() {
    }


}
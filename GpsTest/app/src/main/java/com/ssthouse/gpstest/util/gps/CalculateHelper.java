package com.ssthouse.gpstest.util.gps;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * 计算工具
 * Created by ssthouse on 2015/7/20.
 */
public class CalculateHelper {

    private static final double EARTH_RADIUS = 6378.137;//地球半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 返回单位为米
     *
     * @param latLngBegin
     * @param latLngEnd
     * @return
     */
    public static double getDistance(LatLng latLngBegin, LatLng latLngEnd) {
//        double lat1 = latLngBegin.latitude;
//        double lng1 = latLngBegin.longitude;
//        double lat2 = latLngEnd.latitude;
//        double lng2 = latLngEnd.longitude;
//        double radLat1 = rad(lat1);
//        double radLat2 = rad(lat2);
//        double a = radLat1 - radLat2;
//        double b = rad(lng1) - rad(lng2);
//
//        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
//                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
//        s = s * EARTH_RADIUS;
//        s = Math.round(s * 10000) / 10;
        return DistanceUtil.getDistance(latLngBegin, latLngEnd);
    }
}

package com.ssthouse.gpstest.util.gps;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;

/**
 * 用于对Map进行操作的工具类
 * Created by ssthouse on 2015/7/19.
 */
public class MapHelper {

    public static void animatToPoint(BaiduMap baiduMap, LatLng latLng){
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(u);
    }
}

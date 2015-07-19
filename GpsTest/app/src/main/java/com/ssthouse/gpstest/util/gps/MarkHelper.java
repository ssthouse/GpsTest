package com.ssthouse.gpstest.util.gps;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.ssthouse.gpstest.R;

/**
 * 地图标注---还有选址Util
 * Created by ssthouse on 2015/7/16.
 */
public class MarkHelper {


    /**
     * 标注覆盖物
     */
    public static void mark(BaiduMap baiduMap, LatLng point) {
        if(baiduMap==null || point == null){
            return;
        }
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_launcher);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .title("hahaha")
                .zIndex(10)
                .draggable(true);

        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
    }
}

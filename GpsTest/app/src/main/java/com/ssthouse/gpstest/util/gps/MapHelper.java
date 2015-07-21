package com.ssthouse.gpstest.util.gps;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.ssthouse.gpstest.R;
import com.ssthouse.gpstest.model.MarkerItem;
import com.ssthouse.gpstest.model.PrjItem;

import java.util.List;

/**
 * 用于对Map进行操作的工具类
 * Created by ssthouse on 2015/7/19.
 */
public class MapHelper {

    //标记点相关的
    private static BitmapDescriptor descriptorBlue = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_measure_blue);

    /**
     * 动画聚焦到一个点
     * @param baiduMap
     * @param latLng
     */
    public static void animateToPoint(BaiduMap baiduMap, LatLng latLng) {
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(u);
    }

    /**
     * 动画放大
     * @param baiduMap
     * @param zoomLevel
     */
    public static void animateZoom(BaiduMap baiduMap, int zoomLevel){
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(zoomLevel);
        baiduMap.animateMapStatus(u);
    }

    /**
     * TODO--在各种地图上加载铁路地图
     * 加载铁路地图
     *
     * @return
     */
    public static boolean loadMap(BaiduMap baiduMap, PrjItem prjItem) {

        return true;
    }

    /**
     * 加载Marker
     * @param baiduMap
     * @param prjItem
     * @return
     */
    public static boolean loadMarker(BaiduMap baiduMap, PrjItem prjItem) {
        if (prjItem == null || baiduMap == null) {
            return false;
        }
        if (DBHelper.isPrjEmpty(prjItem)) {
            return false;
        }
        baiduMap.clear();
        List<MarkerItem> markerList = DBHelper.getMarkerList(prjItem);
        //加载marker
        for (int i = 0; i < markerList.size(); i++) {
            OverlayOptions redOverlay = new MarkerOptions()
                    .position(new LatLng(markerList.get(i).getLatitude(),
                            markerList.get(i).getLongitude()))
                    .icon(descriptorBlue)
                    .zIndex(9).draggable(true);
            baiduMap.addOverlay(redOverlay);
        }
        animateToPoint(baiduMap,
                new LatLng(markerList.get(0).getLatitude(),
                        markerList.get(0).getLongitude()));
        return true;
    }
}

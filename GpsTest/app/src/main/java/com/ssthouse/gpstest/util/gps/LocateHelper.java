package com.ssthouse.gpstest.util.gps;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 用于定位
 * Created by ssthouse on 2015/7/17.
 */
public class LocateHelper {

    private LocateHelper(){
    }

    private static  LocationClient locationClient;

    private static LocationClientOption locateOptions;

    /**
     * 开始定位---需要传入一个自定义的监听器
     * @param locationListener
     */
    public static void addListener(Context context, BDLocationListener locationListener){
        initData(context);
        locationClient.registerLocationListener(locationListener);
    }

    public static void start(Context context){
        initData(context);
        locationClient.start();
    }


    public static void stop(Context context){
        initData(context);
        locationClient.stop();
    }


    private static void initData(Context context){
        if(locationClient == null
                || locateOptions == null){
            locationClient = new LocationClient(context);
            locateOptions = new LocationClientOption();

            locateOptions.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
            locateOptions.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
            locateOptions.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
            locateOptions.setIsNeedAddress(true);//返回的定位结果包含地址信息
            locateOptions.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
            locationClient.setLocOption(locateOptions);
        }
    }
}

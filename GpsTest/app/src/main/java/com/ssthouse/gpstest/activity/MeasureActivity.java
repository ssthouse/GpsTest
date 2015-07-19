package com.ssthouse.gpstest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.ssthouse.gpstest.R;

/**
 * 测距Activity
 * Created by ssthouse on 2015/7/19.
 */
public class MeasureActivity extends AppCompatActivity{
    private static final String TAG = "MeasureActivity";

    //地图View
    private MapView mMapView;
    //地图控制器
    private BaiduMap mBaiduMap;
    // 定位相关
    private LocationClient mLocClient;
    //跟随----普通---罗盘---三种定位方式
    private MyLocationConfiguration.LocationMode mCurrentMode =
            MyLocationConfiguration.LocationMode.NORMAL;

    public static void start(Context context){
        context.startActivity(new Intent(context, MeasureActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_measure);

        initView();
    }

    private void initView(){

    }
}
